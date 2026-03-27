package jarmuvek;


import funkcionalisElemek.*;
import vezerles.Skeleton;

public class Auto extends Jarmu {

    @Override
    public void kozlekedik(){
        Skeleton.hiv("a:Auto: kozlekedik()");

        Sav s = new Sav();
        s.mozgat(this);
/*
        boolean jegesE = Skeleton.kerdez("Jeges a sáv amin az autó halad?");

        if (jegesE) {
            csuszik();
        }
*/
        Skeleton.visszater("kozlekedik");
    }

    public void csuszik(){
        Skeleton.hiv("a:Auto: csuszik()");

        Skeleton.visszater("csuszik");
    }

    @Override
    public void utkozik(Jarmu masikJarmu){
        Skeleton.hiv("a:Auto: utkozik()");

        Skeleton.visszater("utkozik");
    }

    public void megall(int korszam){
        Skeleton.hiv("a:Auto: megall(" + korszam + ")");
        Skeleton.naploz("Az autó elakadt a hóban.");
        Skeleton.visszater("megall");
    }
    
    
}
