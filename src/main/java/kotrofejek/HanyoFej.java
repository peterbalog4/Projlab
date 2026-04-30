package kotrofejek;

import funkcionalisElemek.Sav;
import funkcionalisElemek.Ut;



public class HanyoFej extends KotroFej {


    
    @Override
    public void takarit(Sav sav, Ut ut) {
        int eltakaritottMennyiseg = sav.hoTakarit();
        ut.havatAtad(sav, 3, eltakaritottMennyiseg);
        sav.zuzalekEltakarit();
    }

}
