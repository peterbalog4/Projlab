package jarmuvek;

import funkcionalisElemek.Sav;
import vezerles.Skeleton;


/**
 * A buszt reprezentáló osztály, amely a Jármű alaposztályból származik.
 * Felelőssége a menetrend szerinti közlekedés megvalósítása, valamint a 
 * végállomások (fordulatok) detektálása és adminisztrálása, amely a 
 * Buszvezető Játékos pontszerzésének alapja.
 */
public class Busz extends Jarmu {

    /** A busz által eddig sikeresen teljesített fordulók száma. */
    private int forduloSzam = 0;

    /**
     * Konstruktor a Busz osztályhoz.
     * @param id A busz egyedi azonosítója a naplózáshoz.
     */
    public Busz(String id) {
        super(id);
    }
    public void csuszik(){

    }

    /**
     * A busz haladását vezérlő metódus.
     * Minden körben ellenőrzi a busz mozgásképességét. Amennyiben a busz 
     * haladni tud, megkísérli az előrejutást az aktuális sávon, majd 
     * ellenőrzi, hogy elért-e egy végállomást.
     */
    @Override
    public void kozlekedik(){
        Skeleton.hiv(this.id + ":Busz: kozlekedik()");

        if (Skeleton.kerdez("Végállomáshoz ért a busz?")) {
            this.forduloNovel();
        }

        Skeleton.visszater("kozlekedik");
    }

    @Override
    public void utkozik(Jarmu masikJarmu){

    }
    
    /**
     * Növeli a teljesített fordulók számát.
     * Ez a metódus felelős a sikeres körök adminisztrálásáért és a 
     * pontszám növekedésének jelzéséért a naplóban.
     */
    public void forduloNovel() {
        Skeleton.hiv(this.id + ":Busz: forduloNov()");
        this.forduloSzam++;
        Skeleton.naploz("A busz teljesített egy fordulót. Aktuális fordulók: " + forduloSzam);
        Skeleton.naploz("A játékos pontszáma növekedett.");
        Skeleton.visszater("forduloNov");
    }

}
