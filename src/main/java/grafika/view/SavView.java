package grafika.view;

import funkcionalisElemek.Sav;
import grafika.Observer;
import segedOsztalyok.Irany;

import javax.imageio.ImageIO;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.io.File;
import java.io.IOException;

/**
 * A játéktér egy sávjának megjelenítéséért felelős nézet.
 *
 * A sáv alapját a megosztott aszfalt-textúra (ut.PNG) adja, erre kerülnek
 * rá az állapotjelző rétegek: hóréteg esetén a ho.PNG, jegesedés esetén a
 * jeg.png ikonok, egy féláttetsző színréteggel kombinálva. Így a játékos
 * vizuálisan megkülönbözteti a havas, jeges és zúzalékos sávokat (spec 11.1.2).
 */
public class SavView implements Observer {

    // ── Megosztott textúrák (egyszer töltődnek be az összes sávhoz) ──────────
    private static final Image UT_IMG  = betoltKep("ut.PNG");
    private static final Image HO_IMG  = betoltKep("ho.PNG");
    private static final Image JEG_IMG = betoltKep("jeg.png");

    /** Egy textúrát tölt be a projekt gyökeréből; hiba esetén null-t ad vissza. */
    private static Image betoltKep(String nev) {
        try {
            Image img = ImageIO.read(new File(nev));
            if (img == null) {
                System.out.println("SavView: nincs kepolvaso ehhez: " + nev);
            }
            return img;
        } catch (IOException e) {
            System.out.println("SavView: nem sikerult betolteni: " + nev + " (" + e.getMessage() + ")");
            return null;
        }
    }

    private Sav modell;           // Referencia a megfigyelt logikai sávra
    private final int xKord;      // Statikus X koordináta a képernyőn
    private final int yKord;      // Statikus Y koordináta a képernyőn
    private int homennyiseg;
    private boolean jeges;
    private boolean zuzalekos;
    private final int MERET = 60; // A sáv vastagsága (a haladási irányra merőleges)
    private final Irany irany;

    public SavView(Sav modell, int xKord, int yKord, Irany irany) {
        this.modell = modell;
        this.xKord = xKord;
        this.yKord = yKord;
        this.irany = irany;
        modell.addObserver(this);
        update(); // Kezdeti állapot lekérése a megjelenítéshez
    }

    /**
     * Állapotváltozás (hóesés, jégképződés, takarítás) esetén aktiválódik.
     * PULL fázis: a friss állapotot a modellből kérdezzük le.
     */
    @Override
    public void update() {
        homennyiseg = modell.getHo();
        jeges = modell.isJeg();
        zuzalekos = modell.isZuzalek();
    }

    public Sav getModell() { return modell; }

    /** A sáv képernyő-koordinátái (a JarmuView pozicionálásához hasznos). */
    public int getXKord() { return xKord; }
    public int getYKord() { return yKord; }

    /**
     * Kirajzolja a sávot: előbb az aszfalt-alapot a ut.PNG-ből, majd az
     * aktuális állapotnak megfelelő réteget (jég / hó / zúzalék).
     */
    public void draw(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        boolean fuggoleges = (irany == Irany.FEL || irany == Irany.LE);
        int hossz = modell.getHossz();
        int w = fuggoleges ? MERET : hossz;   // a sáv téglalapjának szélessége
        int h = fuggoleges ? hossz : MERET;   // és magassága

        // 1. Aszfalt-alap a ut.PNG-ből (függőleges sávnál 90°-kal elforgatva)
        rajzolUtAlap(g2d, w, h, fuggoleges);

        // 2. Szaggatott felezővonal a sáv közepén (a hóréteg majd elfedi)
        rajzolSavjelzes(g2d, w, h, fuggoleges);

        // 3. Állapotjelző réteg a fontossági sorrend szerint
        if (jeges) {
            rajzolReteg(g2d, JEG_IMG, new Color(0, 190, 255, 90), w, h, fuggoleges);
        } else if (homennyiseg > 0) {
            int alpha = Math.min(210, 50 + homennyiseg * 35); // mélyebb hó = fehérebb
            rajzolReteg(g2d, HO_IMG, new Color(255, 255, 255, alpha), w, h, fuggoleges);
        } else if (zuzalekos) {
            // Zúzalékhoz nincs külön kép: érdes, szürkés féláttetsző réteg
            g2d.setColor(new Color(110, 110, 110, 120));
            g2d.fillRect(xKord, yKord, w, h);
        }

        g2d.dispose();
    }

    /** Az aszfalt-textúra kirajzolása a sáv téglalapjába (függőlegesnél elforgatva). */
    private void rajzolUtAlap(Graphics2D g2d, int w, int h, boolean fuggoleges) {
        if (UT_IMG == null) {
            g2d.setColor(Color.DARK_GRAY);
            g2d.fillRect(xKord, yKord, w, h);
            return;
        }
        if (!fuggoleges) {
            g2d.drawImage(UT_IMG, xKord, yKord, w, h, null);
        } else {
            // A vízszintes textúrát 90°-kal elforgatva illesztjük a függőleges sávba,
            // hogy az aszfalt mintázata a haladási irány mentén fusson.
            Graphics2D r = (Graphics2D) g2d.create();
            r.translate(xKord, yKord);
            r.rotate(Math.toRadians(90));
            r.drawImage(UT_IMG, 0, -w, h, w, null);
            r.dispose();
        }
    }

    /** Szaggatott felezővonal a sáv közepén, a haladási irány mentén (úttest-jelölés). */
    private void rajzolSavjelzes(Graphics2D g2d, int w, int h, boolean fuggoleges) {
        g2d.setColor(KanyarView.LANE_MARK);
        g2d.setStroke(new BasicStroke(2f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND,
                10f, new float[]{14f, 12f}, 0f));
        if (fuggoleges) {
            int x = xKord + w / 2;
            g2d.drawLine(x, yKord + 4, x, yKord + h - 4);
        } else {
            int y = yKord + h / 2;
            g2d.drawLine(xKord + 4, y, xKord + w - 4, y);
        }
    }

    /** Féláttetsző színréteg + ismétlődő állapotikon a sáv hossza mentén. */
    private void rajzolReteg(Graphics2D g2d, Image ikon, Color szin, int w, int h, boolean fuggoleges) {
        g2d.setColor(szin);
        g2d.fillRect(xKord, yKord, w, h);

        if (ikon == null) return;
        final int ICON = 34;
        final int STEP = 60;
        if (fuggoleges) {
            int ix = xKord + (w - ICON) / 2;
            for (int iy = yKord + (STEP - ICON) / 2; iy + ICON <= yKord + h; iy += STEP) {
                g2d.drawImage(ikon, ix, iy, ICON, ICON, null);
            }
        } else {
            int iy = yKord + (h - ICON) / 2;
            for (int ix = xKord + (STEP - ICON) / 2; ix + ICON <= xKord + w; ix += STEP) {
                g2d.drawImage(ikon, ix, iy, ICON, ICON, null);
            }
        }
    }
}
