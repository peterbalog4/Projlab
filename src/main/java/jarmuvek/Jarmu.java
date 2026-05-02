package jarmuvek;

import funkcionalisElemek.Sav;
import funkcionalisElemek.Ut;
import segedOsztalyok.Irany;


/**
 * A játékban közlekedő járművek általános absztrakt alaposztálya.
 * Közös interfészt és alapvető állapotfelügyeletet biztosít minden járműtípus számára.
 * Felelős a jármű pozíciójának (Sáv), mozgásképességének és az alapvető 
 * manővereknek (megállás, sávváltás) a kezeléséért[cite: 1548, 1569].
 */
public abstract class Jarmu{

    /** A jármű egyedi azonosítója a naplózáshoz. */
    protected String id;
    /** A sáv, amelyen a jármű jelenleg tartózkodik. */
    protected Sav aktualisSav;
    /** Jelzi, hogy a jármű képes-e a mozgásra, vagy elakadt/ütközött */
    protected boolean mozgaskepes = true;

    /**
     * Konstruktor a Jármű osztályhoz.
     * @param id A jármű szöveges azonosítója.
     */
    public Jarmu(String id) {
        this.id = id;
    }

    /**
     * Visszaadja a jármű azonosítóját.
     * @return A jármű String formátumú azonosítója.
     */
    public String getId(){
        return this.id;
    }


    /**
     * Beállítja a jármű aktuális sávját.
     * @param s A cél sáv objektum.
     */
    public void setSav(Sav s){
  
    }

    /**
     * Kezeli a jármű kanyarodását az út végén.
     * @param i Az irány, amerre a jármű kanyarodni kíván.
     */
    public void kanyarodik(Irany i){
        
    }

    /**
     * Absztrakt metódus a körönkénti haladás megvalósításához.
     * Minden járműtípus a saját szabályai szerint hajtja végre[cite: 1548, 1553].
     */
    public abstract void kozlekedik();


    /**
     * Megállítja a járművet egy adott időtartamra.
     * Elakadás vagy ütközés esetén hívódik meg, ilyenkor a jármű mozgásképtelenné válik.
     * @param korszam Hány körön keresztül maradjon mozgásképtelen a jármű.
     */
    public void megall(int korszam){
 
    }

    /**
     * Kezeli a más járművel történő ütközést.
     * @param masikJarmu Az az objektum, amellyel a jármű érintkezett.
     */
    public void utkozik(Jarmu masikJarmu){

    }

    /**
     * Kezdeményezi a sávváltást a megadott irányba.
     * A jármű az Út objektumon keresztül kéri az áthelyezést a szomszédos sávba.
     * @param irany A cél sáv iránya az aktuálishoz képest.
     */
    public void savvaltas(Irany i) {
        aktualisSav.savValtas(this, i);
    }

    /**
     * Megvalósítja a jármű megcsúszását jeges útfelületen.
     * Csúszás közben a jármű kontrollálatlanul mozoghat és ütközhet.
     */
    public void csuszik(){

    }

}
