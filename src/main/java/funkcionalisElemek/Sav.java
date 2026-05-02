package funkcionalisElemek;
import jarmuvek.Hokotro;
import jarmuvek.Jarmu;
import kotrofejek.KotroFej;
import segedOsztalyok.*;

import java.util.ArrayList;
import java.util.List;


/**
 * Az úthálózat egy egységnyi szakaszát reprezentáló osztály.
 * Felelős a rajta tartózkodó járművek nyilvántartásáért, a környezeti állapotok 
 * (hómennyiség, jegesedés) kezeléséért, valamint a járművekre gyakorolt hatások 
 * (megállás, megcsúszás) kiváltásáért.
 */
public class Sav {


    private int athaladtJarmuvekSzama = 0; 
    private boolean jeg = false;
    private int ho = 0;
    private boolean zuzalek = false;
    private List<Jarmu> jarmuvek = new ArrayList<>();
    private Ut ut;
    private int sozottIdotartam = 0;
    private int lezarvaKorig = 0;
    private HaladasiIrany haladasiIrany;
    protected String id;
    private int hossz;


    /**
     * Konstruktor a sáv létrehozásához.
     * @param id A sáv egyedi azonosítója a naplózáshoz.
     */
    public Sav(String id, Ut ut, HaladasiIrany irany, int hossz) {
        this.id = id;
        this.ut  = ut;
        this.haladasiIrany = irany;
        this.hossz = hossz;
    }

    /**
     * Visszaadja a sáv azonosítóját.
     * @return A sáv String formátumú azonosítója.
     */
    public String getId(){
        return id;
    }

    /**
     * Járművet ad a sávhoz (pl. inicializáláskor vagy sikeres sávváltáskor).
     * @param j A hozzáadni kívánt jármű.
     */
    public void addJarmu(Jarmu j) {
        if (!jarmuvek.contains(j)) {
            this.jarmuvek.add(j);
            this.athaladtJarmuvekSzama++;
        }
    }
    public HaladasiIrany getIrany(){
        return haladasiIrany;
    }

    /**
     * Visszaadja az utat, amelyhez a sáv tartozik.
     * @return A tartalmazó Út objektum.
     */
    public void setUt(Ut u) {
        ut = u;
    }

    public Ut getUt() {
        return ut;
    }

    public void setHossz(int i){
        hossz = i;
    }

    /**
     * Visszaadja a sáv hosszát méterben.
     *
     * @return A sáv hossza.
     */
    public int getHossz() {
        return hossz;
    }

   

