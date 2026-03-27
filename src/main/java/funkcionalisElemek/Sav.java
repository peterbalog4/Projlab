package funkcionalisElemek;
import jarmuvek.Jarmu;
import vezerles.Skeleton;

public class Sav {

    public int hoTakarit(){

        return 0;
    }

    public void jegFeltor(){

    }
    public void lezar(int kor){
        
    }
    public void elfogad(Jarmu j){
        
    }

    public void hatasAlkalmaz(Jarmu j){
        
    }

    public void mozgat(Jarmu j){
        Skeleton.hiv("s:Sav: mozgat(a)");

        boolean havasE = Skeleton.kerdez("Mély havas a sáv?");
        if (havasE){
            j.megall(2);
        }

        Skeleton.visszater("mozgat");
    }
    public void allapotFrissit(){
        
    }   

    public void hoNovel(int i){
        
    }
}
