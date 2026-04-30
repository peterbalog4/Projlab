package jarmuvek;


import funkcionalisElemek.*;


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

    }

    /**
     * Megvalósítja az autó megcsúszását jeges útfelületen.
     * A folyamat során ellenőrzi, hogy történik-e ütközés egy másik, 
     * a sávban tartózkodó járművel.
     */
    @Override
    public void csuszik(){

    }

    /**
     * Kezeli a más járművel való ütközés eseményét.
     * Ütközés esetén mindkét jármű mozgásképtelenné válik (megáll), 
     * és a sáv lezárásra kerül az ütközés helyszínén.
     * @param masikJarmu Az a jármű, amellyel az autó összeütközött.
     */
    @Override
    public void utkozik(Jarmu masikJarmu){
    }
    
    
}
