package grafika.view;

import funkcionalisElemek.Sav;
import grafika.Observer;
import segedOsztalyok.HaladasiIrany;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

/**
 * A játéktér egy sávjának megjelenítéséért felelős nézet.
 */
public class SavView implements Observer {

    

    private Sav modell; // Referencia a megfigyelt logikai sávra
    private final int xKord;  // Statikus X koordináta a képernyőn
    private final int yKord;  // Statikus Y koordináta a képernyőn
    private int homennyiseg;
    private boolean jeges;
    private boolean zuzalekos;
    private final int MERET = 60; // Egy sáv négyzet alakú, ennek az oldalhossza

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

        Graphics2D g2d = (Graphics2D) g.create();

        // Kiszámoljuk a sáv középpontját
        int kozepX = xKord + MERET / 2;
        int kozepY = yKord + MERET / 2;
        
        // Elforgatjuk a rajzteret a középpont körül az irány szöge alapján
        g2d.rotate(Math.toRadians(irany.getSzog()), kozepX, kozepY);

        // --- INNEN MINDENT ÚGY RAJZOLUNK, MINTHA ALAPÉRTELMEZETTEN KELETRE (JOBBRA) NÉZNE ---
        // Alap sáv útburkolata
        g2d.setColor(Color.DARK_GRAY);
        g2d.fillRect(xKord, yKord, MERET, modell.getHossz());

        if (this.jeges) {
            g2d.setColor(Color.CYAN);
            g2d.fillRect(xKord, yKord, MERET, modell.getHossz());
        } else if (this.ho > 0) {
            g2d.setColor(Color.WHITE);
            g2d.fillRect(xKord, yKord, MERET, modell.getHossz());
        }

        if (this.zuzalekos) {
            g2d.setColor(Color.GRAY);
            g2d.fillOval(xKord + 20, yKord + 20, 10, 10);
        }

        // Egy sárga vonal a sáv alján, ami segít látni az út orientációját kanyarokban
        g2d.setColor(Color.YELLOW);
        g2d.fillRect(xKord, yKord + MERET - 4, modell.getHossz(), 4);

        // Fekete keret a sáv szélének
        g2d.setColor(Color.BLACK);
        g2d.drawRect(xKord, yKord, MERET, modell.getHossz());

        // Erőforrás felszabadítása
        g2d.dispose();
        
    }
}