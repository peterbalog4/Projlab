package kotrofejek;

import funkcionalisElemek.*;

/**
 * A Sávra zuzalékot szór, aminek hatására a jeges Sáv csúszóssága elmúlik. Ha a takarító játékos kikapcsolta a fej
 * működését, akkor nem szór zuzalékot, de nem is használja el. Azonban a zuzalékot a Hányó fej és Söprő fej
 * eltakarítja, ha a Sávot takarítják. Ekkor a jeges Sáv újra csúszós lesz. A zuzalékra rá tud esni hó, ami betemeti,
 * viszont a Sáv továbbra sem lesz csúszós. Ha már a ráesett hórétegre állna fennt a jegesedés, akkor a zuzalék belefagy
 * a jégbe, eltűnik a Sávról és megszűnik a hatása is, viszont ezt az úgymond 2 jégréteget is a megfelelő kotrófejek 1
 * takarítással fel tudják törni/el tudják takarítani. A zuzalék szórása fogyasztja a Hókotróban tárolt zuzalék
 * mennyiségét, ami 10 lépés után elfogy. Ha a játékos úgy cserél fejet, hogy a fejben még volt zuzalék, akkor az nem
 * veszik el, hiszen az már bele van töltve a fejbe.
 */
public class ZuzalekszoroFej extends KotroFej {
    /**
     * A Hókotróban aktuálisan tárolt zuzalék mennyiségét tartja számon.
     */
    private int zuzalek = 10;

    /**
     * Tárolja, hogy a takarító játékos bekapcsolta-e a fej működését.
     */
    private boolean fejBekapcsolva = false;

    /**
     *
     * @param sav A takarítandó sáv.
     * @param ut  Az az út, amelyhez a sáv tartozik.
     * @return {@code true}, ha a takarítás eredményes volt és a sáv állapota javult, {@code false}, ha nem történt
     * változás (pl. üres tartály). A fizettség kiosztásához kell.
     */
    @Override
    public boolean takarit(Sav sav, Ut ut) {
        if (fejBekapcsolva && zuzalek > 0) {
            sav.zuzalekSzor();
            zuzalek--;
            return true;
        }
        return false;
    }

    /**
     * Bekapcsolja a fej működését.
     */
    public void bekapcsol() {
        fejBekapcsolva = true;
    }

    /**
     * Kikapcsolja a fej működését.
     */
    public void kikapcsol() {
        fejBekapcsolva = false;
    }

    /**
     * Beállítja a zúzalék készletet tesztelési célból.
     *
     * @param mennyiseg A beállítandó zúzalék mennyiség.
     */
    public void setZuzalek(int mennyiseg) {
        this.zuzalek = mennyiseg;
    }

    /**
     * Kiírja a fej zúzalék készletét. Biokerozin és só nullás értékkel szerepel.
     *
     * @param kimenet A célstream.
     */
    @Override
    public void kiirKeszlet(java.io.PrintStream kimenet) {
        kimenet.println("- Biokerozin: 0");
        kimenet.println("- So: 0");
        kimenet.println("- Zuzalek: " + zuzalek);
    }

    /**
     * Újratölti a tárolt zuzalékot a telephely raktárából.
     *
     * @param telephely
     */
    public void ujratolt(Telephely telephely) {
        if (telephely.zuzalekUjratolt())
            zuzalek = 10;
    }

    /**
     * Megkísérli az újratöltést, és visszajelzi, hogy sikeres volt-e. A tesztekhez kell.
     *
     * @param telephely A készletet biztosító telephely.
     * @return {@code true}, ha volt elegendő zúzalék és a töltés megtörtént.
     */
    public boolean ujratoltEllenorzott(Telephely telephely) {
        if (telephely.zuzalekUjratolt()) {
            zuzalek = 10;
            return true;
        }
        return false;
    }
}