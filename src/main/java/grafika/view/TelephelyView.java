package grafika.view;

import funkcionalisElemek.Telephely;
import grafika.Observer;
import kotrofejek.KotroFej;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;
import java.awt.event.ActionListener;

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