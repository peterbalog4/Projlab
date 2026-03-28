package funkcionalisElemek;
import kotrofejek.KotroFej;
import vezerles.Skeleton;
import jarmuvek.Hokotro;

public class Telephely {

    public void useFej(KotroFej fej, Hokotro h){
        Skeleton.hiv("t:Telephely: useFej(fej, h)");

        boolean vanIlyen = Skeleton.kerdez("Volt ilyen fej a Telephelyen?");

        if (vanIlyen) {
            h.fejcsere(fej);
            this.tarolFej(null); 
        } else {
            Skeleton.naploz("Nem cserélte le, mert nem volt ilyen fej a Telephelyen.");
        }

        Skeleton.visszater("useFej");
    }

    public void tarolFej(KotroFej eddigiFej){
        Skeleton.hiv("t:Telephely: tarol(eddigiFej)");
        Skeleton.naploz("A hókotró eddigi feje bekerült a Telephely raktárába.");
        Skeleton.visszater("tarol");
    }   

   public void vasarol(String item) {
        Skeleton.hiv("t:Telephely: vasarol(" + item + ")");
        
        Bolt b = new Bolt();
        
        b.vasarol(this, item);
        
        Skeleton.visszater("vasarol");
    }

    public void JMFmodosit(int count) throws Exception{
        Skeleton.hiv("t:Telephely: JMFmodosit(" + count + ")");
        if (count < 0) {
            boolean vanEleg = Skeleton.kerdez("Van elég JMF a telephelyen a tranzakcióhoz?");
            
            if (!vanEleg) {
                Skeleton.naploz("Nincs elég JMF a tranzakcióhoz, kivétel dobása.");
                Skeleton.visszater("JMFmodosit");
                throw new Exception("Nincs eleg JMF!"); 
            } else {
                Skeleton.naploz("A levonás sikeres, az egyenleg csökkent.");
            }
        } else {
            Skeleton.naploz("A hozzáadás sikeres, az egyenleg nőtt.");
        }

        Skeleton.visszater("JMFmodosit");
    }


}
