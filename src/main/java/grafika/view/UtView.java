package grafika.view;

import funkcionalisElemek.Ut;
import grafika.Observer;

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
