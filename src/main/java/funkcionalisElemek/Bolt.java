package funkcionalisElemek;
import vezerles.Skeleton;

public class Bolt {

    public void vasarol(Telephely t, String item) {
<<<<<<< HEAD
        Skeleton.hiv("b:Bolt: vasarol(telep, item)");
        

        t.JMFmodosit(-1000);
        
        if (Skeleton.kerdez("Sikeres volt a fizetés?")) {
            Skeleton.naploz("A megvásárolt " + item + " a Telephely raktárába került. ");
        }
        
        Skeleton.visszater("vasarol");
    }
=======
        Skeleton.hiv("b:Bolt: vasarol(t, " + item + ")");
>>>>>>> a2eda4a534e76a41f32e3aec577c862fbb6e3a66

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