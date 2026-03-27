package jarmuvek;


import funkcionalisElemek.*;
import vezerles.Skeleton;

public class Auto extends Jarmu {

    public Auto(String id) {
        super(id);
    }

    @Override
    public void kozlekedik(){
        Skeleton.hiv(this.id + ":Auto: kozlekedik()");

        if (!mozgaskepes) {
            Skeleton.naploz("A jármű mozgásképtelen, ezért kimarad a körből.");
            Skeleton.visszater("kozlekedik");
            return;
        }

        if (aktualisSav != null) {
            aktualisSav.mozgat(this);
        }

        Skeleton.visszater("kozlekedik");
    }

    public void csuszik(){
        Skeleton.hiv(this.id + ":Auto: csuszik()");

        boolean vanMasikJarmu = Skeleton.kerdez("Van másik jármű a sávban, amivel ütközik?");

        if (vanMasikJarmu){
            Jarmu a2 = aktualisSav.getMasikJarmu(this);

            if (a2 != null) {
                this.utkozik(a2);
            } else {
                Skeleton.naploz("Bár akartunk ütközni, nincs másik jármű a sávban!");
            }
        } else {
            Skeleton.naploz("Az autó megcsúszott, de nem ütközött semmivel.");
        }

        Skeleton.visszater("csuszik");
    }

    @Override
    public void utkozik(Jarmu masikJarmu){
        Skeleton.hiv(this.id + ":Auto: utkozik()");

        Skeleton.naploz("A járművek összeütköztek és mozgásképtelenné váltak.");
        this.megall(10);
        masikJarmu.megall(10);

        if (aktualisSav != null) {
            aktualisSav.lezar(10);
        }

        Skeleton.visszater("utkozik");
    }

    public void megall(int korszam){
        Skeleton.hiv(this.id + ":Auto: megall(" + korszam + ")");
        this.mozgaskepes = false;
        Skeleton.visszater("megall");
    }
    
    
}
