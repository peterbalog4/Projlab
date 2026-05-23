package grafika.view;

import funkcionalisElemek.Ut;
import grafika.Observer;
import segedOsztalyok.Irany;
import funkcionalisElemek.Sav;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

//TODO: bekérésben megcsinálni, hogy adjon az utaknak irányt és kapcsolódjanak a végpontok mentén

/**
 * Az egész út globális megjelenítését és a sávok összefogását reprezentáló nézet.
 */
public class UtView implements Observer {

    private Ut modell; // Referencia a megfigyelt logikai út osztályra

    // A váz-építő jó tanácsa: Érdemes itt tárolni az úthoz tartozó sávok nézeteit
    private List<SavView> savNezetek;
    private Irany utIrany;              //ez az út iránya a térképen, nem a haladási irány
    public final int SAV_SZELLESEG = 60; // Egy sáv szélessége a grafikus megjelenítésben

    public UtView(Ut modell, int startX, int startY, Irany utIrany) {
        this.modell = modell;
        this.savNezetek = new ArrayList<>();
        this.utIrany = utIrany;
        modell.addObserver(this);
        // TODO a csapatnak: Végigiterálni a modell.getSavok() listán,
        // és mindegyikhez példányosítani egy SavView-t a megfelelő X, Y koordinátákkal,
        // majd hozzáadni a savNezetek listához.

        List<Sav> frissSavok = modell.getSavok();
        
        // Tisztítjuk az eddigi nézeteket, majd újra felépítjük őket
        // (Ha a sávok száma statikus, akkor elég lenne a meglévő SavView-kon végigmenni és frissíteni őket)
        savNezetek.clear();
        
        int currentX = startX;              //ezek mindig a baloldali végállomás koordinátái
        int currentY = startY;              //ezek mindig a lenti végállomás koordinátái

        for (Sav sav : frissSavok) {
            savNezetek.add(new SavView(sav, currentX, currentY));

            // A haladási irányra MERŐLEGESEN toljuk el a következő sáv koordinátáját,
            // hiszen egy úton belül a sávok párhuzamosan, egymás mellett vannak.
            switch (utIrany) {
                case FEL:
                    savNezetek.add(new SavView(sav, currentX, currentY)); //már úgy adjuk át, hogy csak ki kelljen rajzolni
                    currentX += SAV_SZELLESEG; 
                    break;
                case LE :
                    // Függőleges haladásnál a sávok vízszintesen vannak egymás mellett
                    savNezetek.add(new SavView(sav, currentX, currentY-modell.getHossz())); //már úgy adjuk át, hogy csak ki kelljen rajzolni
                    currentX += SAV_SZELLESEG; 
                    break;
                case JOBBRA:
                    savNezetek.add(new SavView(sav, currentX, currentY)); //már úgy adjuk át, hogy csak ki kelljen rajzolni
                    currentY += SAV_SZELLESEG; 
                    break;
                case BALRA:
                    // Vízszintes haladásnál a sávok függőlegesen vannak egymás alatt/felett
                    savNezetek.add(new SavView(sav, currentX-SAV_SZELLESEG, currentY)); //már úgy adjuk át, hogy csak ki kelljen rajzolni
                    currentY += SAV_SZELLESEG; 
                    break;
            }
        }
    }


   

    /**
     * Frissíti a nézetet, ha az út állapota globálisan megváltozik.
     */
    @Override
    public void update() {
        // Ha valami egész utat érintő változás van, itt kezeljük le.
        //TODO szerintem ilyen nem lehet, csak a sávokban
        
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
