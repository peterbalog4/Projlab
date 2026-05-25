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
        // Ha már korábban elérte a végét, de még vár a parancsra (nem kanyarodott el):
        if (this.megtettTavolsag >= this.savHossz) {
            jarmu.elertSavVeget();
            return;
        }
        
        this.megtettTavolsag += sebesseg;
        
        // Ha éppen most érte el a sáv végét:
        if (this.megtettTavolsag >= this.savHossz) {
            this.megtettTavolsag = this.savHossz; // Rögzítjük a maximumot, nem engedjük túlfutni!
            jarmu.elertSavVeget(); 
        }
    }
    
    public void ujSavraLep(Sav ujSav, int ujSavHossz) {
        this.sav = ujSav;
        this.savHossz = ujSavHossz;
        this.megtettTavolsag = 0; // Új sávon nulláról indulunk
    }
    
    public boolean utkozikE(Pozicio masikPozicio) {
        // Egyszerű távolságalapú ütközésvizsgálat (pl. 5 méteren belül vannak)
        return this.megtettTavolsag == masikPozicio.megtettTavolsag;
    }

    public int getMegtettTavolsag() {
        return this.megtettTavolsag;
    }
}