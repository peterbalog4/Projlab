package vezerles;

import funkcionalisElemek.KorSzamlalo;
import funkcionalisElemek.Sav;
import jarmuvek.Auto;

public class TesztFuggvenyek {

    public static void tesztElakadas() {
        Skeleton.naploz("Inicializálás: Autó elakadása hóban");

        KorSzamlalo k = new KorSzamlalo();
        Auto a = new Auto("a");
        Sav s = new Sav();

        a.setSav(s);
        k.addJarmu(a);

        Skeleton.naploz("Teszt indítása");

        k.leptet();
    }

    public static void tesztUtkozes(){
        Skeleton.naploz("Inicializálás: Járművek ütközése jégen");

        KorSzamlalo k = new KorSzamlalo();
        Sav s = new Sav();

        Auto a1 = new Auto("a1");
        Auto a2 = new Auto("a2");

        s.addJarmu(a1);
        s.addJarmu(a2);

        k.addJarmu(a1);
        k.addJarmu(a2);

        Skeleton.naploz("Teszt indítása");

        k.leptet();

    }
}
