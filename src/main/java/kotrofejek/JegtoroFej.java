package kotrofejek;

import funkcionalisElemek.Sav;
import funkcionalisElemek.Ut;
import vezerles.Skeleton;


/**
 * A Jégtörő típusú kotrófejet reprezentáló osztály.
 * Ez a munkaeszköz speciálisan a sávokon kialakult jégréteg feltörésére szolgál.
 * A dokumentáció alapján a havat nem takarítja el, és nem használ üzemanyagot a működéséhez.
 */
public class JegtoroFej extends KotroFej {

    /**
     * Elvégzi a jégmentesítést az adott sávon.
     * Meghívja a sáv jégfeltörő metódusát, de a hóréteget érintetlenül hagyja.
     * @param sav Az a sáv, amelyen a jég feltörése történik.
     * @param ut Az úthálózat referenciája (ebben az esetben nincs hóátadás).
     */
    @Override
    public void takarit(Sav sav, Ut ut) {
        Skeleton.hiv("jegtoro:JegtoroFej: tisztit("+ sav.getId() + ", ut)");

        sav.jegFeltor();

        Skeleton.visszater("tisztit");
    }

}
