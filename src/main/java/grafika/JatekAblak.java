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

    public void jatekInditas(String jatekMod, Telephely telephelyModell) {
        System.out.println("--> JatekAblak: jatekInditas() elindult, régi panel eltávolítása...");

        this.getContentPane().removeAll();

        // 2. Felső sáv a Körszámlálónak
        JPanel felsoSav = new JPanel(new FlowLayout(FlowLayout.LEFT));
        korLabel = new JLabel("KÖR: " + modell.getKor() + " / 50");
        korLabel.setFont(new Font("Arial", Font.BOLD, 16));
        felsoSav.add(korLabel);
        this.getContentPane().add(felsoSav, BorderLayout.NORTH);


       // ÚJ: CardLayout a jobb oldali panelnek, hogy cserélgetni tudjuk a HUD-ot
        java.awt.CardLayout cardLayout = new java.awt.CardLayout();
        JPanel jobbPanel = new JPanel(cardLayout);
        this.getContentPane().add(jobbPanel, BorderLayout.EAST);

        // 4. Játéktér panel középre
        JatekterPanel jatekter = new JatekterPanel(modell); 
        modell.addObserver(jatekter);
        this.getContentPane().add(jatekter, BorderLayout.CENTER);

        Map_generator map = new Map_generator(modell);
        map.load("src/main/java/vezerles/nagy_palya.txt", jatekter, telephelyModell);

        java.util.List<funkcionalisElemek.Ut> utak = modell.getUtak();

        // ── Autók spawnolása (a korábban megírt kódod itt marad) ──
        if (utak.size() >= 9) { 
            for (int i = 0; i < 4; i++) {
                funkcionalisElemek.Ut otthonUt = utak.get(i * 2 + 1);      
                funkcionalisElemek.Ut munkahelyUt = utak.get(i * 2 + 2);   
                if (!otthonUt.getSavok().isEmpty()) {
                    funkcionalisElemek.Sav induloSav = otthonUt.getSavok().get(0);
                    jarmuvek.Auto induloAuto = new jarmuvek.Auto("auto_start_" + (i + 1), otthonUt, munkahelyUt);
                    if (induloSav.elfogad(induloAuto)) modell.addJarmu(induloAuto); 
                }
            }
        }

        if (!utak.isEmpty() && !utak.get(0).getSavok().isEmpty()) {
            funkcionalisElemek.Sav induloSav = utak.get(0).getSavok().get(0);

            if ("MULTIPLAYER".equals(jatekMod)) {
                // HÓKOTRÓ LÉTREHOZÁSA (indul az első útról)
                funkcionalisElemek.Sav hokotroSav = utak.get(0).getSavok().get(0);
                jarmuvek.Hokotro kezdoHokotro = new jarmuvek.Hokotro("hokotro_1", 0, telephelyModell);
                kotrofejek.SoproFej kezdoFej = new kotrofejek.SoproFej();
                telephelyModell.tarol(kezdoFej);
                kezdoHokotro.fejcsere(kezdoFej);
                if (hokotroSav.elfogad(kezdoHokotro)) modell.addJarmu(kezdoHokotro);

                TelephelyView telephelyHud = new TelephelyView(telephelyModell);
                telephelyModell.addObserver(telephelyHud);
                jobbPanel.add(telephelyHud, "HOKOTRO");

                // BUSZ LÉTREHOZÁSA (indul az utolsó útról)
                funkcionalisElemek.Ut vegallomas1 = utak.get(0);
                funkcionalisElemek.Ut vegallomas2 = utak.get(utak.size() - 1);
                vegallomas1.setVegallomas(true);
                vegallomas2.setVegallomas(true);
                
                funkcionalisElemek.Sav buszSav = vegallomas2.getSavok().get(0);
                jarmuvek.Busz kezdoBusz = new jarmuvek.Busz("busz_1");
                kezdoBusz.setRoute(vegallomas2, vegallomas1);
                if (buszSav.elfogad(kezdoBusz)) modell.addJarmu(kezdoBusz);

                grafika.view.BuszView buszHud = new grafika.view.BuszView(kezdoBusz);
                modell.addObserver(buszHud); 
                jobbPanel.add(buszHud, "BUSZ");
            } 
            
            modell.notifyObservers(); 
        }

        // Hóesés globális bekapcsolása multiplayer és sima módoknál is
        modell.setHoesik(true);

        // 5. Vezérlő panel alulra az új logikával
        JPanel alsoPanel = new JPanel();
        JButton leptoGomb = new JButton();
        leptoGomb.setFont(new Font("Arial", Font.BOLD, 14));
        alsoPanel.add(leptoGomb);
        this.getContentPane().add(alsoPanel, BorderLayout.SOUTH);

        // Kezdőállapot beállítása
        if ("MULTIPLAYER".equals(jatekMod)) {
            jatekter.setAktivJatekos("HOKOTRO");
            cardLayout.show(jobbPanel, "HOKOTRO");
            leptoGomb.setText("Következő játékos (Busz)!");
        } else {
            leptoGomb.setText("Következő kör!");
        }

        // Gombnyomás logikája
        leptoGomb.addActionListener(e -> {
            if ("MULTIPLAYER".equals(jatekMod)) {
                if ("HOKOTRO".equals(jatekter.getAktivJatekos())) {
                    jatekter.setAktivJatekos("BUSZ");
                    cardLayout.show(jobbPanel, "BUSZ");
                    leptoGomb.setText("Következő kör indítása!");
                } else {
                    modell.leptet();
                    jatekter.setAktivJatekos("HOKOTRO");
                    cardLayout.show(jobbPanel, "HOKOTRO");
                    leptoGomb.setText("Következő játékos (Busz)!");
                }
            } else {
                modell.leptet();
            }
        });
        
        this.getContentPane().revalidate();
        this.getContentPane().repaint();
    }

    /**
     * Frissíti az ablak adatait, amikor a KörSzámláló léptet.
     */
    @Override
    public void update() {
        // PULL fázis: Amikor a modell szólt, hogy telt az idő, frissítjük a szöveget!
        if (korLabel != null) {
            korLabel.setText("KÖR: " + modell.getKor() + " / " + KorSzamlalo.MAX_KOR);
        }
        repaint();
    }

    /**
     * A játék vége: egyszerű győzelmi képernyőt jelenít meg, majd visszadob a főmenübe.
     * Akkor hívódik, amikor a játékos a {@link KorSzamlalo#MAX_KOR}. kör után próbálna léptetni.
     */
    private void jatekVege() {
        JOptionPane.showMessageDialog(
                this,
                "<html><div style='text-align:center;'>"
                        + "<h1>🏆 GYŐZELEM! 🏆</h1>"
                        + "<p>Letelt mind a(z) " + KorSzamlalo.MAX_KOR + " kör.</p>"
                        + "<p>A játéknak vége – szép munka volt!</p></div></html>",
                "Játék vége",
                JOptionPane.INFORMATION_MESSAGE);
        foMenubeVissza();
    }

    /**
     * Letakarítja a játéktér paneljeit, és visszaállítja a főmenüt.
     */
    private void foMenubeVissza() {
        this.getContentPane().removeAll();
        korLabel = null; // a régi (eltávolított) felirat referenciáját elengedjük
        initLayoutVaz();
        this.getContentPane().revalidate();
        this.getContentPane().repaint();
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