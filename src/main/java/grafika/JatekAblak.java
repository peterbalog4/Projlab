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

        // ÚJ: Létrehozunk egy üres konténert a jobb oldali HUD számára
        JPanel jobbPanel = new JPanel(new BorderLayout());
        this.getContentPane().add(jobbPanel, BorderLayout.EAST);

        // 4. Játéktér panel középre
        JatekterPanel jatekter = new JatekterPanel(modell); 
        modell.addObserver(jatekter);
        this.getContentPane().add(jatekter, BorderLayout.CENTER);

        Map_generator map = new Map_generator(modell);
        map.load("src/main/java/vezerles/nagy_palya.txt", jatekter, telephelyModell);

        java.util.List<funkcionalisElemek.Ut> utak = modell.getUtak();

        if (!utak.isEmpty() && !utak.get(0).getSavok().isEmpty()) {
            funkcionalisElemek.Sav induloSav = utak.get(0).getSavok().get(0);

            if ("HOKOTRO".equals(jatekMod)) {
                jarmuvek.Hokotro kezdoHokotro = new jarmuvek.Hokotro("hokotro_1", 0, telephelyModell);
                kotrofejek.SoproFej kezdoFej = new kotrofejek.SoproFej();
                
                telephelyModell.tarol(kezdoFej);
                kezdoHokotro.fejcsere(kezdoFej);
                
                if (induloSav.elfogad(kezdoHokotro)) {
                    modell.addJarmu(kezdoHokotro);
                }

                // JAVÍTÁS: Csak hókotró esetén adjuk hozzá a TelephelyView-t
                TelephelyView telephelyHud = new TelephelyView(telephelyModell);
                telephelyModell.addObserver(telephelyHud);
                jobbPanel.add(telephelyHud, BorderLayout.CENTER);

            } else if ("BUSZ".equals(jatekMod)) {
                jarmuvek.Busz kezdoBusz = new jarmuvek.Busz("busz_1");
                funkcionalisElemek.Ut vegallomas1 = utak.get(0);
                funkcionalisElemek.Ut vegallomas2 = utak.get(utak.size() - 1);
                
                vegallomas1.setVegallomas(true);
                vegallomas2.setVegallomas(true);
                
                kezdoBusz.setRoute(vegallomas1, vegallomas2);
                
                if (induloSav.elfogad(kezdoBusz)) {
                    modell.addJarmu(kezdoBusz);
                }

                // JAVÍTÁS: Busz esetén a BuszView-t adjuk hozzá, és feliratkoztatjuk a körszámlálóra
                grafika.view.BuszView buszHud = new grafika.view.BuszView(kezdoBusz);
                modell.addObserver(buszHud); 
                jobbPanel.add(buszHud, BorderLayout.CENTER);
            }
            modell.notifyObservers(); 
        }

        // 5. Vezérlő panel alulra (innen minden marad a régiben)
        // ...

        // 5. Vezérlő panel alulra
        JPanel vezerloPanel = new JPanel();
        JButton ujAutoGomb = new JButton("🚗 Új Autó Lerakása");
        ujAutoGomb.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Lekérjük a modellből a rendelkezésre álló sávokat és utakat
                java.util.List<Sav> savok = modell.getSavok();
                java.util.List<Ut> utak = modell.getUtak();
                
                if (savok.size() > 0 && utak.size() > 0) {
                    // Kiválasztjuk az legelső sávot induló helynek, és az utolsót célnak
                    Sav induloSav = savok.get(0);
                    Ut induloUt = induloSav.getUt();
                    Ut celUt = utak.get(utak.size() - 1);
                    
                    // Létrehozzuk a logikai autót (egyedi azonosítóval, hogy ne ütközzenek a nevek)
                    jarmuvek.Auto ujAuto = new jarmuvek.Auto("auto_" + System.currentTimeMillis(), induloUt, celUt);
                    
                    // Megpróbáljuk rátuszkolni a sávra
                    if (induloSav.elfogad(ujAuto)) {
                        modell.addJarmu(ujAuto); // Bejegyezzük a központi modellbe
                        
                        // PUSH: Szólunk a grafikus felületnek, hogy "Hé, változás történt!"
                        modell.notifyObservers(); 
                    } else {
                        JOptionPane.showMessageDialog(JatekAblak.this, "A kezdő sáv tele van, nem sikerült lerakni az autót!");
                    }
                }
            }
        });
        JButton kovetkezoKorGomb = new JButton("Következő Kör");
        kovetkezoKorGomb.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Körlimit: MAX_KOR kör játszható le. Ha már letelt mind, a következő
                // kör (a MAX_KOR+1.) helyett a játéknak vége.
                if (modell.getKor() >= KorSzamlalo.MAX_KOR) {
                    jatekVege();
                    return;
                }
                System.out.println("Gombnyomás: Következő kör!");
                modell.leptet();
            }
        });
        // Havazás bekapcsolása indításkor + kapcsoló a vezérlőpulton.
        // Bekapcsolt állapotban a leptet() minden körben havat szór minden sávra,
        // így megfigyelhető a hófelhalmozódás és (5 áthaladás után) a jegesedés.
        modell.setHoesik(true);
        JToggleButton havazasGomb = new JToggleButton("❄ Havazás: BE", true);
        havazasGomb.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                boolean be = havazasGomb.isSelected();
                modell.setHoesik(be);
                havazasGomb.setText(be ? "❄ Havazás: BE" : "❄ Havazás: KI");
            }
        });

        vezerloPanel.add(ujAutoGomb);
        vezerloPanel.add(kovetkezoKorGomb);
        vezerloPanel.add(havazasGomb);
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