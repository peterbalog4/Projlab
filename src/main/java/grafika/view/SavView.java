package grafika.view;

import funkcionalisElemek.Sav;
import grafika.Observer;
import segedOsztalyok.HaladasiIrany;
import segedOsztalyok.Irany;

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
    private final Irany irany;

    public SavView(Sav modell, int xKord, int yKord, Irany irany) {
        this.modell = modell;
        this.xKord = xKord;
        this.yKord = yKord;
        this.irany = irany;
        modell.addObserver(this);
        update(); // Kezdeti állapot lekérése a megjelenítéshez
    }

    /**
     * Állapotváltozás (pl. hóesés, jégképződés, takarítás) esetén aktiválódik.
     */
    @Override
    public void update() {
        homennyiseg = modell.getHo();
        jeges = modell.isJeg();
        zuzalekos = modell.isZuzalek();

    }

    /**
     * Kirajzolja a sávot a rögzített koordinátákra a tulajdonságok alapján.
     */
    public void draw(Graphics g) {
        // Itt kell vizuálisan megkülönböztetni az állapotokat (pl. kék szín a jégnek,
        // fehér vastagodó téglalap a hónak).

        Graphics2D g2d = (Graphics2D) g.create();

        
        
        // Elforgatjuk a rajzteret a középpont körül az irány szöge alapján

        // --- INNEN MINDENT ÚGY RAJZOLUNK, MINTHA ALAPÉRTELMEZETTEN KELETRE (JOBBRA) NÉZNE ---
        // Alap sáv útburkolata
        g2d.setColor(Color.DARK_GRAY);

        if (this.jeges) {               //TODO: ide a rendes képeket beszúrni
            g2d.setColor(Color.CYAN);
        } else if (this.homennyiseg > 2) {
            g2d.setColor(Color.WHITE);
        }
        else if (this.zuzalekos) {
            g2d.setColor(Color.GRAY);
        }
        else {
            g2d.setColor(Color.DARK_GRAY);
        }
        switch(irany){
            case FEL:
            case LE:
                g2d.fillRect(xKord, yKord, MERET, modell.getHossz());
                g2d.setColor(Color.YELLOW);
                g2d.drawRect(xKord, yKord, MERET, modell.getHossz());
                break;
            case JOBBRA:
            case BALRA:
                 g2d.fillRect(xKord, yKord, modell.getHossz(), MERET);
                 g2d.setColor(Color.YELLOW);
                 g2d.drawRect(xKord, yKord, modell.getHossz(), MERET);
                 break;
        }
        // TODO: kanyarodást megcsinálni
        // g2d.setColor(Color.YELLOW);
        // g2d.fillRect(xKord, yKord + MERET - 4, modell.getHossz(), 4);

        // Erőforrás felszabadítása
        g2d.dispose();
        
    }
}