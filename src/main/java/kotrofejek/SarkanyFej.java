package kotrofejek;

import funkcionalisElemek.Sav;
import funkcionalisElemek.Telephely;
import funkcionalisElemek.Ut;
import vezerles.Skeleton;

/**
 * A Sárkány típusú kotrófejet reprezentáló osztály.
 * Ez a munkaeszköz biokerozin égetésével azonnal elolvasztja mind a havat, 
 * mind a jeget a sávon. Működése közben üzemanyagot fogyaszt, amelynek 
 * kifogyása esetén a fej hatástalanná válik.
 */
public class SarkanyFej extends KotroFej {

    /** A Telephely referenciája az üzemanyag újratöltéséhez. */
    private Telephely telephely;

    /**
     * Elvégzi a sáv komplex megtisztítását (hó és jég olvasztása).
     * A folyamat során a sáv jégfeltörő és hótakarító metódusai is lefutnak, 
     * miközben a hókotró biokerozin mennyisége csökken.
     * @param sav Az aktuálisan takarított sáv.
     * @param ut Az úthálózat referenciája.
     */
    @Override
    public void takarit(Sav sav, Ut ut) {
        Skeleton.hiv("sarkany:SarkanyFej: tisztit("+ sav.getId() + ", ut)");

        sav.jegFeltor();
        sav.hoTakarit();

        Skeleton.visszater("tisztit");
    }

    /**
     * Beállítja a fejhez tartozó telephelyet.
     * @param t A Telephely objektum, ahonnan az üzemanyagot vételezi a fej.
     */
    public void setTelephely(Telephely t) {
    this.telephely = t;
    }

    /**
     * Feltölti a fej biokerozin készletét a Telephelyről.
     * Ellenőrzi, hogy a Telephelyen rendelkezésre áll-e a szükséges nyersanyag. 
     * Sikeres töltés után a fej ismét működőképessé válik.
     */
<<<<<<< HEAD
=======
    
>>>>>>> 547a13a (ripped out the skeleton)
    public void ujratolt() {
        Skeleton.hiv("sarkany:SarkanyFej: ujratolt()");
        
        boolean vanEleg = Skeleton.kerdez("Van elegendő biokerozin a Telephelyen?");
        
        if (vanEleg) {
            Skeleton.naploz("A Sárkány fej feltöltődött és újra működőképes."); 
        } else {
            Skeleton.naploz("Nincs elég anyag, az újratöltés sikertelen.");
        }
        
        Skeleton.visszater("ujratolt");
    }
}
