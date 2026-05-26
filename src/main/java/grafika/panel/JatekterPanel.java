package grafika.panel;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JPanel;

import funkcionalisElemek.KorSzamlalo;
import grafika.Observer;
import grafika.view.JarmuView;
import grafika.view.KanyarView;
import grafika.view.KeresztezodesView;
import grafika.view.UtView;
import jarmuvek.Jarmu;

/**
 * A fő játékablak központi része, a pálya vizualizációja.
 * Itt jelennek meg az Utak és a rajtuk mozgó járművek.
 */
public class JatekterPanel extends JPanel implements Observer {

    // A kirajzolandó grafikus nézetek listái
    private List<UtView> utakNezetei;
    private List<KanyarView> kanyarNezetei;
    private List<KeresztezodesView> keresztezodesNezetei;
    private List<JarmuView> jarmuvekNezetei;
    private KorSzamlalo modell;
    private Map<Jarmu, JarmuView> jarmuNezetekMap;
    private String aktivJatekos = "HOKOTRO";
    private jarmuvek.Hokotro aktivHokotro = null;

    public void kovetkezoHokotro() {
        java.util.List<jarmuvek.Hokotro> list = new java.util.ArrayList<>();
        for (jarmuvek.Jarmu j : modell.getJarmuvek()) {
            if (j instanceof jarmuvek.Hokotro) {
                list.add((jarmuvek.Hokotro) j);
            }
        }
        if (list.isEmpty()) return;
        
        if (aktivHokotro == null || !list.contains(aktivHokotro)) {
            aktivHokotro = list.get(0);
        } else {
            int idx = list.indexOf(aktivHokotro);
            aktivHokotro = list.get((idx + 1) % list.size());
        }
    }
    
    public void setAktivJatekos(String aktivJatekos) {
        this.aktivJatekos = aktivJatekos;
        for (grafika.view.UtView uv : utakNezetei) {
            for (grafika.view.SavView sv : uv.getSavNezetek()) {
                sv.setKijeloles(0);
            }
        }
        repaint();
    }

    public String getAktivJatekos() {
        return aktivJatekos;
    }


