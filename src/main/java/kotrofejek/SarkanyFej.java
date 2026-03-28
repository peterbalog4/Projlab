package kotrofejek;

import funkcionalisElemek.Sav;
import funkcionalisElemek.Ut;
import vezerles.Skeleton;

public class SarkanyFej extends KotroFej {

    @Override
    public void takarit(Sav sav, Ut ut) {
        Skeleton.hiv("sarkany:SarkanyFej: tisztit("+ sav.getId() + ", ut)");

        sav.jegFeltor();
        sav.hoTakarit();

        Skeleton.visszater("tisztit");
    }

    public void ujratolt(){
        
    }
}
