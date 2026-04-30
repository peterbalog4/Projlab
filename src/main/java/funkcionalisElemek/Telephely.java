package funkcionalisElemek;
import kotrofejek.KotroFej;
import jarmuvek.Hokotro;



/**
 * A játékos bázisát és logisztikai központját reprezentáló osztály.
 * Felelős a Jégmentesítési Forint (JMF) egyenleg kezeléséért, a raktározott 
 * kotrófejek nyilvántartásáért, valamint a vásárlási és felszerelési folyamatok 
 * koordinálásáért
 */
public class Telephely {
    private final int jatekosID;
    private int biokerozin = 0;
    private int so = 0;
    private int zuzalek = 0;
    private int JMF = 0;
    private List<KotroFej> kotroFejek = new ArrayList<>();
    

    public void useFej(KotroFej ujFej, Hokotro h) { //kinda nem kell a hokotro
        kotroFejek.remove(ujFej);
    }
    public void tarol(Kotrofej fej){
        kotroFejek.add(fej);
    }
    public boolean vasarol(String item){
        //TODO: milyen boltot hív meg? hol van példányosítva, ha mindenkinek ugyanaz?
        
    }
    public void JMFmodosit(int count){
       JMF += count;
    }
}
