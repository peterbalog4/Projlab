package vezerles;

import java.io.*;
import java.nio.file.*;
import java.util.*;

/**
 * A tényleges és az elvárt kimenet összehasonlítására szolgáló osztály.
 *
 * Sorról sorra veti össze a {@code Test<n>_out.txt} (tényleges) és az
 * {@code Test<n>_exp.txt} (elvárt) fájlokat, figyelmen kívül hagyva a
 * felesleges szóközöket. Az eltérésekről részletes jelentést készít.
 */
public class OutputComparator {

    /** A tesztfájlok könyvtárának elérési útja. */
    private static final String TESTS_DIR  = "tests/";

    /** A tényleges kimenet utótagja. */
    private static final String OUT_SUFFIX = "_out.txt";

    /** Az elvárt kimenet utótagja. */
    private static final String EXP_SUFFIX = "_exp.txt";

    /**
     * Összehasonlítja a megadott teszteset tényleges és elvárt kimenetét.
     *
     * @param tesztNev A teszteset neve kiterjesztés nélkül (pl. {@code "Test1"}).
     * @return {@code true}, ha a kimenet megegyezik az elvárttal.
     */
    public boolean hasonlit(String tesztNev) {
        String tenylegesUt = TESTS_DIR + tesztNev + OUT_SUFFIX;
        String elvartUt    = TESTS_DIR + tesztNev + EXP_SUFFIX;

        List<String> tenyleges = beolvas(tenylegesUt);
        List<String> elvart    = beolvas(elvartUt);

        if (tenyleges == null) {
            System.out.println("[SKIP] " + tesztNev + " – nincs kimeneti fajl.");
            return false;
        }
        if (elvart == null) {
            System.out.println("[SKIP] " + tesztNev + " – nincs elvart fajl.");
            return false;
        }

        List<String> eltéresek = osszehasonlit(tenyleges, elvart);

        if (eltéresek.isEmpty()) {
            System.out.println("[OK]   " + tesztNev);
            return true;
        } else {
            System.out.println("[FAIL] " + tesztNev);
            eltéresek.forEach(System.out::println);
            return false;
        }
    }

    /**
     * Összehasonlítja az összes {@code Test<n>_exp.txt} fájlhoz tartozó kimenetet.
     *
     * @return Tömb: {@code [osszes, sikeres]}.
     */
    public int[] hasonlitOsszes() {
        File dir = new File(TESTS_DIR);
        if (!dir.exists() || !dir.isDirectory()) {
            System.err.println("ERROR: A tests/ mappa nem letezik.");
            return new int[]{0, 0};
        }

        File[] elvartFajlok = dir.listFiles((d, name) ->
                name.matches("Test\\d+_exp\\.txt"));

        if (elvartFajlok == null || elvartFajlok.length == 0) {
            System.out.println("Nem talalhato elvart kimeneti fajl.");
            return new int[]{0, 0};
        }

        Arrays.sort(elvartFajlok, Comparator.comparingInt(f -> {
            String num = f.getName().replaceAll("\\D", "");
            return num.isEmpty() ? 0 : Integer.parseInt(num);
        }));

        int osszes  = 0;
        int sikeres = 0;

        for (File f : elvartFajlok) {
            String nev = f.getName().replace(EXP_SUFFIX, "");
            osszes++;
            if (hasonlit(nev)) sikeres++;
        }

        System.out.println("\nEredmeny: " + sikeres + "/" + osszes + " teszt sikeres.");
        return new int[]{osszes, sikeres};
    }

    /**
     * Összehasonlítja a tényleges és az elvárt sorokat whitespace-t figyelmen kívül hagyva.
     */
    private List<String> osszehasonlit(List<String> tenyleges, List<String> elvart) {
        List<String> eltéresek = new ArrayList<>();
        int maxSor = Math.max(tenyleges.size(), elvart.size());

        for (int i = 0; i < maxSor; i++) {
            String t = i < tenyleges.size() ? tenyleges.get(i) : "<HIANYZIK>";
            String e = i < elvart.size()    ? elvart.get(i)    : "<HIANYZIK>";

            if (!normalizal(t).equals(normalizal(e))) {
                eltéresek.add(String.format("  Sor %d:", i + 1));
                eltéresek.add(String.format("    Elvart   : %s", e));
                eltéresek.add(String.format("    Tenyleges: %s", t));
            }
        }

        return eltéresek;
    }

    private String normalizal(String sor) {
        return sor.trim().replaceAll("\\s+", " ");
    }

    private List<String> beolvas(String ut) {
        try {
            return Files.readAllLines(Path.of(ut));
        } catch (IOException e) {
            return null;
        }
    }
}