package funkcionalisElemek;
import jarmuvek.Hokotro;
import jarmuvek.Jarmu;
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
     * Inicializálja a sáv alapvető tulajdonságait és a befoglaló utat.
     *
     * @param id    A sáv egyedi szöveges azonosítója.
     * @param ut    Az a {@link Ut}, amelyhez a sáv tartozik.
     * @param irany A sáv haladási iránya a hálózaton belül.
     * @param hossz A sáv fizikai hossza méterben.
     */
    public Sav(String id, Ut ut, HaladasiIrany irany, int hossz) {
        this.id = id;
        this.ut  = ut;
        this.haladasiIrany = irany;
        this.hossz = hossz;
    }

    /**
     * Visszaadja a sáv egyedi szöveges azonosítóját.
     * Ezt az azonosítót használjuk a parancsfeldolgozóban és a naplózáshoz.
     *
     * @return A sáv {@code String} formátumú azonosítója.
     */
    public String getId(){
        return id;
    }

   /**
     * Felveszi a megadott járművet a sáv belső nyilvántartásába.
     * Növeli a sávon áthaladt járművek számlálóját az eljegesedés méréséhez.
     *
     * @param j A hozzáadni kívánt {@link Jarmu}.
     */
    public void addJarmu(Jarmu j) {
        if (!jarmuvek.contains(j)) {
            this.jarmuvek.add(j);
            this.athaladtJarmuvekSzama++;
        }
    }

    /**
     * Lekérdezi a sáv hálózati haladási irányát.
     * Az út ez alapján dönti el, hogy a jármű az út melyik végpontja felé tart.
     *
     * @return A sáv aktuális {@link HaladasiIrany}-a.
     */
    public HaladasiIrany getIrany(){
        return haladasiIrany;
    }

    /**
     * Hozzárendeli a sávhoz azt az utat, amelynek a részét képezi.
     * A hivatkozáson keresztül éri el a sáv a globális útszintű metódusokat.
     *
     * @param u A befogadó {@link Ut} objektum.
     */
    public void setUt(Ut u) {
        ut = u;
    }

    /**
     * Visszaadja a sávot magába foglaló út objektumot.
     * Ezt a referenciát használjuk a kanyarodások delegálásakor.
     *
     * @return Az útszakasz, amelyhez a sáv tartozik.
     */
    public Ut getUt() {
        return ut;
    }

    /**
     * Módosítja a sáv fizikai hosszát.
     * Ez a távolság határozza meg, hogy a járművek mikor érik el a sáv végét.
     *
     * @param i A sáv új hossza.
     */
    public void setHossz(int i){
        hossz = i;
    }

    
    /**
     * Visszaadja a sáv fizikai kiterjedését.
     * A pozíció objektumok ezt használják a határellenőrzéshez.
     *
     * @return A sáv hossza méterben.
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
     * Eltakarítja a sávon lévő havat és átadja azt a szomszédos sávoknak.
     *
     * @param tavolsag Hány sávval arrébb kell tolni a havat (pozitív egész szám).
     * @return A sikeresen eltakarított hómennyiség.
     */
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

    /**
     * Feltöri a jeget a sávon, ezáltal a jég normál, letaposatlan hóvá alakul.
     * Jégtörő fejjel felszerelt hókotrók használják.
     */
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
     * Fogadja a belépő járművet a sávba.
     *
     * Regisztrálja a listában, értesíti a járművet az új sávjáról,
     * lekezeli az esetleges ütközéseket és alkalmazza a környezeti hatásokat.
     *
     * @param j A belépni kívánó {@link Jarmu}.
     * @return {@code true}, ha a jármű sikeresen belépett, {@code false}, ha a sáv lezárt.
     */
    public boolean elfogad(Jarmu j) {
        if (lezarvaKorig > 0) return false;
        
        addJarmu(j); 
        j.setSav(this);
        jarmuMozgott(j);
        hatasAlkalmaz(j);
        
        return true;
    }

    /**
     * Végigkérdezi a sávban lévő többi járművet a pozícióegyezésről.
     *
     * @param mozgottJarmu A jármű, amelyik éppen helyzetet változtatott a sávon belül.
     */
    public void jarmuMozgott(Jarmu mozgottJarmu) {
        for (Jarmu masik : jarmuvek) {
            if (masik != mozgottJarmu) {
                mozgottJarmu.utkozesVizsgalat(masik); 
            }
        }
    }

    /**
     * Eltávolítja a járművet a sáv belső nyilvántartásából sávváltás vagy lekanyarodás esetén.
     *
     * @param j Az eltávolítandó {@link Jarmu}.
     */
    public void eltavolit(Jarmu j){
        jarmuvek.remove(j);
    }

    /**
     * Alkalmazza a jég vagy a mély hó hatásait a sávban tartózkodó járműveken.
     * Hókotrók esetében ezek a hatások figyelmen kívül maradnak.
     *
     * @param j A vizsgált {@link Jarmu}.
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

    /**
     * Delegálja a kanyarodási szándékot az útnak a saját irányával kiegészítve.
     *
     * @param j     A kanyarodást végrehajtó {@link Jarmu}.
     * @param celUt A célként kiszemelt {@link Ut}.
     */
    public void jarmuKanyarodik(Jarmu j, Ut celUt) {
        this.ut.kanyarodik(j, celUt, this, this.haladasiIrany);
    }

   /**
     * Felszólítja a járművet a saját mozgási logikájának végrehajtására.
     *
     * @param j A mozgatni kívánt {@link Jarmu}.
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

        if (ho >= 3) {
            jarmuvek.forEach(this::hatasAlkalmaz);
        }

    }

    /**
     * Frissíti a sáv belső állapotát. 
     * Ha 5 jármű áthaladt a havas sávon, a letaposott hó jéggé válik.
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

    /**
     * Sót juttat a sávra, ami 5 kör erejéig meggátolja a hó felgyülemlését és a jegesedést.
     */
    public void soSzor(){
        sozottIdotartam = 5; //5 ugye?
    }


    /**
     * Érdesítő zúzalékot szór a sávra, meggátolva a járművek jégen való megcsúszását.
     */
    public void zuzalekSzor(){
        zuzalek = true;
    }


    /**
     * Eltakarítja a sávra korábban kiszórt érdesítő zúzalékot.
     */
    public void zuzalekEltakarit(){
        zuzalek = false;
    }


    /**
     * Lekérdezi, hogy a sáv felülete jelenleg be van-e szórva zúzalékkal.
     *
     * @return {@code true}, ha zúzalékos, {@code false} egyébként.
     */
    public boolean isZuzalek(){
        return zuzalek;
    }


    /**
     * TDA: Kéri az út objektumot a jármű megfelelő irányba történő átmozgatására.
     *
     * @param j A sávot váltó {@link Jarmu}.
     * @param i A sávváltás kívánt {@link Irany}-a.
     */
    public void savValtas(Jarmu j, Irany i){
        ut.jarmuSavotValt(j, i, this);
    }
    
    /**
     * Közvetlenül felülírja a sáv sózott állapotának hátralévő idejét (teszteléshez).
     *
     * @param idotartam A beállítandó sózási időtartam körökben.
     */
    public void setSozottIdotartam(int idotartam) {
        this.sozottIdotartam = idotartam;
    }


    /**
     * Közvetlenül beállítja a hó szintjét és a jegesedést a sávon (teszteléshez).
     * A változásokat azonnal érvényesíti a bent álló járműveken is.
     *
     * @param ho  A beállítandó hószint.
     * @param jeg {@code true}, ha jeges legyen a pálya.
     */
     public void setAllapot(int ho, boolean jeg) {
        this.ho  = ho;
        this.jeg = jeg;
        jarmuvek.forEach(this::hatasAlkalmaz);
    }


    /**
     * Biztonsági ellenőrzés, hogy a sáv szabad-e a megadott jármű számára sávváltáskor.
     *
     * @param j A sávba belépni kívánó {@link Jarmu}.
     * @return {@code true}, ha a sáv tiszta és fogadóképes, {@code false} egyébként.
     */
    public boolean tisztaE(Jarmu j) {
        if (lezarvaKorig > 0) return false; 
        if (!(j instanceof Hokotro) && ho >= 3) return false; 
        return true;
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
        jarmuvek.forEach(j -> sb.append(" ").append(j.getClass().getSimpleName().toLowerCase()).append(" ").append(j.getId()));
        kimenet.println(sb);
    }

}