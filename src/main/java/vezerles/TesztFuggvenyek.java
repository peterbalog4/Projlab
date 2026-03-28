package vezerles;

import funkcionalisElemek.KorSzamlalo;
import funkcionalisElemek.Sav;
import funkcionalisElemek.Ut;
import jarmuvek.Auto;
import jarmuvek.Hokotro;
import kotrofejek.KotroFej;
import kotrofejek.SoproFej;

public class TesztFuggvenyek {

    public static void tesztElakadas() {
        Skeleton.naploz("Inicializálás: Autó elakadása hóban");

        KorSzamlalo k = new KorSzamlalo();
        Auto a = new Auto("a");
        Sav s = new Sav("s");

        a.setSav(s);
        k.addJarmu(a);

        Skeleton.naploz("Teszt indítása");

        k.leptet();
    }

    public static void tesztUtkozes(){
        Skeleton.naploz("Inicializálás: Járművek ütközése jégen");

        KorSzamlalo k = new KorSzamlalo();
        Sav s = new Sav("s");

        Auto a1 = new Auto("a1");
        Auto a2 = new Auto("a2");

        s.addJarmu(a1);
        s.addJarmu(a2);

        k.addJarmu(a1);
        k.addJarmu(a2);

        Skeleton.naploz("Teszt indítása");

        k.leptet();

    }
    public static void tesztHokotroTakaritas(){
        Skeleton.naploz("Inicializálás: Hókotró utat takarít");

        Sav s1 = new Sav("s1");
        Sav s2 = new Sav("s2");
        Ut u = new Ut();

        Hokotro h1 = new Hokotro("h1");
        KotroFej fej = new SoproFej();

        h1.setKotrofej(fej);
        h1.setUt(u);
        h1.setSav(s1);

        u.addSav(s1);
        u.addSav(s2);

        s1.addJarmu(h1);
        s1.setUt(u);
        s2.setUt(u);

        Skeleton.naploz("--- Teszt indítása ---");
        h1.dolgozik();
    }

}
