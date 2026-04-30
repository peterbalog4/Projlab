package kotrofejek;

import funkcionalisElemek.Sav;
import funkcionalisElemek.Telephely;
import funkcionalisElemek.Ut;



public class SarkanyFej extends KotroFej {

    private int biokerozin = 0;
    private boolean fejBekapcsolva = false;

    
    @Override
    public void takarit(Sav sav, Ut ut) {
        if(fejBekapcsolva && biokerozin > 0){
            sav.jegFeltor();
            int eltakaritottMennyiseg = sav.hoTakarit();
            biokerozin --;
        }
    }
    
    
    
    public void ujratolt(Telephely telephely) { //!!!! ez még nem jó telephely átadása
        if(telephely.biokerozin>0){
            biokerozin = 10;
            telephely.biokerozin --;
        }
    }

    public void bekapcsol(){
        fejBekapcsolva = true;
    }

    public void kikapcsol(){
        fejBekapcsolva = false;
    }
}
