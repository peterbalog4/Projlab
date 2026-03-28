package kotrofejek;

import funkcionalisElemek.Sav;
import funkcionalisElemek.Ut;
import vezerles.Skeleton;

public class JegtoroFej extends KotroFej {

    @Override
    public void takarit(Sav sav, Ut ut) {
        Skeleton.hiv("jegtoro:JegtoroFej: tisztit("+ sav.getId() + ", ut)");

        sav.jegFeltor();

        Skeleton.visszater("tisztit");
    }

}
