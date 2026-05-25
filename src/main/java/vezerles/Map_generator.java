package vezerles;

import funkcionalisElemek.KorSzamlalo;
import funkcionalisElemek.Sav;
import funkcionalisElemek.SzakaszTipus;
import funkcionalisElemek.Telephely;
import funkcionalisElemek.Ut;
import grafika.panel.JatekterPanel;
import grafika.view.KanyarView;
import grafika.view.KeresztezodesView;
import grafika.view.UtView;
import segedOsztalyok.Irany;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * Betölti és felépíti a játéktérképet egy szöveges konfigurációs fájlból.
 *
 * ── Fájlformátum ────────────────────────────────────────────────────────────
 *
 *  # Komment (a # utáni rész figyelmen kívül marad)
 *
 *  node <id> <x> <y>
 *      Csomópontot (kereszteződés / végpont) definiál pixelkoordinátákkal.
 *      Példa:  node n0 50 50
 *
 *  road <id> <savokA> <savokB> <nodeA_id> <nodeB_id> [tipus]
 *      Utat hoz létre a két csomópont között.
 *      A hossz és az irány (FEL/LE/JOBBRA/BALRA) automatikusan számítódik:
 *        – Ha nodeA.x == nodeB.x → függőleges út (FEL vagy LE)
 *        – Ha nodeA.y == nodeB.y → vízszintes út (JOBBRA vagy BALRA)
 *        – Egyéb esetben hiba (átlós utak nem támogatottak).
 *      Az opcionális <tipus> mező híddá vagy alagúttá teszi a szakaszt
 *      (hid|alagut), amelyen nincs ütközés – a járművek egymás felett/alatt
 *      haladnak el. Megadása nélkül a szakasz normál.
 *      Példa:  road r0 2 2 n0 n1
 *      Példa:  road r1 1 1 n1 n2 hid
 *
 *  depot <id> <node_id> <jmf>
 *      Telephelyet hoz létre a megadott csomópontnál.
 *      Példa:  depot d0 n0 5000
 *
 *  connect <ut1_id> <ut1_vege> <ut2_id> <ut2_vege>
 *      Manuálisan összekapcsol két utat (ha a node-alapú összekötés nem elég).
 *      Példa:  connect r0 B r1 A
 *
 * ── Koordináta-számítás ─────────────────────────────────────────────────────
 *
 *  Az UtView-nak mindig a sávok "bal felső sarkát" adjuk át (startX, startY),
 *  ahol "bal felső" a képernyő-koordináta-rendszerben értendő (Y lefelé nő).
 *
 *  Vízszintes út (nodeA balra van):
 *      startX = min(nodeA.x, nodeB.x)
 *      startY = min(nodeA.y, nodeB.y)
 *      hossz  = |nodeB.x - nodeA.x|
 *      irány  = JOBBRA
 *
 *  Függőleges út (nodeA fent van):
 *      startX = min(nodeA.x, nodeB.x)
 *      startY = min(nodeA.y, nodeB.y)
 *      hossz  = |nodeB.y - nodeA.y|
 *      irány  = FEL
 *
 *  A sávok egymás mellé (merőleges irányba) kerülnek SAV_SZELLESEG lépésközzel,
 *  ezt az UtView kezeli.
 */
public class Map_generator {

    // ── Belső segédosztály: csomópont ──────────────────────────────────────

    private static class Node {
        final String id;
        final int x, y;

        Node(String id, int x, int y) {
            this.id = id;
            this.x = x;
            this.y = y;
        }
    }

    // ── Belső segédosztály: csomópontból kiinduló utak iránya ────────────────

    /**
     * Egy csomóponthoz nyilvántartja, hogy mely égtájak felé indul út belőle,
     * és a csatlakozó utak közül a legnagyobb sávszámot (a kanyar-cella méretéhez).
     * É = north, D = south, K = east (jobbra), Ny = west (balra).
     */
    private static class NodeAcc {
        boolean N, S, E, W;
        int iranyokSzama = 0;
        int savSzam = 0;

        void hozzaad(char dir, int savok) {
            switch (dir) {
                case 'N': if (!N) { N = true; iranyokSzama++; } break;
                case 'S': if (!S) { S = true; iranyokSzama++; } break;
                case 'E': if (!E) { E = true; iranyokSzama++; } break;
                case 'W': if (!W) { W = true; iranyokSzama++; } break;
            }
            savSzam = Math.max(savSzam, savok);
        }
    }

    // ── Állapot ────────────────────────────────────────────────────────────

    /** Egy sáv (és így egy kanyar-cella) szélessége pixelben. Egyezik az UtView/SavView 60-ával. */
    private static final int SAV_SZELESSEG = 60;

