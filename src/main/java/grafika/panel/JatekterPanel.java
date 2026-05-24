package grafika.panel;

import grafika.Observer;
import grafika.view.JarmuView;
import grafika.view.UtView;
import jarmuvek.Hokotro;
import jarmuvek.Jarmu;

import javax.swing.*;

import org.w3c.dom.events.MouseEvent;

import funkcionalisElemek.KorSzamlalo;
import funkcionalisElemek.Ut;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

/**
 * A fő játékablak központi része, a pálya vizualizációja.
 * Itt jelennek meg az Utak és a rajtuk mozgó járművek.
 */
public class JatekterPanel extends JPanel implements Observer {

    // A kirajzolandó grafikus nézetek listái
    private List<UtView> utakNezetei;
    private List<JarmuView> jarmuvekNezetei;
    private KorSzamlalo modell;
    private Map<Jarmu, JarmuView> jarmuNezetekMap;

public JatekterPanel(KorSzamlalo modell) {
        this.modell = modell;
        this.utakNezetei = new ArrayList<>();
        this.jarmuvekNezetei = new ArrayList<>();
        this.jarmuNezetekMap = new HashMap<>();

        setBackground(new Color(240, 248, 255));
        
        // EGÉR IRÁNYÍTÁS BEKÖTÉSE
        // EGÉR IRÁNYÍTÁS BEKÖTÉSE
        addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                int mouseX = e.getX();
                int mouseY = e.getY();
                funkcionalisElemek.Ut celUt = null;

                // 1. Megkeressük, melyik útra/sávra kattintott a játékos
                for (grafika.view.UtView uv : utakNezetei) {
                    if (uv.contains(mouseX, mouseY)) {
                        celUt = uv.getModell();
                        break;
                    }
                }

                // 2. Ha érvényes útra kattintott, átadjuk a parancsot a Hókotrónak
                if (celUt != null) {
                    for (jarmuvek.Jarmu j : modell.getJarmuvek()) {
                        if (j instanceof jarmuvek.Hokotro) {
                            jarmuvek.Hokotro hokotro = (jarmuvek.Hokotro) j;
                            hokotro.setKovetkezoUt(celUt);
                            System.out.println("Játékos kattintott! Új célpont a hókotrónak sikeresen átadva.");
                            break;
                        }
                    }
                }
            }
        });
    }

    // Ezeket a metódusokat a pályabetöltő fogja használni,
    // hogy felpakolja a nézeteket a vászonra
    public void addUtView(UtView uv) {
        utakNezetei.add(uv);
    }

    /**
     * Ezt hívja meg a KorSzamlalo a notifyObservers() révén minden körben,
     * illetve ha állapotváltozás történik.
     */
    @Override
public void update() {
        // PULL fázis: Lekérjük az összes aktuális járművet a modelltől
        List<Jarmu> aktualisJarmuvek = modell.getJarmuvek();

        for (Jarmu j : aktualisJarmuvek) {
            // Ha felbukkan egy olyan jármű, aminek még nincs grafikus nézete
            if (!jarmuNezetekMap.containsKey(j)) {
                System.out.println("JatekterPanel: Új jármű észlelve! Nézet generálása...");
                JarmuView ujNezet = new JarmuView(j);
                
                // KONKRÉT IMPLEMENTÁCIÓ: Hozzáadás a Map-hez ÉS a kirajzolandó listához is!
                jarmuNezetekMap.put(j, ujNezet);
                jarmuvekNezetei.add(ujNezet); 
            }
        }

        // Frissítjük az összes létező járműnézetet (pozíciók újraszámolása)
        for (JarmuView jv : jarmuNezetekMap.values()) {
            jv.update();
        }

        // A Swing motorjának jelezzük, hogy rajzoljon újra mindent
        repaint();
    }

    /**
     * A Swing grafikus motorja hívja meg, amikor újra kell rajzolni a panelt.
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g); // Alapértelmezett törlés/háttérfestés

        // 1. Először az utakat (és a sávokat) rajzoljuk ki, mert azok vannak legalul
        for (UtView uv : utakNezetei) {
            uv.draw(g);
        }

        // 2. Erre rajzoljuk rá a járműveket, hogy takarják az aszfaltot/havat
        for (JarmuView jv : jarmuvekNezetei) {
            jv.draw(g);
        }
    }
}
