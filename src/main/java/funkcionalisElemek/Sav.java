package funkcionalisElemek;
import jarmuvek.Jarmu;
import kotrofejek.KotroFej;
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

    }

    /**
     * Visszaadja az utat, amelyhez a sáv tartozik.
     * @return A tartalmazó Út objektum.
     */
    public void setUt(Ut u) {

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

        return null;
    }


    /**
     * Megkeres egy másik sávot a sávban ütközés detektálásához.
     * @param errolVanSzo A vizsgált sáv, akit ki kell hagyni a keresésből.
     * @return Egy másik sáv, vagy null, ha nincs senki más.
     */
    public Sav getMasikSav(Sav errolVanSzo){

        return null;
    }

    public void hoTakarit(){


    }

    public void jegFeltor(){

    }

    /**
     * Lezárja a sávot adott ideig (pl. baleset miatt).
     * @param kor A lezárás időtartama körökben.
     */
    public void lezar(int kor){

    }

    /**
     * Megvizsgálja, hogy a sáv be tud-e fogadni egy járművet (pl. sávváltásnál).
     * @param j A belépni kívánó jármű.
     */
    public void elfogad(Jarmu j){

    }

    /**
     * Kiváltja a jármű megcsúszását jeges sáv esetén.
     * @param j Az érintett jármű.
     */
    public void hatasAlkalmaz(Jarmu j){


    }

    /**
     * Kezeli a jármű áthaladását a sávon. 
     * Számolja az áthaladásokat az eljegesedéshez, és alkalmazza a sáv aktuális 
     * állapotából adódó hatásokat a járműre.
     * @param j A sávon éppen áthaladó jármű.
     */

    public void mozgat(Jarmu j) {

    }

    /**
     * Növeli a sávon található hó mennyiségét.
     * @param mennyiseg A hóréteg növekedésének mértéke.
     */
   public void hoNovel(int mennyiseg) {

    }

    /**
     * Frissíti a sáv belső állapotát. 
     * Ha több jármű áthaladt a havas sávon, a letaposott hó jéggé válik.
     */
    public void allapotFrissit() {

    }

}
