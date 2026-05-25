package jarmuvek;

import funkcionalisElemek.Sav;
import funkcionalisElemek.Ut;
import segedOsztalyok.HaladasiIrany;
import segedOsztalyok.Irany;

import java.util.*;

public class Auto extends Jarmu {

    private Ut otthon;
    private Ut munkahely;
    private LinkedList<Ut> utvonal;

    public Auto(String id, Ut otthon, Ut munkahely) {
        super(id);
        this.otthon = otthon;
        this.munkahely = munkahely;
        this.utvonal = new LinkedList<>();
    }

    @Override
    public void elertSavVeget() {
        // 1. Ellenőrizzük, hogy elértük-e a célt (munkahely vagy épp az otthon)
        if (aktualisSav != null && aktualisSav.getUt().equals(munkahely)) {
            // Megkeressük a jelenlegi út szembe sávját
            HaladasiIrany ellentetes = (aktualisSav.getIrany() == HaladasiIrany.A_BOL_B_BE) 
                    ? HaladasiIrany.B_BOL_A_BA 
                    : HaladasiIrany.A_BOL_B_BE;
                    
            for (funkcionalisElemek.Sav s : aktualisSav.getUt().getSavok()) {
                if (s.getIrany() == ellentetes) {
                    if (s.elfogad(this)) {
                        // Siker esetén felcseréljük az úti célokat
                        Ut tmp = otthon;
                        otthon = munkahely;
                        munkahely = tmp;
                        
                        utvonal.clear(); // Töröljük a régi útvonalat
                        System.out.println("Auto " + id + " célhoz ért, megfordul és indul vissza!");
                        return; 
                    }
                }
            }
            // Ha a szembe sáv épp foglalt, várunk 1 kört
            megall(1);
            return;
        }

        // 2. Normál haladás és útvonalkövetés
        Ut kovetkezo = kovetkezoUt();
        if (kovetkezo != null) {
            kanyarodik(kovetkezo);
        } else {
            megall(1);
        }
    }

    private Ut kovetkezoUt() {
        if (utvonal.isEmpty()) {
            utvonal = dijkstra(aktualisSav.getUt(), munkahely);
        }
        return utvonal.isEmpty() ? null : utvonal.poll();
    }

    @Override
    public void kozlekedik() {
        if (varakozasiIdo > 0) {
            varakozasiIdo--;
            if (varakozasiIdo == 0) {
                allapot = Allapot.KOZLEKEDIK;
            }
            return;
        }

        if (allapot == Allapot.ELAKADT) {
            probaljSavotValtani();
            return;
        }

        if (aktualisSav == null || pozicio == null) return;

        if (this.allapot != Allapot.CSUSZKAL) {
            this.allapot = Allapot.KOZLEKEDIK;
            pozicio.halad(this, 50);
            
            // Ütközésvizsgálat a normál mozgás után
            if (aktualisSav != null) {
                aktualisSav.jarmuMozgott(this);
            }
        }
    }

    private void probaljSavotValtani() {
        Sav elotteSav = aktualisSav;
            
        this.varakozasiIdo = 0;
        this.allapot = Allapot.KOZLEKEDIK;

        savvaltas(Irany.BALRA);
        if (aktualisSav != elotteSav) return;

        savvaltas(Irany.JOBBRA);
        if (aktualisSav != elotteSav) return;

        this.varakozasiIdo = -1;
        this.allapot = Allapot.ELAKADT;
    }

    @Override
    public void csuszik() {
        this.allapot = Allapot.CSUSZKAL;
        if (pozicio != null) {
            pozicio.halad(this, 10);
            
            // Ütközésvizsgálat a csúszás után
            if (aktualisSav != null) {
                aktualisSav.jarmuMozgott(this);
            }
        }
    }

    @Override
    public void utkozik(Jarmu masikJarmu) {
        if (varakozasiIdo >= 0 && varakozasiIdo < 10) {
            this.megall(10);
            masikJarmu.utkozik(this);
            if (aktualisSav != null) {
                aktualisSav.lezar(10);
            }
        }
    }

    /**
     * Irányfüggő Dijkstra algoritmus a legrövidebb, fizikailag járható út megkeresésére.
     */
    private LinkedList<Ut> dijkstra(Ut forras, Ut cel) {
        
        // Belső osztály a gráf csomópontjainak (út + haladási irány) reprezentálására
        class AllapotG {
            Ut ut;
            HaladasiIrany irany;
            AllapotG(Ut ut, HaladasiIrany irany) {
                this.ut = ut;
                this.irany = irany;
            }
            @Override
            public boolean equals(Object o) {
                if (this == o) return true;
                if (!(o instanceof AllapotG)) return false;
                AllapotG that = (AllapotG) o;
                return ut.equals(that.ut) && irany == that.irany;
            }
            @Override
            public int hashCode() {
                return java.util.Objects.hash(ut, irany);
            }
        }

        Map<AllapotG, Integer> tavolsag = new HashMap<>();
        Map<AllapotG, AllapotG> elozo = new HashMap<>();
        PriorityQueue<AllapotG> sor = new PriorityQueue<>(Comparator.comparingInt(tavolsag::get));

        // A start állapot
        AllapotG start = new AllapotG(forras, aktualisSav.getIrany());
        tavolsag.put(start, 0);
        sor.add(start);

        AllapotG celAllapot = null;

        while (!sor.isEmpty()) {
            AllapotG aktualis = sor.poll();

            // Ha megtaláltuk a célutat, befejezzük a keresést
            if (aktualis.ut.equals(cel)) {
                celAllapot = aktualis;
                break; 
            }

            int aktualisTav = tavolsag.get(aktualis);
            // Csak az adott irányból elérhető kapcsolatokat kérjük le!
            Map<Ut, String> szomszedok = aktualis.ut.getKapcsolatok(aktualis.irany);

            if (szomszedok != null) {
                for (Map.Entry<Ut, String> bejegyzes : szomszedok.entrySet()) {
                    Ut szomszedUt = bejegyzes.getKey();
                    String csatlakozasiPont = bejegyzes.getValue();
                    
                    // Meghatározzuk, hogy az új úton melyik sávon fogunk haladni
                    HaladasiIrany ujIrany = csatlakozasiPont.equals("vegA") 
                            ? HaladasiIrany.A_BOL_B_BE 
                            : HaladasiIrany.B_BOL_A_BA;
                    
                    AllapotG ujAllapot = new AllapotG(szomszedUt, ujIrany);
                    int ujTav = aktualisTav + 1;

                    if (ujTav < tavolsag.getOrDefault(ujAllapot, Integer.MAX_VALUE)) {
                        tavolsag.put(ujAllapot, ujTav);
                        elozo.put(ujAllapot, aktualis);
                        sor.remove(ujAllapot);
                        sor.add(ujAllapot);
                    }
                }
            }
        }

        // Visszafejtjük a legrövidebb utat
        LinkedList<Ut> utvonal = new LinkedList<>();
        AllapotG lepes = celAllapot;
        while (lepes != null && elozo.containsKey(lepes)) {
            utvonal.addFirst(lepes.ut);
            lepes = elozo.get(lepes);
        }

        return utvonal;
    }
}