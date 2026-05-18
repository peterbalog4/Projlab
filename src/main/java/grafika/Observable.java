package grafika;

/**
 * Ezt az interfészt valósítják meg a megfigyelt modell objektumok.
 * Felelős a feliratkozott megfigyelők nyilvántartásáért és értesítéséért (push).
 */
public interface Observable {

    /**
     * Hozzáad egy új megfigyelőt a nyilvántartáshoz.
     * @param o A hozzáadandó megfigyelő (nézet).
     */
    void addObserver(Observer o);

    /**
     * Eltávolít egy meglévő megfigyelőt a nyilvántartásból.
     * @param o Az eltávolítandó megfigyelő (nézet).
     */
    void removeObserver(Observer o);

    /**
     * Végigiterál a regisztrált megfigyelőkön, és meghívja az update() metódusukat.
     */
    void notifyObservers();
}