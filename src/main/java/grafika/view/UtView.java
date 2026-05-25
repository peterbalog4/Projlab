package grafika.view;
 
import funkcionalisElemek.Ut;
import funkcionalisElemek.SzakaszTipus;
import grafika.Observer;
import segedOsztalyok.Irany;
import funkcionalisElemek.Sav;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.ArrayList;
import java.util.List;
 
/**
 * Az egész út globális megjelenítését és a sávok összefogását reprezentáló nézet.
 *
 * Bugfix: az eredeti kód minden sávot kétszer adott a listához (egyszer a for-ciklus
 * elején, egyszer a switch-en belül). Javítva: csak a switch-en belül adjuk hozzá,
 * a helyes koordinátákkal.
 *
 * Koordináta-logika:
 *   - FEL / LE  (függőleges út): a sávok vízszintesen sorakoznak → currentX nő
 *   - JOBBRA / BALRA (vízszintes út): a sávok függőlegesen sorakoznak → currentY nő
 *
 * Az út "hossza" a modell szerint a haladási irány mentén értendő (getHossz()).
 * A sáv szélessége (SAV_SZELLESEG = 60 px) arra merőleges.
 */
public class UtView implements Observer {
 
    private Ut modell;
    private List<SavView> savNezetek;
    private Irany utIrany;
 
    public final int SAV_SZELLESEG = 60; // px – egy sáv szélessége

    private int startX;
    private int startY;
    private int kijeloles = 0;

    public void setKijeloles(int kijelolesSzint) {
        this.kijeloles = kijelolesSzint;
    }
 
    /**
     * @param modell   A megfigyelt logikai Ut objektum.
     * @param startX   Az út bal felső sarkának X koordinátája a képernyőn.
     * @param startY   Az út bal felső sarkának Y koordinátája a képernyőn.
     * @param utIrany  Az út haladási iránya a térképen.
     */
    public UtView(Ut modell, int startX, int startY, Irany utIrany) {
        this.modell = modell;
        this.savNezetek = new ArrayList<>();
        this.utIrany = utIrany;
        this.startX = startX;
        this.startY = startY;
        modell.addObserver(this);
 
        buildSavNezetek(startX, startY);
    }
 
    /**
     * Felépíti a sávnézetek listáját a helyes koordinátákkal.
     * Egy irányban (a haladási iránnyal párhuzamosan) az út hossza adott.
     * Arra merőlegesen a sávok egymás mellé kerülnek, SAV_SZELLESEG lépésközzel.
     */
    private void buildSavNezetek(int startX, int startY) {
        savNezetek.clear();
 
        List<Sav> savok = modell.getSavok();
        int currentX = startX;
        int currentY = startY;
 
        for (Sav sav : savok) {
            switch (utIrany) {
                case FEL:
                case LE:
                    // Függőleges út, lefelé haladó. Az út teteje startY, alja startY+hossz.
                    savNezetek.add(new SavView(sav, currentX, currentY, utIrany));
                    currentX += SAV_SZELLESEG;
                    break;
 
                case JOBBRA:
                case BALRA:
                    // Vízszintes út, balra haladó. Az út jobb széle startX-nál van,
                    // de a SavView-nak a bal felső sarokot adjuk át (startX).
                    savNezetek.add(new SavView(sav, currentX, currentY, utIrany));
                    currentY += SAV_SZELLESEG;
                    break;
            }
        }
    }
 
    /**
     * Frissíti a nézetet, ha az út állapota globálisan megváltozik.
     * (Tipikusan csak a sávok változnak, így ez általában üres marad.)
     */
    @Override
    public void update() {
        // Ha egész utat érintő változás lenne, itt kezeljük.
    }
 
