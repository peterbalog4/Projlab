package funkcionalisElemek;

import grafika.AbstractObservable;

/**
 * Egy kereszteződés vagy kanyar „mezeje" (tile). A sávokkal ellentétben itt
 * nem közlekednek járművek és nem alakul ki jég sem — a tile csak hó-állapotot
 * tárol, hogy a kereszteződéseken/sarkokon is hullhasson hó a térképen, ne csak
 * a sávokon. A vizuális megjelenítéshez (`KanyarView`/`KeresztezodesView`) ad
 * jelet a megfigyelőknek a {@code notifyObservers()}-en keresztül.
 *
 * Egyszerűen tartott állapotgép: {@code ho} az aktuális hómennyiség (0..N).
 * A {@link #havazas()}-t a {@link KorSzamlalo#leptet()} hívja minden olyan
 * körben, amikor a havazás be van kapcsolva — 1/10 esélyel hullik egy egységnyi
 * hó (ugyanaz a valószínűség, mint az {@link Ut#hoNovel()} sávonkénti
 * havazásnál, hogy a megjelenés vizuálisan konzisztens legyen).
 */
public class Tile extends AbstractObservable {

    private static final java.util.Random HO_RANDOM = new java.util.Random();

    /** A tile-on felgyülemlett hó mennyisége. */
    private int ho = 0;

    /** A tile azonosítója (csak naplózáshoz / debughoz). */
    private final String id;

    public Tile(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public int getHo() {
        return ho;
    }

    /**
     * 1/10 eséllyel növeli a hómennyiséget egy egységgel, és értesíti a
     * megfigyelőket. A {@link KorSzamlalo} hívja minden körben, ha aktív a havazás.
     */
    public void havazas() {
        if (HO_RANDOM.nextInt(10) == 0) {
            ho++;
            notifyObservers();
        }
    }

    /** Teljesen eltakarítja a tile-on lévő havat. */
    public void hoTakarit() {
        if (ho > 0) {
            ho = 0;
            notifyObservers();
        }
    }
}
