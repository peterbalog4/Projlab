package kotrofejek;

import funkcionalisElemek.Sav;
import funkcionalisElemek.Telephely;
import funkcionalisElemek.Ut;


/**
 * A Sószóró típusú kotrófejet reprezentáló osztály.
 * Vegyi úton (só kiszórásával) szünteti meg a jegesedést és a hóréteget a sávon.
 * Működéséhez sóra van szüksége, amelynek fogytával a fej hatástalanná válik.
 * Az üzemanyag pótlása a Telephelyen keresztül történik.
 */
public class SoszoroFej extends KotroFej{

    /** A Telephely referenciája a sókészlet utánpótlásához. */
    private Telephely telephely;


    /**
     * Elvégzi a sáv jég- és hómentesítését sózással.
     * A folyamat során a sáv jégfeltörő és hótakarító funkciói is aktiválódnak, 
     * miközben a fej sókészlete csökken.
     * @param sav Az aktuálisan takarított sáv.
     * @param ut Az úthálózat referenciája.
     */
    @Override
    public void takarit(Sav sav, Ut ut) {
 
    }

    /**
     * Beállítja a fejhez tartozó telephelyet.
     * @param t A Telephely objektum, amely a sókészletet tárolja.
     */
    public void setTelephely(Telephely t) {
    this.telephely = t;
    }

    /**
     * Feltölti a fej sókészletét a Telephely raktárából.
     * Ellenőrzi, hogy rendelkezésre áll-e elegendő só a Telephelyen az újratöltéshez.
     */
    public void ujratolt() {
 
    }
}
