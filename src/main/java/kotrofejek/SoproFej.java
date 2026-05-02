package kotrofejek;

import funkcionalisElemek.Sav;
import funkcionalisElemek.Ut;



public class SoproFej extends KotroFej{

    
    @Override
    public boolean takarit(Sav sav, Ut ut) {
        int eltakaritottMennyiseg = sav.hoTakarit(1);
        return true;
    }
    

}
