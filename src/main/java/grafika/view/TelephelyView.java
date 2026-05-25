package grafika.view;

import funkcionalisElemek.Telephely;
import grafika.Observer;
import kotrofejek.KotroFej;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * A játékost segítő telephely adatainak (JMF egyenleg, készletek, kotrófejek)
 * grafikus megjelenítéséért felelős nézet osztály.
 */
public class TelephelyView extends JPanel implements Observer {

    // Referencia a telephely logikai modelljére a pull alapú adatlekéréshez
    private Telephely modell;

    // Swing UI elemek a statikus információk megjelenítéséhez
    private JLabel jmfLabel;
    private JLabel biokerozinLabel;
    private JLabel soLabel;
    private JLabel zuzalekLabel;
    private JLabel kotrofejekLabel;
    private JButton boltGomb;

    /**
     * Konstruktor, amely inicializálja a panelt és a UI elemeket.
     * 
     * @param modell A megfigyelendő Telephely modell.
     */
    public TelephelyView(Telephely modell) {
        this.modell = modell;

        // A panel elrendezésének és stílusának beállítása
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createTitledBorder("TELEPHELY"));
        setPreferredSize(new Dimension(250, 0)); // Fix szélesség az oldalsó sávnak

        // Telephely fejléc-kép – a jobb oldali HUD-on jelenik meg (nem a térképen).
        // Kattintásra megnyitja a telephely részletező ablakát.
        JLabel ikonLabel = keszitIkonLabel();
        if (ikonLabel != null) {
            ikonLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            ikonLabel.setToolTipText("Kattints a telephely részleteihez");
            ikonLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            ikonLabel.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    Window parentWindow = SwingUtilities.getWindowAncestor(TelephelyView.this);
                    new TelephelyDialog(parentWindow, modell).setVisible(true);
                }
            });
            add(ikonLabel);
            add(Box.createRigidArea(new Dimension(0, 12)));
        }

        // UI elemek inicializálása
        jmfLabel = new JLabel("JMF Egyenleg: 0");
        biokerozinLabel = new JLabel("Biokerozin: 0 liter");
        soLabel = new JLabel("Só: 0 kg");
        zuzalekLabel = new JLabel("Zúzalék: 0 kg");
        kotrofejekLabel = new JLabel("Elérhető kotrófejek: Nincs");

        boltGomb = new JButton("Bolt megnyitása");

        // Elemek hozzáadása a panelhez
        add(new JLabel("ERŐFORRÁSOK ÉS KÉSZLETEK:"));
        add(Box.createRigidArea(new Dimension(0, 10))); // Térköz
        add(jmfLabel);
        add(biokerozinLabel);
        add(soLabel);
        add(zuzalekLabel);
        add(Box.createRigidArea(new Dimension(0, 20)));
        add(kotrofejekLabel);
        add(Box.createRigidArea(new Dimension(0, 20)));
        add(boltGomb);

        // A Bolt gomb eseménykezelőjének váza
        boltGomb.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Megkeressük a szülő ablakot (JFrame / JatekAblak) a dialogushoz
                Window parentWindow = SwingUtilities.getWindowAncestor(TelephelyView.this);

                // Példányosítjuk és megjelenítjük a boltot
                BoltDialog boltAblak = new BoltDialog(parentWindow, modell);
                boltAblak.setVisible(true);
            }
        });
        update();
    }

    /**
     * Betölti és arányosan átméretezi a telephely ikonját ({@code Telephely.PNG}) egy
     * {@link JLabel}-be, amit a HUD tetejére teszünk. Hiba esetén {@code null}-t ad vissza,
     * ekkor a panel egyszerűen kép nélkül jelenik meg.
     */
    private JLabel keszitIkonLabel() {
        try {
            Image kep = ImageIO.read(new File("Telephely.PNG"));
            if (kep == null) {
                System.out.println("TelephelyView: nincs kepolvaso a Telephely.PNG-hez");
                return null;
            }
            int magassag = 120;
            float arany = kep.getWidth(null) / (float) kep.getHeight(null);
            int szelesseg = Math.round(magassag * arany);
            Image atmeretezett = kep.getScaledInstance(szelesseg, magassag, Image.SCALE_SMOOTH);
            return new JLabel(new ImageIcon(atmeretezett));
        } catch (IOException ex) {
            System.out.println("TelephelyView: nem sikerult betolteni a Telephely.PNG-t: " + ex.getMessage());
            return null;
        }
    }

    /**
     * Az Observer interfész implementációja.
     * Értesül a telephely belső állapotának (pl. egyenleg) változásáról.
     */
    @Override
    public void update() {
        // PULL fázis: A friss adatok lekérése a modelltől
        int frissJmf = modell.getJMF();
        int frissBiokerozin = modell.getBiokerozin();
        int frissSo = modell.getSo();
        int frissZuzalek = modell.getZuzalek();
        // List<Kotrofej> frissFejek = modell.getKotrofejek();

        // UI elemek szövegének frissítése
        jmfLabel.setText("JMF Egyenleg: " + frissJmf);
        biokerozinLabel.setText("Biokerozin: " + frissBiokerozin + " liter");
        soLabel.setText("Só: " + frissSo + " kg");
        zuzalekLabel.setText("Zúzalék: " + frissZuzalek + " kg");

        List<KotroFej> frissFejek = modell.getKotrofejek();
        StringBuilder fejekSzoveg = new StringBuilder("<html><b>Elérhető kotrófejek:</b><br>");
        if (frissFejek == null || frissFejek.isEmpty()) {
            fejekSzoveg.append("<i>- Nincs raktáron</i>");
        } else {
            for (KotroFej fej : frissFejek) {
                // A getClass().getSimpleName() kiveszi az osztály nevét (pl. "SoproFej")
                fejekSzoveg.append("- ").append(fej.getClass().getSimpleName()).append("<br>");
            }
        }
        fejekSzoveg.append("</html>");
        kotrofejekLabel.setText(fejekSzoveg.toString());
        // Újrarajzolás kérése a Swing keretrendszertől
        repaint();
    }

    /**
     * Kirajzolja a telephelyhez tartozó UI elemeket.
     * Mivel a HUD Swing komponenseket (JLabel, JButton) használ,
     * a Swing automatikusan elvégzi a renderelést, de az egyedi rajzoláshoz
     * a paintComponent-et kell felülírni, amiből meghívható ez a metódus.
     */
    public void draw(Graphics g) {
        // TODO: Ha a csapat egyedi ikonokat (pl. JMF érme ikon) vagy egyedi grafikát
        // szeretne rajzolni a szövegek mellé közvetlenül a Graphics objektummal,
        // azt ide tehetik.
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // Összekötjük a Swing rajzoló ciklusát a specifikált draw metódussal
        draw(g);
    }
}