package vezerles;

import funkcionalisElemek.*;
import jarmuvek.*;
import kotrofejek.*;

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
        int valasztottFej = Skeleton.kerdezOpcio("Milyen fej legyen a Hókotrón? \n1=hányó \n2=Jégtörő \n3=Sárkány \n4=Söprő \n5=Sószóró\n", 5);

        Sav s1 = new Sav("s1");
        Sav s2 = new Sav("s2");
        Ut u = new Ut();

        Hokotro h1 = new Hokotro("h1");

        KotroFej hanyo = new HanyoFej();
        KotroFej jeg = new JegtoroFej();
        KotroFej sarkany = new SarkanyFej();
        KotroFej sopro = new SoproFej();
        KotroFej so = new SoszoroFej();

        switch (valasztottFej){
            case 1:
                h1.setKotrofej(hanyo);
                break;
            case 2:
                h1.setKotrofej(jeg);
                break;
            case 3:
                h1.setKotrofej(sarkany);
                break;
            case 4:
                h1.setKotrofej(sopro);
                break;
            case 5:
                h1.setKotrofej(so);
                break;
        }


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
