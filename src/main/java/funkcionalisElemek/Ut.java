package funkcionalisElemek;

import jarmuvek.Jarmu;
import segedOsztalyok.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * Az úthálózatot reprezentáló osztály, amely összefogja a párhuzamos sávokat.
 * Felelős a sávok közötti közlekedés koordinálásáért (sávváltás, kanyarodás), 
 * a környezeti hatások (hóesés) globális menedzseléséért, valamint a hó sávok 
 * közötti mozgatásáért.
 */
public class Ut {

    /** Az úthoz tartozó sávok listája.  */
    private List<Sav> savok = new ArrayList<>();
    private List<Sav> B_bol_A= new ArrayList<>();
    private List<Sav> A_bol_B = new ArrayList<>();
    
    private int hossz;

    public void addSav(Sav s) {
        savok.add(s);
        s.setUt(this);
        s.setHossz(hossz);
        if(s.getIrany() == HaladasiIrany.A_BOL_B_BE){
            A_bol_B.add(s);
        }
        else{
            B_bol_A.add(s);
        }

    }

    /**
     * Kezeli a jármű kanyarodását az út végén.
     * A járművet a megadott irányban lévő következő útszakaszra tereli.
     * @param j A kanyarodni kívánó Jármű.
     * @param i A kanyarodás iránya.
     */
    public void kanyarodik(Jarmu j, Irany i){

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
     */
    public void jarmuSavotValt(Jarmu j, Irany i, Sav s){
        int idx = (i == Irany.BALRA) ? 1 : - 1;
        savKeres(s, idx).elfogad(j);
    }

    private Sav savKeres(Sav s, int i){
        List<Sav> celLista = A_bol_B.contains(s) ? A_bol_B : B_bol_A;
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

    List<Sav> lista = A_bol_B.contains(honnan) ? A_bol_B : B_bol_A;
        int ujIndex = lista.indexOf(honnan) - tavolsag;

        if (ujIndex >= 0 && ujIndex < lista.size()) {
            Sav celsav =  lista.get(ujIndex);
            celsav.hoNovel(mennyiseg);
            if(zuzalek) 
                celsav.zuzalekSzor();
        } 
    }
}
