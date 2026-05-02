package vezerles;

import java.io.*;
import java.nio.file.*;
import java.util.*;

/**
 * A tényleges és az elvárt kimenet összehasonlítására szolgáló osztály.
 *
 * Sorról sorra veti össze a {@code _out.txt} (tényleges) és az {@code _exp.txt}
 * (elvárt) fájlokat, figyelmen kívül hagyva a felesleges szóközöket és
 * tabulátorokat. Az eltérésekről részletes jelentést készít.
 */
public class OutputComparator {

    /** A tényleges kimenet fájljainak utótagja. */
    private static final String OUT_SUFFIX = "_out.txt";

    /** Az elvárt kimenet fájljainak utótagja. */
    private static final String EXP_SUFFIX = "_exp.txt";

    /** A tesztfájlok könyvtárának elérési útja. */
    private static final String TESTS_DIR = "tests/";

    /**
     * Összehasonlítja a megadott teszteset tényleges és elvárt kimenetét.
     *
     * @param tesztNev A teszteset neve kiterjesztés nélkül.
     * @return {@code true}, ha a kimenet megegyezik az elvárttal, {@code false} egyébként.
     */
    public boolean hasonlit(String tesztNev) {
        String tenylegesUt = TESTS_DIR + tesztNev + OUT_SUFFIX;
        String elvartUt    = TESTS_DIR + tesztNev + EXP_SUFFIX;

        List<String> tenyleges = beolvas(tenylegesUt);
        List<String> elvart    = beolvas(elvartUt);

        if (tenyleges == null || elvart == null) return false;

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
     * Összehasonlítja az összes tesztesetet a {@code tests/} mappában.
     *
     * @return Az összes teszt száma és az elbukott tesztek száma vesszővel elválasztva,
     *         {@code "[osszes, sikeres]"} formátumban.
     */
    public int[] hasonlitOsszes() {
        File dir = new File(TESTS_DIR);
        if (!dir.exists() || !dir.isDirectory()) {
            System.err.println("ERROR: A tests/ mappa nem letezik.");
            return new int[]{0, 0};
        }

        File[] elvartFajlok = dir.listFiles((d, name) -> name.endsWith(EXP_SUFFIX));
        if (elvartFajlok == null || elvartFajlok.length == 0) {
            System.out.println("Nem talalhato elvárt kimeneti fajl a tests/ mappaban.");
            return new int[]{0, 0};
        }

        Arrays.sort(elvartFajlok);
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
     * Összehasonlítja a tényleges és az elvárt sorokat, whitespace-t figyelmen kívül hagyva.
     *
     * @param tenyleges A tényleges kimenet sorai.
     * @param elvart    Az elvárt kimenet sorai.
     * @return Az eltérések listája, üres ha minden egyezik.
     */
    private List<String> osszehasonlit(List<String> tenyleges, List<String> elvart) {
        List<String> eltéresek = new ArrayList<>();
        int maxSor = Math.max(tenyleges.size(), elvart.size());

        for (int i = 0; i < maxSor; i++) {
            String t = i < tenyleges.size() ? tenyleges.get(i).trim() : "<HIANYZIK>";
            String e = i < elvart.size()    ? elvart.get(i).trim()    : "<HIANYZIK>";

            if (!normalizal(t).equals(normalizal(e))) {
                eltéresek.add(String.format("  Sor %d:", i + 1));
                eltéresek.add(String.format("    Elvart  : %s", e));
                eltéresek.add(String.format("    Tenyleges: %s", t));
            }
        }

        return eltéresek;
    }

    /**
     * Normalizálja a sort összehasonlításhoz: trimmel és többszörös whitespace-t egyre cseréli.
     *
     * @param sor A normalizálandó sor.
     * @return A normalizált sor.
     */
    private String normalizal(String sor) {
        return sor.trim().replaceAll("\\s+", " ");
    }

    /**
     * Beolvassa egy fájl sorait listába.
     *
     * @param ut A fájl elérési útja.
     * @return A sorok listája, vagy {@code null} hiba esetén.
     */
    private List<String> beolvas(String ut) {
        try {
            return Files.readAllLines(Path.of(ut));
        } catch (IOException e) {
            System.err.println("ERROR: Nem sikerult beolvasni: " + ut);
            return null;
        }
    }
}