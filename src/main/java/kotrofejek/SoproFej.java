package kotrofejek;

import funkcionalisElemek.Sav;
import funkcionalisElemek.Ut;
import vezerles.Skeleton;

public class SoproFej extends KotroFej{

    @Override
    public void takarit(Sav sav, Ut ut) {
        Skeleton.hiv("sopro:SoproFej: tisztit("+ sav.getId() + ", ut)");

        sav.hoTakarit();

        Skeleton.naploz("A sáv megtisztult! A hó eltűnt.");
        Skeleton.visszater("tisztit");
    }
    

}
