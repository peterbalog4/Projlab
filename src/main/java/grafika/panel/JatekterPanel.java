package grafika.panel;

import grafika.view.JarmuView;
import grafika.view.UtView;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * A fő játékablak központi része, a pálya vizualizációja.
 * Itt jelennek meg az Utak és a rajtuk mozgó járművek.
 */
public class JatekterPanel extends JPanel {

    // A kirajzolandó grafikus nézetek listái
    private List<UtView> utakNezetei;
    private List<JarmuView> jarmuvekNezetei;

    public JatekterPanel() {
        this.utakNezetei = new ArrayList<>();
        this.jarmuvekNezetei = new ArrayList<>();

        // Háttérszín beállítása (pl. téli, havas tájhoz egy halvány szürke/kék)
        setBackground(new Color(240, 248, 255));
    }

    // Ezeket a metódusokat a pályabetöltő fogja használni,
    // hogy felpakolja a nézeteket a vászonra
    public void addUtView(UtView uv) {
        utakNezetei.add(uv);
    }

    public void addJarmuView(JarmuView jv) {
        jarmuvekNezetei.add(jv);
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
