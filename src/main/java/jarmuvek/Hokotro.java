package jarmuvek;
import funkcionalisElemek.Sav;
import funkcionalisElemek.Ut;
import kotrofejek.KotroFej;



/**
 * A hókotró járművet reprezentáló osztály, amely a Jármű alaposztályból származik.
 * Felelőssége az úttakarítási feladatok elvégzése a rászerelt kotrófej működtetésével, 
 * az útviszonyok javítása, valamint a különböző típusú kotrófejek fogadása és cseréje.
 */
public class Hokotro extends Jarmu {

    /** A hókotróra aktuálisan felszerelt munkaeszköz. */
    private KotroFej aktivFej;
    /** Az úthálózat referenciája, amelyen a hókotró a takarítást végzi. */
    private Ut aktivUt;
    /** A sáv amiben a hókotró tartózkodik. */
    private Sav aktivSav;

    /**
     * Konstruktor a Hokotro osztályhoz.
     * @param id A hókotró egyedi azonosítója a naplózáshoz.
     */
    public Hokotro(String id) {
        super(id);
    }

    /**
     * Beállítja vagy lecseréli a hókotrón lévő munkaeszközt.
     * @param k Az új KotroFej objektum.
     */
    public void setKotrofej(KotroFej fej) {
  
    }

    /**
     * Beállítja az utat, amelyen a hókotró dolgozik.
     * @param u Az Út objektum referenciája.
     */
    public void setUt(Ut u) {
  
    }

    public void setSav(Sav s) {
  
    }


    /**
     * A hókotró haladását megvalósító metódus.
     * A hókotró speciális mozgása során automatikusan elvégzi a takarítási 
     * munkát is (dolgozik hívás).
     */
    @Override
    public void kozlekedik() {

    }

    /**
     * Végrehajtja a kotrófej fizikai cseréjét a járművön.
     * A Telephely useFej metódusa hívja meg a "Kotrófej cseréje" folyamat során.
     * @param ujFej Az újonnan felszerelendő KotroFej objektum.
     */
    public void fejcsere(KotroFej ujfej) {
 
    }

    /**
     * Kezeli az ütközést más járművekkel.
     * @param masikJarmu A jármű, amellyel a hókotró ütközött.
     */
    @Override
    public void utkozik(Jarmu masikJarmu){

    }

    /**
     * Kezdeményezi az úttakarítási folyamatot.
     * Meghívja a felszerelt kotrófej takarító metódusát az aktuális sávon és úton. 
     * Ez a metódus valósítja meg a "Hókotró utat takarít" use-case központi lépését.
     */
    public void dolgozik(){

    }

    
}
