package grafika.view;

import funkcionalisElemek.Telephely;
import grafika.Observer;
import jarmuvek.Hokotro;
import jarmuvek.Jarmu;
import kotrofejek.KotroFej;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * A telephely (depó) tartalmát megjelenítő modális ablak, amely a játéktéren lévő
 * telephely-ikonra kattintva nyílik meg.
 *
 * Mutatja az erőforrásokat (JMF, biokerozin, só, zúzalék) és a raktáron lévő
 * kotrófejeket, illetve innen nyitható a Bolt is. Feliratkozik a telephelyre
 * ({@link Observer}), így a vásárlások azonnal frissülnek; bezáráskor leiratkozik.
 */
public class TelephelyDialog extends JDialog implements Observer {

    private final Telephely modell;
    private final Hokotro hokotro;
    private final JLabel jmfLabel = new JLabel();
    private final JLabel biokerozinLabel = new JLabel();
    private final JLabel soLabel = new JLabel();
    private final JLabel zuzalekLabel = new JLabel();
    private final JLabel fejekLabel = new JLabel();
    private final JComboBox<String> fejekCombo = new JComboBox<>();

    public TelephelyDialog(Window parent, Telephely modell, Hokotro hokotro) {
        super(parent, "TELEPHELY", ModalityType.APPLICATION_MODAL);
        this.modell = modell;
        this.hokotro = hokotro;

        initUI();
        modell.addObserver(this);
        update(); // kezdeti adatok betöltése

        // Bezáráskor leiratkozunk, hogy ne maradjon halott megfigyelő a modellen.
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosed(java.awt.event.WindowEvent e) {
                modell.removeObserver(TelephelyDialog.this);
            }
        });
    }

    private void initUI() {
        setSize(360, 380);
        setLocationRelativeTo(getOwner());
        setLayout(new BorderLayout(10, 10));

        JPanel kozep = new JPanel();
        kozep.setLayout(new BoxLayout(kozep, BoxLayout.Y_AXIS));
        kozep.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JLabel cim = new JLabel("ERŐFORRÁSOK ÉS KÉSZLETEK");
        cim.setFont(new Font("Arial", Font.BOLD, 14));
        kozep.add(cim);
        kozep.add(Box.createRigidArea(new Dimension(0, 12)));
        kozep.add(jmfLabel);
        kozep.add(biokerozinLabel);
        kozep.add(soLabel);
        kozep.add(zuzalekLabel);
        kozep.add(Box.createRigidArea(new Dimension(0, 16)));
        kozep.add(fejekLabel);
        add(kozep, BorderLayout.CENTER);

        JPanel del = new JPanel();
        JButton boltGomb = new JButton("Bolt megnyitása");
        boltGomb.addActionListener(e -> new BoltDialog(this, modell).setVisible(true));
        JButton bezarGomb = new JButton("Bezárás");
        bezarGomb.addActionListener(e -> dispose());
        del.add(boltGomb);
        del.add(bezarGomb);
        add(del, BorderLayout.SOUTH);

        // --- ÚJ RÉSZ: FELSZERELÉS PANEL ---
        JPanel equipPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        equipPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        JButton equipGomb = new JButton("Felszerel");
        
        equipGomb.addActionListener(e -> felszerel());

        equipPanel.add(new JLabel("Kiválasztott fej: "));
        equipPanel.add(fejekCombo);
        equipPanel.add(equipGomb);
        
        kozep.add(equipPanel);
    }

    private void felszerel() {
        String valasztott = (String) fejekCombo.getSelectedItem();
        if (valasztott == null) return;

        // Megkeressük a tényleges objektumot a raktárban a neve alapján
        KotroFej kivalasztottFej = null;
        for (KotroFej f : modell.getKotrofejek()) {
            if (f.getClass().getSimpleName().equals(valasztott)) {
                kivalasztottFej = f;
                break;
            }
        }

        if (kivalasztottFej != null) {
            // A fejcsere() leveszi a Hókotróról a régit (beteszi a telephelyre), az újat pedig rárakja
            hokotro.fejcsere(kivalasztottFej); 
            update(); // UI frissítése, hogy eltűnjön a lenyíló listából
            JOptionPane.showMessageDialog(this, 
                "Sikeresen felszerelted a járműre: " + valasztott, 
                "Sikeres művelet", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    /** PULL: friss adatok lekérése a telephelytől. */
    @Override
    public void update() {
        jmfLabel.setText("JMF egyenleg: " + modell.getJMF());
        biokerozinLabel.setText("Biokerozin: " + modell.getBiokerozin() + " töltet");
        soLabel.setText("Só: " + modell.getSo() + " töltet");
        zuzalekLabel.setText("Zúzalék: " + modell.getZuzalek() + " töltet");

        List<KotroFej> fejek = modell.getKotrofejek();
        StringBuilder sb = new StringBuilder("<html><b>Raktáron lévő kotrófejek:</b><br>");
        if (fejek == null || fejek.isEmpty()) {
            sb.append("<i>- Nincs raktáron</i>");
        } else {
            for (KotroFej f : fejek) {
                sb.append("- ").append(f.getClass().getSimpleName()).append("<br>");
            }
        }
        sb.append("</html>");
        fejekLabel.setText(sb.toString());

        String jelenlegi = (String) fejekCombo.getSelectedItem();
        
        fejekCombo.removeAllItems();
        for (KotroFej f : modell.getKotrofejek()) {
            fejekCombo.addItem(f.getClass().getSimpleName());
        }
        
        if (jelenlegi != null) {
            fejekCombo.setSelectedItem(jelenlegi);
        }
        
        repaint();
    }
}
