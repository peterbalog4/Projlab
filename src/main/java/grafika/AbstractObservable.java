package grafika;

import java.util.ArrayList;
import java.util.List;

/**
 * Absztrakt ősosztály a megfigyelhető modell objektumok számára.
 * Megvalósítja az Observable interfészt, kezeli a feliratkozott megfigyelők listáját,
 * és megírja a nyilvántartáshoz és értesítéshez szükséges logikát.
 */
public abstract class AbstractObservable implements Observable {

    // A feliratkozott megfigyelők (View-k) listája
    private List<Observer> observers = new ArrayList<>();

    @Override
    public void addObserver(Observer o) {
        if (o != null && !observers.contains(o)) {
            observers.add(o);
        }
    }

    @Override
    public void removeObserver(Observer o) {
        observers.remove(o);
    }

    @Override
    public void notifyObservers() {
        // Egy másolaton iterálunk végig a szálbiztonság és
        // a ConcurrentModificationException elkerülése érdekében.
        List<Observer> copy = new ArrayList<>(observers);
        for (Observer o : copy) {
            o.update();
        }
    }
}