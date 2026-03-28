package funkcionalisElemek;

import jarmuvek.*;
import vezerles.Skeleton;

import java.util.ArrayList;
import java.util.List;

public class KorSzamlalo {

    private List<Jarmu> jarmuvek = new ArrayList<>();
    private Ut aktivUt;
    public void setUt(Ut u) { this.aktivUt = u; }

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

        // Hóesés use-case logikája:
        if (aktivUt != null && Skeleton.kerdez("Történjen hóesés a kör végén?")) {
            aktivUt.hoNovel();
        }

        Skeleton.visszater("leptet");
    }
}