    public JatekterPanel(KorSzamlalo modell) {
        this.modell = modell;
        this.utakNezetei = new ArrayList<>();
        this.kanyarNezetei = new ArrayList<>();
        this.keresztezodesNezetei = new ArrayList<>();
        this.jarmuvekNezetei = new ArrayList<>();
        this.jarmuNezetekMap = new HashMap<>();

        // Fűzöld háttér, hogy az utak közti (nem-úttest) terület ne fehéren villogjon,
        // és a kanyarok fűhátere zökkenőmentesen olvadjon bele.
        setBackground(KanyarView.GRASS);
        
        // EGÉR IRÁNYÍTÁS BEKÖTÉSE
        addMouseListener(new java.awt.event.MouseAdapter(){
           @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                int mouseX = e.getX();
                int mouseY = e.getY();
                
                // 1. Megkeressük az aktuális játékos járművét
                jarmuvek.Jarmu jatekosJarmu = null;
                if ("HOKOTRO".equals(aktivJatekos)) {
                    if (aktivHokotro == null || !modell.getJarmuvek().contains(aktivHokotro)) {
                        kovetkezoHokotro(); 
                    }
                    jatekosJarmu = aktivHokotro; 
                } else if ("BUSZ".equals(aktivJatekos)) {
                    for (jarmuvek.Jarmu j : modell.getJarmuvek()) {
                        if (j instanceof jarmuvek.Busz) { jatekosJarmu = j; break; }
                    }
                }

                // 2. Minden sáv kijelölésének törlése
                for (grafika.view.UtView uv : utakNezetei) {
                    for (grafika.view.SavView sv : uv.getSavNezetek()) {
                        sv.setKijeloles(0);
                    }
                }

                // 3. Megkeressük, melyik SavView-ra kattintottunk
                grafika.view.SavView celSavView = null;
                for (grafika.view.UtView uv : utakNezetei) {
                    for (grafika.view.SavView sv : uv.getSavNezetek()) {
                        if (sv.contains(mouseX, mouseY)) {
                            // Ugyanazt a sávot ne válasszuk ki, amin épp vagyunk
                            if (jatekosJarmu != null && jatekosJarmu.getAktualisSav() == sv.getModell()) {
                                continue;
                            }
                            celSavView = sv;
                            break;
                        }
                    }
                    if (celSavView != null) break;
                }

                // 4. Kijelölés érvényesítése
                if (celSavView != null && jatekosJarmu != null) {
                    funkcionalisElemek.Sav celSav = celSavView.getModell();
                    funkcionalisElemek.Ut celUt = celSav.getUt();
                    funkcionalisElemek.Sav aktSav = jatekosJarmu.getAktualisSav();
                    boolean ervenyesKanyar = false;

                    if (aktSav != null) {
                        funkcionalisElemek.Ut aktUt = aktSav.getUt();
                        segedOsztalyok.HaladasiIrany irany = aktSav.getIrany();
                        // Fizikai kapcsolat ellenőrzése
                        if (aktUt.getKapcsolatok(irany) != null && aktUt.getKapcsolatok(irany).containsKey(celUt)) {
                            String erkezesiVeg = aktUt.getKapcsolatok(irany).get(celUt);
                            // Irány (sáv oldalának) ellenőrzése
                            segedOsztalyok.HaladasiIrany vartIrany = erkezesiVeg.equals("vegA") ? segedOsztalyok.HaladasiIrany.A_BOL_B_BE : segedOsztalyok.HaladasiIrany.B_BOL_A_BA;
                            
                            if (celSav.getIrany() == vartIrany) {
                                ervenyesKanyar = true;
                            }
                        }
                    }

                    if (ervenyesKanyar) {
                        if (jatekosJarmu instanceof jarmuvek.Hokotro) {
                            ((jarmuvek.Hokotro) jatekosJarmu).setKovetkezoSav(celSav);
                        } else if (jatekosJarmu instanceof jarmuvek.Busz) {
                            ((jarmuvek.Busz) jatekosJarmu).setKovetkezoSav(celSav);
                        }
                        System.out.println("Érvényes kijelölés! Új célpont sáv: " + celSav.getId());
                    } else {
                        celSavView.setKijeloles(2); // Érvénytelen cél = piros villanás
                        System.out.println("Érvénytelen kanyarodási cél sáv (Nincs út vagy rossz irány)!");
                        repaint();
                        return; 
                    }
                }
                
                frissitKijelolesek();
                
                // Panel azonnali frissítése, hogy a keretek megjelenjenek
                repaint();
            }
        });
    }
    

    // Ezeket a metódusokat a pályabetöltő fogja használni,
    // hogy felpakolja a nézeteket a vászonra
    public void addUtView(UtView uv) {
        utakNezetei.add(uv);
    }

    /** A pályabetöltő hívja: felveszi egy sarok kanyar-nézetét. */
    public void addKanyarView(KanyarView kv) {
        kanyarNezetei.add(kv);
    }

    /** A pályabetöltő hívja: felveszi egy 3-/4-ágú kereszteződés nézetét. */
    public void addKeresztezodesView(KeresztezodesView kv) {
        keresztezodesNezetei.add(kv);
    }

    /**
     * Ezt hívja meg a KorSzamlalo a notifyObservers() révén minden körben,
     * illetve ha állapotváltozás történik.
     */
    @Override
    public void update() {
        // PULL fázis: Lekérjük az összes aktuális járművet a modelltől
        List<Jarmu> aktualisJarmuvek = modell.getJarmuvek();

        for (Jarmu j : aktualisJarmuvek) {
            // Ha felbukkan egy olyan jármű, aminek még nincs grafikus nézete
            if (!jarmuNezetekMap.containsKey(j)) {
                System.out.println("JatekterPanel: Új jármű észlelve! Nézet generálása...");
                JarmuView ujNezet = new JarmuView(j);
                
                // KONKRÉT IMPLEMENTÁCIÓ: Hozzáadás a Map-hez ÉS a kirajzolandó listához is!
                jarmuNezetekMap.put(j, ujNezet);
                jarmuvekNezetei.add(ujNezet); 
            }
        }

        // Frissítjük az összes létező járműnézetet (pozíciók újraszámolása)
        for (JarmuView jv : jarmuNezetekMap.values()) {
            jv.update();
        }

        // A Swing motorjának jelezzük, hogy rajzoljon újra mindent
        frissitKijelolesek();
        repaint();
    }

    /**
     * A Swing grafikus motorja hívja meg, amikor újra kell rajzolni a panelt.
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g); // Alapértelmezett törlés/háttérfestés

        // 1. Először az utakat (és a sávokat) rajzoljuk ki, mert azok vannak legalul
        for (UtView uv : utakNezetei) {
            uv.draw(g);
        }

        // 2. A kanyarok a sarkokra, az egyenes sávok fölé, hogy elfedjék az ott
        //    átfedő téglalapokat egy rendezett ívvel.
        for (KanyarView kv : kanyarNezetei) {
            kv.draw(g);
        }

        // 3. A 3-/4-ágú kereszteződések egységes aszfaltfoltja, szintén az utak fölé,
        //    hogy a keresztben futó sávjelzések ne zsúfolják össze a csomópontot.
        for (KeresztezodesView kv : keresztezodesNezetei) {
            kv.draw(g);
        }

        // 4. Legfelülre a járművek, hogy takarják az aszfaltot/havat
        for (JarmuView jv : jarmuvekNezetei) {
            jv.draw(g);
        }
        // 5. Kijelölt jármű megjelölése sárga gyűrűvel
        if ("HOKOTRO".equals(aktivJatekos) && aktivHokotro != null) {
            JarmuView jv = jarmuNezetekMap.get(aktivHokotro);
            if (jv != null) {
                Graphics2D g2 = (Graphics2D) g;
                Stroke regiStroke = g2.getStroke();
                g2.setColor(new Color(255, 215, 0, 220)); // Arany sárga
                g2.setStroke(new BasicStroke(4f));
                g2.drawOval(jv.getXKalkulalt() - 10, jv.getYKalkulalt() - 10, 70, 70); 
                g2.setStroke(regiStroke);
            }
        }

    }

    

    public void frissitKijelolesek() {
        for (grafika.view.UtView uv : utakNezetei) {
            for (grafika.view.SavView sv : uv.getSavNezetek()) {
                sv.setKijeloles(0);
                for (jarmuvek.Jarmu jrm : modell.getJarmuvek()) {
                    if (jrm instanceof jarmuvek.Hokotro && ((jarmuvek.Hokotro) jrm).getKovetkezoSav() == sv.getModell()) {
                        sv.setKijeloles(1);
                    } else if (jrm instanceof jarmuvek.Busz && ((jarmuvek.Busz) jrm).getKovetkezoSav() == sv.getModell()) {
                        sv.setKijeloles(1);
                    }
                }
            }
        }
        repaint();
    }

    public void setAktivHokotro(jarmuvek.Hokotro h) {
        this.aktivHokotro = h;
        repaint();
    }

    public jarmuvek.Hokotro getAktivHokotro() {
        return this.aktivHokotro;
    }
}
