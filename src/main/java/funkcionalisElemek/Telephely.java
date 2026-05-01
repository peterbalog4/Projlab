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
    static private final int soprofej = 120000;
    static private final int jegtorofej = 250000;
    static private final int hanyofej = 200000;
    static private final int sarkanyfej = 600000;
    static private final int soszorofej = 400000;
    static private final int zuzalekszorofej = 400000;
    static private final int biokerozin = 5000;
    static private final int so = 8000;
    static private final int zuzalek = 5000;
    static private final int hokotro = 2000000;

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
        switch (item) {
            case "soprofej":
                if (JMF >= soprofej) {
                    t.JMFmodosit(-soprofej);
                    t.tarol(new kotrofejek.SoproFej());
                    return true;
                }
                break;
            case "jegtorofej":
                if (JMF >= jegtorofej) {
                    t.JMFmodosit(-jegtorofej);
                    t.tarol(new kotrofejek.JegToroFej());
                    return true;
                }
                break;
            case "hanyofej":
                if (JMF >= hanyofej) {
                    t.JMFmodosit(-hanyofej);
                    t.tarol(new kotrofejek.HanyoFej());
                    return true;
                }
                break;
            case "sarkanyfej":
                if (JMF >= sarkanyfej) {
                    t.JMFmodosit(-sarkanyfej);
                    t.tarol(new kotrofejek.SarkanyFej());
                    return true;
                }
                break;
            case "soszorofej":
                if (JMF >= soszorofej) {
                    t.JMFmodosit(-soszorofej);
                    t.tarol(new kotrofejek.SoSzorFej());
                    return true;
                }
                break;
            case "zuzalekszorofej":
                if (JMF >= zuzalekszorofej) {
                    t.JMFmodosit(-zuzalekszorofej);
                    t.tarol(new kotrofejek.ZuzalekSzorFej());
                    return true;
                }
                break;
            case "biokerozin":
                if (JMF >= biokerozin) {
                    t.JMFmodosit(-biokerozin);
                }
                break;
            case "so":
                if (JMF >= so) {
                    t.JMFmodosit(-so);
                }
                break;
            case "zuzalek":
                if (JMF >= zuzalek) {
                    t.JMFmodosit(-zuzalek);
                }
                break;
            case "hokotro": //TODO: ez új object lesz main-ben
                if (JMF >= hokotro) {
                    t.JMFmodosit(-hokotro);
                }
                break;
        }
        return false;
    }
        
    }
    public void JMFmodosit(int count){
       JMF += count;
    }
}
