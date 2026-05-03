package vezerles;

import funkcionalisElemek.*;
import jarmuvek.*;
import kotrofejek.*;
import segedOsztalyok.*;

import java.io.*;
import java.nio.file.*;
import java.util.*;

/**
 * A prototípus bemeneti parancsnyelvét értelmező és végrehajtó osztály.
 *
 * Soronként dolgozza fel a bemenetet, és az egyes parancsokhoz
 * (load, spawn, step, stat, set_surface, equip, stb.) meghívja
 * a megfelelő modellbeli metódusokat. Hibás parancs vagy érvénytelen
 * paraméter esetén {@code ERROR:} előtagú üzenetet ír a kimenetre.
 */
public class Commander {

    /** Az összes ismert út, azonosító szerint indexelve. */
    private final Map<String, Ut> utak = new LinkedHashMap<>();

    /** Az összes ismert sáv, azonosító szerint indexelve. */
    private final Map<String, Sav> savok = new LinkedHashMap<>();

    /** Az összes ismert jármű, azonosító szerint indexelve. */
    private final Map<String, Jarmu> jarmuvek = new LinkedHashMap<>();

    /** Az összes ismert telephely, azonosító szerint indexelve. */
    private final Map<String, Telephely> telephelyek = new LinkedHashMap<>();

    /** A körszámláló, amely a szimulációt lépteti. */
    private final KorSzamlalo korSzamlalo = new KorSzamlalo();

    /** Ha hamis, a véletlenszerű elemek ki vannak kapcsolva. */
    private boolean randomOn = true;

    /** A kimenet célstreame. */
    private final PrintStream kimenet;

    /**
     * Konstruktor a Commander osztályhoz.
     *
     * @param kimenet A kimenet célstreame.
     */
    public Commander(PrintStream kimenet) {
        this.kimenet = kimenet;
    }

    /**
     * Feldolgoz egy teljes bemeneti szkriptet soronként.
     *
     * @param sorok A végrehajtandó parancssorok listája.
     */
    public void feldolgoz(List<String> sorok) {
        for (String sor : sorok) {
            vegrehajt(sor.trim());
        }
    }

    /**
     * Végrehajt egyetlen parancssort.
     *
     * @param sor A végrehajtandó parancssor.
     */
    public void vegrehajt(String sor) {
        if (sor == null || sor.isEmpty()) return;
        String[] r = sor.split("\\s+");
        switch (r[0].toLowerCase()) {
            case "load"         -> load(r);
            case "random"       -> random(r);
            case "step"         -> step(r);
            case "spawn"        -> spawn(r);
            case "stat"         -> stat(r);
            case "set_surface"  -> setSurface(r);
            case "equip"        -> equip(r);
            case "move"         -> move(r);
            case "change_lane"  -> changeLane(r);
            case "set_material" -> setMaterial(r);
            case "add"          -> add(r);
            case "buy"          -> buy(r);
            case "reload"       -> reload(r);
            case "set_route"    -> setRoute(r);
            default             -> hiba("Ismeretlen parancs: " + r[0]);
        }
    }

    // -------------------------------------------------------------------------
    // Parancsok implementációi
    // -------------------------------------------------------------------------

    /**
     * Betölt egy pályaleíró fájlt és felépíti a modellt.
     * Formátum: {@code load <fajlnev>}
     */
    private void load(String[] r) {
        if (r.length < 2) { hiba("Hianyzik a fajlnev."); return; }
        try {
            List<String> sorok = Files.readAllLines(Path.of("tests/" + r[1]));
            for (String sor : sorok) {
                sor = sor.trim();
                if (sor.isEmpty()) continue;
                String[] reszek = sor.split("\\s+");
                switch (reszek[0].toLowerCase()) {
                    case "road"    -> road(reszek);
                    case "connect" -> connect(reszek);
                    case "depot"   -> depot(reszek);
                    default        -> hiba("Ismeretlen palya-parancs: " + reszek[0]);
                }
            }
        } catch (IOException e) {
            hiba("Nem sikerult beolvasni a fajlt: " + r[1]);
        }
    }

