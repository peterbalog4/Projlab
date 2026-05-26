package grafika.view;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import funkcionalisElemek.Sav;
import grafika.Observer;
import segedOsztalyok.Irany;

/**
 * A játéktér egy sávjának megjelenítéséért felelős nézet.
 */
public class SavView implements Observer {

    // Globális regiszter a sávok nézeteinek gyors megtalálásához
    public static java.util.Map<Sav, SavView> nezetRegiszter = new java.util.HashMap<>();

    // ── Megosztott textúrák (egyszer töltődnek be az összes sávhoz) ──────────
    private static final Image UT_IMG  = betoltKep("ut.PNG");
    private static final Image HO_IMG  = betoltKep("ho.PNG");
    private static final Image JEG_IMG = betoltKep("jeg.png");
    private static final Image VEGALLOMAS_IMG = betoltKep("vegallomas.png");
    // ÚJ: Lezárás ikonjának betöltése
    private static final Image LEZARVA_IMG = betoltKep("lezarva.png");

    private static final Image soKep = betoltKep("so.png");
    private static final Image zuzalekKep = betoltKep("zuzalek.png");

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
    private boolean lezarva;      // ÚJ: Állapot a lezárás nyilvántartására
    private final int MERET = 60; // A sáv vastagsága (a haladási irányra merőleges)
    private final Irany irany;

    private boolean sozott; // Hogy ezt is egyszerűen elérjük
    private java.util.List<String> retegSorrend = new java.util.ArrayList<>();
    private int elozoHo = 0;
    private boolean elozoJeg = false;
    private boolean elozoZuzalek = false;
    private boolean elozoSo = false;
    private int kijeloles = 0; // 0: nincs, 1: kiválasztott (zöld), 2: érvénytelen (piros)

    public SavView(Sav modell, int xKord, int yKord, Irany irany) {
        this.modell = modell;
        this.xKord = xKord;
        this.yKord = yKord;
        this.irany = irany;
        modell.addObserver(this);
        
        // Regisztráljuk a nézetet, hogy a JarmuView megtalálja
        nezetRegiszter.put(modell, this);
        
        update(); // Kezdeti állapot lekérése a megjelenítéshez
    }

    @Override
    public void update() {
        homennyiseg = modell.getHo();
        jeges = modell.isJeg();
        zuzalekos = modell.isZuzalek();
        lezarva = modell.isLezarva(); 
        sozott = modell.isSozott(); // Kérlek ellenőrizd, hogy a Sav.java-ban tényleg ez a getter neve!

        // ─── IDŐRENDI SORREND FRISSÍTÉSE ───
        
        // Jég figyelése
        if (jeges && !elozoJeg) {
            retegSorrend.remove("jeg");
            retegSorrend.add("jeg"); // Legfelülre tesszük
        } else if (!jeges) {
            retegSorrend.remove("jeg");
        }
        elozoJeg = jeges;

        // Hó figyelése (ha nőtt a hó, akkor új hó esett, tehát felülre kerül)
        if (homennyiseg > 0 && homennyiseg > elozoHo) {
            retegSorrend.remove("ho");
            retegSorrend.add("ho");
        } else if (homennyiseg == 0) {
            retegSorrend.remove("ho");
        }
        elozoHo = homennyiseg;

        // Zúzalék figyelése
        if (zuzalekos && !elozoZuzalek) {
            retegSorrend.remove("zuzalek");
            retegSorrend.add("zuzalek");
        } else if (!zuzalekos) {
            retegSorrend.remove("zuzalek");
        }
        elozoZuzalek = zuzalekos;

        // Só figyelése
        if (sozott && !elozoSo) {
            retegSorrend.remove("so");
            retegSorrend.add("so");
        } else if (!sozott) {
            retegSorrend.remove("so");
        }
        elozoSo = sozott;
    }

    public void setKijeloles(int kijelolesSzint) {
        this.kijeloles = kijelolesSzint;
    }

    public boolean contains(int mouseX, int mouseY) {
        boolean fuggoleges = (irany == Irany.FEL || irany == Irany.LE);
        int h = fuggoleges ? modell.getHossz() : MERET;
        int w = fuggoleges ? MERET : modell.getHossz();
        int padding = 60; // Hogy a kereszteződésben is rákattinthass

        if (fuggoleges) {
            return mouseX >= xKord && mouseX <= xKord + w &&
                   mouseY >= (yKord - padding) && mouseY <= (yKord + h + padding);
        } else {
            return mouseX >= (xKord - padding) && mouseX <= (xKord + w + padding) &&
                   mouseY >= yKord && mouseY <= yKord + h;
        }
    }

    

