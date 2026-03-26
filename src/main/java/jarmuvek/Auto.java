package jarmuvek;


import vezerles.Skeleton;

public class Auto extends Jarmu {

    @Override
    public void kozlekedik(){
        Skeleton.hiv("a:Auto: kozlekedik()");

        boolean jegesE = Skeleton.kerdez("Jeges a sáv amin az autó halad?");

        if (jegesE) {
            csuszik();
        }

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
    
    
}
