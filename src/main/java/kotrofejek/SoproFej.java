package kotrofejek;

import funkcionalisElemek.Sav;
import funkcionalisElemek.Ut;



public class SoproFej extends KotroFej{

    
    @Override
    public void takarit(Sav sav, Ut ut) {
        int eltakaritottMennyiseg = sav.hoTakarit();
        ut.havatAtad(sav, 1, eltakaritottMennyiseg);
        sav.zuzalekEltakarit();
    }
    

}
