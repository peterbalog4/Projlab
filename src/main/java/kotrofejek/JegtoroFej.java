package kotrofejek;

import funkcionalisElemek.Sav;
import funkcionalisElemek.Ut;


/**
 * Feltöri a jeget a Sávról, viszont nem takarítja le azt. Ennek következtében a Sáv csúszós marad, viszont már egy
 * Söprő vagy egy Hányó fej is el tudja takarítani a jégtörmeléket.
 */
public class JegtoroFej extends KotroFej {

    /**
     * Feltöri a jeget a Sávról.
     * @param sav A takarítandó sáv.
     * @param ut  Az az út, amelyhez a sáv tartozik.
     * @return {@code true}, ha a takarítás eredményes volt és a sáv állapota javult, {@code false}, ha nem történt
     * változás (pl. üres tartály). A fizettség kiosztásához kell.
     */
    @Override
    public boolean takarit(Sav sav, Ut ut) {
        sav.jegFeltor();
        return true;
    }

}
