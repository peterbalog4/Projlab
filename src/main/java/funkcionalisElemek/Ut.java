package funkcionalisElemek;

import jarmuvek.Jarmu;
import segedOsztalyok.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Az úthálózatot reprezentáló osztály, amely összefogja a párhuzamos sávokat.
 * Felelős a sávok közötti közlekedés koordinálásáért (sávváltás, kanyarodás), 
 * a környezeti hatások (hóesés) globális menedzseléséért, valamint a hó sávok 
 * közötti mozgatásáért.
 */
public class Ut {

    /** Az úthoz tartozó sávok listája.  */
    String id;
    private List<Sav> savok = new ArrayList<>();
    private List<Sav> B_bol_A_savok= new ArrayList<>();
    private List<Sav> A_bol_B_savok = new ArrayList<>();
    private Map<Ut, String> vegA_kapcsolatok = new HashMap<>();
    private Map<Ut, String> vegB_kapcsolatok = new HashMap<>();
    private int hossz;

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


    public Sav addSav(String id, HaladasiIrany irany, int hossz){
        Sav s = new Sav(id, this, irany, hossz);
        return s;
    }


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

    public void addKapcsolat(String sajatVeg, Ut celUt, String celVeg) {
        if (sajatVeg.equals("vegA")) vegA_kapcsolatok.put(celUt, celVeg);
        else vegB_kapcsolatok.put(celUt, celVeg);
        }

    /**
     * Kezeli a jármű kanyarodását az út végén.
     * A járművet a megadott irányban lévő következő útszakaszra tereli.
     * @param j A kanyarodni kívánó Jármű.
     * @param celUt Az út amire kanyarodni akarunk
     */
    public void kanyarodik(Jarmu j, Ut celUt, Sav honnanSav, HaladasiIrany honnanIrany) {
        Map<Ut, String> kapcsolatok = (honnanIrany == HaladasiIrany.A_BOL_B_BE) ? vegB_kapcsolatok : vegA_kapcsolatok;
        
        String erkezesiVeg = kapcsolatok.get(celUt);
        
        if (erkezesiVeg != null) {

            List<Sav> forrasLista = (honnanIrany == HaladasiIrany.A_BOL_B_BE) ? A_bol_B_savok : B_bol_A_savok;
            int savIndex = forrasLista.indexOf(honnanSav);
            
            celUt.befogad(j, erkezesiVeg, savIndex);
        }
    }


    public void befogad(Jarmu j, String erkezesiVeg, int erkezesiIndex) {
        List<Sav> celLista = erkezesiVeg.equals("vegA") ? A_bol_B_savok : B_bol_A_savok;
        int biztonsagosIndex = Math.max(0, Math.min(erkezesiIndex, celLista.size() - 1));
        Sav induloSav = celLista.get(biztonsagosIndex);
        
        if (induloSav.elfogad(j)) {
            j.setSav(induloSav); 
        }
    }

    public int getHossz(){
        return hossz;
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
     * Visszaadja az út adott végéhez csatlakozó utakat és a csatlakozás végét.
     * A Dijkstra-algoritmus számára szükséges szomszédsági információt szolgáltatja.
     *
     * @param irany Ha {@code A_BOL_B_BE}, a B végnél lévő kapcsolatokat adja vissza,
     *              ha {@code B_BOL_A_BA}, az A végnél lévőket.
     * @return Az adott véghez tartozó szomszédos utak és érkezési végük.
     */
    public Map<Ut, String> getKapcsolatok(HaladasiIrany irany) {
        return irany == HaladasiIrany.A_BOL_B_BE ? vegB_kapcsolatok : vegA_kapcsolatok;
    }

    /**
     * Végrehajtja a globális hóesést az úton.
     * Minden sávján meghívja a hóréteg növelését végző függvényt. 
     */
    public void hoNovel(){
        savok.forEach(sav -> sav.hoNovel(1));
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
        if (celSav != s && celSav.elfogad(j)) {
            j.setSav(celSav);
        }
    }

    private Sav savKeres(Sav s, int i){
        List<Sav> celLista = A_bol_B_savok.contains(s) ? A_bol_B_savok : B_bol_A_savok;
        int idx = celLista.indexOf(s);
        int biztonsagosIDX = Math.max(0, Math.min(idx + i, celLista.size() - 1));
        return celLista.get(biztonsagosIDX);
        
    }

    /**
     * A havat mozgatja sávok között vagy az útról lefelé.
     * A hókotró takarítási folyamata során hívódik meg, a kotrófej típusától 
     * függő távolságra helyezi át a havat. 
     * @param honnan A forrás sáv, ahonnan a havat eltávolították.
     * @param tavolsag Hány sávval arrébb kerüljön a hó és zuzalék (ha van) (pozitív érték).
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