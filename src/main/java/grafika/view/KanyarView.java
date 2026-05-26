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
 * Egy kereszteződésnél lévő útkanyar grafikus megjelenítése.
 *
 * Korábban a kanyar.PNG csempét rajzoltuk ki, de annak fehér kerete és élénk
 * türkiz háttere „matricaként" elütött az utaktól. Helyette most programozottan,
 * az egyenes utakkal megegyező aszfaltszínű negyed-korongot rajzolunk fűhátérre,
 * így a kanyar zökkenőmentesen folytatja az utakat.
 *
 * A negyed-korong a kanyar belső (homorú) sarka köré rajzolódik, sugara az út
 * teljes szélessége, így a korong két egyenes éle pontosan a csatlakozó utak
 * szélességét fedi le. A forgatás kódolása megegyezik a korábbival:
 *   0 → {Ny,D}   1 → {É,Ny}   2 → {É,K}   3 → {K,D}
 *
 * A nézet statikus; a geometriát a {@code Map_generator} számolja ki betöltéskor.
 */
public class KanyarView implements Observer {

    /** Az egyenes utak (ut.PNG) átlagos aszfaltszíne, hogy az ív tónusban illeszkedjen. */
    public static final Color ASPHALT  = new Color(53, 67, 69);
    /** A nem-úttest (fű) színe; megegyezik a JatekterPanel hátterével a folytonos kinézetért. */
    public static final Color GRASS    = new Color(105, 160, 95);
    /** Halvány, sárgás felezővonal-jelölés. */
    public static final Color LANE_MARK = new Color(235, 225, 130, 200);
    /** Hó-textúra (a SavView-val megegyező), egyszer töltődik be. */
    private static final Image HO_IMG = betoltKep("ho.PNG");

    private static Image betoltKep(String nev) {
        try { return ImageIO.read(new File(nev)); } catch (Exception e) { return null; }
    }

    private final int cellX;         // a sarokcella bal-fenti X koordinátája
    private final int cellY;         // a sarokcella bal-fenti Y koordinátája
    private final int meret;         // a négyzet alakú cella oldalhossza (= út szélessége)
    private final int forgatasLepes; // 0..3, az alap {Ny,D} álláshoz képest
    private final Tile tile;         // a kanyar tile-ja (havazási állapot); lehet null
    private int homennyiseg = 0;

    public KanyarView(int cellX, int cellY, int meret, int forgatasLepes, Tile tile) {
        this.cellX = cellX;
        this.cellY = cellY;
        this.meret = meret;
        this.forgatasLepes = ((forgatasLepes % 4) + 4) % 4;
        this.tile = tile;
        if (tile != null) tile.addObserver(this);
    }

    @Override
    public void update() {
        if (tile != null) homennyiseg = tile.getHo();
    }

    /** Kirajzolja a sima aszfalt-ívet fűhátérre, felezővonallal. */
    public void draw(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // 1. Fűhátér a cellára (elfedi az itt átfedő egyenes sávokat)
        g2.setColor(GRASS);
        g2.fillRect(cellX, cellY, meret, meret);

        // 2. A belső (homorú) sarok középpontja és a negyed-korong kezdőszöge.
        //    A fillArc 0°-a a 3 óra felé mutat, a pozitív szög óramutatóval ellentétes.
        int cx, cy, kezdoSzog;
        switch (forgatasLepes) {
            case 0:  cx = cellX;         cy = cellY + meret; kezdoSzog = 0;   break; // {Ny,D} belső: DNy
            case 1:  cx = cellX;         cy = cellY;         kezdoSzog = 270; break; // {É,Ny} belső: ÉNy
            case 2:  cx = cellX + meret; cy = cellY;         kezdoSzog = 180; break; // {É,K}  belső: ÉK
            default: cx = cellX + meret; cy = cellY + meret; kezdoSzog = 90;  break; // {K,D}  belső: DK
        }

        // 3. Aszfalt negyed-korong
        g2.setColor(ASPHALT);
        g2.fillArc(cx - meret, cy - meret, 2 * meret, 2 * meret, kezdoSzog, 90);

        // 4. Havazás: ugyanarra a negyed-körre festünk fehér réteget (csak az
        //    aszfaltra esik, a fűhátérre nem), az alpha-t a hómennyiség adja.
        //    A SavView-val megegyezően egy hó-textúra ikont is rárakunk középre.
        if (homennyiseg > 0) {
            int alpha = Math.min(210, 50 + homennyiseg * 35);
            g2.setColor(new Color(255, 255, 255, alpha));
            g2.fillArc(cx - meret, cy - meret, 2 * meret, 2 * meret, kezdoSzog, 90);
            if (HO_IMG != null) {
                final int ICON = Math.max(20, meret / 3);
                // A negyed-körív geometriai súlypontja a belső saroktól kifelé
                // ~0.42*r távolságra van, a sugár felezőtengelye mentén; ezt
                // közelítjük úgy, hogy a belső saroktól meret/2 távolságra
                // helyezzük a snowflake-et, mindig a kifelé hajló irányba.
                int sx = cx + (cx == cellX ? meret / 2 - ICON / 2 : -meret / 2 - ICON / 2);
                int sy = cy + (cy == cellY ? meret / 2 - ICON / 2 : -meret / 2 - ICON / 2);
                g2.drawImage(HO_IMG, sx, sy, ICON, ICON, null);
            }
        }

        // 5. Szaggatott felezővonal az ív közepén (az út középvonalán)
        g2.setColor(LANE_MARK);
        g2.setStroke(new BasicStroke(2f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND,
                10f, new float[]{14f, 12f}, 0f));
        int r = meret / 2;
        g2.drawArc(cx - r, cy - r, 2 * r, 2 * r, kezdoSzog, 90);

        g2.dispose();
    }
}
