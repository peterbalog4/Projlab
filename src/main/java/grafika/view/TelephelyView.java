package grafika.view;

import funkcionalisElemek.Telephely;
import grafika.Observer;
import kotrofejek.KotroFej;
import funkcionalisElemek.KorSzamlalo;
import grafika.panel.JatekterPanel;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * A játékost segítő telephely adatainak (JMF egyenleg, készletek, kotrófejek)
 * grafikus megjelenítéséért felelős nézet osztály.
 */
public class TelephelyView extends JPanel implements Observer {

    // Referencia a telephely logikai modelljére a pull alapú adatlekéréshez
    private Telephely modell;
    private jarmuvek.Hokotro hokotro;
    private KorSzamlalo korszamlalo;
    private JatekterPanel jatekter;

    // Swing UI elemek a statikus információk megjelenítéséhez
    private JLabel jmfLabel;
    private JLabel biokerozinLabel;
    private JLabel soLabel;
    private JLabel zuzalekLabel;
    private JLabel kotrofejekLabel;
    private JLabel jelenlegiFejLabel;
    private JButton refillGomb;
    private JButton boltGomb;
    private JButton ujHokotroGomb;
    private JButton valtasGomb;

    private JComboBox<String> fejekCombo;
    private JButton equipGomb;

    /**
     * Konstruktor, amely inicializálja a panelt és a UI elemeket.
     * 
     * @param modell A megfigyelendő Telephely modell.
     */
    public TelephelyView(Telephely modell, jarmuvek.Hokotro hokotro, KorSzamlalo korszamlalo, JatekterPanel jatekter) {
        this.modell = modell;
        this.hokotro = hokotro;
        this.korszamlalo = korszamlalo;
        this.jatekter = jatekter;

        // A panel elrendezésének és stílusának beállítása
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createTitledBorder("TELEPHELY"));
        setPreferredSize(new Dimension(250, 0)); // Fix szélesség az oldalsó sávnak

