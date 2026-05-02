package jarmuvek;

import funkcionalisElemek.Sav;
import funkcionalisElemek.Telephely;
import funkcionalisElemek.Ut;
import kotrofejek.KotroFej;

/**
 * A hókotró járművet reprezentáló osztály, amely a Jármű alaposztályból származik.
 *
 * Felelőssége az úttakarítási feladatok elvégzése a rászerelt kotrófej működtetésével,
 * az útviszonyok javítása, valamint a különböző típusú kotrófejek fogadása és cseréje.
 * A hókotró nem csúszik jégen, és ütközés sem hat rá – lánctalpas kialakításának
 * köszönhetően mindkét hatást figyelmen kívül hagyja.
 * Sikeres takarítás esetén a tulajdonos telephelyének JMF egyenlege növekszik.
 */
public class Hokotro extends Jarmu {

    /**
     * A hókotrót irányító játékos azonosítója.
     */
    private final int tulajdonos;

    /**
     * A hókotróra aktuálisan felszerelt munkaeszköz.
     * {@code null}, ha nincs fej felszerelve.
     */
    private KotroFej aktivFej;

    /**
     * A játékos telephelye, amelynek JMF egyenlege a takarítás jutalmát kapja.
     */
    private Telephely telephely;

    /**
     * A játékos által megadott következő út, amelyre a hókotró kanyarodni fog.
     * {@code null}, ha nincs előre megadott irány.
     */
    private Ut kovetkezoUt;

    /**
     * Konstruktor a Hokotro osztályhoz.
     *
     * @param id         A hókotró egyedi azonosítója a naplózáshoz.
     * @param tulajdonos A hókotrót irányító játékos azonosítója.
     * @param telephely  A játékos telephelye, ahova a megszerzett JMF kerül.
     */
    public Hokotro(String id, int tulajdonos, Telephely telephely) {
        super(id);
        this.tulajdonos = tulajdonos;
        this.telephely = telephely;
        this.kovetkezoUt = null;
    }

    /**
     * Beállítja a következő utat, amelyre a hókotró kanyarodni fog a következő
     * kereszteződésben. A játékos adja meg a {@code move} paranccsal.
     *
     * @param ut A kívánt következő {@link Ut}.
     */
    public void setKovetkezoUt(Ut ut) {
        this.kovetkezoUt = ut;
    }

    /**
     * A hókotró haladását megvalósító metódus, amelyet a KörSzámláló hív meg minden körben.
     *
     * Mozgás közben automatikusan elvégzi a takarítást ({@link #dolgozik}).
     * Ha a sáv végére ér és van megadott következő út, arra kanyarodik.
     * Ha nincs megadott irány, egy kört vár a kereszteződésben.
     */
    @Override
    public void kozlekedik() {
        if (varakozasiIdo > 0) {
            varakozasiIdo--;
            if (varakozasiIdo == 0) {
                allapot = Allapot.KOZLEKEDIK;
            }
            return;
        }

        if (aktualisSav == null) return;

        dolgozik();

        if (savVegenVan()) {
            if (kovetkezoUt != null) {
                Ut cel = kovetkezoUt;
                kovetkezoUt = null;
                kanyarodik(cel);
            } else {
                megall(1);
            }
        }
    }

    /**
     * Megvizsgálja, hogy a hókotró elérte-e az aktuális sáv végét.
     *
     * @return {@code true}, ha a sáv végén van, {@code false} egyébként.
     */
    private boolean savVegenVan() {
        return aktualisSav.getMasikJarmu(this) == null;
    }

    /**
     * Végrehajtja a kotrófej fizikai cseréjét a járművön.
     *
     * A régi fejet visszaadja a telephelynek ({@link Telephely#tarol}),
     * majd az újat felszereli. Ha nincs régi fej, csak az újat szereli fel.
     *
     * @param ujFej Az újonnan felszerelendő {@link KotroFej} objektum.
     */
    public void fejcsere(KotroFej ujFej) {
        if (this.aktivFej != null) {
            telephely.tarol(this.aktivFej);
        }
        this.aktivFej = ujFej;
    }

    /**
     * Kezeli az ütközést más járművekkel.
     *
     * A hókotró lánctalpas kialakítása miatt az ütközés nem hat rá,
     * ezért ez a metódus szándékosan üres.
     *
     * @param masikJarmu A jármű, amellyel a hókotró ütközött.
     */
    @Override
    public void utkozik(Jarmu masikJarmu) {
        // A hókotróra nem hat az ütközés, szándékosan üres.
    }

    /**
     * Kezdeményezi az úttakarítási folyamatot az aktuális sávon.
     *
     * Ha van felszerelt kotrófej, meghívja annak {@link KotroFej#takarit} metódusát.
     * Ha a takarítás eredményes volt (a sáv hószintje csökkent vagy a jég eltűnt),
     * a tulajdonos telephelyének JMF egyenlege 10 000-rel nő.
     */
        /**
     * Kezdeményezi az úttakarítási folyamatot az aktuális sávon.
     *
     * Ha van felszerelt kotrófej, meghívja annak {@link KotroFej#takarit} metódusát.
     * Ha a kotrófej jelzi, hogy eredményes volt a takarítás, a tulajdonos
     * telephelyének JMF egyenlege 10 000-rel nő.
     */
    public void dolgozik() {
        if (aktivFej != null && aktualisSav != null) {
            boolean tisztabb = aktivFej.takarit(aktualisSav, aktualisSav.getUt());
            if (tisztabb) {
                telephely.JMFmodosit(10000);
            }
        }
    }

    /**
     * Visszaadja a hókotróra felszerelt aktív kotrófejet.
     *
     * @return Az aktuális {@link KotroFej}, vagy {@code null}, ha nincs fej felszerelve.
     */
    public KotroFej getAktivFej() {
        return aktivFej;
    }
}