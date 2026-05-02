package kotrofejek;

import funkcionalisElemek.Sav;
import funkcionalisElemek.Telephely;
import funkcionalisElemek.Ut;

public class ZuzalekszoroFej extends KotroFej{

    private int zuzalek = 10;
    private boolean fejBekapcsolva = false;


    @Override
    public void takarit(Sav sav, Ut ut) {
        if(fejBekapcsolva && zuzalek > 0){
            sav.zuzalekSzor();
            zuzalek--;
        }
    }

    public void bekapcsol(){
        fejBekapcsolva = true;
    }

    public void kikapcsol(){
        fejBekapcsolva = false;
    }

   
    public void ujratolt(Telephely telephely) { //!!!! ez még nem jó kell valami telephely átadása neki
        if(telephely.zuzalekUjratolt())
            zuzalek=10;
    }
}