    private final Map<String, Node>      nodeok     = new HashMap<>();
    private final Map<String, Ut>        utak       = new HashMap<>();
    private final Map<String, Telephely> telephelyek = new HashMap<>();
    private final Map<String, NodeAcc>   csomopontIranyok = new HashMap<>();
    private final KorSzamlalo           korszamlalo;

    // ── Konstruktor ────────────────────────────────────────────────────────

    public Map_generator(KorSzamlalo korszamlalo) {
        this.korszamlalo = korszamlalo;
    }

    // ── Betöltés ───────────────────────────────────────────────────────────

    public void load(String filename, JatekterPanel jatekter, Telephely aktivTelephely) {
        try (Scanner scanner = new Scanner(new File(filename))) {
            int lineNumber = 0;

            while (scanner.hasNextLine()) {
                lineNumber++;
                String raw = scanner.nextLine();

                // Komment levágása, whitespace trim
                int ci = raw.indexOf('#');
                String line = (ci >= 0 ? raw.substring(0, ci) : raw).trim();
                if (line.isEmpty()) continue;

                String[] p = line.split("\\s+");

                try {
                    switch (p[0]) {

                        // ── node ──────────────────────────────────────────
                        case "node": {
                            // node <id> <x> <y>
                            requireFields(p, 4, "node", lineNumber);
                            String id = p[1];
                            int x = Integer.parseInt(p[2]);
                            int y = Integer.parseInt(p[3]);
                            nodeok.put(id, new Node(id, x, y));
                            System.out.printf("[Map] Csomópont: %s  (%d, %d)%n", id, x, y);
                            break;
                        }

                        // ── road ──────────────────────────────────────────
                        case "road": {
                            // road <id> <savokA> <savokB> <nodeA_id> <nodeB_id>
                            requireFields(p, 6, "road", lineNumber);
                            String utId  = p[1];
                            int savokA   = Integer.parseInt(p[2]);
                            int savokB   = Integer.parseInt(p[3]);
                            String idA   = p[4];
                            String idB   = p[5];

                            Node a = requireNode(idA, lineNumber);
                            Node b = requireNode(idB, lineNumber);
                            if (a == null || b == null) break;

                            // Irány és hossz meghatározása
                            RoadGeometry geo = computeGeometry(a, b, lineNumber);
                            if (geo == null) break;

                            System.out.printf("[Map] Út: %s  %s→%s  hossz=%d  irány=%s  px=(%d,%d)%n",
                                    utId, idA, idB, geo.hossz, geo.irany, geo.startX, geo.startY);

                            Ut u = new Ut(utId, geo.hossz, savokA, savokB);
                            utak.put(utId, u);
                            korszamlalo.addUt(u);

                            // A sávokat is regisztráljuk a körszámlálóba, különben a
                            // leptet()-ben az allapotFrissit() üres listán futna, és
                            // sosem indulna be a jegesedés, sem a sáv-View frissítése.
                            for (Sav s : u.getSavok()) {
                                korszamlalo.addSav(s);
                            }

                            // Opcionális 7. mező: szakasz-típus (hid / alagut). Ha jelen van,
                            // a szakasz ütközésmentes lesz (a járművek egymás felett/alatt mennek el).
                            if (p.length >= 7) {
                                SzakaszTipus tipus = parseSzakaszTipus(p[6], lineNumber);
                                if (tipus != null && tipus != SzakaszTipus.NORMAL) {
                                    u.setTipus(tipus);
                                    System.out.printf("[Map]   ↳ %s = %s (ütközésmentes)%n", utId, tipus);
                                }
                            }

                            // Feljegyezzük, melyik csomópontból milyen irányba indul az út,
                            // hogy a betöltés végén legenerálhassuk a kanyar-csempéket.
                            int savokOsszesen = savokA + savokB;
                            char dirA, dirB;
                            if (a.y == b.y) {                 // vízszintes út
                                dirA = (b.x > a.x) ? 'E' : 'W';
                                dirB = (b.x > a.x) ? 'W' : 'E';
                            } else {                          // függőleges út
                                dirA = (b.y > a.y) ? 'S' : 'N';
                                dirB = (b.y > a.y) ? 'N' : 'S';
                            }
                            csomopontIranyok.computeIfAbsent(idA, k -> new NodeAcc()).hozzaad(dirA, savokOsszesen);
                            csomopontIranyok.computeIfAbsent(idB, k -> new NodeAcc()).hozzaad(dirB, savokOsszesen);

                            UtView utView = new UtView(u, geo.startX, geo.startY, geo.irany);
                            jatekter.addUtView(utView);
                            break;
                        }

                        // ── depot ─────────────────────────────────────────
                        case "depot": {
                            // depot <id> <node_id> <jmf>
                            requireFields(p, 4, "depot", lineNumber);
                            String telepId = p[1];
                            String nodeId  = p[2];
                            int jmf        = Integer.parseInt(p[3]);

                            Node n = requireNode(nodeId, lineNumber);
                            if (n == null) break;

                            // A pálya telephelyét a játékos AKTÍV telephelyéhez kötjük, hogy a
                            // depó induló JMF-je a játékoshoz kerüljön. Ha nincs átadva aktív
                            // telephely (pl. teszt), külön példányt hozunk létre.
                            Telephely t = (aktivTelephely != null)
                                    ? aktivTelephely
                                    : new Telephely(extractNumber(telepId));
                            t.JMFmodosit(jmf);
                            telephelyek.put(telepId, t);
                            System.out.printf("[Map] Telephely: %s  node=%s  JMF=%d%n",
                                    telepId, nodeId, jmf);
                            break;
                        }

                        // ── connect ───────────────────────────────────────
                        case "connect": {
                            // connect <ut1_id> <ut1_vege> <ut2_id> <ut2_vege>
                            requireFields(p, 5, "connect", lineNumber);
                            Ut u1 = utak.get(p[1]);
                            Ut u2 = utak.get(p[3]);

                            if (u1 == null) { System.err.printf("[Map] %d. sor: ismeretlen út '%s'%n", lineNumber, p[1]); break; }
                            if (u2 == null) { System.err.printf("[Map] %d. sor: ismeretlen út '%s'%n", lineNumber, p[3]); break; }

                            // JAVÍTÁS: addKapcsolat() helyett connect() kell a kétirányú fizikai összekötéshez!
                            u1.connect(p[2], u2, p[4]); 
                            System.out.printf("[Map] Kapcsolat: %s.%s ↔ %s.%s%n", p[1], p[2], p[3], p[4]);
                            break;
                        }

                        default:
                            System.err.printf("[Map] %d. sor: ismeretlen kulcsszó: '%s'%n", lineNumber, p[0]);
                            break;
                    }

                } catch (NumberFormatException e) {
                    System.err.printf("[Map] %d. sor: hibás számformátum – %s%n", lineNumber, line);
                } catch (MapLoadException e) {
                    System.err.printf("[Map] %d. sor: %s%n", lineNumber, e.getMessage());
                }
            }

        } catch (FileNotFoundException e) {
            System.err.println("[Map] Hiba: fájl nem található: " + filename);
        }

        generaljCsomopontGrafikat(jatekter);

        System.out.printf("[Map] Betöltés kész – %d út, %d telephely, %d csomópont%n",
                utak.size(), telephelyek.size(), nodeok.size());
    }

