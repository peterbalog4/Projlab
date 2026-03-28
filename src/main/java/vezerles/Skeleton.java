package vezerles;

import java.util.Scanner;


/**
 * A Skeleton osztály felelős a rendszer tesztelése során a naplózási tevékenység 
 * elvégzéséért és a felhasználói interakciók kezeléséért.
 * Ez az osztály valósítja meg a terminálon az esetek közötti navigálást, 
 * a behúzások kezelését a hívási szintek jelzésére, valamint a tesztelőtől 
 * érkező igen/nem válaszok beolvasását.
 */

public class Skeleton {

    /** Az aktuális hívási mélységet tárolja a tabulátoros behúzáshoz. */
    private static int behuzasSzintje = 0;
    /** A bemeneti válaszok olvasására szolgáló Scanner objektum. */
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
     * Visszatérési érték nélküli (void) metódusokhoz.
     */
    public static void visszater(String metodusNev) {
        behuzasSzintje--;
        behuzasKiiras();
        System.out.println("<- " + metodusNev);
    }
    /**
     * Kiírja a visszatérést és csökkenti a behúzást.
     * Visszatérési értékkel (pl. boolean, int) rendelkező metódusokhoz.
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

    /**
     * Választós kérdés, ahol a menüpontokat számokkal lehet kiválasztani.
     */
    public static int kerdezOpcio(String kerdes, int maxOpcio) {
        while (true) {
            behuzasKiiras();
            System.out.print("? " + kerdes + " (1-" + maxOpcio + "): ");
            try {
                int valasz = Integer.parseInt(scanner.nextLine().trim());
                if (valasz >= 1 && valasz <= maxOpcio) {
                    return valasz;
                }
            } catch (NumberFormatException e) {
                // Nem számot adott meg
            }
            behuzasKiiras();
            System.out.println("Érvénytelen válasz! Adj meg egy számot 1 és " + maxOpcio + " között.");
        }
    }
}
