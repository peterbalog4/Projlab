import funkcionalisElemek.KorSzamlalo;
import grafika.panel.JatekterPanel;
import vezerles.Map_generator;

import javax.imageio.ImageIO;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;

/** Headless render-ellenőrző az új pályához (csak verifikáció). */
public class VerifyRender {
    static final int W = 780, H = 700;

    public static void main(String[] args) throws Exception {
        KorSzamlalo modell = new KorSzamlalo();
        JatekterPanel panel = new JatekterPanel(modell);
        panel.setSize(W, H);

        Map_generator map = new Map_generator(modell);
        map.load("src/main/java/vezerles/nagy_palya.txt", panel);

        render(panel, "verify_tmp/nagy_initial.png");

        modell.setHoesik(true);
        for (int i = 0; i < 4; i++) modell.leptet();
        render(panel, "verify_tmp/nagy_snow.png");
        System.out.println("OK");
    }

    static void render(JatekterPanel panel, String path) throws Exception {
        BufferedImage img = new BufferedImage(W, H, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = img.createGraphics();
        panel.paint(g);
        g.dispose();
        ImageIO.write(img, "png", new File(path));
    }
}
