package grafika.view;

import funkcionalisElemek.Telephely;
import grafika.Observer;
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
    private final JLabel jmfLabel = new JLabel();
    private final JLabel biokerozinLabel = new JLabel();
    private final JLabel soLabel = new JLabel();
    private final JLabel zuzalekLabel = new JLabel();
    private final JLabel fejekLabel = new JLabel();

    public TelephelyDialog(Window parent, Telephely modell) {
        super(parent, "TELEPHELY", ModalityType.APPLICATION_MODAL);
        this.modell = modell;

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
        repaint();
    }
}
