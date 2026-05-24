package grafika.view;

import grafika.Observer;
import jarmuvek.Jarmu;
import jarmuvek.Auto;
import jarmuvek.Busz;
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

    private Jarmu modell; // Referencia a megfigyelt logikai modellre
    private Image sprite; // A járművet reprezentáló kép 
    
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
            else if (modell instanceof Auto) {
                // Az Auto típusú jármű képének betöltése
                sprite = ImageIO.read(new File("auto.png"));
            }
            else if (modell instanceof Busz) {
                // A Busz típusú jármű képének betöltése 
                sprite = ImageIO.read(new File("busz.png"));
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
        // 1. Lekérjük a modellből a jármű aktuális sávját
        funkcionalisElemek.Sav aktualisPozicio = modell.getAktualisSav();
        
        if (aktualisPozicio != null) {
            // Mivel az MVC elv szerint a logikai modell nem tartalmaz pixelkoordinátát, 
            // a JarmuView-nak kell azt kiszámolnia vagy lekérnie.
            // (Ha van KoordinataKalkulator osztályotok, azt használd, egyébként ideiglenesen 
            // a teszteléshez egy statikus leképezést adunk, hogy látszódjon a képernyőn):
            
            // TODO: Itt kell lekérdezni a SavView-tól a pontos X és Y koordinátát. 
            // Példa egy egyszerű fallback logikára a teszteléshez:
            this.xKalkulalt = 150; // Helyettesítsd: aktualisPozicio.getX() szerű hívással, ha elkészül
            this.yKalkulalt = 200; // Helyettesítsd: aktualisPozicio.getY() szerű hívással, ha elkészül
        }
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