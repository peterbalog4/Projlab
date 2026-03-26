import java.util.Scanner;

public class Skeleton {

    private static int behuzasSzintje = 0;
    private static Scanner scanner = new Scanner(System.in);

    /**
     * Segédfüggvény a megfelelő mennyiségű tabulátor kiírásához.
     */
    private static void behuzasKiiras(){
        for (int i = 0; i < behuzasSzintje; i++){
            System.out.print("\t");
        }
    }

    /**
     * Növeli a behúzás mértékét és kiírja a függvényhívást.
     * Elvárt formátum bemenetként pl.: "a:Auto: kozlekedik()"
     */
    public static void hiv(String uzenet){
        behuzasKiiras();
        System.out.println("-> " + uzenet);
        behuzasSzintje++;
    }

    /**
     * Kiírja a visszatérést és csökkenti a behúzást.
     * Visszatérési érték nélküli metódusokhoz.
     */
    public static void visszater(String metodusNev, String visszateresiErtek){
        behuzasSzintje--;
        behuzasKiiras();
        System.out.println("<- " + metodusNev + " (" + visszateresiErtek + ")");
    }

    /**
     * Általános informatív üzenet kiírása az aktuális szinten.
     */
    public static void naploz(String uzenet){
        behuzasKiiras();
        System.out.println(uzenet);
    }

    /**
     * A beolvasást és a válasz validálását kezeli.
     * Igazat ad vissza, ha a válasz 'y' vagy 'igen', hamisat, ha 'n' vagy 'nem'.
     */
    public static boolean kerdez(String kerdes){
        while (true){
            behuzasKiiras();
            System.out.print("? " + kerdes + " (y/n): ");
            String valasz = scanner.nextLine().trim().toLowerCase();

            if (valasz.equals("y") || valasz.equals("yes") || valasz.equals("igen")) {
                return true;
            } else if (valasz.equals("n") || valasz.equals("no") || valasz.equals("nem")) {
                return false;
            } else {
                behuzasKiiras();
                System.out.println("Érvénytelen válasz! Kérlek, 'y' vagy 'n' betűt adj meg.");
            }
        }
    }
}