    /**
     * Megkeres egy másik járművet a sávban ütközés detektálásához.
     *
     * @param errolVanSzo A vizsgált jármű, akit ki kell hagyni a keresésből.
     * @return Egy másik Jármű a sávban, vagy {@code null}, ha nincs senki más.
     */
    public Jarmu getMasikJarmu(Jarmu errolVanSzo) {
        for (Jarmu j : jarmuvek) {
            if (j != errolVanSzo) return j;
        }
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

    public int hoTakarit(int tavolsag) {
    int eltakaritottMennyiseg = this.ho;
    this.ho = 0;
    this.jarmuvek.forEach(jarmu -> jarmu.megall(0));
    if (tavolsag > 0 && (eltakaritottMennyiseg > 0 || zuzalek)) {
        this.ut.havatAtad(this, tavolsag, eltakaritottMennyiseg, zuzalek);
        zuzalekEltakarit();
    }
    return eltakaritottMennyiseg;
}

    public void jegFeltor() {
        if (jeg) {
            this.jeg = false;
            this.ho++;
        }
    }

    /**
     * Lezárja a sávot adott ideig (pl. baleset miatt).
     * @param kor A lezárás időtartama körökben.
     */
    public void lezar(int kor){
        lezarvaKorig = kor;
    }

    /**
     * Megvizsgálja, hogy a sáv be tud-e fogadni egy járművet (pl. sávváltásnál).
     * @param j A belépni kívánó jármű.
     */

    /*
    public boolean elfogad(Jarmu j) {
        if (lezarvaKorig > 0) return false;
        
        if (j.getAktualisSav() != null && j.getAktualisSav() != this) {
            j.getAktualisSav().eltavolit(j);
        }
        j.initSav(this);
        
        if (!this.jarmuvek.contains(j)) {
            this.jarmuvek.add(j);
            this.athaladtJarmuvekSzama++;
        }
        
        hatasAlkalmaz(j);
        j.setPozicio(new segedOsztalyok.Pozicio(this, this.hossz));
        return true;
    }
        */

    public boolean elfogad(Jarmu j) {
    if (lezarvaKorig > 0) return false;
    
    addJarmu(j); // Sáv felveszi a listába
    
    // 1. TDA: Szólunk a járműnek. A Jarmu.setSav() elintézi a Pozíció létrehozását!
    j.setSav(this);
    
    // 2. Csak ezután alkalmazzuk a hatást, amikor a jármű már "tudja", hol van.
    hatasAlkalmaz(j);
    
    return true;
}


    public void jarmuMozgott(Jarmu mozgottJarmu) {
    for (Jarmu masik : jarmuvek) {
        if (masik != mozgottJarmu) {
            mozgottJarmu.utkozesVizsgalat(masik); // TDA delegálás
        }
    }
}

    public void eltavolit(Jarmu j){
        jarmuvek.remove(j);
    }

    /**
     * Kiváltja a jármű megcsúszását jeges sáv esetén.
     * @param j Az érintett jármű.
     */
    public void hatasAlkalmaz(Jarmu j){
        if(!(j instanceof Hokotro)){
            if(ho < 3 && jeg  && !zuzalek){
                j.csuszik();
        }
            else if(ho >= 3){
                j.megall(-1);
            }
    }

    }
    public void jarmuKanyarodik(Jarmu j, Ut celUt) {
        this.ut.kanyarodik(j, celUt, this, this.haladasiIrany);
    }

    /**
     * Kezeli a jármű áthaladását a sávon. 
     * Számolja az áthaladásokat az eljegesedéshez, és alkalmazza a sáv aktuális 
     * állapotából adódó hatásokat a járműre.
     * @param j A sávon éppen áthaladó jármű.
     */

    public void jarmuMozgat(Jarmu j) { //ez jó??
        j.kozlekedik();

    }

    /**
     * Növeli a sávon található hó mennyiségét.
     * @param mennyiseg A hóréteg növekedésének mértéke.
     */
   public void hoNovel(int mennyiseg) {
        if(sozottIdotartam == 0){
            ho += mennyiseg;
        }

    }

    /**
     * Frissíti a sáv belső állapotát. 
     * Ha több jármű áthaladt a havas sávon, a letaposott hó jéggé válik.
     */
    public void allapotFrissit() {
        if(lezarvaKorig > 0){
            lezarvaKorig--;
        }

        if(sozottIdotartam > 0){
            jeg = false;
            ho = 0;
            athaladtJarmuvekSzama = 0;
            sozottIdotartam--;
        } else if(athaladtJarmuvekSzama >= 5 && ho > 0){
            jeg = true;
            ho = 0;
            athaladtJarmuvekSzama = 0;
        }
    }

    public void soSzor(){
        sozottIdotartam = 5; //5 ugye?
    }

    public void zuzalekSzor(){
        zuzalek = true;
    }

    public void zuzalekEltakarit(){
        zuzalek = false;
    }

    public boolean isZuzalek(){
        return zuzalek;
    }

    public void savValtas(Jarmu j, Irany i){
        ut.jarmuSavotValt(j, i, this);
    }
    
        /**
     * Beállítja a sózott időtartamot tesztelési célból.
     * A {@code set_material} parancs hívja meg.
     *
     * @param idotartam A beállítandó körök száma.
     */
    public void setSozottIdotartam(int idotartam) {
        this.sozottIdotartam = idotartam;
    }

     public void setAllapot(int ho, boolean jeg) {
        this.ho  = ho;
        this.jeg = jeg;
    }
    /**
     * Kiírja a sáv aktuális állapotát a megadott kimenetre.
     * A {@code stat} parancs hívja meg – a sáv maga felelős a saját
     * állapotának megjelenítéséért.
     *
     * @param id      A sáv azonosítója a kimenetben.
     * @param kimenet A célstream.
     */
    public void statKiir(String id, java.io.PrintStream kimenet) {
        kimenet.println("STAT " + id + ":");
        kimenet.println("- Hoszint: " + ho);
        kimenet.println("- Jeges: " + jeg);
        kimenet.println("- Zuzalekos: " + zuzalek);
        kimenet.println("- Sozott_idotartam: " + sozottIdotartam);
        kimenet.println("- Athaladt_jarmuvek_szama: " + athaladtJarmuvekSzama);
        kimenet.println("- Lezarva: " + lezarvaKorig);
        StringBuilder sb = new StringBuilder("- Jarmuvek_rajta:");
        jarmuvek.forEach(j -> sb.append(" ")
                                .append(j.getClass().getSimpleName().toLowerCase())
                                .append(" ")
                                .append(j.getId()));
        kimenet.println(sb);
    }

}