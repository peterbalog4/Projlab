package jarmuvek;

import funkcionalisElemek.Sav;
import vezerles.Skeleton;

public class Busz extends Jarmu {


    private int forduloSzam = 0;

    public Busz(String id) {
        super(id);
    }
    public void csuszik(){

    }

    @Override
    public void kozlekedik(){
        Skeleton.hiv(this.id + ":Busz: kozlekedik()");

        if (Skeleton.kerdez("Végállomáshoz ért a busz?")) {
            this.forduloNovel();
        }

        Skeleton.visszater("kozlekedik");
    }

    @Override
    public void utkozik(Jarmu masikJarmu){

    }
    

    public void forduloNovel() {
        Skeleton.hiv(this.id + ":Busz: forduloNov()");
        this.forduloSzam++;
        Skeleton.naploz("A busz teljesített egy fordulót. Aktuális fordulók: " + forduloSzam);
        Skeleton.naploz("A játékos pontszáma növekedett.");
        Skeleton.visszater("forduloNov");
    }

}
