package funkcionalisElemek;

import jarmuvek.Jarmu;
import segedOsztalyok.*;
import grafika.AbstractObservable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Az úthálózatot reprezentáló osztály, amely összefogja a párhuzamos sávokat.
 * Felelős a sávok közötti közlekedés koordinálásáért (sávváltás, kanyarodás), 
 * a környezeti hatások (hóesés) globális menedzseléséért, valamint a hó sávok 
 * közötti mozgatásáért.
 */
public class Ut extends AbstractObservable {

    /** Az úthoz tartozó sávok listája.  */
    public final String id; // Az út egyedi azonosítója
    private List<Sav> savok = new ArrayList<>();
    private List<Sav> B_bol_A_savok= new ArrayList<>();
    private List<Sav> A_bol_B_savok = new ArrayList<>();
    private Map<Ut, String> vegA_kapcsolatok = new HashMap<>();
    private Map<Ut, String> vegB_kapcsolatok = new HashMap<>();
    private final int hossz;
    private boolean vegallomas = false;
    /** A szakasz fajtája (normál / híd / alagút). A hídon és alagútban nincs ütközés. */
    private SzakaszTipus tipus = SzakaszTipus.NORMAL;
    /** Véletlenszám-generátor a sávonkénti (1/10 esélyű) hóeséshez. */
    private static final java.util.Random HO_RANDOM = new java.util.Random();
    private boolean aktivCel = false;
    /**
     * Az út két végén lévő kereszteződés/kanyar tile-jai. A {@link vezerles.Map_generator}
     * tölti ki a térkép betöltése után, így a hókotró {@code elertSavVeget()}-ben tudja
     * letakarítani a megfelelő végén lévő tile-t (haladási irány szerint).
     * Lehet {@code null}, ha az adott végen nincs kanyar/kereszteződés (pl. zsákutca).
     */
    private Tile vegA_tile;
    private Tile vegB_tile;

    /** A megadott vég ({@code "vegA"} / {@code "vegB"}) tile-jának beállítása. */
    public void setVegTile(String veg, Tile tile) {
        if ("vegA".equalsIgnoreCase(veg)) vegA_tile = tile;
        else if ("vegB".equalsIgnoreCase(veg)) vegB_tile = tile;
    }

    /** A megadott vég tile-jának lekérdezése (null, ha nincs). */
    public Tile getVegTile(String veg) {
        return "vegA".equalsIgnoreCase(veg) ? vegA_tile : vegB_tile;
    }

    public boolean isAktivCel() {
        return this.aktivCel;
    }

    public void setAktivCel(boolean aktivCel) {
        this.aktivCel = aktivCel;
    }


    /**
     * Konstruktor az út létrehozásához.
     * Legenerálja és irány szerint beköti az úthoz tartozó sávokat.
     *
     * @param id         Az út egyedi szöveges azonosítója.
     * @param hossz      Az út (és ezáltal a sávok) fizikai hossza.
     * @param savokAbolB Az 'A' végből 'B' végbe tartó sávok száma.
     * @param savokBbolA A 'B' végből 'A' végbe tartó sávok száma.
     */
    public Ut(String id, int hossz, int savokAbolB, int savokBbolA) {
        this.id = id;
        this.hossz = hossz;

        for (int i = 1; i <= savokAbolB; i++) {
            String savId = id + "_vegB_" + i;
            Sav ujSav = new Sav(savId, this, HaladasiIrany.A_BOL_B_BE, hossz);
            this.savok.add(ujSav);
            this.A_bol_B_savok.add(ujSav);
        }

        for (int i = 1; i <= savokBbolA; i++) {
            String savId = id + "_vegA_" + i;
            Sav ujSav = new Sav(savId, this, HaladasiIrany.B_BOL_A_BA, hossz);
            this.savok.add(ujSav);
            this.B_bol_A_savok.add(ujSav);
        }
    }


    public boolean isVegallomas() {
        return this.vegallomas;
    }

    public void setVegallomas(boolean vegallomas) {
        this.vegallomas = vegallomas;
    }

    /**
     * Létrehoz egy új sáv objektumot a megadott paraméterekkel.
     *
     * @param id    A sáv azonosítója.
     * @param irany A sáv haladási iránya.
     * @param hossz A sáv hossza.
     * @return A frissen létrehozott {@link Sav} példány.
     */
    public Sav addSav(String id, HaladasiIrany irany, int hossz){
        Sav s = new Sav(id, this, irany, hossz);
        return s;
    }



