package grafika.view;
 
import funkcionalisElemek.Ut;
import grafika.Observer;
import segedOsztalyok.Irany;
import funkcionalisElemek.Sav;
 
import java.awt.Graphics;
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
                    // Függőleges út, felfelé haladó. A sáv téglalapja: szélesség=SAV_SZELLESEG, magasság=hossz.
                    // A sávok vízszintesen (X irányban) sorakoznak.
                    savNezetek.add(new SavView(sav, currentX, currentY));
                    currentX += SAV_SZELLESEG;
                    break;
 
                case LE:
                    // Függőleges út, lefelé haladó. Az út teteje startY, alja startY+hossz.
                    savNezetek.add(new SavView(sav, currentX, currentY));
                    currentX += SAV_SZELLESEG;
                    break;
 
                case JOBBRA:
                    // Vízszintes út, jobbra haladó. A sáv téglalapja: szélesség=hossz, magasság=SAV_SZELLESEG.
                    // A sávok függőlegesen (Y irányban) sorakoznak.
                    savNezetek.add(new SavView(sav, currentX, currentY));
                    currentY += SAV_SZELLESEG;
                    break;
 
                case BALRA:
                    // Vízszintes út, balra haladó. Az út jobb széle startX+hossz-nál van,
                    // de a SavView-nak a bal felső sarokot adjuk át (startX).
                    savNezetek.add(new SavView(sav, currentX, currentY));
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
     */
    public void draw(Graphics g) {
        for (SavView sv : savNezetek) {
            sv.draw(g);
        }
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
}