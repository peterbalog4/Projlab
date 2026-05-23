package grafika.view;

import grafika.Observer;
import jarmuvek.Jarmu;
import jarmuvek.Hokotro; // Hókotró importálása a típusvizsgálathoz

import javax.imageio.ImageIO;
import java.awt.Graphics;
import java.awt.Image;
import java.io.File;
import java.io.IOException;

/**
 * A járművek grafikus megjelenítéséért felelős osztály.
 */
public class JarmuView implements Observer {

    private Jarmu modell; // Referencia a megfigyelt logikai modellre [cite: 557]
    private Image sprite; // A járművet reprezentáló kép [cite: 558]
    
    // Ideiglenes változók a kirajzolás helyének
    private int xKalkulalt = 100; 
    private int yKalkulalt = 100;

    public JarmuView(Jarmu modell) {
        this.modell = modell;
        betoltSprite();
    }

    /**
     * Betölti a megfelelő képet a modell típusa alapján.
     */
    private void betoltSprite() {
        try {
            if (modell instanceof Hokotro) {
                // A csatolt kép betöltése a fájlrendszerből
                sprite = ImageIO.read(new File("hokotro.png"));
                
                // Opcionális: Kép átméretezése, ha túl nagy lenne a játéktérhez
                // sprite = sprite.getScaledInstance(64, 64, Image.SCALE_SMOOTH);
            }
            // Később ide jöhet a Busz típusú jármű képe is
        } catch (IOException e) {
            System.out.println("Hiba a kép betöltésekor: " + e.getMessage());
        }
    }

    /**
     * Az értesítés hatására aktiválódik, és lekérdezi a megjelenítéshez szükséges friss adatokat[cite: 560].
     */
    @Override
    public void update() {
        // A belső modell csak a pálya logikai felépítését ismeri, képernyő-koordinátákat nem tartalmaz.
        // Itt kell a lekérdezett Sav referenciát konkrét X és Y képernyő-koordinátákra leképezni.
        
        // PULL fázis példa:
        // Sav aktualisPozicio = modell.getPozicio();
        // xKalkulalt = KoordinataKalkulator.szamolX(aktualisPozicio);
        // yKalkulalt = KoordinataKalkulator.szamolY(aktualisPozicio);
    }

    /**
     * A jármű sprite-jának tényleges kirajzolását végzi a képernyőre
     */
    public void draw(Graphics g) {
        if (sprite != null) {
            // Kép kirajzolása a kiszámolt koordinátákra
            g.drawImage(sprite, xKalkulalt, yKalkulalt, null);
        } else {
            // Hibakereséshez fallback vizualizáció, ha nem töltődött be a kép
            g.setColor(java.awt.Color.RED);
            g.fillRect(xKalkulalt, yKalkulalt, 50, 50);
        }
    }
}