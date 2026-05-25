package grafika.view;

import grafika.Observer;
import jarmuvek.Busz;

import javax.swing.*;
import java.awt.*;

/**
 * A buszos játékmódhoz tartozó jobb oldali HUD, amely a teljesített fordulókat mutatja.
 */
public class BuszView extends JPanel implements Observer {

    private Busz busz;
    private JLabel forduloLabel;

    public BuszView(Busz busz) {
        this.busz = busz;

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createTitledBorder("BUSZ MENETREND"));
        setPreferredSize(new Dimension(250, 0)); // Ugyanaz a szélesség, mint a TelephelyView

        forduloLabel = new JLabel("Teljesített fordulók: 0");
        forduloLabel.setFont(new Font("Arial", Font.BOLD, 14));
        forduloLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        add(Box.createRigidArea(new Dimension(0, 20))); // Térköz fentről
        add(forduloLabel);
    }

    @Override
    public void update() {
        if (busz != null) {
            forduloLabel.setText("Teljesített fordulók: " + busz.getForduloSzam());
        }
        // Újrarajzolás kérése a Swing-től
        repaint();
    }
}