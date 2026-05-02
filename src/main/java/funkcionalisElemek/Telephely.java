package funkcionalisElemek;
import kotrofejek.*;
import jarmuvek.Hokotro;

import java.util.*;


public class Telephely {
    static private final int soprofejAr = 120000;
    static private final int jegtorofejAr = 250000;
    static private final int hanyofejAr = 200000;
    static private final int sarkanyfejAr = 600000;
    static private final int soszorofejAr = 400000;
    static private final int zuzalekszorofejAr = 400000;
    static private final int biokerozinAr = 5000;
    static private final int soAr = 8000;
    static private final int zuzalekAr = 5000;
    static private final int hokotroAr = 2000000;

    private final int jatekosID;
    private int biokerozin = 0;
    private int so = 0;
    private int zuzalek = 0;
    private int JMF = 0;
    private List<KotroFej> kotroFejek = new ArrayList<>();

    public Telephely(final int jatekosID) {
        this.jatekosID = jatekosID;
    }

    /**
     * Konstruktor kezdőtőkével, a {@code depot} parancshoz.
     *
     * @param jatekosID A tulajdonos játékos azonosítója.
     * @param kezdoJMF  A telephely kezdő JMF egyenlege.
     */
    public Telephely(final int jatekosID, int kezdoJMF) {
        this.jatekosID = jatekosID;
        this.JMF       = kezdoJMF;
    }

    public void useFej(KotroFej ujFej) { //kinda nem kell a hokotro
        kotroFejek.remove(ujFej);
    }

    public void tarol(KotroFej fej) {
        kotroFejek.add(fej);
    }

    public boolean vasarol(String item) {
        switch (item) {
            case "soprofej":
                if (JMF >= soprofejAr) {
                    JMF -= soprofejAr;
                    kotroFejek.add(new SoproFej());
                    return true;
                }
                break;
            case "jegtorofej":
                if (JMF >= jegtorofejAr) {
                    JMF -= jegtorofejAr;
                    kotroFejek.add(new JegtoroFej());
                    return true;
                }
                break;
            case "hanyofej":
                if (JMF >= hanyofejAr) {
                    JMF -= hanyofejAr;
                    kotroFejek.add(new HanyoFej());
                    return true;
                }
                break;
            case "sarkanyfej":
                if (JMF >= sarkanyfejAr) {
                    JMF -= sarkanyfejAr;
                    kotroFejek.add(new SarkanyFej());
                    return true;
                }
                break;
            case "soszorofej":
                if (JMF >= soszorofejAr) {
                    JMF -= soszorofejAr;
                    kotroFejek.add(new SoszoroFej());
                    return true;
                }
                break;
            case "zuzalekszorofej":
                if (JMF >= zuzalekszorofejAr) {
                    JMF -= zuzalekszorofejAr;
                    kotroFejek.add(new ZuzalekszoroFej());
                    return true;
                }
                break;
            case "biokerozin":
                if (JMF >= biokerozinAr) {
                    JMF -= biokerozinAr;
                    biokerozin++;
                }
                break;
            case "so":
                if (JMF >= soAr) {
                    JMF -= soAr;
                    so++;
                }
                break;
            case "zuzalek":
                if (JMF >= zuzalekAr) {
                    JMF -= zuzalekAr;
                    zuzalek++;
                }
                break;
            case "hokotro": //TODO: ez új object lesz main-ben
                if (JMF >= hokotroAr) {
                    JMF -= hokotroAr;
                }
                break;
        }
        return false;
    }


    public void JMFmodosit(int count) {
        JMF += count;
    }


    public boolean zuzalekUjratolt() {
        if(zuzalek>0) {
            zuzalek--;
            return true;
        }
        return false;
    }

    public boolean biokerozinUjratolt() {
        if(biokerozin>0) {
            biokerozin--;
            return true;
        }
        return false;
    }

    public boolean soUjratolt() {
        if(so>0) {
            so--;
            return true;
        }
        return false;
    }

    /**
     * Felszerel egy kotrófejet a megadott hókotróra a telephely készletéből.
     * Ha a kért típusú fej nem található, hibaüzenetet ír a kimenetre.
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
     * Beállítja a telephely egy anyagkészletét tesztelési célból.
     * A {@code set_material} parancs hívja meg.
     *
     * @param anyag     Az anyag neve ({@code "biokerozin"}, {@code "so"}, {@code "zuzalek"}).
     * @param mennyiseg A beállítandó mennyiség.
     */
    public void setKeszlet(String anyag, int mennyiseg) {
        switch (anyag) {
            case "biokerozin" -> biokerozin = mennyiseg;
            case "so"         -> so         = mennyiseg;
            case "zuzalek"    -> zuzalek    = mennyiseg;
        }
    }

        /**
     * Hozzáad egy megadott mennyiséget a telephely anyagkészletéhez.
     * Az {@code add} parancs hívja meg a vásárlást megkerülve.
     *
     * @param anyag     Az anyag neve ({@code "biokerozin"}, {@code "so"}, {@code "zuzalek"}).
     * @param mennyiseg A hozzáadandó mennyiség.
     */
    public void addKeszlet(String anyag, int mennyiseg) {
        switch (anyag) {
            case "biokerozin" -> biokerozin += mennyiseg;
            case "so"         -> so         += mennyiseg;
            case "zuzalek"    -> zuzalek    += mennyiseg;
        }
    }

        /**
     * Kiírja a telephely aktuális állapotát a megadott kimenetre.
     * A {@code stat} parancs hívja meg – a telephely maga felelős a saját
     * állapotának megjelenítéséért.
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
}
