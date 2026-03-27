package funkcionalisElemek;

import jarmuvek.*;
import vezerles.Skeleton;

import java.util.ArrayList;
import java.util.List;

public class KorSzamlalo {

    private List<Jarmu> jarmuvek = new ArrayList<>();

    public void addJarmu(Jarmu j) {
        Skeleton.hiv("k:KorSzamlalo: addJarmu(j)");
        jarmuvek.add(j);
        Skeleton.visszater("addJarmu");
    }

    public void leptet(){
        Skeleton.hiv("k:KorSzamlalo: leptet()");

        for (Jarmu j : jarmuvek) {
            j.kozlekedik();
        }

        Skeleton.visszater("leptet");
    }
}
