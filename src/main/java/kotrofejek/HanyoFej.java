package kotrofejek;

import funkcionalisElemek.Sav;
import funkcionalisElemek.Ut;

/**
 * A havat 3 sávval arrébb lövi, közvetlen a Hókotró előtt, de a jeget nem tudja feltörni. Ha nincs mellette Sáv, akkor
 * letolja az Útról. A Sávról eltakarítja a rajta lévő zuzalékot is, aminek a hatására az eddig nem csúszós Sáv újra
 * csúszóssá válik. Eltakarítja a jégtörmeléket, aminek a hatására a Sáv jeges állapota megszűnik. Az eltakarított havat
 * továbbadja a megfelelő Sávnak, aminek a hómennyiséghez ez hozzáadódik. A feltört jeget és a zuzalékot is továbbadja.
 */

public class HanyoFej extends KotroFej {

    /**
     * A havat 3 sávval arrébb lövi, közvetlen a Hókotró előtt. Eltakarítja a zuzalékot és a jégtörmeléket. Az
     * eltakarított anyagokat továbbadja a Sávnak.
     *
     * @param sav A takarítandó sáv.
     * @param ut  Az az út, amelyhez a sáv tartozik.
     * @return {@code true}, ha a takarítás eredményes volt és a sáv állapota javult, {@code false}, ha nem történt
     * változás (pl. üres tartály). A fizettség kiosztásához kell.
     */
    @Override
    public boolean takarit(Sav sav, Ut ut) {
        int eltakaritottMennyiseg = sav.hoTakarit(3);
        return true;
    }

}
