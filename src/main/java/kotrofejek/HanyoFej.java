package kotrofejek;

import funkcionalisElemek.Sav;
import funkcionalisElemek.Ut;


/**
 * A Hányó típusú kotrófejet reprezentáló osztály.
 * Ez a munkaeszköz a havat nagy erővel távolítja el a sávból, 
 * és a dokumentációban meghatározott 3 sávnyi távolságra juttatja azt arrébb.
 * A jégréteg feltörésére nem alkalmas.
 */
public class HanyoFej extends KotroFej {


    /**
     * Elvégzi a hóeltakarítást az adott sávon.
     * Meghívja a sáv hótakarító funkcióját, majd az út segítségével 
     * 3 sávnyi távolságra mozgatja az összegyűjtött havat.
     * * @param sav Az a sáv, amelyen a takarítás éppen történik.
     * @param ut Az úthálózat, amely koordinálja a hó sávok közötti átadását.
     */
    @Override
    public void takarit(Sav sav, Ut ut) {
        int eltakaritottMennyiseg = sav.hoTakarit();
        ut.havatAtad(sav, 3, eltakaritottMennyiseg);

    }

}
