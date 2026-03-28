package funkcionalisElemek;
import vezerles.Skeleton;

public class Bolt {

    public void vasarol(Telephely t, String item) {
        Skeleton.hiv("b:Bolt: vasarol(t, " + item + ")");

        int ar = -50; 
        
        try {
            t.JMFmodosit(ar);
            
            Skeleton.naploz("A bolt sikeresen eladta a terméket: " + item);
            
        } catch (Exception e) {
            Skeleton.naploz("A vásárlás meghiúsult: " + e.getMessage());
        }

        Skeleton.visszater("vasarol");
    }
}