    /**
     * Az út grafikus elemeinek kirajzolását hajtja végre.
     * A sávok kirajzolása után, ha a szakasz híd vagy alagút, ráfesti a megkülönböztető
     * jelölést (korlátok / alagútszáj) a teljes útszakaszra.
     */
/**
     * Az út grafikus elemeinek kirajzolását hajtja végre.
     */
    public void draw(Graphics g) {
        // 1. Sávok kirajzolása
        for (SavView sv : savNezetek) {
            sv.draw(g);
        }

        SzakaszTipus tipus = modell.getTipus();
        if (tipus == SzakaszTipus.HID) {
            rajzolHid(g);
        } else if (tipus == SzakaszTipus.ALAGUT) {
            rajzolAlagut(g);
        }

        if (modell.isAktivCel()) {
            java.awt.Graphics2D g2 = (java.awt.Graphics2D) g;
            java.awt.Stroke regiStroke = g2.getStroke();
            g2.setStroke(new java.awt.BasicStroke(8f)); // Jó vastag legyen
            g2.setColor(new java.awt.Color(255, 200, 0, 220)); // Feltűnő sárga szín

            int utHossz = modell.getHossz();
            int utSzelesseg = getTeljesszelesseg();

            if (utIrany == Irany.FEL || utIrany == Irany.LE) {
                g2.drawRect(startX, startY, utSzelesseg, utHossz);
            } else {
                g2.drawRect(startX, startY, utHossz, utSzelesseg);
            }
            g2.setStroke(regiStroke); 
        }

        // 2. Kijelölő keret kirajzolása (ha van)
        if (kijeloles != 0) {
            java.awt.Graphics2D g2 = (java.awt.Graphics2D) g;
            java.awt.Stroke regiStroke = g2.getStroke();
            g2.setStroke(new java.awt.BasicStroke(6f)); // 6 pixel vastag keret

            if (kijeloles == 1) {
                // Zöld, félig átlátszó keret
                g2.setColor(new java.awt.Color(0, 255, 0, 180)); 
            } else {
                // Szürke, félig átlátszó keret
                g2.setColor(new java.awt.Color(128, 128, 128, 180)); 
            }

            int utHossz = modell.getHossz();
            int utSzelesseg = getTeljesszelesseg();

            // A keret orientációja az út iránya alapján
            if (utIrany == Irany.FEL || utIrany == Irany.LE) {
                g2.drawRect(startX, startY, utSzelesseg, utHossz);
            } else {
                g2.drawRect(startX, startY, utHossz, utSzelesseg);
            }

            // Visszaállítjuk az ecsetet az eredetire
            g2.setStroke(regiStroke); 
        }
    }

    /** A teljes útszakasz képernyő-téglalapja {x, y, szélesség, magasság}. */
    private int[] szakaszTeglalap() {
        boolean fuggoleges = (utIrany == Irany.FEL || utIrany == Irany.LE);
        int hossz = modell.getHossz();
        int szel  = getTeljesszelesseg();
        int rw = fuggoleges ? szel : hossz;
        int rh = fuggoleges ? hossz : szel;
        return new int[]{ startX, startY, rw, rh };
    }