    /**
     * Felveszi a sávot az út nyilvántartásába, és irány alapján besorolja azt.
     *
     * @param s A hozzáadni kívánt {@link Sav}.
     */
    public void addSav(Sav s) {
        savok.add(s);
        s.setUt(this);
        s.setHossz(hossz);
        if(s.getIrany() == HaladasiIrany.A_BOL_B_BE){
            A_bol_B_savok.add(s);
        }
        else{
            B_bol_A_savok.add(s);
        }

    }


    /**
     * Egyirányú kapcsolatot regisztrál az út egyik vége és egy célút között.
     *
     * @param sajatVeg Az út saját végpontja ({@code "vegA"} vagy {@code "vegB"}).
     * @param celUt    A csatlakozó cél {@link Ut}.
     * @param celVeg   A célút érintett végpontja.
     */
    public void addKapcsolat(String sajatVeg, Ut celUt, String celVeg) {
        if (sajatVeg.equals("vegA")) vegA_kapcsolatok.put(celUt, celVeg);
        else vegB_kapcsolatok.put(celUt, celVeg);
        }

    /**
     * Átnavigálja a járművet a szomszédos célútra, kiszámítva a cél-sávindexet.
     *
     * @param j           A kanyarodó {@link Jarmu}.
     * @param celUt       A célállomásként megjelölt {@link Ut}.
     * @param honnanSav   A sáv, amiből a manőver megindult.
     * @param honnanIrany Az indulási sáv haladási iránya.
     */
    public void kanyarodik(Jarmu j, Ut celUt, Sav honnanSav, HaladasiIrany honnanIrany) {
        Map<Ut, String> kapcsolatok = (honnanIrany == HaladasiIrany.A_BOL_B_BE) ? vegB_kapcsolatok : vegA_kapcsolatok;
        String honnanVeg = (honnanIrany == HaladasiIrany.A_BOL_B_BE) ? "vegB" : "vegA";
        
        String erkezesiVeg = kapcsolatok.get(celUt);
        
        if (erkezesiVeg != null) {
            System.out.println("    >>> KANYARODÁS SIKERES! Jármű átlépett a(z) " + celUt.id + " útra.");
            List<Sav> forrasLista = (honnanIrany == HaladasiIrany.A_BOL_B_BE) ? A_bol_B_savok : B_bol_A_savok;
            int savIndex = forrasLista.indexOf(honnanSav);
            
            celUt.befogad(j, erkezesiVeg, savIndex);
        } else {
            // Ez a sor leleplezi, ha a térkép alapján illegális a kanyarodás:
            System.out.println("    [HIBA] Nem lehet kanyarodni! A(z) " + this.id + " út " + honnanVeg + " vége fizikailag nincs összekötve a(z) " + celUt.id + " úttal!");
        }
    }



    /**
     * Lefordítja a kanyarodás során kapott adatokat sávra, és befogadja a járművet.
     * Ha a célút keskenyebb, a járművet biztonságosan a legszélső elérhető sávba sorolja.
     *
     * @param j             A befogadandó {@link Jarmu}.
     * @param erkezesiVeg   A végpont, amin keresztül a jármű megérkezik.
     * @param erkezesiIndex Az eredeti úton elfoglalt sávindexe.
     */
    public void befogad(Jarmu j, String erkezesiVeg, int erkezesiIndex) {
        List<Sav> celLista = erkezesiVeg.equals("vegA") ? A_bol_B_savok : B_bol_A_savok;
        int biztonsagosIndex = Math.max(0, Math.min(erkezesiIndex, celLista.size() - 1));
        Sav induloSav = celLista.get(biztonsagosIndex);
        
        induloSav.elfogad(j);
    }



    /**
     * Visszaadja az út fizikai hosszát.
     *
     * @return Az út hossza méterben.
     */
    public int getHossz(){
        return hossz;
    }

    /**
     * Beállítja a szakasz fajtáját, és ennek megfelelően az összes sávján be-/kikapcsolja
     * az ütközésvizsgálatot. Híd és alagút esetén a járművek egymás felett/alatt haladnak el,
     * ezért nem ütköznek.
     *
     * @param tipus Az új {@link SzakaszTipus} (NORMAL / HID / ALAGUT).
     */
    public void setTipus(SzakaszTipus tipus) {
        this.tipus = tipus;
        boolean utkozesmentes = (tipus == SzakaszTipus.HID || tipus == SzakaszTipus.ALAGUT);
        for (Sav s : savok) {
            s.setUtkozesmentes(utkozesmentes);
        }
    }

    /**
     * @return A szakasz fajtája (alapból {@link SzakaszTipus#NORMAL}).
     */
    public SzakaszTipus getTipus() {
        return tipus;
    }

