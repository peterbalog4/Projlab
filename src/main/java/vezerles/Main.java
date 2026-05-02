package vezerles;

import java.io.*;
import java.nio.file.*;
import java.util.*;

/**
 * A prototípus belépési pontja.
 *
 * Interaktív módban megjeleníti a 22 tesztesetet számozva, és szám alapján
 * lehetővé teszi azok futtatását. A tesztfájlok neve {@code Test<n>.txt},
 * a menüben megjelenő nevek a részletes tervek szerintiek.
 * Parancssori argumentummal közvetlenül is megadható egy bemeneti szkript fájl.
 */
public class Main {

    /**
     * A tesztesetek dokumentációbeli nevei sorban.
     * Az i-edik elem a {@code Test<i+1>.txt} fájlhoz tartozik.
     */
    private static final String[] TESZTESETEK = {
        "Autó elakadása a hóban",
        "Busz elakadása a hóban",
        "Autó ütközése autóval jégen",
        "Hókotró takarít Söprő Fejjel széles úton",
        "Hókotró takarít Söprő fejjel",
        "Hókotró takarít Hányó fejjel széles úton",
        "Hókotró takarít Hányó fejjel keskeny úton",
        "Hókotró takarít Jégtörő fejjel",
        "Hókotró takarít Sárkány fejjel",
        "Hókotró takarít Sószóró fejjel",
        "Hókotró takarít Zúzalékszóró fejjel",
        "Sárkány fejből kifogy a biokerozin",
        "A sáv eljegesedése",
        "Kotrófej cseréje",
        "Kotrófej cseréje sikertelen",
        "Vásárlás a Boltban",
        "Vásárlás meghiúsul",
        "Sárkány fej újratöltése",
        "Sárkány fej újratöltésének meghiúsulása",
        "Autó Sávot vált",
        "JMF növelése a Telephelyen",
        "Busz forduló teljesítése",
    };

    /**
     * A program belépési pontja.
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
     * Beolvas és végrehajt egy bemeneti szkript fájlt, a kimenetet
     * a szabványos kimenetre írja.
     *
     * @param fajlUt A végrehajtandó szkript elérési útja.
     */
    private static void futtasFajlbol(String fajlUt) {
        try {
            List<String> sorok = Files.readAllLines(Path.of(fajlUt));
            new Commander(System.out).feldolgoz(sorok);
        } catch (IOException e) {
            System.err.println("ERROR: Nem sikerult beolvasni: " + fajlUt);
        }
    }

    /**
     * Interaktív menü, amellyel a tesztelő szám alapján választhat tesztesetet.
     */
    private static void interaktivMod() {
        Scanner sc            = new Scanner(System.in);
        TestRunner runner     = new TestRunner();
        OutputComparator komp = new OutputComparator();

        while (true) {
            System.out.println("\n=== Bizz bennem, mernok leszek – Prototipus ===");
            System.out.println("Tesztesetek:");
            System.out.println();
            for (int i = 0; i < TESZTESETEK.length; i++) {
                System.out.printf("  %2d. %s%n", i + 1, TESZTESETEK[i]);
            }
            System.out.println();
            System.out.println("   0. Osszes teszteset futtatasa es osszehasonlitasa");
            System.out.println("  iq. Interaktiv parancssor");
            System.out.println("   k. Kilepes");
            System.out.print("\nValasztas: ");

            String v = sc.nextLine().trim().toLowerCase();

            if (v.equals("k")) {
                System.out.println("Viszlat!");
                break;
            }

            if (v.equals("iq")) {
                interaktivParancssor(sc);
                continue;
            }

            if (v.equals("0")) {
                runner.futtat(null);
                komp.hasonlitOsszes();
                continue;
            }

            try {
                int valasztas = Integer.parseInt(v);
                if (valasztas < 1 || valasztas > TESZTESETEK.length) {
                    System.out.println("Ervenytelen szam. Adj meg 1 es "
                            + TESZTESETEK.length + " kozotti erteket.");
                    continue;
                }
                String tesztNev = "Test" + valasztas;
                System.out.println("\nFuttatás: " + TESZTESETEK[valasztas - 1]);
                runner.futtat(tesztNev);
                komp.hasonlit(tesztNev);
            } catch (NumberFormatException e) {
                System.out.println("Ervenytelen bemenet.");
            }
        }

        sc.close();
    }

    /**
     * Interaktív parancssori mód, ahol a felhasználó közvetlenül gépelhet
     * bemeneti parancsokat és azonnal látja a kimenetet.
     *
     * @param sc A bemeneti Scanner.
     */
    private static void interaktivParancssor(Scanner sc) {
        Commander cmd = new Commander(System.out);
        System.out.println("Interaktiv mod (kilepes: 'exit'):");

        while (true) {
            System.out.print("> ");
            String sor = sc.nextLine().trim();
            if (sor.equalsIgnoreCase("exit")) break;
            cmd.vegrehajt(sor);
        }
    }
}