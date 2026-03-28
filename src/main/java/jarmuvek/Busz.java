package jarmuvek;

import funkcionalisElemek.Sav;
import vezerles.Skeleton;

public class Busz extends Jarmu {

    public Busz(String id) {
        super(id);
    }
    public void csuszik(){

    }

    @Override
    public void kozlekedik(){
        Skeleton.hiv(this.id + ":Busz: kozlekedik()");

        Skeleton.visszater("kozlekedik");
    }

    @Override
    public void utkozik(Jarmu masikJarmu){

    }
    
    //forduloNov volt, gond ha átírtam novelre? jobban érthető imo
    public void forduloNovel(){

    }

}
