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
    private final int xKord;  // Statikus X koordináta a képernyőn, ezek igazából egy úton belül megegyeznek
    private final int yKord;  // Statikus Y koordináta a képernyőn
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

        g.setColor(Color.DARK_GRAY);
        g.fillRect(xKord, yKord, 60, 60);

        // Fizikai állapotok vizualizációja a letárolt adatok alapján
        if (this.jeges) {
            g.setColor(Color.CYAN);
            g.fillRect(xKord, yKord, 60, 60);
        } else if (this.homennyiseg > 0) {
            g.setColor(Color.WHITE);
            g.fillRect(xKord, yKord, 60, 60);
        }

        if (this.zuzalekos) {
            g.setColor(Color.GRAY);
            g.fillOval(xKord + 10, yKord + 10, 10, 10);
        }
        
        g.setColor(Color.BLACK);
        g.drawRect(xKord, yKord, 60, 60); // Keret
        
    }
}