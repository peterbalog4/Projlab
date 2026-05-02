package jarmuvek;

import funkcionalisElemek.Sav;
import funkcionalisElemek.Ut;
import segedOsztalyok.HaladasiIrany;
import segedOsztalyok.Irany;

import java.util.*;

/**
 * A személyautót reprezentáló osztály, amely a Jármű általános tulajdonságait
 * specifikus viselkedéssel egészíti ki.
 *
 * Az autó önállóan közlekedik az otthona és munkahelye között, a legrövidebb
 * utat Dijkstra-algoritmussal számítja ki. Képes jeges úton megcsúszni,
 * más járművel ütközni, valamint elakadás esetén szabad szomszédos sávba váltani.
 */
public class Auto extends Jarmu {

    /**
     * Az autó kiindulópontjaként szolgáló út.
     * Amikor eléri a munkahelyét, visszafordul ide.
     */
    private Ut otthon;

    /**
     * Az autó célállomásaként szolgáló út.
     * Amikor eléri, megfordul és otthon felé indul.
     */
    private Ut munkahely;

    /**
     * Az aktuálisan követett útvonal útobjektumainak sorban rendezett listája.
     * A Dijkstra-algoritmus eredménye; az autó minden kanyarodáskor
     * a következő elemet veszi ki belőle.
     */
    private LinkedList<Ut> utvonal;

    /**
     * Konstruktor az Auto osztályhoz.
     *
     * @param id        Az autó egyedi azonosítója, amelyet a naplózás során használunk.
     * @param otthon    Az autó indulási útja.
     * @param munkahely Az autó célútja.
     */
    public Auto(String id, Ut otthon, Ut munkahely) {
        super(id);
        this.otthon = otthon;
        this.munkahely = munkahely;
        this.utvonal = new LinkedList<>();
    }

    /**
     * A sáv végének elérésekor a Pozicio hívja meg.
     * Az autó kiszámolja a következő utat és kanyarodik.
     */
    @Override
    public void elertSavVeget() {
        Ut kovetkezo = kovetkezoUt();
        if (kovetkezo != null) {
            kanyarodik(kovetkezo);
        }
    }
     /*
     * Ha a jármű vár (varakozasiIdo > 0), csökkenti a számlálót és nem mozdul.
     * Ha elakadt (varakozasiIdo == -1), megpróbál szabad szomszédos sávba váltani.
     * Egyébként előre mozdul a sávon; ha a sáv végére ér, kiszámolja a következő
     * utat Dijkstrával és kanyarodik.
     */
    @Override
    public void kozlekedik() {
        if (varakozasiIdo > 0) {
            varakozasiIdo--;
            if (varakozasiIdo == 0) {
                allapot = Allapot.KOZLEKEDIK;
            }
            return;
        }

        if (allapot == Allapot.ELAKADT) {
            probaljSavotValtani();
            return;
        }

        if (aktualisSav == null || pozicio == null) return;

        Jarmu masik = aktualisSav.getMasikJarmu(this);
        if (masik != null) {
            utkozik(masik);
            return;
        }

        allapot = Allapot.KOZLEKEDIK;
        pozicio.halad(this, 1);
    }

    /**
     * Meghatározza a következő utat, amelyre az autónak kanyarodnia kell.
     *
     * Ha az útvonallista üres (vagy lejárt), újraszámítja Dijkstrával.
     * Ha eléri a munkahelyét, felcseréli az otthont és a munkahelyet,
     * majd újra tervez.
     *
     * @return A következő {@link Ut}, vagy {@code null}, ha nincs elérhető útvonal.
     */
    private Ut kovetkezoUt() {
        if (utvonal.isEmpty()) {
            Ut cel = munkahely;
            if (aktualisSav.getUt().equals(cel)) {
                Ut tmp = otthon;
                otthon = munkahely;
                munkahely = tmp;
                cel = munkahely;
            }
            utvonal = dijkstra(aktualisSav.getUt(), cel);
        }

        return utvonal.isEmpty() ? null : utvonal.poll();
    }

