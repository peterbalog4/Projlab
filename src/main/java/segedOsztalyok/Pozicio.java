package segedOsztalyok;

import funkcionalisElemek.Sav;
import jarmuvek.Jarmu;

public class Pozicio {
    private Sav sav;
    private int megtettTavolsag;
    private int elozoTavolsag; // ÚJ: Nyilvántartja honnan indultunk az adott körben
    private int savHossz; 

    public Pozicio(Sav sav, int savHossz) {
        this.sav = sav;
        this.savHossz = savHossz;
        this.megtettTavolsag = 0;
        this.elozoTavolsag = 0;
    }

    public void halad(Jarmu jarmu, int sebesseg) {
        if (this.megtettTavolsag >= this.savHossz) {
            jarmu.elertSavVeget();
            return;
        }
        
        this.elozoTavolsag = this.megtettTavolsag; // JAVÍTÁS: Eltároljuk a kiindulási pontot
        this.megtettTavolsag += sebesseg;
        
        if (this.megtettTavolsag >= this.savHossz) {
            this.megtettTavolsag = this.savHossz;
            jarmu.elertSavVeget(); 
        }
    }
    
    public void ujSavraLep(Sav ujSav, int ujSavHossz) {
        this.sav = ujSav;
        this.savHossz = ujSavHossz;
        this.megtettTavolsag = 0; 
        this.elozoTavolsag = 0; 
    }
    
    public boolean utkozikE(Pozicio masikPozicio) {
        int masikTav = masikPozicio.getMegtettTavolsag();
        
        // 1. Átugrás vizsgálata: a másik jármű az előző és a mostani helyünk közé esik
        boolean atugrottuk = (this.elozoTavolsag <= masikTav && this.megtettTavolsag >= masikTav);
        
        // 2. Közelség vizsgálata: megálltunk-e 40 méteren belül
        boolean kozelVan = Math.abs(this.megtettTavolsag - masikTav) < 40;
        
        return atugrottuk || kozelVan;
    }

    public int getMegtettTavolsag() {
        return this.megtettTavolsag;
    }
}