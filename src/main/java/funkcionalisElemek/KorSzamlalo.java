package funkcionalisElemek;

import jarmuvek.*;


import java.util.ArrayList;
import java.util.List;


/**
 * A játék belső időzítéséért és a körök eszkalációjáért felelős osztály.
 * Nyilvántartja a pályán lévő járműveket, és minden körben kezdeményezi azok mozgatását,
 * valamint az automatikus környezeti eseményeket, például a hóesést.
 */
public class KorSzamlalo {

    /** A körben érintett járművek listája. */
    private List<Jarmu> jarmuvek = new ArrayList<>();

    /** Az úthálózat referenciája az időjárási effektusok alkalmazásához. */
    private Ut aktivUt;

    /**
     * Beállítja a körszámlálóhoz tartozó utat.
     * @param u Az Út objektum, amelyen a hóesés effektus végrehajtódik.
     */
    public void setUt(Ut u) { this.aktivUt = u; }

    /**
     * Új járművet ad a szimulációhoz.
     * @param j A hozzáadni kívánt Jármű objektum
     */
    public void addJarmu(Jarmu j) {
  
    }


    /**
     * Végrehajtja a kör léptetését.
     * Sorban meghívja minden aktív jármű közlekedik metódusát, majd a kör végén
     * aktiválja az automatikus hóesést, amennyiben az út be van állítva.
     */
    public void leptet(){

    }
}
