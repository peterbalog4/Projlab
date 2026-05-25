package grafika.panel;

import funkcionalisElemek.Telephely;
import grafika.JatekAblak;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * A játék indításakor megjelenő letisztult főmenü panel.
 */
public class FomenuPanel extends JPanel {

    // Referencia a főablakra, hogy a gombok hatására ablakot/panelt tudjunk váltani
    private JFrame szuloAblak;

    public FomenuPanel(JFrame szuloAblak) {
        this.szuloAblak = szuloAblak;

        // Elrendezés beállítása: egy oszlopba rendezett komponensek
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15); // Térköz a gombok között
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // 1. Cím felirat a specifikáció ASCII art-ja alapján
        JLabel cimLabel = new JLabel("HÓKOTRÓS JÁTÉK", SwingConstants.CENTER);
        cimLabel.setFont(new Font("Arial", Font.BOLD, 28));
        gbc.gridy = 0;
        add(cimLabel, gbc);

        // 2. "Új Játék Hókotróval" gomb
        JButton ujJatekHokotrovalBtn = new JButton("Új Játék Hókotróval");
        ujJatekHokotrovalBtn.setFont(new Font("Arial", Font.PLAIN, 16));
        gbc.gridy = 1;
        add(ujJatekHokotrovalBtn, gbc);

        // 3. "Új Játék Busszal" gomb
        JButton ujJatekBusszalBtn = new JButton("Új Játék Busszal");
        ujJatekBusszalBtn.setFont(new Font("Arial", Font.PLAIN, 16));
        gbc.gridy = 2;
        add(ujJatekBusszalBtn, gbc);

        // ÚJ: 4. "Többjátékos (Hókotró vs Busz)" gomb
        JButton tobbjatekosBtn = new JButton("Többjátékos (Hókotró vs Busz)");
        tobbjatekosBtn.setFont(new Font("Arial", Font.BOLD, 16));
        gbc.gridy = 3;
        add(tobbjatekosBtn, gbc);

        // 5. "Kilépés" gomb (lejjebb csúszott a gridy = 4-re)
        JButton kilepesBtn = new JButton("Kilépés");
        kilepesBtn.setFont(new Font("Arial", Font.PLAIN, 16));
        gbc.gridy = 4;
        add(kilepesBtn, gbc);

        // --- ESEMÉNYKEZELŐK (A vezérlés váza) ---

        ujJatekHokotrovalBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                inditUjJatek("HOKOTRO");
            }
        });

        ujJatekBusszalBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                inditUjJatek("BUSZ");
            }
        });

        // ÚJ: Többjátékos gomb eseménykezelője
        tobbjatekosBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                inditUjJatek("MULTIPLAYER");
            }
        });

        kilepesBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Alkalmazás biztonságos bezárása
                System.exit(0);
            }
        });
    }

    /**
     * Vezérlési csonk az új játék indításához és a felület átváltásához.
     */
    private void inditUjJatek(String jatekMod) {
        System.out.println("Választás: Új játék indítása ebben a módban: " + jatekMod);

        int dummyJatekosId = 0;
        Telephely ujTelephelyModell = new Telephely(dummyJatekosId);

        if (szuloAblak != null) {
            if (szuloAblak instanceof JatekAblak) {
                System.out.println("--> Szülő ablak megtalálva (JatekAblak). Panelváltás indítása...");
                JatekAblak foablak = (JatekAblak) szuloAblak;
                
                // JAVÍTÁS: Átadjuk a jatekMod paramétert is a főablaknak!
                foablak.jatekInditas(jatekMod, ujTelephelyModell);
                
            } else {
                System.out.println("HIBA: A szuloAblak nem JatekAblak típusú, hanem: " + szuloAblak.getClass().getName());
            }
        } else {
            System.out.println("HIBA: A szuloAblak NULL! (Nem adtad át a konstruktorban?)");
        }
    }
}