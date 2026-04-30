package kotrofejek;

import funkcionalisElemek.Sav;
import funkcionalisElemek.Telephely;
import funkcionalisElemek.Ut;



public class SoszoroFej extends KotroFej{

    private int so = 10;
    private boolean fejBekapcsolva = false;


    @Override
    public void takarit(Sav sav, Ut ut) {
        if(fejBekapcsolva && so > 0){
            sav.soSzor();
            so--;
        }
    }

    public void bekapcsol(){
        fejBekapcsolva = true;
    }

    public void kikapcsol(){
        fejBekapcsolva = false;
    }

   
    public void ujratolt() { //!!!! ez még nem jó
        so = 10;
    }
}
