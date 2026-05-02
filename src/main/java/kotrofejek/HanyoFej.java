package kotrofejek;

import funkcionalisElemek.Sav;
import funkcionalisElemek.Ut;



public class HanyoFej extends KotroFej {


    
    @Override
    public boolean takarit(Sav sav, Ut ut) {
        int eltakaritottMennyiseg = sav.hoTakarit(3);
        return true;
    }

}
