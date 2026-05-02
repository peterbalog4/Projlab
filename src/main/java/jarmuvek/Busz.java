package jarmuvek;

import funkcionalisElemek.Sav;
import funkcionalisElemek.Ut;

/**
 * A buszt reprezentáló osztály, amely a Jármű alaposztályból származik.
 *
 * Felelőssége a menetrend szerinti közlekedés megvalósítása két végállomás között,
 * valamint a fordulatok detektálása és adminisztrálása, amely a buszvezető játékos
 * pontszerzésének alapja. A busz jeges úton megcsúszik, mély hóban elakad,
 * ütközés esetén 5 körre mozgásképtelenné válik.
 */
public class Busz extends Jarmu {

    /**
     * A busz által eddig sikeresen teljesített fordulók száma.
     */
    private int forduloSzam = 0;

    /**
     * A busz egyik kijelölt végállomása.
     */
    private Ut vegallomas1;

    /**
     * A busz másik kijelölt végállomása.
     */
    private Ut vegallomas2;

    /**
     * Az a végállomás, amely felé a busz éppen halad.
     */
    private Ut aktualisCel;

    /**
     * A játékos által megadott következő út, amelyre a busz kanyarodni fog.
     * {@code null}, ha nincs előre megadott irány.
     */
    private Ut kovetkezoUt;

    /**
     * Konstruktor a Busz osztályhoz.
     *
     * @param id A busz egyedi azonosítója a naplózáshoz.
     */
    public Busz(String id) {
        super(id);
    }

    /**
     * Beállítja a busz két végállomását és az első célt.
     * A {@code set_route} parancs hatására hívódik meg.
     *
     * @param vegallomas1 Az egyik végállomás útja.
     * @param vegallomas2 A másik végállomás útja.
     */
    public void setRoute(Ut vegallomas1, Ut vegallomas2) {
        this.vegallomas1 = vegallomas1;
        this.vegallomas2 = vegallomas2;
        this.aktualisCel = vegallomas2;
    }

    /**
     * Beállítja a következő utat, amelyre a busz kanyarodni fog a következő
     * kereszteződésben. A játékos adja meg a {@code move} paranccsal.
     *
     * @param ut A kívánt következő {@link Ut}.
     */
    public void setKovetkezoUt(Ut ut) {
        this.kovetkezoUt = ut;
    }

    /**
     * Megvalósítja a busz megcsúszását jeges útfelületen.
     *
     * A sáv által hívódik meg, ha a felszín jeges és nincs zúzalék.
     * A busz irányíthatatlanul csúszik; ha ütközési partnert talál a sávon,
     * ütközést kezdeményez.
     */
    @Override
    public void csuszik() {
        allapot = Allapot.CSUSZKAL;
        if (aktualisSav == null) return;

        Jarmu masik = aktualisSav.getMasikJarmu(this);
        if (masik != null) {
            utkozik(masik);
        }
    }

    /**
     * A sáv végének elérésekor a Pozicio hívja meg.
     * A busz ellenőrzi, hogy végállomáson van-e, majd kanyarodik vagy vár.
     */
    @Override
    public void elertSavVeget() {
        if (aktualisSav != null) {
            Ut jelenlegi = aktualisSav.getUt();
            if (jelenlegi.equals(aktualisCel) || (forduloSzam == 0 && (jelenlegi.equals(vegallomas1) || jelenlegi.equals(vegallomas2)))) {
                forduloNovel();
            }
        }
        
        if (kovetkezoUt != null) {
            Ut cel = kovetkezoUt;
            kovetkezoUt = null;
            kanyarodik(cel);
        } else {
            this.varakozasiIdo = 0;
            this.allapot = Allapot.KOZLEKEDIK;
        }
    }
     /*
     * Ha a busz vár (varakozasiIdo > 0), csökkenti a számlálót.
     * Egyébként előre mozdul; ha a sáv végére ér és elérte a célállomást,
     * fordulót teljesít. Ha van megadott következő út, arra kanyarodik,
     * egyébként vár a játékos utasítására.
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

        if (aktualisSav == null || pozicio == null) return;

        if (allapot == Allapot.ELAKADT) { //TODO ezt átgondolni
            return; 
        }

        allapot = Allapot.KOZLEKEDIK;
        pozicio.halad(this, 1);
    }

    /**
     * Megvizsgálja, hogy a busz elérte-e az aktuális sáv végét.
     *
     * @return {@code true}, ha a sáv végén van, {@code false} egyébként.
     */
    private boolean savVegenVan() {
        return aktualisSav.getMasikJarmu(this) == null;
    }

    /**
     * Kezeli a más járművel való ütközés eseményét.
     *
     * Csak akkor vált ki hatást, ha a busz még nincs ütközési büntetés alatt
     * (varakozasiIdo >= 0 és < 5). Ilyenkor mindkét jármű 5 körre megáll,
     * és a sáv lezárásra kerül.
     *
     * @param masikJarmu Az a jármű, amellyel a busz összeütközött.
     */
    @Override
    public void utkozik(Jarmu masikJarmu) {
        if (varakozasiIdo >= 0 && varakozasiIdo < 5) {
            this.megall(5);
            masikJarmu.utkozik(this);
            if (aktualisSav != null) {
                aktualisSav.lezar(5);
            }
        }
    }

    /**
     * Növeli a teljesített fordulók számát eggyel, és megfordítja az aktuális célt.
     *
     * Ha az aktuális cél {@code vegallomas1} volt, az új cél {@code vegallomas2} lesz,
     * és fordítva. Ezt a metódust a {@link #kozlekedik} hívja meg, amikor a busz
     * eléri az aktuális végállomást.
     */
    public void forduloNovel() {
        forduloSzam++;
        if (aktualisCel == vegallomas1) {
            aktualisCel = vegallomas2;
        } else {
            aktualisCel = vegallomas1;
        }
    }

    /**
     * Visszaadja a busz által teljesített fordulók számát.
     *
     * @return Az eddigi fordulók száma.
     */
    public int getForduloSzam() {
        return forduloSzam;
    }

    /**
     * Kiírja a busz aktuális állapotát a megadott kimenetre.
     * Az alaposztály mezői mellett a fordulószámot is megjeleníti.
     *
     * @param id      A busz azonosítója a kimenetben.
     * @param kimenet A célstream.
     */
    @Override
    public void statKiir(String id, java.io.PrintStream kimenet) {
        super.statKiir(id, kimenet);
        kimenet.println("- Forduloszam: " + forduloSzam);
    }
}