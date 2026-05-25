package grafika.view;

import funkcionalisElemek.Telephely;
import grafika.Observer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * A játékbeli Bolt (Shop) grafikus felülete (JDialog).
 * A specifikáció alapján gombokra kattintva lehet vásárolni[cite: 92].
 */
public class BoltDialog extends JDialog implements Observer {

    private Telephely modell;
    private JLabel egyenlegLabel;

    public BoltDialog(Window parent, Telephely modell) {
        super(parent, "BOLT", ModalityType.APPLICATION_MODAL); // Modális ablak [cite: 66]
        this.modell = modell;

        // Ha a Telephely megvalósítja az Observable-t, feliratkozunk rá [cite: 102]
        // modell.addObserver(this); 

        initUI();
        update(); // Kezdeti egyenleg betöltése [cite: 64]
    }

    private void initUI() {
        setTitle("BOLT");
        setSize(550, 450);
        setLocationRelativeTo(getOwner()); // Középre helyezés
        setLayout(new BorderLayout(10, 10));

        // --- ÉSZAKI PANEL: Egyenleg megjelenítése ---
        JPanel northPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        northPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 10));
        egyenlegLabel = new JLabel("Elérhető egyenleg: 0 JMF");
        egyenlegLabel.setFont(new Font("Arial", Font.BOLD, 14));
        northPanel.add(egyenlegLabel);
        add(northPanel, BorderLayout.NORTH);

        // --- KÖZÉPSŐ PANEL: Kétoszlopos elrendezés (Kotrófejek vs Anyagok+Járművek) ---
        JPanel centerPanel = new JPanel(new GridLayout(1, 2, 15, 15));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

        // Bal oszlop: Kotrófejek [cite: 67]
        JPanel balOszlop = new JPanel();
        balOszlop.setLayout(new BoxLayout(balOszlop, BoxLayout.Y_AXIS));
        balOszlop.setBorder(BorderFactory.createTitledBorder("KOTRÓFEJEK"));

        centerPanel.add(balOszlop);

        // Jobb oszlop: Anyagok és Járművek [cite: 68, 88]
        JPanel jobbOszlop = new JPanel();
        jobbOszlop.setLayout(new BoxLayout(jobbOszlop, BoxLayout.Y_AXIS));
        
        JPanel anyagokPanel = new JPanel();
        anyagokPanel.setLayout(new BoxLayout(anyagokPanel, BoxLayout.Y_AXIS));
        anyagokPanel.setBorder(BorderFactory.createTitledBorder("ANYAGOK / UTÁNPÓTLÁS"));
        
        JPanel jarmuvekPanel = new JPanel();
        jarmuvekPanel.setLayout(new BoxLayout(jarmuvekPanel, BoxLayout.Y_AXIS));
        jarmuvekPanel.setBorder(BorderFactory.createTitledBorder("JÁRMŰVEK"));
        
        jobbOszlop.add(anyagokPanel);
        jobbOszlop.add(Box.createRigidArea(new Dimension(0, 10)));
        jobbOszlop.add(jarmuvekPanel);
        
        centerPanel.add(jobbOszlop);
        add(centerPanel, BorderLayout.CENTER);

        // --- GOMBOK ÉS TERMÉKEK LÉTREHOZÁSA (Specifikáció szerinti árakkal) --- [cite: 69-90]
        
        // 1. Kotrófejek hozzáadása [cite: 69-87]
        hozzaadBoltElem(balOszlop, "[1] Söprő fej", 120000, "SOPRO");
        hozzaadBoltElem(balOszlop, "[2] Hányó fej", 200000, "HANYO");
        hozzaadBoltElem(balOszlop, "[3] Jégtörő fej", 250000, "JEGTORO");
        hozzaadBoltElem(balOszlop, "[4] Sárkány fej", 600000, "SARKANY");
        hozzaadBoltElem(balOszlop, "[5] Sószóró fej", 400000, "SOSZORO");
        hozzaadBoltElem(balOszlop, "[6] Zúzalékszóró fej", 350000, "ZUZALEKSZORO");

        // 2. Anyagok hozzáadása [cite: 71-82]
        hozzaadBoltElem(anyagokPanel, "[7] Biokerozin (töltet)", 5000, "BIOKEROZIN");
        hozzaadBoltElem(anyagokPanel, "[8] Só (töltet)", 8000, "SO");
        hozzaadBoltElem(anyagokPanel, "[9] Zúzalék (töltet)", 5000, "ZUZALEK");

        // 3. Járművek hozzáadása [cite: 89, 90]
        hozzaadBoltElem(jarmuvekPanel, "[H] Új Hókotró", 2000000, "HOKOTRO");

        // --- DÉLI PANEL: Instrukció és Bezárás gomb --- [cite: 92, 93]
        JPanel southPanel = new JPanel();
        southPanel.setLayout(new BoxLayout(southPanel, BoxLayout.Y_AXIS));
        southPanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));

        JLabel infoLabel = new JLabel("Vásárláshoz kattints rá!", SwingConstants.CENTER); 
        infoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        infoLabel.setFont(new Font("Arial", Font.ITALIC, 12));

        JButton bezarGomb = new JButton("BEZÁRÁS"); 
        bezarGomb.setAlignmentX(Component.CENTER_ALIGNMENT);
        bezarGomb.addActionListener(e -> dispose()); // Ablak bezárása [cite: 93]

        southPanel.add(infoLabel);
        southPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        southPanel.add(bezarGomb);
        add(southPanel, BorderLayout.SOUTH);
    }

    /**
     * Segédfüggvény egy boltban vásárolható tétel gombjának legenerálásához.
     */
    private void hozzaadBoltElem(JPanel panel, String nev, int ar, String tipusId) {
        JButton gomb = new JButton(String.format("%s - %,d JMF", nev, ar));
        gomb.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        gomb.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        gomb.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                vasarlasKezeles(nev, ar, tipusId);
            }
        });

        panel.add(gomb);
        panel.add(Box.createRigidArea(new Dimension(0, 5)));
    }

    /**
     * Tranzakciókezelés. Ellenőrzi a fedezetet és meghívja a modell módosító metódusait.
     */
    private void vasarlasKezeles(String nev, int ar, String tipusId) {
        
        // 1. Átfordítjuk a Bolt belső azonosítóit a Telephely által várt szavakra
        String telephelyItem = "";
        switch(tipusId) {
            case "SOPRO": telephelyItem = "soprofej"; break;
            case "HANYO": telephelyItem = "hanyofej"; break;
            case "JEGTORO": telephelyItem = "jegtorofej"; break;
            case "SARKANY": telephelyItem = "sarkanyfej"; break;
            case "SOSZORO": telephelyItem = "soszorofej"; break;
            case "ZUZALEKSZORO": telephelyItem = "zuzalekszorofej"; break;
            case "BIOKEROZIN": telephelyItem = "biokerozin"; break;
            case "SO": telephelyItem = "so"; break;
            case "ZUZALEK": telephelyItem = "zuzalek"; break;
            case "HOKOTRO": telephelyItem = "hokotro"; break;
        }

        // 2. TÉNYLEGES VÁSÁRLÁS MEGHÍVÁSA A MODELLEN
        boolean sikeres = modell.vasarol(telephelyItem);

        // 3. Vizuális visszajelzés a felhasználónak
        if (sikeres) {
            System.out.println("BOLT: Sikeres vásárlás! (" + telephelyItem + ")");
            JOptionPane.showMessageDialog(this, 
                "Sikeresen megvásároltad: " + nev, 
                "Sikeres vásárlás", 
                JOptionPane.INFORMATION_MESSAGE);
        } else {
            System.out.println("BOLT: Sikertelen vásárlás (nincs elég JMF)!");
            JOptionPane.showMessageDialog(this, 
                "Nincs elég JMF egyenleged a vásárláshoz!", 
                "Sikertelen vásárlás", 
                JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Az Observer interfész megvalósítása (PULL fázis). [cite: 111, 112]
     * Ha változik a telephely egyenlege, a bolt azonnal újrarajzolja a címkét[cite: 109, 113].
     */
    @Override
    public void update() {
        int frissJmf = modell.getJMF(); // PULL 
        egyenlegLabel.setText(String.format("Elérhető egyenleg: %,d JMF", frissJmf)); 
        repaint();
    }
}