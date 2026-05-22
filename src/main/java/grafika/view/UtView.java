package grafika.view;

import funkcionalisElemek.Ut;
import grafika.Observer;
import funkcionalisElemek.Sav;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

/**
 * Az egész út globális megjelenítését és a sávok összefogását reprezentáló nézet.
 */
public class UtView implements Observer {

    private Ut modell; // Referencia a megfigyelt logikai út osztályra

    // A váz-építő jó tanácsa: Érdemes itt tárolni az úthoz tartozó sávok nézeteit
    private List<SavView> savNezetek;

    public UtView(Ut modell) {
        this.modell = modell;
        this.savNezetek = new ArrayList<>();
        modell.addObserver(this);
        update();

        // TODO a csapatnak: Végigiterálni a modell.getSavok() listán,
        // és mindegyikhez példányosítani egy SavView-t a megfelelő X, Y koordinátákkal,
        // majd hozzáadni a savNezetek listához.
    }


   

    /**
     * Frissíti a nézetet, ha az út állapota globálisan megváltozik.
     */
    @Override
    public void update() {
        // Ha valami egész utat érintő változás van, itt kezeljük le.
        List<Sav> frissSavok = modell.getSavok();
        
        // Tisztítjuk az eddigi nézeteket, majd újra felépítjük őket
        // (Ha a sávok száma statikus, akkor elég lenne a meglévő SavView-kon végigmenni és frissíteni őket)
        savNezetek.clear();
        
        int currentX = 50;  // Kezdőpozíció
        int currentY = 200; // Fix magasság
        int savSzelesseg = 60;

        for (Sav sav : frissSavok) {
            savNezetek.add(new SavView(sav, currentX, currentY));
            currentX += savSzelesseg; 
        }
    }

    /**
     * Az út grafikus elemeinek kirajzolását hajtja végre.
     */
    public void draw(Graphics g) {
        // Végigkérjük az úthoz tartozó összes sávot, hogy rajzolják ki magukat
        for (SavView sv : savNezetek) {
            sv.draw(g);
        }

        // TODO a csapatnak: Esetleges út-specifikus grafikák (pl. út széle, padka) kirajzolása.
    }
}
