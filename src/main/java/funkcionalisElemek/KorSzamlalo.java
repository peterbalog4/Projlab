package funkcionalisElemek;

import grafika.AbstractObservable;
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
public class KorSzamlalo extends AbstractObservable {

    /**
     * A játék körlimitje. Ennyi kör (1..MAX_KOR) játszható le; a MAX_KOR. kör után
     * a játéknak vége. A GUI ezt használja a „KÖR: x / MAX_KOR" feliratban és a
     * játék végének eldöntéséhez.
     */
    public static final int MAX_KOR = 50;

    /**
     * A játék kezdete óta eltelt körök száma.
     */
    private int kor = 0;

    /**
     * A pályán található összes jármű listája.
     */
    private final List<Jarmu> jarmuvek = new ArrayList<>();

    /**
     * A pályán található összes út listája.
     * A hóesés szimulálásához szükséges.
     */
    private final List<Ut> utak = new ArrayList<>();

    /**
     * A pályán található összes sáv listája.
     * Az állapotfrissítéshez szükséges.
     */
    private List<Sav> savok = new ArrayList<>();

    /**
     * A pályán található összes „kereszteződés-tile" (kanyar + T- és 4-ágú
     * kereszteződés) listája. Ezekre is hullik hó, ha a havazás aktív.
     */
    private List<Tile> tilek = new ArrayList<>();

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
     * Új kereszteződés-tile-t ad a szimulációhoz. A havazási kör során minden
     * tile-on 1/10 eséllyel hullik hó, ugyanúgy, mint a sávokon.
     */
    public void addTile(Tile t) {
        tilek.add(t);
    }

    /** A pályán nyilvántartott tile-ok listája (csak olvasásra). */
    public List<Tile> getTilek() {
        return tilek;
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
     * Visszaállítja a körszámlálót egy új játékhoz: nullázza a körszámot, és kiüríti
     * a nyilvántartott járműveket, utakat és sávokat, hogy a főmenüből indított új
     * játék tiszta lappal kezdjen (ne maradjon benne az előző játék pályája/állapota).
     * A megfigyelőket (View-k) nem érinti.
     */
    public void reset() {
        kor = 0;
        jarmuvek.clear();
        utak.clear();
        savok.clear();
        tilek.clear();
        hoesik = false;
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
     * Visszaadja a járművek listáját.
     *
     * @return A járművek listája.
     */
    public List<Jarmu> getJarmuvek() {
        return jarmuvek;
    }

    /**
     * Visszaadja a szimulációban lévő sávok listáját.
     */
    public List<Sav> getSavok() {
        return savok;
    }

    /**
     * Visszaadja a szimulációban lévő utak listáját.
     */
    public List<Ut> getUtak() {
        return utak;
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
        tilek.forEach(Tile::havazas);
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
    //3. PUSH FÁZIS: Miután minden állapot megváltozott, értesítjük a feliratkozott View-kat (pl. JatekAblak)
    notifyObservers();
    }
}