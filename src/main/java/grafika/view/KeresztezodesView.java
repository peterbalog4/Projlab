package grafika.view;

import funkcionalisElemek.Tile;
import grafika.Observer;

import javax.imageio.ImageIO;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.io.File;

/**
 * Egy 3- vagy 4-ágú kereszteződés letisztult megjelenítése.
 *
 * A csomópont celláját egyetlen, egységes aszfaltfolttal tölti ki — ezzel elfedi
 * az itt egymásra csúszó egyenes sávokat és a keresztben futó felezővonalakat,
 * amelyek korábban zsúfolt rácsot adtak. Az úttal rendelkező oldalakra fehér
 * megállási vonalat (stop-line) rajzol, így a kereszteződés egyértelmű marad.
 *
 * A cella – a kanyarokhoz hasonlóan – a csomóponthoz illesztett, az út
 * szélességével megegyező oldalú négyzet (az utak +x/+y irányba terülnek el).
 */
public class KeresztezodesView implements Observer {

    private static final Color STOP_LINE = new Color(245, 245, 245, 220);
    private static final Image HO_IMG = betoltKep("ho.PNG");

    private static Image betoltKep(String nev) {
        try { return ImageIO.read(new File(nev)); } catch (Exception e) { return null; }
    }

    private final int cellX, cellY, meret;
    private final boolean eszak, del, kelet, nyugat;
    private final Tile tile;
    private int homennyiseg = 0;

    /**
     * @param cellX  A cella bal-fenti X koordinátája.
     * @param cellY  A cella bal-fenti Y koordinátája.
     * @param meret  A cella oldalhossza (= az út szélessége).
     * @param eszak  Indul-e út észak felé (felső él).
     * @param del    Indul-e út dél felé (alsó él).
     * @param kelet  Indul-e út kelet felé (jobb él).
     * @param nyugat Indul-e út nyugat felé (bal él).
     * @param tile   A kereszteződéshez tartozó tile (hó-állapot). Lehet null.
     */
    public KeresztezodesView(int cellX, int cellY, int meret,
                             boolean eszak, boolean del, boolean kelet, boolean nyugat,
                             Tile tile) {
        this.cellX = cellX;
        this.cellY = cellY;
        this.meret = meret;
        this.eszak = eszak;
        this.del = del;
        this.kelet = kelet;
        this.nyugat = nyugat;
        this.tile = tile;
        if (tile != null) tile.addObserver(this);
    }

    @Override
    public void update() {
        if (tile != null) homennyiseg = tile.getHo();
    }

    /** Egységes aszfaltfolt + fehér megállási vonalak az úttal rendelkező oldalakon. */
    public void draw(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Egységes aszfalt – elfedi a kereszteződő sávjelzéseket
        g2.setColor(KanyarView.ASPHALT);
        g2.fillRect(cellX, cellY, meret, meret);

        // Havazás a kereszteződés tile-on (a sávhavazással konzisztens alpha)
        if (homennyiseg > 0) {
            int alpha = Math.min(210, 50 + homennyiseg * 35);
            g2.setColor(new Color(255, 255, 255, alpha));
            g2.fillRect(cellX, cellY, meret, meret);
            if (HO_IMG != null) {
                final int ICON = Math.max(20, meret / 4);
                int ix = cellX + (meret - ICON) / 2;
                int iy = cellY + (meret - ICON) / 2;
                g2.drawImage(HO_IMG, ix, iy, ICON, ICON, null);
            }
        }

        // Megállási vonalak kissé beljebb húzva az éltől
        g2.setColor(STOP_LINE);
        g2.setStroke(new BasicStroke(4f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        int be = Math.max(6, meret / 12);   // beljebb húzás
        int p  = meret / 6;                 // a vonal vége az éltől (rövidebb, mint a teljes él)

        int x0 = cellX, y0 = cellY, x1 = cellX + meret, y1 = cellY + meret;
        if (eszak)  g2.drawLine(x0 + p, y0 + be, x1 - p, y0 + be);
        if (del)    g2.drawLine(x0 + p, y1 - be, x1 - p, y1 - be);
        if (nyugat) g2.drawLine(x0 + be, y0 + p, x0 + be, y1 - p);
        if (kelet)  g2.drawLine(x1 - be, y0 + p, x1 - be, y1 - p);

        g2.dispose();
    }
}
