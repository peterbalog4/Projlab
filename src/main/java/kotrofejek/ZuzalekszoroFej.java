package kotrofejek;

import funkcionalisElemek.Sav;
import funkcionalisElemek.Telephely;
import funkcionalisElemek.Ut;

public class ZuzalekszoroFej extends KotroFej{

    private int zuzalek = 10;
    private boolean fejBekapcsolva = false;


    @Override
    public boolean takarit(Sav sav, Ut ut) {
        if(fejBekapcsolva && zuzalek > 0){
            sav.zuzalekSzor();
            zuzalek--;
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

       /**
     * Beállítja a zúzalék készletet tesztelési célból.
     *
     * @param mennyiseg A beállítandó zúzalék mennyiség.
     */
    public void setZuzalek(int mennyiseg) {
        this.zuzalek = mennyiseg;
    }

    /**
     * Kiírja a fej zúzalék készletét. Biokerozin és só nullás értékkel szerepel.
     *
     * @param kimenet A célstream.
     */
    @Override
    public void kiirKeszlet(java.io.PrintStream kimenet) {
        kimenet.println("- Biokerozin: 0");
        kimenet.println("- So: 0");
        kimenet.println("- Zuzalek: " + zuzalek);
    }

    public void ujratolt(funkcionalisElemek.Telephely telephely) {
        telephely.zuzalekUjratolt();
        zuzalek = 10;
    }

    /**
     * Megkísérli az újratöltést, és visszajelzi, hogy sikeres volt-e.
     *
     * @param telephely A készletet biztosító telephely.
     * @return {@code true}, ha volt elegendő zúzalék és a töltés megtörtént.
     */
    public boolean ujratoltEllenorzott(funkcionalisElemek.Telephely telephely) {
        if (telephely.zuzalekUjratolt()) {
            zuzalek = 10;
            return true;
        }
        return false;
    }
}