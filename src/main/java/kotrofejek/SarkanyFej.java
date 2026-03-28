package kotrofejek;

import funkcionalisElemek.Sav;
import funkcionalisElemek.Telephely;
import funkcionalisElemek.Ut;
import vezerles.Skeleton;

public class SarkanyFej extends KotroFej {

    private Telephely telephely;

    @Override
    public void takarit(Sav sav, Ut ut) {
        Skeleton.hiv("sarkany:SarkanyFej: tisztit("+ sav.getId() + ", ut)");

        sav.jegFeltor();
        sav.hoTakarit();

        Skeleton.visszater("tisztit");
    }

    public void setTelephely(Telephely t) {
    this.telephely = t;
    }

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
