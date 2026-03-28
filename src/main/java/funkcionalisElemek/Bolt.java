package funkcionalisElemek;
import vezerles.Skeleton;

public class Bolt {

    public void vasarol(Telephely t, String item) {
        Skeleton.hiv("b:Bolt: vasarol(telep, item)");
        

        t.JMFmodosit(-1000);
        
        if (Skeleton.kerdez("Sikeres volt a fizetés?")) {
            Skeleton.naploz("A megvásárolt " + item + " a Telephely raktárába került. ");
        }
        
        Skeleton.visszater("vasarol");
    }
}