        // Telephely fejléc-kép – a jobb oldali HUD-on jelenik meg (nem a térképen).
        // Kattintásra megnyitja a telephely részletező ablakát.
        JLabel ikonLabel = keszitIkonLabel();
        if (ikonLabel != null) {
            ikonLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            ikonLabel.setToolTipText("Kattints a telephely részleteihez");
            ikonLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            ikonLabel.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    Window parentWindow = SwingUtilities.getWindowAncestor(TelephelyView.this);
                    new TelephelyDialog(parentWindow, modell, hokotro).setVisible(true); // ITT!
                }
            });
            add(ikonLabel);
            add(Box.createRigidArea(new Dimension(0, 12)));
        }

        // UI elemek inicializálása
        jmfLabel = new JLabel("JMF Egyenleg: 0");
        biokerozinLabel = new JLabel("Biokerozin: 0 liter");
        soLabel = new JLabel("Só: 0 kg");
        zuzalekLabel = new JLabel("Zúzalék: 0 kg");
        kotrofejekLabel = new JLabel("Elérhető kotrófejek: Nincs");
        jelenlegiFejLabel = new JLabel("Aktuális fej: Nincs");

        refillGomb = new JButton("Újratöltés a telephelyről");
        refillGomb.setAlignmentX(Component.LEFT_ALIGNMENT);
        refillGomb.addActionListener(e -> ujratoltFej());

        boltGomb = new JButton("Bolt megnyitása");

        fejekCombo = new JComboBox<>();
        fejekCombo.setMaximumSize(new Dimension(200, 30)); // Ne nyúljon túl nagyra
        fejekCombo.setAlignmentX(Component.CENTER_ALIGNMENT);

        equipGomb = new JButton("Felszerel");
        equipGomb.setAlignmentX(Component.CENTER_ALIGNMENT);
        equipGomb.addActionListener(e -> felszerel());

        // Elemek hozzáadása a panelhez
        add(new JLabel("ERŐFORRÁSOK ÉS KÉSZLETEK:"));
        add(Box.createRigidArea(new Dimension(0, 10))); // Térköz
        add(jmfLabel);
        add(biokerozinLabel);
        add(soLabel);
        add(zuzalekLabel);
        add(Box.createRigidArea(new Dimension(0, 20)));
        add(jelenlegiFejLabel);
        add(Box.createRigidArea(new Dimension(0, 5)));
        add(refillGomb);
        add(Box.createRigidArea(new Dimension(0, 10)));
        add(kotrofejekLabel);
        add(Box.createRigidArea(new Dimension(0, 20)));
        add(boltGomb);

        add(kotrofejekLabel);
        add(Box.createRigidArea(new Dimension(0, 5)));
        add(fejekCombo); // Lenyíló lista
        add(Box.createRigidArea(new Dimension(0, 5)));
        add(equipGomb); // Felszerel gomb

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

        ujHokotroGomb = new JButton("🚜 Lerakás Raktárból (0 db)");
        ujHokotroGomb.setAlignmentX(Component.CENTER_ALIGNMENT);
        ujHokotroGomb.addActionListener(e -> {
            if (this.modell.getRaktaronLevoHokotrok() > 0) {
                java.util.List<funkcionalisElemek.Sav> savok = this.korszamlalo.getSavok();
                if (!savok.isEmpty()) {
                    jarmuvek.Hokotro ujHk = new jarmuvek.Hokotro("hokotro_" + System.currentTimeMillis(), 0, this.modell);
                    kotrofejek.SoproFej kezdoFej = new kotrofejek.SoproFej();
                    this.modell.tarol(kezdoFej);
                    ujHk.fejcsere(kezdoFej);
                    if (savok.get(0).elfogad(ujHk)) {
                        this.korszamlalo.addJarmu(ujHk);
                        this.modell.kiveszHokotrot();
                        this.korszamlalo.notifyObservers();
                    } else {
                        JOptionPane.showMessageDialog(this, "A kezdősáv le van zárva vagy foglalt!", "Hiba", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });

        valtasGomb = new JButton("🔄 Irányítás Váltása");
        valtasGomb.setAlignmentX(Component.CENTER_ALIGNMENT);
        valtasGomb.addActionListener(e -> {
            this.jatekter.kovetkezoHokotro();
            setHokotro(this.jatekter.getAktivHokotro());
            this.jatekter.frissitKijelolesek();
        });

        add(Box.createRigidArea(new Dimension(0, 20)));
        add(new JLabel("FLOTTA KEZELÉS:"));
        add(Box.createRigidArea(new Dimension(0, 5)));
        add(ujHokotroGomb);
        add(Box.createRigidArea(new Dimension(0, 5)));
        add(valtasGomb);

        update();
    }

    /**
     * Végrehajtja a Hókotrón a fejcserét a lenyíló listában kiválasztott elem
     * alapján.
     */
    private void felszerel() {
        String valasztott = (String) fejekCombo.getSelectedItem();
        if (valasztott == null || hokotro == null)
            return;

        // Kikeressük az objektumot a raktárból
        KotroFej kivalasztottFej = null;
        for (KotroFej f : modell.getKotrofejek()) {
            if (f.getClass().getSimpleName().equals(valasztott)) {
                kivalasztottFej = f;
                break;
            }
        }

        if (kivalasztottFej != null) {
            hokotro.fejcsere(kivalasztottFej); // Fejcsere a Hókotrón
            if (kivalasztottFej instanceof kotrofejek.SoszoroFej) {
                ((kotrofejek.SoszoroFej) kivalasztottFej).bekapcsol();
            } else if (kivalasztottFej instanceof kotrofejek.ZuzalekszoroFej) {
                ((kotrofejek.ZuzalekszoroFej) kivalasztottFej).bekapcsol();
            } else if (kivalasztottFej instanceof kotrofejek.SarkanyFej) {
                ((kotrofejek.SarkanyFej) kivalasztottFej).bekapcsol();
            }
            update(); // UI frissítése, hogy a kiválasztott fej eltűnjön a listából

            JOptionPane.showMessageDialog(this,
                    "Sikeresen felszerelted a járműre: " + valasztott,
                    "Sikeres fejcsere", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    /**
     * Betölti és arányosan átméretezi a telephely ikonját ({@code Telephely.PNG})
     * egy
     * {@link JLabel}-be, amit a HUD tetejére teszünk. Hiba esetén {@code null}-t ad
     * vissza,
     * ekkor a panel egyszerűen kép nélkül jelenik meg.
     */
    private JLabel keszitIkonLabel() {
        try {
            Image kep = ImageIO.read(new File("Telephely.PNG"));
            if (kep == null) {
                System.out.println("TelephelyView: nincs kepolvaso a Telephely.PNG-hez");
                return null;
            }
            int magassag = 120;
            float arany = kep.getWidth(null) / (float) kep.getHeight(null);
            int szelesseg = Math.round(magassag * arany);
            Image atmeretezett = kep.getScaledInstance(szelesseg, magassag, Image.SCALE_SMOOTH);
            return new JLabel(new ImageIcon(atmeretezett));
        } catch (IOException ex) {
            System.out.println("TelephelyView: nem sikerult betolteni a Telephely.PNG-t: " + ex.getMessage());
            return null;
        }
    }

    private void ujratoltFej() {
        if (hokotro == null || hokotro.getAktivFej() == null)
            return;

        KotroFej aktivFej = hokotro.getAktivFej();
        boolean sikeres = false;

        // --- SÓSZÓRÓ FEJ TÖLTÉSE ---
        if (aktivFej instanceof kotrofejek.SoszoroFej) {
            kotrofejek.SoszoroFej fej = (kotrofejek.SoszoroFej) aktivFej;
            sikeres = fej.ujratoltEllenorzott(modell);

            // --- ZÚZALÉKSZÓRÓ FEJ TÖLTÉSE ---
        } else if (aktivFej instanceof kotrofejek.ZuzalekszoroFej) {
            kotrofejek.ZuzalekszoroFej fej = (kotrofejek.ZuzalekszoroFej) aktivFej;
            sikeres = fej.ujratoltEllenorzott(modell);

            // --- SÁRKÁNYFEJ (BIOKEROZIN) TÖLTÉSE ---
        } else if (aktivFej instanceof kotrofejek.SarkanyFej) {
            kotrofejek.SarkanyFej fej = (kotrofejek.SarkanyFej) aktivFej;
            // Ha a SarkanyFej-ben nincs ujratoltEllenorzott, akkor sima ujratolt:
            // (Ha ott is megírtátok az ujratoltEllenorzott-t, akkor cseréld arra a lenti
            // sort!)
            sikeres = fej.ujratoltEllenorzott(modell);
        }

        // --- VISSZAJELZÉS A JÁTÉKOSNAK ---
        if (sikeres) {
            update(); // Frissíti a bal oldali számokat a felületen
            JOptionPane.showMessageDialog(this,
                    "Sikeresen újratöltötted a fejet a telephely készletéből!",
                    "Sikeres töltés", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this,
                    "Nem sikerült az újratöltés! Nincs elég anyag a telephelyen.",
                    "Sikertelen töltés", JOptionPane.WARNING_MESSAGE);
        }
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

        // UI elemek szövegének frissítése
        jmfLabel.setText("JMF Egyenleg: " + frissJmf);
        biokerozinLabel.setText("Biokerozin: " + frissBiokerozin + " liter");
        soLabel.setText("Só: " + frissSo + " kg");
        zuzalekLabel.setText("Zúzalék: " + frissZuzalek + " kg");
        if (hokotro != null && hokotro.getAktivFej() != null) {
            KotroFej aktivFej = hokotro.getAktivFej();
            String activeFejNev = aktivFej.getClass().getSimpleName();
            
            String anyagInfo = "";
            boolean szuksegesUjratolteni = false; // Ezzel akadályozzuk meg a pazarlást
            
            // Anyagmennyiség kiszámítása és a gomb engedélyezése, HA NINCS TELE
            if (aktivFej instanceof kotrofejek.SoszoroFej) {
                int amount = ((kotrofejek.SoszoroFej) aktivFej).getSo();
                String szin = (amount == 0) ? "red" : "blue"; // Ha nulla, piros lesz
                anyagInfo = " (<font color='" + szin + "'>" + amount + "</font>/10 só)";
                if (amount < 10) szuksegesUjratolteni = true;
                
            } else if (aktivFej instanceof kotrofejek.ZuzalekszoroFej) {
                int amount = ((kotrofejek.ZuzalekszoroFej) aktivFej).getZuzalek();
                String szin = (amount == 0) ? "red" : "blue";
                anyagInfo = " (<font color='" + szin + "'>" + amount + "</font>/10 zúzalék)";
                if (amount < 10) szuksegesUjratolteni = true;
                
            } else if (aktivFej instanceof kotrofejek.SarkanyFej) {
                int amount = ((kotrofejek.SarkanyFej) aktivFej).getBiokerozin();
                String szin = (amount == 0) ? "red" : "blue";
                anyagInfo = " (<font color='" + szin + "'>" + amount + "</font>/10 kerozin)";
                if (amount < 10) szuksegesUjratolteni = true;
            }
            
            // Megjelenítjük a fej nevét és a hozzá tartozó készletinfót
            jelenlegiFejLabel.setText("<html><b>Felszerelt fej:</b> <font color='blue'>" + activeFejNev + "</font>" + anyagInfo + "</html>");
            
            // A gomb csak akkor kattintható, ha OLYAN fej van rajta, amit LEHET tölteni, ÉS NINCS TELE
            refillGomb.setEnabled(szuksegesUjratolteni);
            
        } else {
            jelenlegiFejLabel.setText("<html><b>Felszerelt fej:</b> <font color='red'>Nincs</font></html>");
            if (refillGomb != null) {
                refillGomb.setEnabled(false);
            }
        }
        List<KotroFej> frissFejek = modell.getKotrofejek();

        // 1. A SZÖVEGES LISTA (Label) frissítése
        StringBuilder fejekSzoveg = new StringBuilder("<html><b>Elérhető kotrófejek:</b><br>");
        if (frissFejek == null || frissFejek.isEmpty()) {
            fejekSzoveg.append("<i>- Nincs raktáron</i>");
        } else {
            for (KotroFej fej : frissFejek) {
                fejekSzoveg.append("- ").append(fej.getClass().getSimpleName()).append("<br>");
            }
        }
        fejekSzoveg.append("</html>");
        kotrofejekLabel.setText(fejekSzoveg.toString());

        // 2. A LENYÍLÓ LISTA (JComboBox) és FELSZEREL GOMB frissítése
        String jelenlegi = (String) fejekCombo.getSelectedItem(); // Megjegyezzük a mostani választást
        fejekCombo.removeAllItems(); // Kiürítjük a régit

        if (frissFejek != null && !frissFejek.isEmpty()) {
            // Feltöltjük az új adatokkal
            for (KotroFej fej : frissFejek) {
                fejekCombo.addItem(fej.getClass().getSimpleName());
            }
            fejekCombo.setEnabled(true);
            equipGomb.setEnabled(true); // Van raktáron fej, bekapcsoljuk a gombot

            // Ha a korábban kiválasztott fej még mindig megvan, visszarakjuk rá a fókuszt
            if (jelenlegi != null) {
                fejekCombo.setSelectedItem(jelenlegi);
            }
        } else {
            // Ha nincs fej a raktárban, letiltjuk a gombot és a listát
            fejekCombo.setEnabled(false);
            equipGomb.setEnabled(false);
        }
        
        int raktaronLevo = modell.getRaktaronLevoHokotrok();
        ujHokotroGomb.setText("🚜 Lerakás Raktárból (" + raktaronLevo + " db)");
        ujHokotroGomb.setEnabled(raktaronLevo > 0);

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

    public void setHokotro(jarmuvek.Hokotro h) {
        this.hokotro = h;
        update(); // Azonnal frissíti a UI-t
    }
}