    /**
     * Híd: enyhén világosabb pályaszint, a két hosszanti külső élen vastag korlát
     * (kívül vékony árnyékkal), és szabályos haránt dilatációs vonalak – együtt egy
     * felülnézeti hídpályát adnak ki.
     */
    private void rajzolHid(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int[] r = szakaszTeglalap();
        int rx = r[0], ry = r[1], rw = r[2], rh = r[3];
        boolean fuggoleges = (utIrany == Irany.FEL || utIrany == Irany.LE);

        // Pályaszint enyhe kiemelése
        g2.setColor(new Color(150, 152, 158, 55));
        g2.fillRect(rx, ry, rw, rh);

        final Color KORLAT       = new Color(228, 228, 224);
        final Color KORLAT_ARNY  = new Color(35, 40, 45, 130);
        final Color DILATACIO    = new Color(30, 34, 38, 110);

        if (fuggoleges) {
            int xL = rx + 3, xR = rx + rw - 3;
            // dilatációs (haránt) vonalak
            g2.setStroke(new BasicStroke(2f));
            g2.setColor(DILATACIO);
            for (int y = ry + 16; y < ry + rh - 8; y += 26) {
                g2.drawLine(rx + 4, y, rx + rw - 4, y);
            }
            // korlát-árnyék kívül, majd a korlát
            g2.setStroke(new BasicStroke(5f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            g2.setColor(KORLAT_ARNY);
            g2.drawLine(xL - 2, ry, xL - 2, ry + rh);
            g2.drawLine(xR + 2, ry, xR + 2, ry + rh);
            g2.setColor(KORLAT);
            g2.drawLine(xL, ry, xL, ry + rh);
            g2.drawLine(xR, ry, xR, ry + rh);
        } else {
            int yT = ry + 3, yB = ry + rh - 3;
            g2.setStroke(new BasicStroke(2f));
            g2.setColor(DILATACIO);
            for (int x = rx + 16; x < rx + rw - 8; x += 26) {
                g2.drawLine(x, ry + 4, x, ry + rh - 4);
            }
            g2.setStroke(new BasicStroke(5f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            g2.setColor(KORLAT_ARNY);
            g2.drawLine(rx, yT - 2, rx + rw, yT - 2);
            g2.drawLine(rx, yB + 2, rx + rw, yB + 2);
            g2.setColor(KORLAT);
            g2.drawLine(rx, yT, rx + rw, yT);
            g2.drawLine(rx, yB, rx + rw, yB);
        }
        g2.dispose();
    }

    /**
     * Alagút: a szakaszt sötét, féláttetsző réteg fedi (a járművek "alá" mennek), a két
     * végén pedig világos betonszáj (alagútportál) sötét belső peremmel jelzi a be-/kijáratot.
     */
    private void rajzolAlagut(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int[] r = szakaszTeglalap();
        int rx = r[0], ry = r[1], rw = r[2], rh = r[3];
        boolean fuggoleges = (utIrany == Irany.FEL || utIrany == Irany.LE);

        // Sötét fedés a teljes szakaszon
        g2.setColor(new Color(12, 15, 20, 135));
        g2.fillRect(rx, ry, rw, rh);

        final Color PORTAL     = new Color(205, 200, 190); // beton
        final Color PORTAL_BEL = new Color(0, 0, 0, 170);  // belső sötét perem
        final int t = 9;   // portálfal vastagsága
        final int b = 4;   // belső árnyékperem vastagsága

        if (fuggoleges) {
            // A rövid végek fent (ry) és lent (ry+rh); a száj a szélességet (rw) éri át.
            g2.setColor(PORTAL);
            g2.fillRect(rx, ry, rw, t);
            g2.fillRect(rx, ry + rh - t, rw, t);
            g2.setColor(PORTAL_BEL);
            g2.fillRect(rx, ry + t, rw, b);
            g2.fillRect(rx, ry + rh - t - b, rw, b);
        } else {
            g2.setColor(PORTAL);
            g2.fillRect(rx, ry, t, rh);
            g2.fillRect(rx + rw - t, ry, t, rh);
            g2.setColor(PORTAL_BEL);
            g2.fillRect(rx + t, ry, b, rh);
            g2.fillRect(rx + rw - t - b, ry, b, rh);
        }
        g2.dispose();
    }
 
    // --- Getterek (pl. JatekterPanel számára) ---
 
    public Irany getUtIrany() { return utIrany; }
 
    /**
     * Visszaadja az út teljes szélességét pixelben
     * (a haladási iránnyal merőleges dimenzióban).
     */
    public int getTeljesszelesseg() {
        return savNezetek.size() * SAV_SZELLESEG;
    }

    /**
     * Visszaadja az UtView mögött álló logikai modellt.
     * Ezt hívja meg a JatekterPanel az egérkattintáskor.
     */
    public Ut getModell() {
        return this.modell;
    }

    public boolean contains(int mouseX, int mouseY) {
        int utHossz = modell.getHossz(); 
        int utSzelesseg = getTeljesszelesseg(); 
        
        // KIBŐVÍTÉS: A kereszteződések vizuális négyzetei logikailag nincsenek benne az út hosszában.
        // Ezzel a "padding"-gel megnyújtjuk a kattintási zónát (egy sávnyival) a kereszteződésekre is!
        int padding = 60; 

        if (utIrany == Irany.FEL || utIrany == Irany.LE) {
            return mouseX >= (this.startX - padding) && mouseX <= (this.startX + utSzelesseg + padding) &&
                   mouseY >= (this.startY - padding) && mouseY <= (this.startY + utHossz + padding);
        } else {
            return mouseX >= (this.startX - padding) && mouseX <= (this.startX + utHossz + padding) &&
                   mouseY >= (this.startY - padding) && mouseY <= (this.startY + utSzelesseg + padding);
        }
    }

    public double kozepTavolsag(int mouseX, int mouseY) {
        int centerX, centerY;
        if (utIrany == Irany.FEL || utIrany == Irany.LE) {
            centerX = this.startX + (getTeljesszelesseg() / 2);
            centerY = this.startY + (modell.getHossz() / 2);
        } else {
            centerX = this.startX + (modell.getHossz() / 2);
            centerY = this.startY + (getTeljesszelesseg() / 2);
        }
        return Math.sqrt(Math.pow(mouseX - centerX, 2) + Math.pow(mouseY - centerY, 2));
    }
}