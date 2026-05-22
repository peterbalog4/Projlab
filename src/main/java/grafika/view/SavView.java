package grafika.view;

import funkcionalisElemek.Sav;
import grafika.Observer;
import segedOsztalyok.HaladasiIrany;

import java.awt.Color;
import java.awt.Graphics;

/**
 * A játéktér egy sávjának megjelenítéséért felelős nézet.
 */
public class SavView implements Observer {

    private Sav modell; // Referencia a megfigyelt logikai sávra
    private int xKord;  // Statikus X koordináta a képernyőn
    private int yKord;  // Statikus Y koordináta a képernyőn
    private int homennyiseg;
    private boolean jeges;
    private boolean zuzalekos;

    public SavView(Sav modell, int xKord, int yKord) {
        this.modell = modell;
        this.xKord = xKord;
        this.yKord = yKord;
        modell.addObserver(this);
    }

    /**
     * Állapotváltozás (pl. hóesés, jégképződés, takarítás) esetén aktiválódik.
     */
    @Override
    public void update() {
        // PULL fázis: Lekérdezzük a sáv fizikai tulajdonságait
        // int hoMennyiseg = modell.getHo();
        // boolean jegesE = modell.isJeges();
        // boolean zuzalekosE = modell.isZuzalekos();
        int homennyiseg = modell.getHo();
        boolean jeges = modell.isJeg();
        boolean zuzalekos = modell.isZuzalek();

    }

    /**
     * Kirajzolja a sávot a rögzített koordinátákra a tulajdonságok alapján.
     */
    public void draw(Graphics g) {
        // Itt kell vizuálisan megkülönböztetni az állapotokat (pl. kék szín a jégnek,
        // fehér vastagodó téglalap a hónak).

        if(modell.isJeges()) {
            g.setColor(Color.CYAN); // Jég szín
        } else if (modell.getHo() > 0) {
            g.setColor(Color.WHITE); // Hó szín
        } else {
            g.setColor(Color.GRAY); // Alap aszfalt szín
        }
        // Példa váz:
        g.setColor(Color.DARK_GRAY); // Alap aszfalt szín
        if(modell.getIrany() == HaladasiIrany.A_BOL_B_BE){
            g.fillRect(xKord, yKord, 5, modell.getHossz()*5);
        }
        else{
            g.fillRect(xKord, yKord-modell.getHossz()*5, 5, modell.getHossz()*5); //TODO: rendes grafika importálása
        }
        // g.fillRect(xKord, yKord, szelesseg, magassag);
        // if (modell.isJeges()) { ... }
    }
}