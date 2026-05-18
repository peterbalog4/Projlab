package grafika.view;

import funkcionalisElemek.Sav;
import grafika.Observer;

import java.awt.Graphics;

/**
 * A játéktér egy sávjának megjelenítéséért felelős nézet.
 */
public class SavView implements Observer {

    private Sav modell; // Referencia a megfigyelt logikai sávra
    private int xKord;  // Statikus X koordináta a képernyőn
    private int yKord;  // Statikus Y koordináta a képernyőn

    public SavView(Sav modell, int xKord, int yKord) {
        this.modell = modell;
        this.xKord = xKord;
        this.yKord = yKord;
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
    }

    /**
     * Kirajzolja a sávot a rögzített koordinátákra a tulajdonságok alapján.
     */
    public void draw(Graphics g) {
        // TODO a csapatnak: A sáv téglalapjának megrajzolása (g.fillRect).
        // Itt kell vizuálisan megkülönböztetni az állapotokat (pl. kék szín a jégnek,
        // fehér vastagodó téglalap a hónak).

        // Példa váz:
        // g.setColor(Color.DARK_GRAY); // Alap aszfalt szín
        // g.fillRect(xKord, yKord, szelesseg, magassag);
        // if (modell.isJeges()) { ... }
    }
}