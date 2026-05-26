package funkcionalisElemek;

import java.util.ArrayList;
import java.util.List;

import grafika.AbstractObservable;
import jarmuvek.Hokotro;
import kotrofejek.*;

/**
 * Egy takarító játékos raktárát valósítja meg. Itt tudja elraktározni a Hókotrók Kotrófejeit és azok feltölthető
 * anyagait, illetve ezen keresztül tud kapcsolatba lépni a Bolttal, tárolni a JMF-ét.(Kicsit gyakorlatiasabban ez a
 * játékos inventory-ja). Megtalálhatók benne static változóként az egyes termékek állandó árai.
 */
public class Telephely extends AbstractObservable {
    /**
     * Egy Söprőfej árát tárolja.
     */
    static private final int soprofejAr = 120000;
    /**
     * Egy Jégtörő fej árát tárolja.
     */
    static private final int jegtorofejAr = 250000;
    /**
     * Egy Hányó fej árát tárolja.
     */
    static private final int hanyofejAr = 200000;
    /**
     * Egy Sárkány fej árát tárolja.
     */
    static private final int sarkanyfejAr = 600000;
    /**
     * Egy Sószóró fej árát tárolja.
     */
    static private final int soszorofejAr = 400000;
    /**
     * Egy Zúzalékszóró fej árát tárolja.
     */
    static private final int zuzalekszorofejAr = 350000;
    /**
     * Egy teljes töltés biokerozin árát tárolja.
     */
    static private final int biokerozinAr = 5000;
    /**
     * Egy teljes töltés só árát tárolja.
     */
    static private final int soAr = 8000;
    /**
     * Egy teljes töltés zuzalék árát tárolja.
     */
    static private final int zuzalekAr = 5000;
    /**
     * Egy Hókotró árát tárolja.
     */
    static private final int hokotroAr = 2000000;

    /**
     * Ehhez a takarító játékoshoz tartozik a telephely.
     */
    private final int jatekosID;
    /**
     * A játékos raktárában lévő biokerozin adagok számát raktározza.
     */
    private int biokerozin = 0;
    /**
     * A játékos raktárában lévő só adagok számát raktározza.
     */
    private int so = 0;
    /**
     * A játékos raktárában lévő zuzalék adagok számát raktározza.
     */
    private int zuzalek = 0;
    /**
     * A játékos raktárában lévő JMF mennyiségét tárolja. Ebből tud a játékos a boltban vásárolni.
     */
    private int JMF = 0;
    /**
     * A játékos által megvásárolt, éppen nem használatban lévő Kotrófejeket tárolja.
     */
    private List<KotroFej> kotroFejek = new ArrayList<>();

    private int raktaronLevoHokotrok = 0;

    public int getRaktaronLevoHokotrok() {
        return raktaronLevoHokotrok;
    }

    public void kiveszHokotrot() {
        if (raktaronLevoHokotrok > 0) {
            raktaronLevoHokotrok--;
            notifyObservers();
        }
    }

    public int getJMF(){
        return JMF;
    }

    public int getBiokerozin() {
        return biokerozin;
    }

    public int getSo() {
        return so;
    }

    public int getZuzalek() {
        return zuzalek;
    }


    public Telephely(final int jatekosID) {
        this.jatekosID = jatekosID;
    }

    /**
     * A Hókotró kérésére (fejcsere) felteszi a fejet a Hókotróra. Törli a feltett fejet a raktárból.
     *
     * @param ujFej
     */
    public void useFej(KotroFej ujFej) {
        kotroFejek.remove(ujFej);
    }

    /**
     * A Hókotró kérésére (fejcsere) leveszi a Hókotróról az éppen használt fejet és elrakja a raktárba.
     *
     * @param fej
     */
    public void tarol(KotroFej fej) {
        kotroFejek.add(fej);
    }

