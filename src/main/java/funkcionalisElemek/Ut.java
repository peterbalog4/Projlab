package funkcionalisElemek;

import jarmuvek.Irany;
import jarmuvek.Jarmu;
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

    public void addSav(Sav s) {

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

    }

    /**
     * Koordinálja a járművek sávváltását.
     * Megkísérli áthelyezni a járművet a szomszédos sávba, ehhez meghívja a cél 
     * sáv elfogadó metódusát. 
     * @param j A sávot váltani kívánó Jármű.
     * @param i Az irány, amely felé a jármű váltani szeretne.
     */
    public void jarmuSavotValt(Jarmu j, Irany i){

    }


    /**
     * A havat mozgatja sávok között vagy az útról lefelé.
     * A hókotró takarítási folyamata során hívódik meg, a kotrófej típusától 
     * függő távolságra helyezi át a havat. 
     * @param honnan A forrás sáv, ahonnan a havat eltávolították.
     * @param tavolsag Hány sávval arrébb kerüljön a hó (pozitív érték).
     */
    public void havatAtad(Sav honnan, int tavolsag, int mennyiseg){
 

    }

}
