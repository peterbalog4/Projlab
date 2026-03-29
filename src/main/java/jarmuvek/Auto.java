package jarmuvek;


import funkcionalisElemek.*;
import vezerles.Skeleton;

/**
 * A személyautót reprezentáló osztály, amely a Jármű általános tulajdonságait 
 * specifikus viselkedéssel egészíti ki.
 * Az Autó képes a közlekedésre, jeges úton való megcsúszásra, valamint más 
 * járművekkel való ütközésre, amely események befolyásolják a sáv állapotát 
 * és a többi jármű mozgásképességét.
 */
public class Auto extends Jarmu {

    /**
     * Konstruktor az Auto osztályhoz.
     * @param id Az autó egyedi azonosítója, amelyet a naplózás során használunk.
     */
    public Auto(String id) {
        super(id);
    }

    /**
     * Az autó haladását megvalósító metódus.
     * A KörSzámláló hívja meg minden körben. Ha az autó mozgásképes, 
     * megkísérli az előrehaladást az aktuális sávon keresztül.
     */
    @Override
    public void kozlekedik(){
        Skeleton.hiv(this.id + ":Auto: kozlekedik()");

        if (!mozgaskepes) {
            Skeleton.naploz("A jármű mozgásképtelen, ezért kimarad a körből.");
            Skeleton.visszater("kozlekedik");
            return;
        }

        if (aktualisSav != null) {
            aktualisSav.mozgat(this);
        }

        Skeleton.visszater("kozlekedik");
    }

    /**
     * Megvalósítja az autó megcsúszását jeges útfelületen.
     * A folyamat során ellenőrzi, hogy történik-e ütközés egy másik, 
     * a sávban tartózkodó járművel.
     */
    @Override
    public void csuszik(){
        Skeleton.hiv(this.id + ":Auto: csuszik()");

        boolean vanMasikJarmu = Skeleton.kerdez("Van másik jármű a sávban, amivel ütközik?");

        if (vanMasikJarmu){
            Jarmu a2 = aktualisSav.getMasikJarmu(this);

            if (a2 != null) {
                this.utkozik(a2);
            } else {
                Skeleton.naploz("Bár akartunk ütközni, nincs másik jármű a sávban!");
            }
        } else {
            Skeleton.naploz("Az autó megcsúszott, de nem ütközött semmivel.");
        }

        Skeleton.visszater("csuszik");
    }

    /**
     * Kezeli a más járművel való ütközés eseményét.
     * Ütközés esetén mindkét jármű mozgásképtelenné válik (megáll), 
     * és a sáv lezárásra kerül az ütközés helyszínén.
     * @param masikJarmu Az a jármű, amellyel az autó összeütközött.
     */
    @Override
    public void utkozik(Jarmu masikJarmu){
        Skeleton.hiv(this.id + ":Auto: utkozik()");

        Skeleton.naploz("A járművek összeütköztek és mozgásképtelenné váltak.");
        this.megall(10);
        masikJarmu.megall(10);

        if (aktualisSav != null) {
            aktualisSav.lezar(10);
        }

        Skeleton.visszater("utkozik");
    }

    public void megall(int korszam){
        Skeleton.hiv(this.id + ":Auto: megall(" + korszam + ")");
        this.mozgaskepes = false;
        Skeleton.visszater("megall");
    }
    
    
}
