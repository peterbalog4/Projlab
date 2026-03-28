package funkcionalisElemek;
import jarmuvek.Jarmu;
import kotrofejek.KotroFej;
import vezerles.Skeleton;

import java.util.ArrayList;
import java.util.List;

public class Sav {

    private List<Jarmu> jarmuvek = new ArrayList<>();
    private Ut uthozTartozik;
    protected String id;


    public Sav(String id) {
        this.id = id;
    }

    public String getId(){
        return this.id;
    }

    public void addJarmu(Jarmu j) {
        jarmuvek.add(j);
        j.setSav(this);
    }

    public void setUt(Ut u) {
        Skeleton.hiv( this.id + ":Sav: setUt(u)");
        this.uthozTartozik = u;
        Skeleton.visszater("setUt");
    }

    public Jarmu getMasikJarmu(Jarmu errolVanSzo) {
        for (Jarmu j : jarmuvek) {
            if (j != errolVanSzo) {
                return j;
            }
        }
        return null;
    }

    public Sav getMasikSav(Sav errolVanSzo){
        for (Sav s : uthozTartozik.getSavok()){
            if (s != errolVanSzo){
                return s;
            }
        }
        return null;
    }

    public void hoTakarit(){
        Skeleton.hiv(this.id + ":Sav: hoTakarit()");

        Sav s2 = getMasikSav(this);

        if (s2 != null){
            uthozTartozik.havatAtad(this, s2);
        } else{
            Skeleton.naploz("Nincs másik sáv, amire áttőlná a havat.");
        }



        Skeleton.visszater("hoTakarit");

    }

    public void jegFeltor(){

    }
    public void lezar(int kor){
        Skeleton.hiv(this.id + ":Sav: lezar(" + kor + ")");

        Skeleton.naploz("A sáv lezárva " + kor + " körre az ütközés miatt. Akadály alakult ki.");

        Skeleton.visszater("lezar");
    }
    public void elfogad(Jarmu j){
        
    }

    public void hatasAlkalmaz(Jarmu j){
        Skeleton.hiv(this.id + ":Sav: hatasAlkalmaz(a)");
            j.csuszik();
        Skeleton.visszater("hatasAlkalmaz");

    }

    public void mozgat(Jarmu j){
        Skeleton.hiv(this.id + ":Sav: mozgat(a)");

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
