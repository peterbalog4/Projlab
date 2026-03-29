package kotrofejek;

import funkcionalisElemek.Sav;
import funkcionalisElemek.Ut;
import vezerles.Skeleton;

/**
 * A Söprő típusú kotrófejet reprezentáló osztály.
 * Ez a munkaeszköz mechanikus úton takarítja el a havat a sávból.
 * A dokumentáció alapján a havat pontosan 1 sávnyi távolságra helyezi át 
 * az út segítségével, de a jégréteg feltörésére nem alkalmas.
 */
public class SoproFej extends KotroFej{

    /**
     * Elvégzi a mechanikus hóeltakarítást az adott sávon.
     * Meghívja a sáv hótakarító metódusát, majd jelzi az útnak, hogy a havat 
     * 1 sávnyi távolságra mozgassa el.
     * @param sav Az aktuálisan takarított sáv.
     * @param ut Az úthálózat referenciája a hó mozgatásához.
     */
    @Override
    public void takarit(Sav sav, Ut ut) {
        Skeleton.hiv("sopro:SoproFej: tisztit("+ sav.getId() + ", ut)");

        sav.hoTakarit();

        Skeleton.naploz("A sáv megtisztult! A hó eltűnt.");
        Skeleton.visszater("tisztit");
    }
    

}