    /**
     * Létrehoz egy utat a megadott azonosítóval, hosszal és sávszámokkal.
     * Formátum: {@code road <ut_id> <hossz> <savok_vegA> <savok_vegB>}
     */
    private void road(String[] r) {
        if (r.length < 5) { hiba("Hibas road parancs."); return; }
        String utId = r[1];
        int hossz   = Integer.parseInt(r[2]);
        int vegASav = Integer.parseInt(r[3]);
        int vegBSav = Integer.parseInt(r[4]);

        Ut ut = new Ut(utId, hossz, vegBSav, vegASav);
        utak.put(utId, ut);
        korSzamlalo.addUt(ut);

        ut.getSavok().forEach(sav -> {
            savok.put(sav.getId(), sav);
            korSzamlalo.addSav(sav);
        });
    }

    /**
     * Összekapcsol két utat a megadott végeken.
     * Formátum: {@code connect <ut1_id> <ut1_veg> <ut2_id> <ut2_veg>}
     */
    private void connect(String[] r) {
        if (r.length < 5) { hiba("Hibas connect parancs."); return; }
        Ut ut1 = utak.get(r[1]);
        Ut ut2 = utak.get(r[3]);
        if (ut1 == null || ut2 == null) { hiba("Ismeretlen ut azonosito."); return; }
        ut1.connect(r[2], ut2, r[4]);
    }

    /**
     * Létrehoz egy telephelyet a megadott azonosítóval és kezdőtőkével.
     * Formátum: {@code depot <telep_id> <jmf>}
     */
    private void depot(String[] r) {
        if (r.length < 3) { hiba("Hibas depot parancs."); return; }
        Telephely t = new Telephely(telephelyek.size());
        t.JMFmodosit(Integer.parseInt(r[2]));
        telephelyek.put(r[1], t);
    }

    /**
     * Ki- vagy bekapcsolja a véletlenszerűséget.
     * Formátum: {@code random <on|off>}
     */
    private void random(String[] r) {
        if (r.length < 2) { hiba("Hianyzik az on/off parameter."); return; }
        randomOn = r[1].equalsIgnoreCase("on");
        korSzamlalo.setHoesik(randomOn);
    }

    /**
     * Léptet n kört.
     * Formátum: {@code step <n>}
     */
    private void step(String[] r) {
        if (r.length < 2) { hiba("Hianyzik a lepesszam."); return; }
        int n = Integer.parseInt(r[1]);
        for (int i = 0; i < n; i++) korSzamlalo.leptet();
    }

    /**
     * Elhelyez egy járművet egy sávon.
     * Formátum: {@code spawn <tipus> <id> <sav_id> [telep_id]}
     */
    private void spawn(String[] r) {
        if (r.length < 4) { hiba("Hibas spawn parancs."); return; }
        String tipus = r[1].toLowerCase();
        String id    = r[2];
        String savId = r[3];

        if (jarmuvek.containsKey(id)) { hiba("Mar letezik jarmű azonositoval: " + id); return; }
        Sav sav = savok.get(savId);
        if (sav == null) { hiba("Nem letezik sav: " + savId); return; }

        Jarmu j = switch (tipus) {
            case "auto"    -> {
            Ut induloUt = sav.getUt();
            // Keresünk egy másik utat a térképen célként
            Ut celUt = utak.values().stream()
                        .filter(u -> u != induloUt)
                        .findFirst()
                        .orElse(induloUt); 
            yield new Auto(id, induloUt, celUt);
        }
            case "busz"    -> new Busz(id);
            case "hokotro" -> {
                String telepId = r.length > 4 ? r[4] : telephelyek.keySet().iterator().next();
                Telephely t    = telephelyek.get(telepId);
                if (t == null) { hiba("Nem letezik telephely: " + telepId); yield null; }
                yield new Hokotro(id, 0, t);
            }
            default -> { hiba("Ismeretlen jarmu tipus: " + tipus); yield null; }
        };

        if (j == null) return;
        if (sav.elfogad(j)) {
            //j.initSav(sav);
        }
        jarmuvek.put(id, j);
        korSzamlalo.addJarmu(j);
    }

    /**
     * Beállítja egy sáv felületi állapotát.
     * Formátum: {@code set_surface <sav_id> <hoszint> <jeges_e>}
     */
    private void setSurface(String[] r) {
        if (r.length < 4) { hiba("Hibas set_surface parancs."); return; }
        Sav sav = savok.get(r[1]);
        if (sav == null) { hiba("Nem letezik sav: " + r[1]); return; }
        sav.setAllapot(Integer.parseInt(r[2]), Boolean.parseBoolean(r[3]));
    }

