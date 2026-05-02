package vezerles;

import java.io.*;
import java.nio.file.*;
import java.util.*;

/**
 * A tesztesetek automatikus végrehajtásáért felelős osztály.
 *
 * Beolvassa a {@code tests/} mappában található {@code _in.txt} bemeneti
 * fájlokat, soronként átadja őket a {@link Commander}-nek, majd
 * a szimulációs kimenetét egy {@code _out.txt} fájlba menti.
 * Futtatható egyetlen teszteset nevével, vagy paraméter nélkül az összes
 * teszteset egymás utáni végrehajtásához.
 */
public class TestRunner {

    /** A tesztfájlok könyvtárának elérési útja. */
    private static final String TESTS_DIR = "tests/";

    /** A bemeneti fájlok utótagja. */
    private static final String IN_SUFFIX  = "_in.txt";

    /** A kimeneti fájlok utótagja. */
    private static final String OUT_SUFFIX = "_out.txt";

    /**
     * Futtatja a megadott nevű tesztesetet, vagy ha {@code null}-t kap,
     * az összes tesztesetet a {@code tests/} mappában.
     *
     * @param tesztNev A futtatandó teszteset neve (kiterjesztés nélkül),
     *                 vagy {@code null} az összes futtatásához.
     */
    public void futtat(String tesztNev) {
        if (tesztNev != null) {
            futtatEgyet(tesztNev);
        } else {
            osszesFuttat();
        }
    }

    /**
     * Megkeresi és sorban lefuttatja az összes tesztesetet a {@code tests/} mappában.
     */
    private void osszesFuttat() {
        File dir = new File(TESTS_DIR);
        if (!dir.exists() || !dir.isDirectory()) {
            System.err.println("ERROR: A tests/ mappa nem letezik.");
            return;
        }

        File[] bemenetek = dir.listFiles((d, name) -> name.endsWith(IN_SUFFIX));
        if (bemenetek == null || bemenetek.length == 0) {
            System.out.println("Nem talalhato teszteset a tests/ mappaban.");
            return;
        }

        Arrays.sort(bemenetek);
        for (File f : bemenetek) {
            String nev = f.getName().replace(IN_SUFFIX, "");
            futtatEgyet(nev);
        }
    }

    /**
     * Lefuttat egyetlen tesztesetet.
     *
     * Beolvassa a {@code tests/<nev>_in.txt} fájlt, végrehajtja a parancsokat
     * a {@link Commander} segítségével, majd a kimenetet a
     * {@code tests/<nev>_out.txt} fájlba menti.
     *
     * @param nev A teszteset neve kiterjesztés nélkül.
     */
    private void futtatEgyet(String nev) {
        String bemeneti = TESTS_DIR + nev + IN_SUFFIX;
        String kimeneti = TESTS_DIR + nev + OUT_SUFFIX;

        File bemeneti_f = new File(bemeneti);
        if (!bemeneti_f.exists()) {
            System.err.println("ERROR: Nem talalhato bemeneti fajl: " + bemeneti);
            return;
        }

        try {
            List<String> sorok = Files.readAllLines(bemeneti_f.toPath());

            try (PrintStream ps = new PrintStream(new FileOutputStream(kimeneti))) {
                Commander cmdr = new Commander(ps);
                cmdr.feldolgoz(sorok);
            }

            System.out.println("Lefutott: " + nev + " -> " + kimeneti);

        } catch (IOException e) {
            System.err.println("ERROR: IO hiba a(z) " + nev + " tesztnel: " + e.getMessage());
        }
    }
}