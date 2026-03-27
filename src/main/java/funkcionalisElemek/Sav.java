package funkcionalisElemek;
import jarmuvek.Jarmu;
import vezerles.Skeleton;

import java.util.ArrayList;
import java.util.List;

public class Sav {

    private List<Jarmu> jarmuvek = new ArrayList<>();

    public void addJarmu(Jarmu j) {
        jarmuvek.add(j);
        j.setSav(this);
    }

    public Jarmu getMasikJarmu(Jarmu errolVanSzo) {
        for (Jarmu j : jarmuvek) {
            if (j != errolVanSzo) {
                return j;
            }
        }
        return null;
    }

    public int hoTakarit(){

        return 0;
    }

    public void jegFeltor(){

    }
    public void lezar(int kor){
        Skeleton.hiv("s:Sav: lezar(" + kor + ")");

        Skeleton.naploz("A sáv lezárva " + kor + " körre az ütközés miatt. Akadály alakult ki.");

        Skeleton.visszater("lezar");
    }
    public void elfogad(Jarmu j){
        
    }

    public void hatasAlkalmaz(Jarmu j){
        Skeleton.hiv("s:Sav: hatasAlkalmaz(a)");
            j.csuszik();
        Skeleton.visszater("hatasAlkalmaz");

    }

    public void mozgat(Jarmu j){
        Skeleton.hiv("s:Sav: mozgat(a)");

        boolean havasE = Skeleton.kerdez("Mély havas a sáv?");

        if (havasE){
            j.megall(2);
        } else {
            if(Skeleton.kerdez("Jeges a sáv?")){
                hatasAlkalmaz(j);
            }
        }

        Skeleton.visszater("mozgat");
    }
    public void allapotFrissit(){
        
    }   

    public void hoNovel(int i){
        
    }
}
