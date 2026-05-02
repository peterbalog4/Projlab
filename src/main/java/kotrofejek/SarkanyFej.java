package kotrofejek;

import funkcionalisElemek.Sav;
import funkcionalisElemek.Telephely;
import funkcionalisElemek.Ut;


/**
 * Elolvasztja a havat és a jeget a Hókotró előtt, viszont biokerozint használ a fej tárolójából, ami 10 lépés után
 * elfogy. Ha a takarító játékos kikapcsolta a fej működését, akkor nem takarít el semmit, de nem is  használ
 * biokerozint. Tárolja a biokerozin mennyiségét, ami a Hókotrónál van. Ha a játékos úgy cserél fejet, hogy a fejben még
 * volt biokerozin, akkor az nem veszik el, hiszen az már bele van töltve a fejbe.
 */
public class SarkanyFej extends KotroFej {

    /**
     * A Hókotróban aktuálisan tárolt biokerozin mennyiségét tartja számon. Ezt fogyasztja a fej a működése során.
     *
     */
    private int biokerozin = 0;
    /**
     * Tárolja, hogy a takarító játékos bekapcsolta-e a fej működését.
     */
    private boolean fejBekapcsolva = false;

    /**
     * Eltakarítja közvetlen a Hókotró előtt a jeget és a havat a Sávról.
     *
     * @param sav A takarítandó sáv.
     * @param ut  Az az út, amelyhez a sáv tartozik.
     * @return {@code true}, ha a takarítás eredményes volt és a sáv állapota javult, {@code false}, ha nem történt
     * változás (pl. üres tartály). A fizettség kiosztásához kell.
     */
    @Override
    public boolean takarit(Sav sav, Ut ut) {
        if (fejBekapcsolva && biokerozin > 0) {
            sav.jegFeltor();
            int eltakaritottMennyiseg = sav.hoTakarit(0);
            biokerozin--;
            return true;
        }
        return false;
    }

    /**
     * Újratölti a tárolt biokerozint a telephely raktárából.
     *
     * @param telephely
     */
    public void ujratolt(Telephely telephely) { //!!!! ez még nem jó telephely átadása
        if (telephely.biokerozinUjratolt())
            biokerozin = 10;
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
     * Beállítja a biokerozin készletet tesztelési célból.
     *
     * @param mennyiseg A beállítandó biokerozin mennyiség.
     */
    public void setBiokerozin(int mennyiseg) {
        this.biokerozin = mennyiseg;
    }

    /**
     * Kiírja a fej biokerozin készletét. Só és zúzalék nullás értékkel szerepel. Tesztekhez kell.
     *
     * @param kimenet A célstream.
     */
    @Override
    public void kiirKeszlet(java.io.PrintStream kimenet) {
        kimenet.println("- Biokerozin: " + biokerozin);
        kimenet.println("- So: 0");
        kimenet.println("- Zuzalek: 0");
    }

    /**
     * Megkísérli az újratöltést, és visszajelzi, hogy sikeres volt-e. Tesztekhez kell.
     *
     * @param telephely A készletet biztosító telephely.
     * @return {@code true}, ha volt elegendő biokerozin és a töltés megtörtént.
     */
    public boolean ujratoltEllenorzott(funkcionalisElemek.Telephely telephely) {
        if (telephely.biokerozinUjratolt()) {
            biokerozin = 10;
            return true;
        }
        return false;
    }
}
