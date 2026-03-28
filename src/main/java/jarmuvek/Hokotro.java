package jarmuvek;
import funkcionalisElemek.Sav;
import funkcionalisElemek.Ut;
import kotrofejek.KotroFej;
import vezerles.Skeleton;

public class Hokotro extends Jarmu {

    private KotroFej aktivFej;
    private Ut aktivUt;
    private Sav aktivSav;

    public Hokotro(String id) {
        super(id);
    }

    public void setKotrofej(KotroFej fej) {
        Skeleton.hiv(this.id + ":Hokotro: setKotrofej(fej)");
        this.aktivFej = fej;
        Skeleton.visszater("setKotrofej");
    }

    public void setUt(Ut u) {
        Skeleton.hiv( this.id + ":Hokotro: setUt(u)");
        this.aktivUt = u;
        Skeleton.visszater("setUt");
    }

    public void setSav(Sav s) {
        Skeleton.hiv( this.id + ":Hokotro: setSav(s)");
        this.aktivSav = s;
        Skeleton.visszater("setUt");
    }

    @Override
    public void kozlekedik() {
        Skeleton.hiv(this.id + ":Hokotro: kozlekedik()");

        if (!mozgaskepes) {
            Skeleton.naploz("A hókotró mozgásképtelen.");
            Skeleton.visszater("kozlekedik");
            return;
        }

        Skeleton.visszater("kozlekedik");
    }
    public void fejcsere(KotroFej ujfej) {
        Skeleton.hiv(this.id + ":Hokotro: fejcsere(ujfej)");
        
        this.aktivFej = ujfej;
        Skeleton.naploz("A hókotró új fejet kapott.");
        
        Skeleton.visszater("fejcsere");
    }

    @Override
    public void utkozik(Jarmu masikJarmu){

    }
    public void dolgozik(){
        Skeleton.hiv(this.id + ":Hokotro: dolgozik()");

        if (aktivFej != null) {
            aktivFej.takarit(aktivSav, aktivUt);
        }else {
            Skeleton.naploz("A hókotrón nincs kotrófej, nem tud takarítani.");
        }

        Skeleton.visszater("dolgozik");
    }

    
}
