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


        /**
     * Beállítja a biokerozin készletet tesztelési célból.
     * Az {@code equip store} parancs telítettség paramétere hívja meg.
     *
     * @param mennyiseg A beállítandó biokerozin mennyiség.
     */
    public void setBiokerozin(int mennyiseg) {
        this.biokerozin = mennyiseg;
    }

    /**
     * Kiírja a fej biokerozin készletét. Só és zúzalék nullás értékkel szerepel.
     *
     * @param kimenet A célstream.
     */
    @Override
    public void kiirKeszlet(java.io.PrintStream kimenet) {
        kimenet.println("- Biokerozin: " + biokerozin);
        kimenet.println("- So: 0");
        kimenet.println("- Zuzalek: 0");
    }
    
    /**
     * Megkísérli az újratöltést, és visszajelzi, hogy sikeres volt-e.
     *
     * @param telephely A készletet biztosító telephely.
     * @return {@code true}, ha volt elegendő biokerozin és a töltés megtörtént.
     */
    public boolean ujratoltEllenorzott(funkcionalisElemek.Telephely telephely) {
        if (telephely.biokerozinUjratolt()) {
            biokerozin = 10;
            return true;
        }
        return false;
    }
}
