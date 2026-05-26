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
        setSize(1280, 1100);
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

        modell.reset();
        this.getContentPane().removeAll();

        // 1. Felső sáv (Körszámláló)
        JPanel felsoSav = new JPanel(new FlowLayout(FlowLayout.LEFT));
        korLabel = new JLabel("KÖR: " + modell.getKor() + " / " + KorSzamlalo.MAX_KOR);
        korLabel.setFont(new Font("Arial", Font.BOLD, 16));
        felsoSav.add(korLabel);
        this.getContentPane().add(felsoSav, BorderLayout.NORTH);

        // 2. Jobb oldali HUD (CardLayout a játékosváltáshoz)
        java.awt.CardLayout cardLayout = new java.awt.CardLayout();
        JPanel jobbPanel = new JPanel(cardLayout);
        this.getContentPane().add(jobbPanel, BorderLayout.EAST);

        // 3. Központi játéktér – JScrollPane-be ágyazva, mert az 5x5-ös rács 2+2-es
        // főutakkal nagyobb (~1040x1040 px), mint az ablakban rendelkezésre álló terület.
        JatekterPanel jatekter = new JatekterPanel(modell);
        jatekter.setPreferredSize(new java.awt.Dimension(1000, 1000));
        modell.addObserver(jatekter);
        JScrollPane jatekterScroll = new JScrollPane(jatekter);
        jatekterScroll.getVerticalScrollBar().setUnitIncrement(24);
        jatekterScroll.getHorizontalScrollBar().setUnitIncrement(24);
        this.getContentPane().add(jatekterScroll, BorderLayout.CENTER);

        Map_generator map = new Map_generator(modell);
        map.load("src/main/java/vezerles/nagy_palya.txt", jatekter, telephelyModell);

        java.util.List<funkcionalisElemek.Ut> utak = modell.getUtak();

        // Autók spawnolása
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
                // Hókotró
                jarmuvek.Hokotro kezdoHokotro = new jarmuvek.Hokotro("hokotro_1", 0, telephelyModell);
                kotrofejek.SoproFej kezdoFej = new kotrofejek.SoproFej();
                telephelyModell.tarol(kezdoFej);
                kezdoHokotro.fejcsere(kezdoFej);
                if (induloSav.elfogad(kezdoHokotro)) modell.addJarmu(kezdoHokotro);

                TelephelyView telephelyHud = new TelephelyView(telephelyModell, kezdoHokotro);
                telephelyModell.addObserver(telephelyHud);
                jobbPanel.add(telephelyHud, "HOKOTRO");

                // Busz
                funkcionalisElemek.Ut v1 = utak.get(0);
                funkcionalisElemek.Ut v2 = utak.get(utak.size() - 1);
                v1.setVegallomas(true); v2.setVegallomas(true);
                jarmuvek.Busz kezdoBusz = new jarmuvek.Busz("busz_1");
                kezdoBusz.setRoute(v2, v1);
                if (v2.getSavok().get(0).elfogad(kezdoBusz)) modell.addJarmu(kezdoBusz);

                grafika.view.BuszView buszHud = new grafika.view.BuszView(kezdoBusz);
                modell.addObserver(buszHud); 
                jobbPanel.add(buszHud, "BUSZ");
            } else if ("HOKOTRO".equals(jatekMod)) {
                jarmuvek.Hokotro kezdoHokotro = new jarmuvek.Hokotro("hokotro_1", 0, telephelyModell);
                kotrofejek.SoproFej kezdoFej = new kotrofejek.SoproFej();
                telephelyModell.tarol(kezdoFej);
                kezdoHokotro.fejcsere(kezdoFej);
                if (induloSav.elfogad(kezdoHokotro)) modell.addJarmu(kezdoHokotro);

                TelephelyView telephelyHud = new TelephelyView(telephelyModell, kezdoHokotro);
                telephelyModell.addObserver(telephelyHud);
                jobbPanel.add(telephelyHud, "HUD");
            } else if ("BUSZ".equals(jatekMod)) {
                jarmuvek.Busz kezdoBusz = new jarmuvek.Busz("busz_1");
                funkcionalisElemek.Ut v1 = utak.get(0);
                funkcionalisElemek.Ut v2 = utak.get(utak.size() - 1);
                v1.setVegallomas(true); v2.setVegallomas(true);
                kezdoBusz.setRoute(v1, v2);
                if (induloSav.elfogad(kezdoBusz)) modell.addJarmu(kezdoBusz);

                grafika.view.BuszView buszHud = new grafika.view.BuszView(kezdoBusz);
                modell.addObserver(buszHud);
                jobbPanel.add(buszHud, "HUD");
            }
            modell.notifyObservers(); 
        }

        modell.setHoesik(true);

        // 4. Alsó vezérlő panel
        JPanel alsoPanel = new JPanel();
        JButton leptoGomb = new JButton();
        leptoGomb.setFont(new Font("Arial", Font.BOLD, 14));
        
        if ("MULTIPLAYER".equals(jatekMod)) {
            jatekter.setAktivJatekos("HOKOTRO");
            cardLayout.show(jobbPanel, "HOKOTRO");
            leptoGomb.setText("Következő játékos (Busz)!");
        } else {
            leptoGomb.setText("Következő kör!");
        }

        leptoGomb.addActionListener(e -> {
            if (modell.getKor() >= KorSzamlalo.MAX_KOR) {
                jatekVege();
                return;
            }
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

        // Új gombok (raktárkezelés)
        JButton ujHokotroGomb = new JButton("🚜 Lerakás Raktárból");
        ujHokotroGomb.addActionListener(e -> {
            if (telephelyModell.getRaktaronLevoHokotrok() > 0) {
                java.util.List<funkcionalisElemek.Sav> savok = modell.getSavok();
                if (!savok.isEmpty()) {
                    jarmuvek.Hokotro ujHk = new jarmuvek.Hokotro("hokotro_" + System.currentTimeMillis(), 0, telephelyModell);
                    kotrofejek.SoproFej kezdoFej = new kotrofejek.SoproFej();
                    telephelyModell.tarol(kezdoFej);
                    ujHk.fejcsere(kezdoFej);
                    if (savok.get(0).elfogad(ujHk)) {
                        modell.addJarmu(ujHk);
                        telephelyModell.kiveszHokotrot();
                        modell.notifyObservers();
                    }
                }
            }
        });

        JButton valtasGomb = new JButton("🔄 Irányítás Váltása");
        valtasGomb.addActionListener(e -> jatekter.kovetkezoHokotro());

        jobbPanel.add(ujHokotroGomb, "HOKOTRO");
        jobbPanel.add(valtasGomb, "HOKOTRO");
        alsoPanel.add(leptoGomb);
        this.getContentPane().add(alsoPanel, BorderLayout.SOUTH);

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