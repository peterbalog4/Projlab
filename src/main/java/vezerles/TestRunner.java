package vezerles;

import java.io.*;
import java.nio.file.*;
import java.util.*;

/**
 * A tesztesetek automatikus végrehajtásáért felelős osztály.
 *
 * Beolvassa a {@code tests/} mappában található {@code Test<n>.txt} bemeneti
 * fájlokat, soronként átadja őket a {@link Commander}-nek, majd a szimulációs
 * kimenetet egy {@code Test<n>_out.txt} fájlba menti.
 * Futtatható egyetlen teszteset számával, vagy {@code null}-lal az összes futtatásához.
 */
public class TestRunner {

    /** A tesztfájlok könyvtárának elérési útja. */
    private static final String TESTS_DIR = "tests/";

    /** A bemeneti fájlok utótagja. */
    private static final String IN_SUFFIX  = ".txt";

    /** A kimeneti fájlok utótagja. */
    private static final String OUT_SUFFIX = "_out.txt";

    /**
     * Futtatja a megadott nevű tesztesetet, vagy ha {@code null}-t kap,
     * az összes {@code Test<n>.txt} fájlt a {@code tests/} mappában.
     *
     * @param tesztNev A futtatandó teszteset neve (pl. {@code "Test1"}),
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
     * Megkeresi és sorban lefuttatja az összes {@code Test<n>.txt} fájlt.
     */
    private void osszesFuttat() {
        File dir = new File(TESTS_DIR);
        if (!dir.exists() || !dir.isDirectory()) {
            System.err.println("ERROR: A tests/ mappa nem letezik.");
            return;
        }

        File[] bemenetek = dir.listFiles((d, name) ->
                name.matches("Test\\d+\\.txt"));

        if (bemenetek == null || bemenetek.length == 0) {
            System.out.println("Nem talalhato tesztfajl a tests/ mappaban.");
            return;
        }

        Arrays.sort(bemenetek, Comparator.comparingInt(f -> {
            String num = f.getName().replaceAll("\\D", "");
            return num.isEmpty() ? 0 : Integer.parseInt(num);
        }));

        for (File f : bemenetek) {
            String nev = f.getName().replace(IN_SUFFIX, "");
            futtatEgyet(nev);
        }
    }

    /**
     * Lefuttat egyetlen tesztesetet.
     *
     * Beolvassa a {@code tests/<nev>.txt} fájlt, végrehajtja a parancsokat
     * a {@link Commander} segítségével, majd a kimenetet a
     * {@code tests/<nev>_out.txt} fájlba menti.
     *
     * @param nev A teszteset neve kiterjesztés nélkül (pl. {@code "Test1"}).
     */
    public void futtatEgyet(String nev) {
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
                new Commander(ps).feldolgoz(sorok);
            }

            System.out.println("Lefutott: " + nev + " -> " + kimeneti);

        } catch (IOException e) {
            System.err.println("ERROR: IO hiba a(z) " + nev + " tesztnel: " + e.getMessage());
        }
    }
}