    /**
     * Felszerel egy kotrófejet egy hókotróra.
     * Formátum: {@code equip <telep_id|store> <hokotro_id> <fej_tipus> [telitettseg]}
     */
    private void equip(String[] r) {
        if (r.length < 4) { hiba("Hibas equip parancs."); return; }
        String telepIdVagyStore = r[1];
        String hokotroId        = r[2];
        String fejTipus         = r[3].toLowerCase();
        int telitettseg         = r.length > 4 ? Integer.parseInt(r[4]) : -1;

        Jarmu j = jarmuvek.get(hokotroId);
        if (!(j instanceof Hokotro h)) { hiba("Nem letezik hokotro: " + hokotroId); return; }

        if (telepIdVagyStore.equalsIgnoreCase("store")) {
            KotroFej fej = fejLetrehoz(fejTipus, telitettseg);
            if (fej == null) { hiba("Ismeretlen fej tipus: " + fejTipus); return; }
            h.fejcsere(fej);
        } else {
            Telephely t = telephelyek.get(telepIdVagyStore);
            if (t == null) { hiba("Nem letezik telephely: " + telepIdVagyStore); return; }
            t.equipHokotro(fejTipus, h, kimenet);
        }
    }

    /**
     * Megadja a következő utat egy jármű számára.
     * Formátum: {@code move <jarmu_id> <ut_id>}
     */
    private void move(String[] r) {
        if (r.length < 3) { hiba("Hibas move parancs."); return; }
        Jarmu j = jarmuvek.get(r[1]);
        Ut ut   = utak.get(r[2]);
        if (j == null)  { hiba("Nem letezik jarmű: " + r[1]); return; }
        if (ut == null) { hiba("Nem letezik ut: " + r[2]); return; }

        if (j instanceof Hokotro h)   h.setKovetkezoUt(ut);
        else if (j instanceof Busz b) b.setKovetkezoUt(ut);
        else hiba("A move parancs csak busszal vagy hokotrovel hasznalhato.");
    }

    /**
     * Sávváltást kezdeményez egy jármű számára.
     * Formátum: {@code change_lane <jarmu_id> <irany>}
     */
    private void changeLane(String[] r) {
        if (r.length < 3) { hiba("Hibas change_lane parancs."); return; }
        Jarmu j = jarmuvek.get(r[1]);
        if (j == null) { hiba("Nem letezik jarmű: " + r[1]); return; }
        Irany irany = r[2].equalsIgnoreCase("balra") ? Irany.BALRA : Irany.JOBBRA;
        j.savvaltas(irany);
    }

    /**
     * Beállít egy anyagkészletet egy járművön, telephelyen vagy sávon.
     * Formátum: {@code set_material <celId> <anyag> <mennyiseg>}
     */
    private void setMaterial(String[] r) {
        if (r.length < 4) { hiba("Hibas set_material parancs."); return; }
        String celId  = r[1];
        String anyag  = r[2].toLowerCase();
        int mennyiseg = Integer.parseInt(r[3]);

        if (telephelyek.containsKey(celId)) {
            telephelyek.get(celId).setKeszlet(anyag, mennyiseg);
        } else if (savok.containsKey(celId)) {
            savok.get(celId).setSozottIdotartam(mennyiseg);
        } else if (jarmuvek.containsKey(celId)) {
            Jarmu j = jarmuvek.get(celId);
            if (j instanceof Hokotro h) h.setFejKeszlet(anyag, mennyiseg);
            else hiba("set_material hokotron kivuli jarmuvel nem tamogatott.");
        } else {
            hiba("Nem letezik celpontId: " + celId);
        }
    }

    /**
     * Hozzáad egy tárgyat vagy pénzösszeget a telephelyhez a vásárlást megkerülve.
     * Formátum: {@code add <telep_id> <tipus> [mennyiseg]}
     */
    private void add(String[] r) {
        if (r.length < 3) { hiba("Hibas add parancs."); return; }
        Telephely t = telephelyek.get(r[1]);
        if (t == null) { hiba("Nem letezik telephely: " + r[1]); return; }
        String tipus  = r[2].toLowerCase();
        int mennyiseg = r.length > 3 ? Integer.parseInt(r[3]) : 1;

        switch (tipus) {
            case "jmf"        -> t.JMFmodosit(mennyiseg);
            case "biokerozin" -> t.addKeszlet("biokerozin", mennyiseg);
            case "so"         -> t.addKeszlet("so", mennyiseg);
            case "zuzalek"    -> t.addKeszlet("zuzalek", mennyiseg);
            default           -> {
                KotroFej fej = fejLetrehoz(tipus, -1);
                if (fej != null) t.tarol(fej);
                else hiba("Ismeretlen tipus az add parancsban: " + tipus);
            }
        }
    }