    // ── Kanyar-generálás ────────────────────────────────────────────────────

    /**
     * Legenerálja a csomópontok grafikáját a hozzájuk csatlakozó utak alapján:
     *   - pontosan 2, egymásra merőleges út → kanyar ({@link KanyarView}),
     *   - 3 vagy 4 út → kereszteződés ({@link KeresztezodesView}),
     *   - 2 collineáris (egyenes átmenő) vagy 1 (zsákutca) → nincs külön grafika.
     *
     * A cella minden esetben a csomóponthoz illesztett, az út szélességével megegyező
     * oldalú négyzet (az utak +x/+y irányba terülnek el), így pontosan a csatlakozó
     * utak éleire illeszkedik. A kanyar alap-állása a Nyugat+Dél éleket köti össze;
     * a forgatás:  {Ny,D}→0  {É,Ny}→1  {É,K}→2  {K,D}→3  (90°-os lépések).
     */
    private void generaljCsomopontGrafikat(JatekterPanel jatekter) {
        for (Map.Entry<String, NodeAcc> e : csomopontIranyok.entrySet()) {
            Node n = nodeok.get(e.getKey());
            if (n == null) continue;

            NodeAcc acc = e.getValue();
            int meret = acc.savSzam * SAV_SZELESSEG;
            boolean vizszintesVan = acc.E || acc.W;
            boolean fuggolegesVan = acc.N || acc.S;

            if (acc.iranyokSzama == 2 && vizszintesVan && fuggolegesVan) {
                int forgatas;
                if      (acc.W && acc.S) forgatas = 0;
                else if (acc.N && acc.W) forgatas = 1;
                else if (acc.N && acc.E) forgatas = 2;
                else                     forgatas = 3; // K && D
                jatekter.addKanyarView(new KanyarView(n.x, n.y, meret, forgatas));
                System.out.printf("[Map] Kanyar: %s  px=(%d,%d)  meret=%d  forgatas=%d%n",
                        e.getKey(), n.x, n.y, meret, forgatas);

            } else if (acc.iranyokSzama >= 3) {
                jatekter.addKeresztezodesView(
                        new KeresztezodesView(n.x, n.y, meret, acc.N, acc.S, acc.E, acc.W));
                System.out.printf("[Map] Kereszteződés: %s  px=(%d,%d)  meret=%d  agak=%d%n",
                        e.getKey(), n.x, n.y, meret, acc.iranyokSzama);
            }
        }
    }

