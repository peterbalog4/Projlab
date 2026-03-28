package funkcionalisElemek;
import jarmuvek.Jarmu;
import kotrofejek.KotroFej;
import vezerles.Skeleton;

import java.util.ArrayList;
import java.util.List;

public class Sav {


    private int athaladtJarmuvek = 0;

    
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

    public Ut getUt() {
        return this.uthozTartozik;
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

        boolean atrakja = Skeleton.kerdez("A hókotrón lévő fej átrakja a havat másik sávra (pl. Hányó/Söprő)?");

        if (atrakja){
            Sav s2 = getMasikSav(this);
            if (s2 != null){
                uthozTartozik.havatAtad(this, s2);
            } else{
                Skeleton.naploz("Nincs másik sáv, amire áttőlná a havat.");
            }
        } else {
            Skeleton.naploz("A fej elolvasztotta a havat.");
        }



        Skeleton.visszater("hoTakarit");

    }

    public void jegFeltor(){
        Skeleton.hiv(this.id + ":Sav: jegFeltor()");

        Skeleton.naploz("A sávon fel lett törve a jég.");

        Skeleton.visszater("jegFeltor");
    }
    public void lezar(int kor){
        Skeleton.hiv(this.id + ":Sav: lezar(" + kor + ")");

        Skeleton.naploz("A sáv lezárva " + kor + " körre az ütközés miatt. Akadály alakult ki.");

        Skeleton.visszater("lezar");
    }
    public void elfogad(Jarmu j){
        Skeleton.hiv(this.id + ":Sav: elfogad(j)");
        
        boolean siker = Skeleton.kerdez("Be tudja fogadni a sáv a járművet?");
        if (siker) {
            Skeleton.naploz("A jármű sikeresen sávot váltott.");
            this.addJarmu(j);
        } else {
            Skeleton.naploz("A sávváltás meghiúsult.");
        }
        
        Skeleton.visszater("elfogad");
    }

    public void hatasAlkalmaz(Jarmu j){
        Skeleton.hiv(this.id + ":Sav: hatasAlkalmaz(a)");
            j.csuszik();
        Skeleton.visszater("hatasAlkalmaz");

    }

    public void mozgat(Jarmu j) {
        Skeleton.hiv(this.id + ":Sav: mozgat(a)");

        athaladtJarmuvek++;
        
        if (athaladtJarmuvek == 5) {
            this.allapotFrissit();
        }

        if (Skeleton.kerdez("Jeges a sáv?")) {
            hatasAlkalmaz(j);
        } 
        
        else if (Skeleton.kerdez("Mély havas a sáv?")) {
            j.megall(2);
        }

        Skeleton.visszater("mozgat");
    }
   public void hoNovel(int mennyiseg) {
        Skeleton.hiv(this.id + ":Sav: hoNovel(" + mennyiseg + ")");
        Skeleton.naploz("A hóréteg vastagsága nőtt a sávon.");
        Skeleton.visszater("hoNovel");
    }

    public void allapotFrissit() {
        Skeleton.hiv(this.id + ":Sav: allapotFrissit()");
        if (Skeleton.kerdez("Ez az ötödik jármű, ami áthalad a havas sávon?")) {
            Skeleton.naploz("A sáv állapota jegesre változott.");
        }
        Skeleton.visszater("allapotFrissit");
    }

}