    /**
     * Megkísérli a sávváltást, ha az autó elakadt.
     * Először balra, majd jobbra próbál váltani. Ha sikerül, az állapot
     * {@link Allapot#KOZLEKEDIK}-re vált.
     */
    private void probaljSavotValtani() {
        Sav elotteSav = aktualisSav;
        savvaltas(Irany.BALRA);
        if (aktualisSav != elotteSav) {
            varakozasiIdo = 0;
            allapot = Allapot.KOZLEKEDIK;
            return;
        }
        savvaltas(Irany.JOBBRA);
        if (aktualisSav != elotteSav) {
            varakozasiIdo = 0;
            allapot = Allapot.KOZLEKEDIK;
        }
    }

    /**
     * Megvalósítja az autó megcsúszását jeges útfelületen.
     *
     * A sáv által hívódik meg, ha a felszín jeges és nincs zúzalék.
     * Az autó irányíthatatlanul 10 métert csúszik előre; ha ütközési
     * partnert talál a sávon, ütközést kezdeményez.
     */
    @Override
    public void csuszik() {
        this.allapot = Allapot.CSUSZKAL;
        if (aktualisSav == null) return;
        Jarmu masik = aktualisSav.getMasikJarmu(this);
        if (masik != null) {
            this.utkozik(masik);
        }
    }

    /**
     * Kezeli a más járművel való ütközés eseményét.
     *
     * Csak akkor vált ki hatást, ha az autó még nincs ütközési büntetés alatt
     * (varakozasiIdo >= 0 és < 10). Ilyenkor mindkét jármű 10 körre megáll,
     * és a sáv lezárásra kerül.
     *
     * @param masikJarmu Az a jármű, amellyel az autó összeütközött.
     */
    @Override
    public void utkozik(Jarmu masikJarmu) {
        if (varakozasiIdo >= 0 && varakozasiIdo < 10) {
            this.megall(10);
            masikJarmu.utkozik(this);
            if (aktualisSav != null) {
                aktualisSav.lezar(10);
            }
        }
    }

    /**
     * Dijkstra-algoritmussal kiszámítja a legrövidebb utat a forrás útról a cél útig.
     *
     * Az úthálózatot gráfként kezeli, ahol a csúcsok {@link Ut} objektumok,
     * az élek súlya egységnyi (minden útváltás egy lépésnek számít).
     * A szomszédokat az {@link Ut#getKapcsolatok(HaladasiIrany)} metóduson
     * keresztül kérdezi le, figyelembe véve a haladási irányt.
     *
     * @param forras A kiindulási {@link Ut}.
     * @param cel    A célként meghatározott {@link Ut}.
     * @return Az útvonalat alkotó utak sorban rendezett listája (a forrás után kezdve),
     *         vagy üres lista, ha nincs elérhető útvonal.
     */
    private LinkedList<Ut> dijkstra(Ut forras, Ut cel) {
        Map<Ut, Integer> tavolsag = new HashMap<>();
        Map<Ut, Ut> elozo = new HashMap<>();
        PriorityQueue<Ut> sor = new PriorityQueue<>(Comparator.comparingInt(tavolsag::get));

        tavolsag.put(forras, 0);
        sor.add(forras);

        while (!sor.isEmpty()) {
            Ut aktualis = sor.poll();

            if (aktualis.equals(cel)) break;

            int aktualisTav = tavolsag.get(aktualis);

            for (HaladasiIrany irany : HaladasiIrany.values()) {
                Map<Ut, String> szomszedok = aktualis.getKapcsolatok(irany);
                for (Ut szomszed : szomszedok.keySet()) {
                    int ujTav = aktualisTav + 1;
                    if (ujTav < tavolsag.getOrDefault(szomszed, Integer.MAX_VALUE)) {
                        tavolsag.put(szomszed, ujTav);
                        elozo.put(szomszed, aktualis);
                        sor.remove(szomszed);
                        sor.add(szomszed);
                    }
                }
            }
        }

        LinkedList<Ut> utvonal = new LinkedList<>();
        Ut lepes = cel;
        while (elozo.containsKey(lepes)) {
            utvonal.addFirst(lepes);
            lepes = elozo.get(lepes);
        }

        return utvonal;
    }
}