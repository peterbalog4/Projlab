package funkcionalisElemek;
import kotrofejek.KotroFej;
import jarmuvek.Hokotro;



/**
 * A játékos bázisát és logisztikai központját reprezentáló osztály.
 * Felelős a Jégmentesítési Forint (JMF) egyenleg kezeléséért, a raktározott 
 * kotrófejek nyilvántartásáért, valamint a vásárlási és felszerelési folyamatok 
 * koordinálásáért
 */
public class Telephely {

    /** A telephelyhez kapcsolt bolt referenciája a vásárlásokhoz. */
    private Bolt bolt = new Bolt();

    /**
     * Vezényli a hókotró aktuális kotrófejének cseréjét egy raktáron lévőre.
     * @param ujFej A felszerelni kívánt új kotrófej
     * @param h A hókotró, amelyen a cserét végre kell hajtani[
     */
    public void useFej(KotroFej ujFej, Hokotro h) {

    }

    /**
     * Eltárolja a leszerelt kotrófejet a telephely raktárában.
     * @param regiFej A raktárba kerülő kotrófej objektum
     */
    public void tarolFej(KotroFej regiFej) {

    }

    /**
     * Kezdeményezi egy termék megvásárlását a boltból.
     * @param item A megvásárolni kívánt tárgy vagy eszköz neve
     */
    public void vasarol(String item) {

    }

    /**
     * Kezdeményezi egy termék megvásárlását a boltból.
     * @param item A megvásárolni kívánt tárgy vagy eszköz neve
     */
    public boolean JMFmodosit(int count) {
 
    return false;
    }
}
