package grafika.view;

import grafika.Observer;
import jarmuvek.Jarmu;

import java.awt.Graphics;
import java.awt.Image;

/**
 * A járművek grafikus megjelenítéséért felelős osztály.
 */
public class JarmuView implements Observer {

    private Jarmu modell; // Referencia a megfigyelt logikai modellre
    private Image sprite; // A járművet reprezentáló kép

    public JarmuView(Jarmu modell) {
        this.modell = modell;

        // TODO a csapatnak: A sprite betöltése fájlból, például a modell típusa alapján
        // (pl. if (modell instanceof Hokotro) { sprite = ImageIO.read(...); })
    }

    /**
     * Az értesítés hatására aktiválódik, felkészül az újra-rajzolásra.
     */
    @Override
    public void update() {
        // PULL fázis: Itt lehet lekérdezni a modelltől az adatokat a rajzolás előtt
        // Sav aktualisPozicio = modell.getPozicio();
        // int sebesseg = modell.getSebesseg();
    }

    /**
     * A jármű sprite-jának tényleges kirajzolását végzi a képernyőre.
     */
    public void draw(Graphics g) {
        // TODO a csapatnak: Kiszámolni a pixel koordinátákat a modell pozíciója alapján,
        // majd kirajzolni a képet.
        // Példa:
        // if (sprite != null) {
        //     g.drawImage(sprite, xKalkulalt, yKalkulalt, null);
        // }
    }
}