    /**
     * Visszaadja az úthoz tartozó összes sávot.
     * A {@code Commander} használja a sávok nyilvántartásba vételéhez.
     *
     * @return Az összes sáv listája.
     */
    public List<Sav> getSavok() {
        return savok;
    }
    


    
    /**
     * Összekapcsol két utat a megadott végeken.
     * A {@code connect} pályaleíró parancs hívja meg.
     *
     * @param sajatVeg  Az aktuális út érintett vége ({@code "vegA"} vagy {@code "vegB"}).
     * @param masikUt   A másik út objektum.
     * @param masikVeg  A másik út érintett vége ({@code "vegA"} vagy {@code "vegB"}).
     */
    public void connect(String sajatVeg, Ut masikUt, String masikVeg) {
        if (sajatVeg.equalsIgnoreCase("vegA")) {
            vegA_kapcsolatok.put(masikUt, masikVeg);
        } else {
            vegB_kapcsolatok.put(masikUt, masikVeg);
        }
        if (masikVeg.equalsIgnoreCase("vegA")) {
            masikUt.vegA_kapcsolatok.put(this, sajatVeg);
        } else {
            masikUt.vegB_kapcsolatok.put(this, sajatVeg);
        }
    }

    /**
     * Visszaadja a haladási iránynak megfelelő végponthoz csatlakozó szomszédos utakat.
     *
     * @param irany A jármű aktuális {@link HaladasiIrany}-a.
     * @return A csatlakozó utakat és érkezési végeket tartalmazó térkép.
     */
    public Map<Ut, String> getKapcsolatok(HaladasiIrany irany) {
        return irany == HaladasiIrany.A_BOL_B_BE ? vegB_kapcsolatok : vegA_kapcsolatok;
    }

    /**
     * Végrehajtja a hóesést az úton. A hó nem mindenhol és nem minden körben esik:
     * minden sávra (csempére) külön-külön 1/10 az esélye, hogy ebben a körben hullik rá hó.
     * Így a havazás véletlenszerűen, foltokban jelenik meg, nem egyszerre az egész pályán.
     */
    public void hoNovel(){
        for (Sav sav : savok) {
            if (HO_RANDOM.nextInt(10) == 0) { // 1/10 esély csempénként
                sav.hoNovel(1);
            }
        }
    }

    /**
     * Koordinálja a járművek sávváltását.
     * Megkísérli áthelyezni a járművet a szomszédos sávba, ehhez meghívja a cél 
     * sáv elfogadó metódusát. 
     * @param j A sávot váltani kívánó Jármű.
     * @param i Az irány, amely felé a jármű váltani szeretne.
     * @param s A sáv, amelyre áthajt a jármű
     */
    public void jarmuSavotValt(Jarmu j, Irany i, Sav s){
        int idx = (i == Irany.BALRA) ? 1 : -1;
        Sav celSav = savKeres(s, idx);

        if (celSav != s && celSav.tisztaE(j)) {
            celSav.elfogad(j);
        }
    }


    /**
     * Megkeresi a szomszédos sávot a haladási irány és egy eltolási érték alapján.
     * Automatikus határellenőrzéssel akadályozza meg a túlindexelést.
     *
     * @param s A kiinduló sáv.
     * @param i Az eltolás mértéke (1 vagy -1).
     * @return A kiszámolt szomszédos {@link Sav}.
     */
    private Sav savKeres(Sav s, int i){
        List<Sav> celLista = A_bol_B_savok.contains(s) ? A_bol_B_savok : B_bol_A_savok;
        int idx = celLista.indexOf(s);
        int biztonsagosIDX = Math.max(0, Math.min(idx + i, celLista.size() - 1));
        return celLista.get(biztonsagosIDX);
        
    }

    /**
     * Szétosztja a hókotró által letolt havat és zúzalékot a szomszédos sávokra.
     * Ha az eltolt mennyiség a legszélső sávon túlra kerülne, az anyag eltűnik a pályáról.
     *
     * @param honnan    A sáv, ahonnan a hótakarítás megindult.
     * @param tavolsag  A sávok száma, amennyivel arrébb kell tolni az anyagot.
     * @param mennyiseg Az áthelyezendő hó mennyisége.
     * @param zuzalek   {@code true}, ha zúzalékot is tolni kell a hóval.
     */
    public void havatAtad(Sav honnan, int tavolsag, int mennyiseg, boolean zuzalek){

    List<Sav> lista = A_bol_B_savok.contains(honnan) ? A_bol_B_savok : B_bol_A_savok;
        int ujIndex = lista.indexOf(honnan) - tavolsag;

        if (ujIndex >= 0 && ujIndex < lista.size()) {
            Sav celsav =  lista.get(ujIndex);
            celsav.hoNovel(mennyiseg);
            if(zuzalek) 
                celsav.zuzalekSzor();
        } 
    }
}