    // ── Geometria-számítás ─────────────────────────────────────────────────

    /** Az UtView-nak szükséges geometriai adatok egy úthoz. */
    private static class RoadGeometry {
        final int startX, startY, hossz;
        final Irany irany;

        RoadGeometry(int startX, int startY, int hossz, Irany irany) {
            this.startX = startX;
            this.startY = startY;
            this.hossz  = hossz;
            this.irany  = irany;
        }
    }

    /**
     * Kiszámítja az út geometriáját a két végpont-csomópontból.
     *
     * Szabály:
     *   – Vízszintes: nodeA.y == nodeB.y  → irány = JOBBRA
     *   – Függőleges: nodeA.x == nodeB.x  → irány = FEL
     *   – Egyéb: hiba (átlós utak nem támogatottak)
     *
     * A startX/startY mindig a kisebb koordinátájú végpont
     * (azaz a bal-fent sarok), mert az UtView is így várja.
     */
private RoadGeometry computeGeometry(Node a, Node b, int lineNumber) {
        boolean vizszintes = (a.y == b.y);
        boolean fuggoleges = (a.x == b.x);

        if (!vizszintes && !fuggoleges) {
            System.err.printf("[Map] %d. sor: átlós utak nem támogatottak "
                    + "(%s: %d,%d → %s: %d,%d)%n",
                    lineNumber, a.id, a.x, a.y, b.id, b.x, b.y);
            return null;
        }

        if (vizszintes) {
            int startX = Math.min(a.x, b.x);
            int startY = a.y;                   
            int hossz  = Math.abs(b.x - a.x);
            // JAVÍTÁS: Dinamikus irány megállapítása vízszintes útnál
            Irany irany = (b.x > a.x) ? Irany.JOBBRA : Irany.BALRA;
            return new RoadGeometry(startX, startY, hossz, irany);
        } else {
            int startX = a.x;                   
            int startY = Math.min(a.y, b.y);
            int hossz  = Math.abs(b.y - a.y);
            Irany irany = (b.y > a.y) ? Irany.LE : Irany.FEL;
            return new RoadGeometry(startX, startY, hossz, irany);
        }
    }

    // ── Segédmetódusok ─────────────────────────────────────────────────────

    private void requireFields(String[] parts, int needed, String cmd, int line)
            throws MapLoadException {
        if (parts.length < needed) {
            throw new MapLoadException(
                    "'" + cmd + "' parancshoz " + needed + " mező kell, kapott: " + parts.length);
        }
    }

    private Node requireNode(String id, int line) {
        Node n = nodeok.get(id);
        if (n == null) {
            System.err.printf("[Map] %d. sor: ismeretlen csomópont: '%s'%n", line, id);
        }
        return n;
    }

    /**
     * Egy szakasz-típus kulcsszót {@link SzakaszTipus}-ra fordít.
     * Elfogad ékezetes és angol alakot is: {@code hid|híd|bridge} → HID,
     * {@code alagut|alagút|tunnel} → ALAGUT. Ismeretlen érték esetén hibát ír és {@code null}-t ad.
     */
    private static SzakaszTipus parseSzakaszTipus(String s, int line) {
        switch (s.toLowerCase()) {
            case "hid": case "híd": case "bridge":
                return SzakaszTipus.HID;
            case "alagut": case "alagút": case "tunnel":
                return SzakaszTipus.ALAGUT;
            case "normal": case "normál":
                return SzakaszTipus.NORMAL;
            default:
                System.err.printf("[Map] %d. sor: ismeretlen szakasz-típus '%s' "
                        + "(várt: hid / alagut)%n", line, s);
                return null;
        }
    }

    /** Számot nyer ki egy string végéről (pl. "d0" → 0, "depot3" → 3). */
    private static int extractNumber(String s) {
        try {
            return Integer.parseInt(s.replaceAll("[^0-9]", ""));
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    private static class MapLoadException extends Exception {
        MapLoadException(String msg) { super(msg); }
    }

    // ── Getterek ───────────────────────────────────────────────────────────

    public Map<String, Ut>        getUtak()        { return utak; }
    public Map<String, Telephely> getTelephelyek() { return telephelyek; }
    public Map<String, Node>      getNodeok()      { return nodeok; }
}