    public Sav getModell() { return modell; }

    public int getXKord() { return xKord; }
    public int getYKord() { return yKord; }
    
    public Irany getIrany() { return irany; }

    public void draw(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        boolean fuggoleges = (irany == Irany.FEL || irany == Irany.LE);
        int hossz = modell.getHossz();
        int w = fuggoleges ? MERET : hossz;   
        int h = fuggoleges ? hossz : MERET;   

        rajzolUtAlap(g2d, w, h, fuggoleges);
        rajzolSavjelzes(g2d, w, h, fuggoleges);

        for (String reteg : retegSorrend) {
            switch (reteg) {
                case "jeg":
                    rajzolReteg(g2d, JEG_IMG, new Color(0, 190, 255, 90), w, h, fuggoleges);
                    break;
                case "ho":
                    int alpha = Math.min(210, 50 + homennyiseg * 35); 
                    rajzolReteg(g2d, HO_IMG, new Color(255, 255, 255, alpha), w, h, fuggoleges);
                    break;
                case "zuzalek":
                    rajzolReteg(g2d, zuzalekKep, new Color(110, 110, 110, 90), w, h, fuggoleges);
                    break;
                case "so":
                    rajzolReteg(g2d, soKep, new Color(255, 255, 255, 60), w, h, fuggoleges);
                    break;
            }
        }

        // 5. Ha le van zárva, a sáv két végére rárajzoljuk az ikont (MERET x MERET formában)
        if (lezarva && LEZARVA_IMG != null) {
            if (fuggoleges) {
                // Felső vég
                g2d.drawImage(LEZARVA_IMG, xKord, yKord, MERET, MERET, null);
                // Alsó vég
                g2d.drawImage(LEZARVA_IMG, xKord, yKord + h - MERET, MERET, MERET, null);
            } else {
                // Bal vég
                g2d.drawImage(LEZARVA_IMG, xKord, yKord, MERET, MERET, null);
                // Jobb vég
                g2d.drawImage(LEZARVA_IMG, xKord + w - MERET, yKord, MERET, MERET, null);
            }
        }
        // Kijelölő keret rajzolása a konkrét sáv köré
        if (kijeloles != 0) {
            Graphics2D r = (Graphics2D) g2d.create();
            r.setStroke(new BasicStroke(4f));
            if (kijeloles == 1) {
                r.setColor(new Color(0, 255, 0, 180)); // Zöld keret
            } else {
                r.setColor(new Color(255, 0, 0, 180)); // Piros villanás (hibás irány)
            }
            r.drawRect(xKord, yKord, w, h);
            r.dispose();
        }
        g2d.dispose();
    }

    private void rajzolUtAlap(Graphics2D g2d, int w, int h, boolean fuggoleges) {
        boolean vegallomasE = (modell.getUt() != null && modell.getUt().isVegallomas());
        Image alapKep = vegallomasE ? VEGALLOMAS_IMG : UT_IMG;

        if (alapKep == null) {
            g2d.setColor(vegallomasE ? Color.MAGENTA : Color.DARK_GRAY);
            g2d.fillRect(xKord, yKord, w, h);
            return;
        }

        if (vegallomasE) {
            // Végállomás: Forgatás nélkül, négyzet alakban (MERET x MERET) ismételjük
            if (fuggoleges) {
                for (int y = yKord; y < yKord + h; y += MERET) {
                    g2d.drawImage(alapKep, xKord, y, MERET, MERET, null);
                }
            } else {
                for (int x = xKord; x < xKord + w; x += MERET) {
                    g2d.drawImage(alapKep, x, yKord, MERET, MERET, null);
                }
            }
        } else {
            // Sima út: Eredeti nyújtós és forgatós logika
            if (!fuggoleges) {
                g2d.drawImage(alapKep, xKord, yKord, w, h, null);
            } else {
                Graphics2D r = (Graphics2D) g2d.create();
                r.translate(xKord, yKord);
                r.rotate(Math.toRadians(90));
                r.drawImage(alapKep, 0, -w, h, w, null);
                r.dispose();
            }
        }
    }

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