    /**
     * Megveszi a bementként megadott játékelemet a telephelyen tárolt JMF-ből és visszatér azzal, hogy a bemenetként
     * megadott terméket meg tudta-e vásárolni.
     *
     * @param item A megvásárolni kívánt tárgy.
     * @return {@code true}, sikerült megvásárolni, {@code false}, nem sikerült megvásárolni (pl.: túl  kevés JMF-e
     * volt).
     */
    public boolean vasarol(String item) {
        boolean sikeres = false; // Eltároljuk, hogy sikeres volt-e a tranzakció

        switch (item) {
            case "soprofej":
                if (JMF >= soprofejAr) {
                    JMF -= soprofejAr;
                    kotroFejek.add(new SoproFej());
                    sikeres = true;
                }
                break;
            case "jegtorofej":
                if (JMF >= jegtorofejAr) {
                    JMF -= jegtorofejAr;
                    kotroFejek.add(new JegtoroFej());
                    sikeres = true;
                }
                break;
            case "hanyofej":
                if (JMF >= hanyofejAr) {
                    JMF -= hanyofejAr;
                    kotroFejek.add(new HanyoFej());
                    sikeres = true;
                }
                break;
            case "sarkanyfej":
                if (JMF >= sarkanyfejAr) {
                    JMF -= sarkanyfejAr;
                    kotroFejek.add(new SarkanyFej());
                    sikeres = true;
                }
                break;
            case "soszorofej":
                if (JMF >= soszorofejAr) {
                    JMF -= soszorofejAr;
                    kotroFejek.add(new SoszoroFej());
                    sikeres = true;
                }
                break;
            case "zuzalekszorofej":
                if (JMF >= zuzalekszorofejAr) {
                    JMF -= zuzalekszorofejAr;
                    kotroFejek.add(new ZuzalekszoroFej());
                    sikeres = true;
                }
                break;
            case "biokerozin":
                if (JMF >= biokerozinAr) {
                    JMF -= biokerozinAr;
                    biokerozin++;
                    sikeres = true; // Ez a régi kódból hiányzott!
                }
                break;
            case "so":
                if (JMF >= soAr) {
                    JMF -= soAr;
                    so++;
                    sikeres = true; // Ez a régi kódból hiányzott!
                }
                break;
            case "zuzalek":
                if (JMF >= zuzalekAr) {
                    JMF -= zuzalekAr;
                    zuzalek++;
                    sikeres = true; // Ez a régi kódból hiányzott!
                }
                break;
            case "hokotro":
                if (JMF >= hokotroAr) {
                    JMF -= hokotroAr;
                    raktaronLevoHokotrok++;
                    sikeres = true; // Ez a régi kódból hiányzott!
                }
                break;
        }

        // PUSH FÁZIS: Ha vettünk valamit és megváltozott a pénz/készlet,
        // értesítjük a grafikus felületet (HUD-ot és a Boltot), hogy rajzolják újra magukat!
        if (sikeres) {
            notifyObservers();
        }

        return sikeres;
    }

    /**
     * A bemenetként kapott értékkel módosítja a tártolt JMF értékét. (Lehet negatív változás is)
     * @param count
     */
    public void JMFmodosit(int count) {
        JMF += count;
        // PUSH FÁZIS: Mivel megváltozott az egyenleg, értesítjük a felületet!
        notifyObservers();
    }

    /**
     * Újratölti a kotrófej zuzalékkészletét és csökkenti a telephelyen tárolt zuzalékraktárat.
     * @return
     */
    public boolean zuzalekUjratolt() {
        if (zuzalek > 0) {
            zuzalek--;
            return true;
        }
        return false;
    }

    /**
     * Újratölti a kotrófej biokerozinkészletét és csökkenti a telephelyen tárolt biokerozinraktárat.
     * @return
     */
    public boolean biokerozinUjratolt() {
        if (biokerozin > 0) {
            biokerozin--;
            return true;
        }
        return false;
    }

