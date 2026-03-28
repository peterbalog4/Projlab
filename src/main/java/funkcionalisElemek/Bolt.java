package funkcionalisElemek;
import vezerles.Skeleton;


/**
 * A játékban található Boltot reprezentáló osztály.
 * Felelőssége a különböző tárgyak (pl. kotrófejek, üzemanyag) értékesítése
 * a Takarító Játékos számára a Telephelyen keresztül.
 */

public class Bolt {

    /**
     * Tranzakciót indít egy adott tárgy megvásárlására.
     * A folyamat során a Bolt megpróbálja levonni a tárgy árát a megadott 
     * Telephely egyenlegéből.
     * * @param t Az a Telephely, amely a vásárlást kezdeményezi és amelynek az egyenlegét terhelik.
     * @param item A megvásárolni kívánt tárgy vagy eszköz neve.
     */
    public void vasarol(Telephely t, String item) {
        Skeleton.hiv("b:Bolt: vasarol(telep, item)");
        

        t.JMFmodosit(-1000);
        
        if (Skeleton.kerdez("Sikeres volt a fizetés?")) {
            Skeleton.naploz("A megvásárolt " + item + " a Telephely raktárába került. ");
        }
        
        Skeleton.visszater("vasarol");
    }
}