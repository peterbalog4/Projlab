package jarmuvek;

import funkcionalisElemek.Sav;
import vezerles.Skeleton;

public abstract class Jarmu{

    protected String id;
    protected Sav aktualisSav;
    protected boolean mozgaskepes = true;

    public Jarmu(String id) {
        this.id = id;
    }

    public void setSav(Sav s){
        Skeleton.hiv(this.id + ":Jarmu: setSav(s)");
        this.aktualisSav = s;
        Skeleton.visszater("setSav");
    }

    public void kanyarodik(Irany i){

    }

    public abstract void kozlekedik();

    public void megall(int korszam){
        Skeleton.hiv(this.id + ":Jarmu: megall(" + korszam + ")");

        this.mozgaskepes = false;

        Skeleton.visszater("megall");
    }

    public void utkozik(Jarmu masikJarmu){

    }

    public void savvaltas(Irany i){ 

    }

    public void csuszik(){

    }

}
