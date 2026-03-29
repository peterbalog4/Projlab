package funkcionalisElemek;
import jarmuvek.Jarmu;
import kotrofejek.KotroFej;
import vezerles.Skeleton;

import java.util.ArrayList;
import java.util.List;


/**
 * Az úthálózat egy egységnyi szakaszát reprezentáló osztály.
 * Felelős a rajta tartózkodó járművek nyilvántartásáért, a környezeti állapotok 
 * (hómennyiség, jegesedés) kezeléséért, valamint a járművekre gyakorolt hatások 
 * (megállás, megcsúszás) kiváltásáért.
 */
public class Sav {


    private int athaladtJarmuvek = 0;

    
    private boolean isJeges = false;
    private boolean vanHo = false;
    private List<Jarmu> jarmuvek = new ArrayList<>();
    private Ut uthozTartozik;
    protected String id;


    /**
     * Konstruktor a sáv létrehozásához.
     * @param id A sáv egyedi azonosítója a naplózáshoz.
     */
    public Sav(String id) {
        this.id = id;
    }

    /**
     * Visszaadja a sáv azonosítóját.
     * @return A sáv String formátumú azonosítója.
     */
    public String getId(){
        return this.id;
    }

    /**
     * Járművet ad a sávhoz (pl. inicializáláskor vagy sikeres sávváltáskor).
     * @param j A hozzáadni kívánt jármű.
     */
    public void addJarmu(Jarmu j) {
        jarmuvek.add(j);
        j.setSav(this);
    }

    /**
     * Visszaadja az utat, amelyhez a sáv tartozik.
     * @return A tartalmazó Út objektum.
     */
    public void setUt(Ut u) {
        Skeleton.hiv( this.id + ":Sav: setUt(u)");
        this.uthozTartozik = u;
        Skeleton.visszater("setUt");
    }

    public Ut getUt() {
        return this.uthozTartozik;
    }

    /**
     * Megkeres egy másik járművet a sávban ütközés detektálásához.
     * @param errolVanSzo A vizsgált jármű, akit ki kell hagyni a keresésből.
     * @return Egy másik Jármű a sávban, vagy null, ha nincs senki más.
     */
    public Jarmu getMasikJarmu(Jarmu errolVanSzo) {
        for (Jarmu j : jarmuvek) {
            if (j != errolVanSzo) {
                return j;
            }
        }
        return null;
    }


    /**
     * Megkeres egy másik sávot a sávban ütközés detektálásához.
     * @param errolVanSzo A vizsgált sáv, akit ki kell hagyni a keresésből.
     * @return Egy másik sáv, vagy null, ha nincs senki más.
     */
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

    /**
     * Lezárja a sávot adott ideig (pl. baleset miatt).
     * @param kor A lezárás időtartama körökben.
     */
    public void lezar(int kor){
        Skeleton.hiv(this.id + ":Sav: lezar(" + kor + ")");

        Skeleton.naploz("A sáv lezárva " + kor + " körre az ütközés miatt. Akadály alakult ki.");

        Skeleton.visszater("lezar");
    }

    /**
     * Megvizsgálja, hogy a sáv be tud-e fogadni egy járművet (pl. sávváltásnál).
     * @param j A belépni kívánó jármű.
     */
    public void elfogad(Jarmu j){
        Skeleton.hiv(this.id + ":Sav: elfogad(" + j.getId()+ ")");
        
        boolean siker = Skeleton.kerdez("Be tudja fogadni a sáv a járművet?");
        if (siker) {
            Skeleton.naploz("A jármű sikeresen sávot váltott.");
            this.addJarmu(j);
        } else {
            Skeleton.naploz("A sávváltás meghiúsult.");
        }
        
        Skeleton.visszater("elfogad");
    }

    /**
     * Kiváltja a jármű megcsúszását jeges sáv esetén.
     * @param j Az érintett jármű.
     */
    public void hatasAlkalmaz(Jarmu j){
        Skeleton.hiv(this.id + ":Sav: hatasAlkalmaz("+ j.getId()+ ")");
            j.csuszik();
        Skeleton.visszater("hatasAlkalmaz");

    }

    /**
     * Kezeli a jármű áthaladását a sávon. 
     * Számolja az áthaladásokat az eljegesedéshez, és alkalmazza a sáv aktuális 
     * állapotából adódó hatásokat a járműre.
     * @param j A sávon éppen áthaladó jármű.
     */

    public void mozgat(Jarmu j) {
        Skeleton.hiv(this.id + ":Sav: mozgat(a)");


        if (vanHo && !isJeges) {
            athaladtJarmuvek++;
            if (athaladtJarmuvek == 1) {
                this.allapotFrissit();
            }
        }

        if (Skeleton.kerdez("Mély havas a sáv?")) {
            j.megall(-1);
        }
        else if (Skeleton.kerdez("Jeges a sáv?")) {
            hatasAlkalmaz(j);
        }

        Skeleton.visszater("mozgat");
    }

    /**
     * Növeli a sávon található hó mennyiségét.
     * @param mennyiseg A hóréteg növekedésének mértéke.
     */
   public void hoNovel(int mennyiseg) {
        Skeleton.hiv(this.id + ":Sav: hoNovel(" + mennyiseg + ")");
        this.vanHo = true;
        Skeleton.naploz("A sáv havas lett.");
        Skeleton.visszater("hoNovel");
    }

    /**
     * Frissíti a sáv belső állapotát. 
     * Ha több jármű áthaladt a havas sávon, a letaposott hó jéggé válik.
     */
    public void allapotFrissit() {
        Skeleton.hiv(this.id + ":Sav: allapotFrissit()");
        Skeleton.naploz("A hó jéggé fagyott a sávon.");
        this.isJeges = true; 
        this.vanHo = false;  
        Skeleton.visszater("allapotFrissit");
    }

}
