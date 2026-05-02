package kotrofejek;

import funkcionalisElemek.Sav;
import funkcionalisElemek.Telephely;
import funkcionalisElemek.Ut;



public class SoszoroFej extends KotroFej{

    private int so = 10;
    private boolean fejBekapcsolva = false;


    @Override
    public boolean takarit(Sav sav, Ut ut) {
        if(fejBekapcsolva && so > 0){
            sav.soSzor();
            so--;
            return true;
        }
        return false;
    }

    public void bekapcsol(){
        fejBekapcsolva = true;
    }

    public void kikapcsol(){
        fejBekapcsolva = false;
    }

    public void setSo(int mennyiseg) {
        this.so = mennyiseg;
    }

    /**
     * Kiírja a fej só készletét. Biokerozin és zúzalék nullás értékkel szerepel.
     *
     * @param kimenet A célstream.
     */
    @Override
    public void kiirKeszlet(java.io.PrintStream kimenet) {
        kimenet.println("- Biokerozin: 0");
        kimenet.println("- So: " + so);
        kimenet.println("- Zuzalek: 0");
    }

    public void ujratolt(funkcionalisElemek.Telephely telephely) {
        telephely.soUjratolt();
        so = 10;
    }

    /**
     * Megkísérli az újratöltést, és visszajelzi, hogy sikeres volt-e.
     *
     * @param telephely A készletet biztosító telephely.
     * @return {@code true}, ha volt elegendő só és a töltés megtörtént.
     */
    public boolean ujratoltEllenorzott(funkcionalisElemek.Telephely telephely) {
        if (telephely.soUjratolt()) {
            so = 10;
            return true;
        }
        return false;
    }


}