    /**
     * Vásárol egy tárgyat a telephelyen tárolt JMF-ből.
     * Formátum: {@code buy <telep_id> <tipus>}
     */
    private void buy(String[] r) {
        if (r.length < 3) { hiba("Hibas buy parancs."); return; }
        Telephely t = telephelyek.get(r[1]);
        if (t == null) { hiba("Nem letezik telephely: " + r[1]); return; }
        if (!t.vasarol(r[2].toLowerCase().replace("_", ""))) {
            kimenet.println("ERROR: Nincs eleg JMF a vasarlashoz.");
        }
    }

    /**
     * Újratölti a hókotró fejét a telephely készletéből.
     * Formátum: {@code reload <hokotro_id> <telep_id>}
     */
    private void reload(String[] r) {
        if (r.length < 3) { hiba("Hibas reload parancs."); return; }
        Jarmu j = jarmuvek.get(r[1]);
        if (!(j instanceof Hokotro h)) { hiba("Nem letezik hokotro: " + r[1]); return; }
        Telephely t = telephelyek.get(r[2]);
        if (t == null) { hiba("Nem letezik telephely: " + r[2]); return; }
        h.ujratolt(t, kimenet);
    }

    /**
     * Beállítja egy busz útvonalát két végállomás között.
     * Formátum: {@code set_route <busz_id> <vegallomas1_id> <vegallomas2_id>}
     */
    private void setRoute(String[] r) {
        if (r.length < 4) { hiba("Hibas set_route parancs."); return; }
        Jarmu j = jarmuvek.get(r[1]);
        if (!(j instanceof Busz b)) { hiba("Nem letezik busz: " + r[1]); return; }
        Ut v1 = utak.get(r[2]);
        Ut v2 = utak.get(r[3]);
        if (v1 == null || v2 == null) { hiba("Ismeretlen ut azonosito."); return; }
        b.setRoute(v1, v2);
    }

    /**
     * Kiírja egy objektum állapotát a kimenetre.
     * Formátum: {@code stat <id> | all}
     */
    private void stat(String[] r) {
        if (r.length < 2) { hiba("Hianyzik az azonosito."); return; }
        String id = r[1];

        if (id.equalsIgnoreCase("all")) {
            jarmuvek.forEach((k, v)    -> v.statKiir(k, kimenet));
            savok.forEach((k, v)       -> v.statKiir(k, kimenet));
            telephelyek.forEach((k, v) -> v.statKiir(k, kimenet));
            return;
        }

        if (jarmuvek.containsKey(id))   { jarmuvek.get(id).statKiir(id, kimenet);    return; }
        if (savok.containsKey(id))       { savok.get(id).statKiir(id, kimenet);       return; }
        if (telephelyek.containsKey(id)) { telephelyek.get(id).statKiir(id, kimenet); return; }
        hiba("Nem letezik objektum azonositoval: " + id);
    }

    // -------------------------------------------------------------------------
    // Segédmetódusok
    // -------------------------------------------------------------------------

    /**
     * Létrehoz egy kotrófejet a megadott típus és telítettség alapján.
     *
     * @param tipus       A fej típusa (pl. {@code "sopro"}, {@code "sarkany"}).
     * @param telitettseg A fogyóanyag kezdeti mennyisége, {@code -1} az alapértelmezetthez.
     * @return Az új {@link KotroFej} példány, vagy {@code null} ismeretlen típus esetén.
     */
    private KotroFej fejLetrehoz(String tipus, int telitettseg) {
        return switch (tipus) {
            case "sopro"        -> new SoproFej();
            case "hanyo"        -> new HanyoFej();
            case "jegtoro"      -> new JegtoroFej();
            case "soszoro"      -> {
                SoszoroFej f = new SoszoroFej();
                if (telitettseg >= 0) f.setSo(telitettseg);
                f.bekapcsol();
                yield f;
            }
            case "sarkany"      -> {
                SarkanyFej f = new SarkanyFej();
                if (telitettseg >= 0) f.setBiokerozin(telitettseg);
                f.bekapcsol();
                yield f;
            }
            case "zuzalekszoro" -> {
                ZuzalekszoroFej f = new ZuzalekszoroFej();
                if (telitettseg >= 0) f.setZuzalek(telitettseg);
                f.bekapcsol();
                yield f;
            }
            default -> null;
        };
    }

    private void hiba(String uzenet) {
        kimenet.println("ERROR: " + uzenet);
    }
}