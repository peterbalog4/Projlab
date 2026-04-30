package funkcionalisElemek;



/**
 * A játékban található Boltot reprezentáló osztály.
 * Felelőssége a különböző tárgyak (pl. kotrófejek, üzemanyag) értékesítése
 * a Takarító Játékos számára a Telephelyen keresztül.
 */

public class Bolt {

    private final int soprofej;
    private final int jegtorofej;
    private final int hanyofej;
    private final int sarkanyfej;
    private final int soszorofej;
    private final int zuzalekszorofej;
    private final int biokerozin;
    private final int so;
    private final int zuzalek;
    private final int hokotro;


    public boolean vasarol(Telephely t, String item, int JMF) {
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
            case "hokotro": //TODO: ez hova fog menni? xdddd
                if (JMF >= hokotro) {
                    t.JMFmodosit(-hokotro);
                }
                break;
        }
        return false;
    }
}