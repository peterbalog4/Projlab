package funkcionalisElemek;

import jarmuvek.*;
import vezerles.Skeleton;

public class KorSzamlalo {

    public void leptet(){
        Skeleton.hiv("k:KorSzamlalo: leptet()");

        Auto a = new Auto();
        a.kozlekedik();

        Skeleton.visszater("leptet");
    }
}