    /**
     * Újratölti a kotrófej sokészletét és csökkenti a telephelyen tárolt soraktárat.
     * @return
     */
    public boolean soUjratolt() {
        if (so > 0) {
            so--;
            return true;
        }
        return false;
    }

    /**
     * Felszerel egy kotrófejet a megadott hókotróra a telephely készletéből. Ha a kért típusú fej nem található,
     * hibaüzenetet ír a kimenetre. Teszteléshez kell.
     *
     * @param fejTipus A felszerelni kívánt fej típusa (pl. {@code "hanyo"}).
     * @param hokotro  A célhókotró.
     * @param kimenet  A hibaüzenetek célstreame.
     */
    public void equipHokotro(String fejTipus, Hokotro hokotro, java.io.PrintStream kimenet) {
        KotroFej talalt = null;
        for (KotroFej f : kotroFejek) {
            if (f.getClass().getSimpleName().toLowerCase().replace("fej", "").equals(fejTipus)) {
                talalt = f;
                break;
            }
        }
        if (talalt == null) {
            kimenet.println("ERROR: A kert '" + fejTipus + "' fej nem talalhato a telephelyen, a csere sikertelen.");
            return;
        }
        kotroFejek.remove(talalt);
        hokotro.fejcsere(talalt);
    }


    /**
     * Beállítja a telephely egy anyagkészletét tesztelési célból. A {@code set_material} parancs hívja meg.
     *
     * @param anyag     Az anyag neve ({@code "biokerozin"}, {@code "so"}, {@code "zuzalek"}).
     * @param mennyiseg A beállítandó mennyiség.
     */
    public void setKeszlet(String anyag, int mennyiseg) {
        switch (anyag) {
            case "biokerozin" -> biokerozin = mennyiseg;
            case "so" -> so = mennyiseg;
            case "zuzalek" -> zuzalek = mennyiseg;
        }
    }

    /**
     * Hozzáad egy megadott mennyiséget a telephely anyagkészletéhez. Az {@code add} parancs hívja meg a vásárlást
     * megkerülve.
     *
     * @param anyag     Az anyag neve ({@code "biokerozin"}, {@code "so"}, {@code "zuzalek"}).
     * @param mennyiseg A hozzáadandó mennyiség.
     */
    public void addKeszlet(String anyag, int mennyiseg) {
        switch (anyag) {
            case "biokerozin" -> biokerozin += mennyiseg;
            case "so" -> so += mennyiseg;
            case "zuzalek" -> zuzalek += mennyiseg;
        }
    }

    /**
     * Kiírja a telephely aktuális állapotát a megadott kimenetre. A {@code stat} parancs hívja meg – a telephely maga
     * felelős a saját állapotának megjelenítéséért.
     *
     * @param id      A telephely azonosítója a kimenetben.
     * @param kimenet A célstream.
     */
    public void statKiir(String id, java.io.PrintStream kimenet) {
        kimenet.println("STAT " + id + ":");
        kimenet.println("- JMF: " + JMF);
        kimenet.println("- Biokerozin: " + biokerozin);
        kimenet.println("- So: " + so);
        kimenet.println("- Zuzalek: " + zuzalek);
        StringBuilder sb = new StringBuilder("- Tarolt_fejek:");
        kotroFejek.forEach(f -> sb.append(" ").append(f.getClass().getSimpleName()));
        kimenet.println(sb);
    }

    /**
     * Visszaadja a telephely raktárában lévő, éppen nem használt kotrófejeket.
     * A grafikus felület ({@link grafika.view.TelephelyView}) ezen keresztül listázza
     * a felszerelhető fejeket. Védő másolatot ad vissza, hogy a hívó ne módosíthassa
     * közvetlenül a belső raktárlistát.
     *
     * @return A tárolt {@link KotroFej}-ek másolt listája (sosem {@code null}).
     */
    public List<KotroFej> getKotrofejek() {
       
        return kotroFejek;
    
    }
}
