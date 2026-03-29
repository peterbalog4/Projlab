package funkcionalisElemek;
import kotrofejek.KotroFej;
import vezerles.Skeleton;
import jarmuvek.Hokotro;
import vezerles.Skeleton;


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
        Skeleton.hiv("telep:Telephely: useFej(fej, kotro)");
        
        boolean vanRaktaron = Skeleton.kerdez("A kért kotrófej raktáron van a Telephelyen?");
        
        if (vanRaktaron) {
            h.fejcsere(ujFej);
            
            this.tarolFej(ujFej); 
        } else {
            Skeleton.naploz("A kért fej nem található a raktárban.");
        }
        
        Skeleton.visszater("useFej");
    }

    /**
     * Eltárolja a leszerelt kotrófejet a telephely raktárában.
     * @param regiFej A raktárba kerülő kotrófej objektum
     */
    public void tarolFej(KotroFej regiFej) {
        Skeleton.hiv("telep:Telephely: tarol(eddigiFej)");
        Skeleton.naploz("A régi kotrófej bekerült a Telephely raktárába.");
        Skeleton.visszater("tarol");
    }

    /**
     * Kezdeményezi egy termék megvásárlását a boltból.
     * @param item A megvásárolni kívánt tárgy vagy eszköz neve
     */
    public void vasarol(String item) {
        Skeleton.hiv("t:Telephely: vasarol(" + item + ")");
        
        bolt.vasarol(this, item);
        
        Skeleton.visszater("vasarol");
    }

    /**
     * Kezdeményezi egy termék megvásárlását a boltból.
     * @param item A megvásárolni kívánt tárgy vagy eszköz neve
     */
    public boolean JMFmodosit(int count) {
        Skeleton.hiv("t:Telephely: JMFmodosit(" + count + ")");
        boolean siker = true;
        
        if (count < 0) {
            if (Skeleton.kerdez("Van elegendő JMF a vásárláshoz?")) {
                Skeleton.naploz("A JMF levonása sikeres.");
            } else {
                Skeleton.naploz("Hiba: Nincs elég JMF, a vásárlás megszakadt.");
                siker = false;
            }
        } else {
            Skeleton.naploz("JMF hozzáadva.");
        }
        
        Skeleton.visszater("JMFmodosit", String.valueOf(siker));
        return siker;
    }
}
