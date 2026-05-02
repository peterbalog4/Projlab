package kotrofejek;

import funkcionalisElemek.Sav;
import funkcionalisElemek.Ut;

/**
 * A Hókotrók különböző kotrófejeit fogja össze. Minden leszármazottnak meg kell valósítania a takarít() függvényt, ami
 * minden leszármazottnál különböző módon működik és elvégzi a leszármazott kotrófej akcióját a Sávra. Az
 * üzemanyagot(biokerozin, só, zuzalék) használó kotrófejek ki-be tudják kapcsolni azt, hogy takarítanak miközben
 * közlekednek, a többi fej viszont mindig elvégzi a takarítását miközben mozog.  A takarítást folyamatosan, a Hókotró
 * mozgása közben hajtja végre. Egy Hókotrón egyszerre egy kotrófej lehet felszerelve.
 */

public abstract class KotroFej {

    /**
     * Elvégzi a kotrófejre jellemző takarítási műveletet a megadott sávon.
     *
     * @param sav A takarítandó sáv.
     * @param ut  Az az út, amelyhez a sáv tartozik.
     * @return {@code true}, ha a takarítás eredményes volt és a sáv állapota javult, {@code false}, ha nem történt
     * változás (pl. üres tartály). A fizettség kiosztásához kell.
     */
    public abstract boolean takarit(Sav sav, Ut ut);

    /**
     * Tesztekhez kell, kiíratás
     *
     * @param kimenet
     */
    public void kiirKeszlet(java.io.PrintStream kimenet) {
        kimenet.println("- Biokerozin: 0");
        kimenet.println("- So: 0");
        kimenet.println("- Zuzalek: 0");
    }
}
