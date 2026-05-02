package vezerles;

import java.io.*;
import java.nio.file.*;
import java.util.*;

/**
 * A prototípus belépési pontja.
 *
 * Interaktív módban parancssori menüt jelenít meg, amellyel kiválasztható
 * a futtatandó teszteset, az összehasonlítás, a pályagenerálás vagy a
 * naplóelemzés. Parancssori argumentummal közvetlenül is megadható egy
 * bemeneti szkript fájl, amely automatikusan végrehajtódik.
 */
public class Main {

    /**
     * A program fő vezérlő belépési pontja.
     *
     * Ha kap parancssori argumentumot, azt bemeneti fájlként értelmezi
     * és közvetlenül végrehajtja. Egyébként interaktív menüt indít.
     *
     * @param args Opcionálisan egy bemeneti szkript fájl elérési útja.
     */
    public static void main(String[] args) {
        if (args.length > 0) {
            futtasFajlbol(args[0]);
        } else {
            interaktivMod();
        }
    }

    /**
     * Beolvas és végrehajt egy bemeneti szkript fájlt.
     *
     * @param fajlUt A végrehajtandó szkript elérési útja.
     */
    private static void futtasFajlbol(String fajlUt) {
        try {
            List<String> sorok = Files.readAllLines(Path.of(fajlUt));
            Commander ertelmező = new Commander(System.out);
            ertelmező.feldolgoz(sorok);
        } catch (IOException e) {
            System.err.println("ERROR: Nem sikerult beolvasni: " + fajlUt);
        }
    }

    /**
     * Interaktív menü, amellyel a tesztelő kiválaszthatja a kívánt műveletet.
     */
    private static void interaktivMod() {
        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println("\n=== Bizz bennem, mernok leszek – Prototipus ===");
            System.out.println("1. Teszteset futtatasa");
            System.out.println("2. Osszes teszteset futtatasa");
            System.out.println("3. Kimenetek osszehasonlitasa");
            System.out.println("4. Osszes kimenet osszehasonlitasa");
            System.out.println("5. Alap terkep generalasa (test_map.txt)");
            System.out.println("6. Racs terkep generalasa");
            System.out.println("7. Naplo elemzese");
            System.out.println("8. Jarmuvek utvonalanak vizualizalasa");
            System.out.println("9. Interaktiv parancssor");
            System.out.println("0. Kilepes");
            System.out.print("Valasztas: ");

            String valasztas = sc.nextLine().trim();

            switch (valasztas) {
                case "1" -> {
                    System.out.print("Teszteset neve: ");
                    String nev = sc.nextLine().trim();
                    new TestRunner().futtat(nev);
                }
                case "2" -> new TestRunner().futtat(null);
                case "3" -> {
                    System.out.print("Teszteset neve: ");
                    String nev = sc.nextLine().trim();
                    new OutputComparator().hasonlit(nev);
                }
                case "4" -> new OutputComparator().hasonlitOsszes();
                case "5" -> new ScenarioGenerator().generaljAlappálya();
                case "6" -> {
                    System.out.print("Sorok szama: ");
                    int sorok = Integer.parseInt(sc.nextLine().trim());
                    System.out.print("Oszlopok szama: ");
                    int oszlopok = Integer.parseInt(sc.nextLine().trim());
                    System.out.print("Fajlnev (pl. grid.txt): ");
                    String fajl = sc.nextLine().trim();
                    new ScenarioGenerator().generaljRacs(sorok, oszlopok, fajl);
                }
                case "7" -> {
                    System.out.print("Naplo fajlnev: ");
                    String naplo = sc.nextLine().trim();
                    System.out.print("Elvart kimenet fajlnev: ");
                    String elvart = sc.nextLine().trim();
                    new NaploElemzo().elemez(naplo, elvart);
                }
                case "8" -> {
                    System.out.print("Naplo fajlnev: ");
                    String naplo = sc.nextLine().trim();
                    new NaploElemzo().utvonalVizualizal(naplo);
                }
                case "9" -> interaktivParancssor(sc);
                case "0" -> { System.out.println("Viszlat!"); return; }
                default  -> System.out.println("Ervenytelen valasztas.");
            }
        }
    }

    /**
     * Interaktív parancssori mód, ahol a felhasználó közvetlenül gépelhet
     * bemeneti parancsokat és azonnal látja a kimenetet.
     *
     * @param sc A bemeneti Scanner.
     */
    private static void interaktivParancssor(Scanner sc) {
        Commander cmdr = new Commander(System.out);
        System.out.println("Interaktiv mod (kilepes: 'exit'):");

        while (true) {
            System.out.print("> ");
            String sor = sc.nextLine().trim();
            if (sor.equalsIgnoreCase("exit")) break;
            cmdr.vegrehajt(sor);
        }
    }
}