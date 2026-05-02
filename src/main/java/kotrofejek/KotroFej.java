package kotrofejek;
import funkcionalisElemek.Sav;
import funkcionalisElemek.Ut;



public abstract class KotroFej {
 
    /**
     * Elvégzi a kotrófejre jellemző takarítási műveletet a megadott sávon.
     *
     * @param sav A takarítandó {@link Sav}.
     * @param ut  Az a {@link Ut}, amelyhez a sáv tartozik.
     * @return {@code true}, ha a takarítás eredményes volt és a sáv állapota javult,
     *         {@code false}, ha nem történt változás (pl. üres tartály).
     */
    public abstract boolean takarit(Sav sav, Ut ut);

    public void kiirKeszlet(java.io.PrintStream kimenet) {
        kimenet.println("- Biokerozin: 0");
        kimenet.println("- So: 0");
        kimenet.println("- Zuzalek: 0");
    }
}
