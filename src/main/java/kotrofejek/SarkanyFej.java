package kotrofejek;

import funkcionalisElemek.Sav;
import funkcionalisElemek.Telephely;
import funkcionalisElemek.Ut;



public class SarkanyFej extends KotroFej {

    private int biokerozin = 0;
    private boolean fejBekapcsolva = false;

    
    @Override
    public boolean takarit(Sav sav, Ut ut) {
        if(fejBekapcsolva && biokerozin > 0){
            sav.jegFeltor();
            int eltakaritottMennyiseg = sav.hoTakarit(0);
            biokerozin --;
            return true;
        }
        return false;
    }
    
    
    
    public void ujratolt(Telephely telephely) { //!!!! ez még nem jó telephely átadása
        if(telephely.biokerozinUjratolt())
            biokerozin=10;
    }

    public void bekapcsol(){
        fejBekapcsolva = true;
    }

    public void kikapcsol(){
        fejBekapcsolva = false;
    }
}
