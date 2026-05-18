package grafika;

/**
 * Ez az interfész definiálja a megfigyelő (nézet) osztályok számára az általános frissítési metódust.
 */
public interface Observer {
    /**
     * Ezt a metódust hívja meg az Observable objektum, ha az állapota megváltozott.
     */
    void update();
}