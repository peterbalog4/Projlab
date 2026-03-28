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

    public static void tesztJMFModositas() {
        Skeleton.naploz("Inicializálás: JMF módosítása telephelyen");

        Telephely t = new Telephely();
    
        int valasztas = Skeleton.kerdezOpcio("Mit szeretnél tesztelni a JMF-fel? \n1=Hozzáadás (pozitív érték) \n2=Levonás (negatív érték)", 2);
        
        int modositoErtek = 0;
        if (valasztas == 1) {
            modositoErtek = 100; 
        } else if (valasztas == 2) {
            modositoErtek = -50;
        }

        Skeleton.naploz("--- Teszt indítása ---");
        
        try {
            t.JMFmodosit(modositoErtek);
        } catch (Exception e) {
            Skeleton.naploz("Tranzakció megszakítva. Kivétel elkapva: " + e.getMessage());
        }
    }

    public static void tesztFejcsere() {
        Skeleton.naploz("Inicializálás: Fejcsere a telephelyen");

        Telephely t = new Telephely();
        Hokotro h = new Hokotro("h1");

        KotroFej regiFej = new HanyoFej();
        KotroFej ujFej = new SarkanyFej();

        h.setKotrofej(regiFej);
        
        Skeleton.naploz("--- Teszt indítása ---");
        
        t.useFej(ujFej, h);
    }

    public static void tesztVasarlas() {
        Skeleton.naploz("Inicializálás: Vásárlás a boltból");
        
        Telephely t = new Telephely();
        
        int valasztas = Skeleton.kerdezOpcio("Mit szeretnél vásárolni a boltban? \n1=Hányó fej \n2=Jégtörő fej \n3=Sárkány fej \n4=Söprő fej \n5=Sószóró fej \n6=Biokerozin \n7=Só", 7);
        
        String item = "";
        switch (valasztas) {
            case 1: item = "Hányó fej"; break;
            case 2: item = "Jégtörő fej"; break;
            case 3: item = "Sárkány fej"; break;
            case 4: item = "Söprő fej"; break;
            case 5: item = "Sószóró fej"; break;
            case 6: item = "Biokerozin"; break;
            case 7: item = "Só"; break;
        }

        Skeleton.naploz("--- Teszt indítása ---");
        
        t.vasarol(item);
    }

}
