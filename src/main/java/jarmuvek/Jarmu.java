package jarmuvek;

import funkcionalisElemek.Sav;
import funkcionalisElemek.Ut;
import segedOsztalyok.Irany;

/**
 * A játékban közlekedő járművek általános absztrakt alaposztálya.
 *
 * Közös interfészt és alapvető állapotfelügyeletet biztosít minden
 * járműtípus ({@link Auto}, {@link Busz}, {@link Hokotro}) számára.
 * Felelős a jármű aktuális pozíciójának (sáv) nyilvántartásáért,
 * a várakozási idő kezeléséért, valamint az alapvető manőverek –
 * megállás, kanyarodás, sávváltás – végrehajtásáért.
 */
public abstract class Jarmu {

    /**
     * A jármű egyedi szöveges azonosítója.
     * Naplózáshoz és a parancsfeldolgozóban való hivatkozáshoz használatos.
     */
    protected String id;

    /**
     * A sáv, amelyen a jármű jelenleg tartózkodik.
     * {@code null}, ha a jármű még nincs pályán.
     */
    protected Sav aktualisSav;

    /**
     * Hátralévő várakozási körök száma.
     *
     * <ul>
     *   <li>{@code 0}  – a jármű szabadon közlekedhet</li>
     *   <li>{@code > 0} – ennyi körig nem mozog (pl. ütközés után)</li>
     *   <li>{@code -1} – határozatlan ideig áll (pl. mély hóban elakadt)</li>
     * </ul>
     */
    protected int varakozasiIdo;

    /**
     * A jármű aktuális állapota.
     * Az állapotgép szerint lehet: {@code KOZLEKEDIK}, {@code ELAKADT},
     * {@code CSUSZKAL}, {@code OSSZECSUSZOTT}.
     */
    protected Allapot allapot;

    /**
     * A jármű lehetséges állapotait felsoroló belső enum.
     */
    public enum Allapot {
        /** A jármű normálisan halad. */
        KOZLEKEDIK,
        /** A jármű mély hó miatt nem tud tovább haladni. */
        ELAKADT,
        /** A jármű jeges felületen irányíthatatlanul csúszik. */
        CSUSZKAL,
        /** A jármű ütközés következtében mozgásképtelen. */
        OSSZECSUSZOTT
    }

    /**
     * Létrehoz egy új járművet a megadott azonosítóval.
     *
     * @param id A jármű egyedi szöveges azonosítója.
     */
    public Jarmu(String id) {
        this.id = id;
        this.varakozasiIdo = 0;
        this.allapot = Allapot.KOZLEKEDIK;
    }

    /**
     * Visszaadja a jármű azonosítóját.
     *
     * @return A jármű {@code String} formátumú azonosítója.
     */
    public String getId() {
        return this.id;
    }

    /**
     * Visszaadja a jármű aktuális sávját.
     *
     * @return Az a {@link Sav}, amelyen a jármű éppen tartózkodik,
     *         vagy {@code null}, ha még nincs pályán.
     */
    public Sav getAktualisSav() {
        return aktualisSav;
    }

    /**
     * Visszaadja a jármű hátralévő várakozási idejét körökben.
     *
     * @return A várakozási idő értéke ({@code -1} határozatlan, {@code 0} szabad).
     */
    public int getVarakozasiIdo() {
        return varakozasiIdo;
    }

    /**
     * Visszaadja a jármű jelenlegi állapotát.
     *
     * @return Az aktuális {@link Allapot} enum-érték.
     */
    public Allapot getAllapot() {
        return allapot;
    }

    /**
     * Áthelyezi a járművet egy új sávba.
     *
     * Ha a jármű korábban már volt sávon, automatikusan eltávolítja
     * magát az előző sáv nyilvántartásából, mielőtt az újba kerül.
     *
     * @param ujSav A cél {@link Sav} objektum.
     */
    public void setSav(Sav ujSav) {
        if (this.aktualisSav != null) {
            this.aktualisSav.eltavolit(this);
        }
        this.aktualisSav = ujSav;
    }

    /**
     * Végrehajtja a jármű kanyarodását a megadott célútra.
     *
     * Az aktuális sávon keresztül értesíti az {@link Ut} osztályt,
     * amely elvégzi a tényleges átirányítást. Sikeres kanyarodás esetén
     * a jármű állapota {@link Allapot#KOZLEKEDIK} lesz.
     *
     * @param celUt Az az {@link Ut}, amelyre a jármű kanyarodni kíván.
     */
    public void kanyarodik(Ut celUt) {
        if (aktualisSav != null) {
            aktualisSav.jarmuKanyarodik(this, celUt);
            this.allapot = Allapot.KOZLEKEDIK;
        }
    }

    /**
     * Körönként végrehajtandó mozgáslogika – minden leszármazott saját
     * szabályai szerint valósítja meg.
     */
    public abstract void kozlekedik();

    /**
     * Megállítja a járművet a megadott számú körre.
     *
     * Ha {@code korszam} értéke {@code -1}, a jármű határozatlan ideig
     * áll (pl. mély hóban elakadva). A {@code 0} értékkel azonnal
     * felszabadítható egy korábban elakadt jármű.
     * Pozitív értéknél az állapot {@link Allapot#OSSZECSUSZOTT} lesz,
     * {@code -1}-nél {@link Allapot#ELAKADT}.
     *
     * @param korszam Hány körre álljon meg a jármű
     *                ({@code -1} = határozatlan, {@code 0} = azonnali felszabadítás).
     */
    public void megall(int korszam) {
        this.varakozasiIdo = korszam;
        if (korszam == -1) {
            this.allapot = Allapot.ELAKADT;
        } else if (korszam > 0) {
            this.allapot = Allapot.OSSZECSUSZOTT;
        } else {
            this.allapot = Allapot.KOZLEKEDIK;
        }
    }

    /**
     * Kezeli a más járművel való ütközést.
     *
     * Az alapértelmezett implementáció mindkét járművet 10 körre
     * mozgásképtelenné teszi, és lezárja az aktuális sávot ugyanannyi körre.
     * A leszármazottak felülírhatják saját ütközési szabályaikkal.
     *
     * @param masikJarmu Az az {@link Jarmu}, amellyel a jármű összeütközött.
     */
    public void utkozik(Jarmu masikJarmu) {
        if (this.varakozasiIdo >= 0 && this.varakozasiIdo < 10) {
            this.megall(10);
            masikJarmu.utkozik(this);
            if (aktualisSav != null) {
                aktualisSav.lezar(10);
            }
        }
    }

    /**
     * Kezdeményezi a sávváltást a megadott irányba.
     *
     * A kérést az aktuális sávhoz tartozó {@link Ut} osztálynak
     * továbbítja, amely elvégzi az átmozgatást, ha a szomszédos sáv
     * szabad és elfogadja a járművet.
     *
     * @param i A kívánt sávváltási irány ({@link Irany#BALRA} vagy {@link Irany#JOBBRA}).
     */
    public void savvaltas(Irany i) {
        if (aktualisSav != null) {
            aktualisSav.savValtas(this, i);
        }
    }

    /**
     * Megvalósítja a jármű irányíthatatlan csúszását jeges útfelületen.
     *
     * Csúszáskor a jármű 10 méterrel előre kényszerül. Ha ezen a
     * szakaszon másik járművet talál, ütközést kezdeményez.
     * Az alaposztály üres törzset ad; a konkrét leszármazottak
     * felülírják saját csúszáslogikájukkal.
     */
    public void csuszik() {
        // A leszármazottak (Auto, Busz) felülírják saját logikával.
    }
}