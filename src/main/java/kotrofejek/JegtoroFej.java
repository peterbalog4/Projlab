package kotrofejek;

import funkcionalisElemek.Sav;
import funkcionalisElemek.Ut;




public class JegtoroFej extends KotroFej {

    
    @Override
    public boolean takarit(Sav sav, Ut ut) {
        sav.jegFeltor();
        return true;
    }

}
