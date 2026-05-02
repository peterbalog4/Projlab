package segedOsztalyok;

import funkcionalisElemek.Sav;
import jarmuvek.Jarmu;

public class Pozicio {
    private Sav sav;
    private int megtettTavolsag;
    private int savHossz; 

    public Pozicio(Sav sav, int savHossz) {
        this.sav = sav;
        this.savHossz = savHossz;
        this.megtettTavolsag = 0;
    }

    public void halad(Jarmu jarmu, int sebesseg) {
        this.megtettTavolsag += sebesseg;
        
        // TDA: A pozíció maga ellenőrzi a határt, és ha túlléptük, utasítja a járművet
        if (this.megtettTavolsag >= this.savHossz) {
            jarmu.elertSavVeget(); // Ezt az új metódust a Jarmu-ben kell megírnod a kanyarodáshoz
        }
    }
    
    public void ujSavraLep(Sav ujSav, int ujSavHossz) {
        this.sav = ujSav;
        this.savHossz = ujSavHossz;
        this.megtettTavolsag = 0; // Új sávon nulláról indulunk
    }
    
    public boolean utkozikE(Pozicio masikPozicio) {
        // Egyszerű távolságalapú ütközésvizsgálat (pl. 5 méteren belül vannak)
        return Math.abs(this.megtettTavolsag - masikPozicio.megtettTavolsag) <= 5;
    }
}