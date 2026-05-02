package kotrofejek;

import funkcionalisElemek.Sav;
import funkcionalisElemek.Telephely;
import funkcionalisElemek.Ut;

/**
 * Felsózza a Sávot, ennek hatására a jég a sózás utáni körben elolvad és 5 körig nem tud új hó esni a Sávra. A felsózás
 * sót használ a fej tárolójából, ami 10 lépés után elfogy. Ha a takarító játékos kikapcsolta a fej működését, akkor nem
 * szór le sót, de nem is használja el. Ha a játékos úgy cserél fejet, hogy a fejben még volt só, akkor az nem veszik
 * el, hiszen az már bele van töltve a fejbe.
 */

public class SoszoroFej extends KotroFej {
    /**
     * A Hókotróban aktuálisan tárolt só mennyiségét tartja számon.
     */
    private int so = 10;
    /**
     * Tárolja, hogy a takarító játékos bekapcsolta-e a fej működését.
     */
    private boolean fejBekapcsolva = false;

    /**
     * Felsózza a Sávot. Aminek a hatására a sávon 5 körig nem tud új hó esni és elolvad 1 kör alatt az eddig rajta lévő
     * hó.
     *
     * @param sav A takarítandó sáv.
     * @param ut  Az az út, amelyhez a sáv tartozik.
     * @return {@code true}, ha a takarítás eredményes volt és a sáv állapota javult, {@code false}, ha nem történt
     * változás (pl. üres tartály). A fizettség kiosztásához kell.
     */
    @Override
    public boolean takarit(Sav sav, Ut ut) {
        if (fejBekapcsolva && so > 0) {
            sav.soSzor();
            so--;
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
     * Beállítja a só mennyiségét.
     * Teszthez kell.
     *
     * @param mennyiseg Kezdetben megadott só mennyisége
     */
    public void setSo(int mennyiseg) {
        this.so = mennyiseg;
    }

    /**
     * Kiírja a fej só készletét. Biokerozin és zúzalék nullás értékkel szerepel. Teszthez kell.
     *
     * @param kimenet A célstream.
     */
    @Override
    public void kiirKeszlet(java.io.PrintStream kimenet) {
        kimenet.println("- Biokerozin: 0");
        kimenet.println("- So: " + so);
        kimenet.println("- Zuzalek: 0");
    }

    /**
     * Újratölti a tárolt sót a telephely raktárából.
     * @param telephely
     */
    public void ujratolt(funkcionalisElemek.Telephely telephely) {
        if (telephely.soUjratolt())
            so = 10;
    }

    /**
     * Megkísérli az újratöltést, és visszajelzi, hogy sikeres volt-e. Tesztekhez kell.
     *
     * @param telephely A készletet biztosító telephely.
     * @return {@code true}, ha volt elegendő só és a töltés megtörtént.
     */
    public boolean ujratoltEllenorzott(funkcionalisElemek.Telephely telephely) {
        if (telephely.soUjratolt()) {
            so = 10;
            return true;
        }
        return false;
    }


}
