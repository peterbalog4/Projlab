package grafika.view;

import grafika.Observer;
import jarmuvek.Busz;
import jarmuvek.Jarmu;

import javax.swing.*;
import java.awt.*;

/**
 * A buszos játékmódhoz tartozó jobb oldali HUD, amely a teljesített fordulókat 
 * és az esetleges elakadást mutatja.
 */
public class BuszView extends JPanel implements Observer {

    private Busz busz;
    private JLabel forduloLabel;
    private JLabel elakadtLabel; // ÚJ: Felirat az elakadás jelzésére

    public BuszView(Busz busz) {
        this.busz = busz;

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createTitledBorder("BUSZ MENETREND"));
        setPreferredSize(new Dimension(250, 0));

        forduloLabel = new JLabel("Teljesített fordulók: 0");
        forduloLabel.setFont(new Font("Arial", Font.BOLD, 14));
        forduloLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // ÚJ: Elakadás felirat inicializálása feltűnő piros színnel
        elakadtLabel = new JLabel("");
        elakadtLabel.setFont(new Font("Arial", Font.BOLD, 14));
        elakadtLabel.setForeground(Color.RED);
        elakadtLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        add(Box.createRigidArea(new Dimension(0, 20))); // Felső térköz
        add(forduloLabel);
        add(Box.createRigidArea(new Dimension(0, 15))); // Térköz a forduló alatt
        add(elakadtLabel); // ÚJ: Behelyezés a forduló felirata alá
    }

    @Override
    public void update() {
        if (busz != null) {
            // Fordulók számának frissítése
            forduloLabel.setText("Teljesített fordulók: " + busz.getForduloSzam());

            // ÚJ: Állapotellenőrzés a jármű modellje alapján
            if (busz.getAllapot() == Jarmu.Allapot.ELAKADT) {
                elakadtLabel.setText("ELAKADT A HÓBAN!");
            } else if (busz.getAllapot() == Jarmu.Allapot.OSSZECSUSZOTT) {
                elakadtLabel.setText("BALESET / ÜTKÖZÉS!");
            } else {
                elakadtLabel.setText(""); // Ha normálisan közlekedik, eltüntetjük a szöveget
            }
        }
        repaint();
    }
}