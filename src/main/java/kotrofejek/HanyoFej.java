package kotrofejek;

import funkcionalisElemek.Sav;
import funkcionalisElemek.Ut;
import vezerles.Skeleton;

public class HanyoFej extends KotroFej {

    @Override
    public void takarit(Sav sav, Ut ut) {
        Skeleton.hiv("hanyo:HanyoFej: tisztit("+ sav.getId() + ", ut)");

        sav.hoTakarit();

        Skeleton.naploz("A sáv megtisztult! A hó eltűnt.");
        Skeleton.visszater("tisztit");
    }

}
