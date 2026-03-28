package funkcionalisElemek;

import jarmuvek.Irany;
import jarmuvek.Jarmu;
import vezerles.Skeleton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Ut {

    private List<Sav> savok = new ArrayList<>();

    public void addSav(Sav s) {
        Skeleton.hiv("u:Ut: addSav(s)");
        savok.add(s);
        Skeleton.visszater("addSav");
    }

    public List<Sav> getSavok() {
        return Collections.unmodifiableList(savok);
    }

    public void kanyarodik(Jarmu j, Irany i){

    }
    public void hoNovel(){
        Skeleton.hiv("u:Ut: hoNovel()");
        for (Sav s : savok) {
            s.hoNovel(1);
        }
        Skeleton.visszater("hoNovel");
    }

    public void jarmuSavotValt(Jarmu j, Irany i){
        Skeleton.hiv("u:Ut: jarmuSavotValt(j, i)");
        
        if (savok.size() > 1) {
            Sav celSav = savok.get(1); 
            celSav.elfogad(j);
        }
        
        Skeleton.visszater("jarmuSavotValt");
    }

    public void havatAtad(Sav honnan, Sav hova){
        Skeleton.hiv("u:Ut: havatAtad("+ honnan.getId() + ", " + hova.getId() +")");

        Skeleton.naploz(honnan.getId() + " sávról átkerült a hó a " + hova.getId() + " sávra");

        Skeleton.visszater("havatAtad");


    }

}
