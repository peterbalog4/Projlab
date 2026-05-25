package grafika.view;

import grafika.Observer;
import jarmuvek.Jarmu;
import jarmuvek.Auto;
import jarmuvek.Busz;
import jarmuvek.Hokotro;

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
    
    // ÚJ: Statikus változó az ütközés képének, hogy csak egyszer töltődjön be
    private static Image utkozesSprite;
    private static boolean utkozesBetoltve = false;
    
    // Ideiglenes változók a kirajzolás helyének
    private int xKalkulalt = 100; 
    private int yKalkulalt = 100;

    public JarmuView(Jarmu modell) {
        this.modell = modell;
        betoltSprite();
    }

    /**
     * Betölti a megfelelő képet a modell típusa alapján, illetve az ütközés ikonját.
     */
    private void betoltSprite() {
        try {
            if (modell instanceof Hokotro) {
                sprite = ImageIO.read(new File("hokotro.png"));
            }
            else if (modell instanceof Auto) {
                sprite = ImageIO.read(new File("auto.png"));
            }
            else if (modell instanceof Busz) {
                sprite = ImageIO.read(new File("busz.png"));
            }
        } catch (IOException e) {
            System.out.println("Hiba a jármű kép betöltésekor: " + e.getMessage());
        }

        // ÚJ: Ütközés képének betöltése (csak legelőször)
        if (!utkozesBetoltve) {
            try {
                utkozesSprite = ImageIO.read(new File("utkozik.png"));
            } catch (IOException e) {
                System.out.println("Hiba az utkozik.png betöltésekor: " + e.getMessage());
            }
            utkozesBetoltve = true;
        }
    }

    /**
     * Az értesítés hatására aktiválódik, és kiszámolja a pontos képernyő-koordinátákat.
     */
    @Override
    public void update() {
        funkcionalisElemek.Sav aktualisPozicio = modell.getAktualisSav();
        
        if (aktualisPozicio != null) {
            // Kikeresjük, hogy az aktuális sávhoz melyik grafikus nézet tartozik
            grafika.view.SavView sv = grafika.view.SavView.nezetRegiszter.get(aktualisPozicio);
            
            if (sv != null) {
                int baseX = sv.getXKord();
                int baseY = sv.getYKord();
                int tav = modell.getPozicio().getMegtettTavolsag();
                int hossz = aktualisPozicio.getHossz();
                
                segedOsztalyok.Irany utIrany = sv.getIrany(); 
                // Ha B-ből A-ba megyünk, a rajzolást a vonal másik végéről kell indítani!
                boolean abolBbe = (aktualisPozicio.getIrany() == segedOsztalyok.HaladasiIrany.A_BOL_B_BE);
                int vizualisTav = abolBbe ? tav : (hossz - tav);

                // Koordináták kiszámítása az út vizuális haladási iránya alapján
                if (utIrany == segedOsztalyok.Irany.JOBBRA) {
                    this.xKalkulalt = baseX + vizualisTav;
                    this.yKalkulalt = baseY;
                } else if (utIrany == segedOsztalyok.Irany.BALRA) {
                    this.xKalkulalt = baseX + hossz - vizualisTav;
                    this.yKalkulalt = baseY;
                } else if (utIrany == segedOsztalyok.Irany.LE) {
                    this.xKalkulalt = baseX;
                    this.yKalkulalt = baseY + vizualisTav;
                } else if (utIrany == segedOsztalyok.Irany.FEL) {
                    this.xKalkulalt = baseX;
                    this.yKalkulalt = baseY + hossz - vizualisTav;
                }
                
                // Középre igazítás (hogy a jármű ne a sáv szélén csússzon, hanem a közepén)
                if (utIrany == segedOsztalyok.Irany.JOBBRA || utIrany == segedOsztalyok.Irany.BALRA) {
                    this.yKalkulalt += 10; 
                    this.xKalkulalt -= 20; 
                } else {
                    this.xKalkulalt += 10;
                    this.yKalkulalt -= 20;
                }
            }
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

        // ÚJ: Ha a jármű állapota OSSZECSUSZOTT, rárajzoljuk az ütközés ikonját is
        if (modell.getAllapot() == Jarmu.Allapot.OSSZECSUSZOTT && utkozesSprite != null) {
            g.drawImage(utkozesSprite, xKalkulalt, yKalkulalt, null);
        }
    }
}