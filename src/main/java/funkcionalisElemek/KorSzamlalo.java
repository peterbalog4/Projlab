package funkcionalisElemek;

import jarmuvek.Hokotro;
import jarmuvek.Jarmu;

import java.util.ArrayList;
import java.util.List;

/**
 * A játék belső időzítéséért és a körök léptetéséért felelős osztály.
 *
 * Nyilvántartja a pályán lévő összes járművet, sávot és utat.
 * Minden körben elvégzi a következő lépéseket sorban:
 * a hóesés szimulálása az utakon, a sávok belső állapotának frissítése,
 * végül az összes jármű mozgatása.
 */
public class KorSzamlalo {

    /**
     * A játék kezdete óta eltelt körök száma.
     */
    private int kor = 0;

    /**
     * A pályán található összes jármű listája.
     */
    private List<Jarmu> jarmuvek = new ArrayList<>();

    /**
     * A pályán található összes út listája.
     * A hóesés szimulálásához szükséges.
     */
    private List<Ut> utak = new ArrayList<>();

    /**
     * A pályán található összes sáv listája.
     * Az állapotfrissítéshez szükséges.
     */
    private List<Sav> savok = new ArrayList<>();

    /**
     * Jelzi, hogy ebben a körben esik-e hó.
     */
    private boolean hoesik = false;

    /**
     * Új járművet ad a szimulációhoz.
     *
     * @param j A hozzáadni kívánt {@link Jarmu} objektum.
     */
    public void addJarmu(Jarmu j) {
        jarmuvek.add(j);
    }

    /**
     * Eltávolít egy járművet a szimulációból.
     *
     * @param j Az eltávolítandó {@link Jarmu} objektum.
     */
    public void removeJarmu(Jarmu j) {
        jarmuvek.remove(j);
    }

    /**
     * Új utat ad a szimulációhoz.
     *
     * @param u A hozzáadni kívánt {@link Ut} objektum.
     */
    public void addUt(Ut u) {
        utak.add(u);
    }

    /**
     * Új sávot ad a szimulációhoz.
     *
     * @param s A hozzáadni kívánt {@link Sav} objektum.
     */
    public void addSav(Sav s) {
        savok.add(s);
    }

    /**
     * Beállítja, hogy a következő körben esik-e hó.
     *
     * @param hoesik {@code true}, ha hóesés lesz, {@code false} egyébként.
     */
    public void setHoesik(boolean hoesik) {
        this.hoesik = hoesik;
    }

    /**
     * Visszaadja az eltelt körök számát.
     *
     * @return Az eddigi körök száma.
     */
    public int getKor() {
        return kor;
    }

    /**
     * Végrehajtja a kör léptetését.
     *
     * A következő sorrendben hajtja végre a műveleteket:
     * először növeli a körszámlálót, majd ha esik a hó, minden úton
     * meghívja a hónovelést, ezután frissíti minden sáv belső állapotát
     * (jégképződés, só elolvadás, lezárás lejárta), végül mozgatja
     * az összes mozgásképes járművet.
     */
public void leptet() {
    kor++;
    if (hoesik) {
        utak.forEach(Ut::hoNovel);
    }
    savok.forEach(Sav::allapotFrissit);

    // 1. TDA: A hókotrók lépnek és takarítanak először
    jarmuvek.stream()
            .filter(j -> j instanceof Hokotro)
            .forEach(Jarmu::kozlekedik);
            
    // 2. TDA: Utána léphet mindenki más
    jarmuvek.stream()
            .filter(j -> !(j instanceof Hokotro))
            .forEach(Jarmu::kozlekedik);
}
}