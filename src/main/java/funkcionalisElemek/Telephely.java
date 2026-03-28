package funkcionalisElemek;
import kotrofejek.KotroFej;
import jarmuvek.Hokotro;
import vezerles.Skeleton;

public class Telephely {

    private Bolt bolt = new Bolt();

    public void useFej(KotroFej ujFej, Hokotro h) {
        Skeleton.hiv("telep:Telephely: useFej(fej, kotro)");
        
        boolean vanRaktaron = Skeleton.kerdez("A kért kotrófej raktáron van a Telephelyen?");
        
        if (vanRaktaron) {
            h.fejcsere(ujFej);
            
            this.tarolFej(ujFej); 
        } else {
            Skeleton.naploz("A kért fej nem található a raktárban.");
        }
        
        Skeleton.visszater("useFej");
    }

    public void tarolFej(KotroFej regiFej) {
        Skeleton.hiv("telep:Telephely: tarol(eddigiFej)");
        Skeleton.naploz("A régi kotrófej bekerült a Telephely raktárába.");
        Skeleton.visszater("tarol");
    }

    public void vasarol(String item) {
        Skeleton.hiv("t:Telephely: vasarol(" + item + ")");
        
        bolt.vasarol(this, item);
        
        Skeleton.visszater("vasarol");
    }

    public void JMFmodosit(int count) {
        Skeleton.hiv("t:Telephely: JMFmodosit(" + count + ")");
        
        if (count < 0) {
            if (Skeleton.kerdez("Van elegendő JMF a vásárláshoz?")) {
                Skeleton.naploz("A JMF levonása sikeres.");
            } else {
                Skeleton.naploz("Hiba: Nincs elég JMF, a vásárlás megszakadt.");
            }
        } else {
            Skeleton.naploz("JMF hozzáadva.");
        }
        
        Skeleton.visszater("JMFmodosit");
    }
    
}
