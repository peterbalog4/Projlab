package grafika;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import funkcionalisElemek.*;
import grafika.panel.FomenuPanel;
import grafika.panel.JatekterPanel;
import grafika.view.TelephelyView;
import vezerles.Map_generator;


/**
 * A fő alkalmazásablak, amely a játék körszámlálóját jeleníti meg,
 * és menedzseli a grafikus ablak ciklusát.
 */
public class JatekAblak extends JFrame implements Observer {

    // Attribútumok a specifikáció alapján
    private KorSzamlalo modell; // Referencia a körszámláló modelljére
    private FomenuPanel fomenü; // Eltároljuk referenciaként, hogy később le tudjuk venni
    private JLabel korLabel; // Ezt fogjuk frissíteni körönként
    /**
     * Konstruktor, amely inicializálja az ablakot és összeköti a modellel.
     */
    public JatekAblak(KorSzamlalo modell) {
        this.modell = modell;

        // Alapvető ablakbeállítások
        setTitle("Hókotrós Játék - Bízz bennem, mérnök leszek");
        setSize(1024, 768);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Ablak középre helyezése
        setLayout(new BorderLayout());

        // A keretrendszer inicializálása (ide pakolhatják majd a többiek a paneleket)
        initLayoutVaz();

        // Az ablak láthatóvá tétele, miután a grafikus kontextus készen áll
        setVisible(true);
    }

    /**
     * Létrehozza a felület strukturális vázát (HUD helye, játéktér helye).
     */
    private void initLayoutVaz() {
        FomenuPanel fomenü = new FomenuPanel(this);

        // Elhelyezzük az ablak közepén
        this.add(fomenü, BorderLayout.CENTER);
    }

    public void jatekInditas(Telephely telephelyModell) {
        System.out.println("--> JatekAblak: jatekInditas() elindult, régi panel eltávolítása...");

        // 1. Letakarítunk mindent a JFrame-ről! Ez a legbiztosabb módszer.
        this.getContentPane().removeAll();

        // 2. Felső sáv a Körszámlálónak
        JPanel felsoSav = new JPanel(new FlowLayout(FlowLayout.LEFT));
        korLabel = new JLabel("KÖR: " + modell.getKor() + " / 50");
        korLabel.setFont(new Font("Arial", Font.BOLD, 16));
        felsoSav.add(korLabel);
        this.getContentPane().add(felsoSav, BorderLayout.NORTH);

        // 3. Telephely HUD panel jobbra
        TelephelyView telephelyHud = new TelephelyView(telephelyModell);
        telephelyModell.addObserver(telephelyHud);
        this.getContentPane().add(telephelyHud, BorderLayout.EAST);

        


        // 4. Játéktér panel középre (Ez lesz a központi vászon)
        JatekterPanel jatekter = new JatekterPanel();
        this.getContentPane().add(jatekter, BorderLayout.CENTER);
        
        //map betöltése
        Map_generator map = new Map_generator(modell);
        map.load("tests/test_map.txt", jatekter);

        // 5. Vezérlő panel alulra
        JPanel vezerloPanel = new JPanel();
        JButton kovetkezoKorGomb = new JButton("Következő Kör");
        kovetkezoKorGomb.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Gombnyomás: Következő kör!");
                modell.leptet();
            }
        });
        vezerloPanel.add(kovetkezoKorGomb);
        this.getContentPane().add(vezerloPanel, BorderLayout.SOUTH);

        // 6. Swing képernyőfrissítés (kötelező a removeAll után)
        this.getContentPane().revalidate();
        this.getContentPane().repaint();

        System.out.println("--> JatekAblak: Új panelek felrakva, ablak frissítve!");
    }

    /**
     * Frissíti az ablak adatait, amikor a KörSzámláló léptet.
     */
    @Override
    public void update() {
        // PULL fázis: Amikor a modell szólt, hogy telt az idő, frissítjük a szöveget!
        if (korLabel != null) {
            korLabel.setText("KÖR: " + modell.getKor() + " / 50");
        }
        repaint();
    }

    /**
     * A főablak tartalmának újra-renderelését koordináló metódus.
     */
    public void render() {
        // Swing környezetben a repaint() hívás jelzi a keretrendszernek,
        // hogy a komponenseket újra kell rajzolni a Graphics kontextus segítségével.
        repaint();
    }
}