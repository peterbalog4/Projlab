package kotrofejek;
import funkcionalisElemek.Sav;
import funkcionalisElemek.Ut;
import vezerles.Skeleton;

/**
 * A hókotrókra felszerelhető munkaeszközök absztrakt alaposztálya. 
 * Meghatározza a takarítási folyamat közös interfészét, amelyet minden 
 * specifikus kotrófej típusnak (pl. Söprő, Hányó, Jégtörő) meg kell valósítania 
 * a saját takarítási logikája szerint. 
 */
public abstract class KotroFej{

    /**
     * Absztrakt metódus a sávon végzett takarítási munka elvégzésére. 
     * A konkrét leszármazottak ebben a metódusban hívják meg a sáv hó- vagy 
     * jégmentesítő funkcióit, illetve az út hóátadási logikáját.
     * * @param sav Az aktuálisan takarított Sáv objektum.
     * @param ut Az Út objektum, amelyen a takarítás és a hó mozgatása zajlik.
     */
    public abstract void takarit(Sav sav, Ut ut);
}
