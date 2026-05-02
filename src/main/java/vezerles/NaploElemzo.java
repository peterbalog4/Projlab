package vezerles;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.regex.*;

/**
 * A futás közbeni naplófájlok elemzésére és a hibás sorok kiszűrésére szolgáló osztály.
 *
 * Beolvassa a prototípus által generált naplófájlokat, és összeveti az egyes
 * bejegyzéseket az elvárt értékekkel. Kiszűri a sikeresen lefutott sorokat,
 * és csak a hibásakat listázza ki részletes eltéréssel.
 * Segít vizualizálni a járművek útvonalát és az állapotváltozásokat.
 */
public class NaploElemzo {

    /** A naplófájlok könyvtára. */
    private static final String TESTS_DIR = "tests/";

    /** Az ERROR: előtagú sorokra illeszkedő minta. */
    private static final Pattern HIBA_MINTA = Pattern.compile("^ERROR:.*");

    /** A STAT blokkot nyitó sorokra illeszkedő minta. */
    private static final Pattern STAT_MINTA = Pattern.compile("^STAT (.+):$");

    /** A tulajdonság-érték párokra illeszkedő minta a STAT blokkon belül. */
    private static final Pattern TULAJDONSAG_MINTA = Pattern.compile("^- (.+): (.+)$");

    /**
     * Elemzi a megadott naplófájlt, és kiírja a hibás sorokat az elvártaktól
     * való eltéréssel együtt.
     *
     * @param naploFajl  A naplófájl neve a {@code tests/} mappán belül.
     * @param elvartFajl Az elvárt kimenet fájlja a {@code tests/} mappán belül.
     */
    public void elemez(String naploFajl, String elvartFajl) {
        List<String> naplo  = beolvas(TESTS_DIR + naploFajl);
        List<String> elvart = beolvas(TESTS_DIR + elvartFajl);

        if (naplo == null || elvart == null) return;

        List<String> hibas = szurHibas(naplo, elvart);

        if (hibas.isEmpty()) {
            System.out.println("Naplo elemzes: nincsenek eltéresek.");
        } else {
            System.out.println("Hibas sorok (" + hibas.size() + " db):");
            hibas.forEach(System.out::println);
        }
    }

    /**
     * Megkeresi és kiírja az összes {@code ERROR:} előtagú sort a naplóban.
     *
     * @param naploFajl A naplófájl neve a {@code tests/} mappán belül.
     */
    public void hibakListaz(String naploFajl) {
        List<String> naplo = beolvas(TESTS_DIR + naploFajl);
        if (naplo == null) return;

        System.out.println("ERROR sorok a(z) " + naploFajl + " naplóban:");
        naplo.stream()
             .filter(s -> HIBA_MINTA.matcher(s.trim()).matches())
             .forEach(System.out::println);
    }

    /**
     * Vizualizálja a járművek útvonalát a naplóban található STAT blokkok alapján.
     *
     * Minden járműhöz kiírja a pozíciójának változásait sorban,
     * így könnyen azonosítható, ha egy jármű nem a várt útvonalon haladt.
     *
     * @param naploFajl A naplófájl neve a {@code tests/} mappán belül.
     */
    public void utvonalVizualizal(String naploFajl) {
        List<String> naplo = beolvas(TESTS_DIR + naploFajl);
        if (naplo == null) return;

        Map<String, List<String>> utvonalak = new LinkedHashMap<>();
        String aktualisId = null;

        for (String sor : naplo) {
            sor = sor.trim();
            Matcher statM = STAT_MINTA.matcher(sor);
            if (statM.matches()) {
                aktualisId = statM.group(1);
                continue;
            }

            if (aktualisId != null) {
                Matcher tulajM = TULAJDONSAG_MINTA.matcher(sor);
                if (tulajM.matches() && tulajM.group(1).equalsIgnoreCase("Pozicio")) {
                    utvonalak.computeIfAbsent(aktualisId, k -> new ArrayList<>())
                             .add(tulajM.group(2));
                }
            }
        }

        if (utvonalak.isEmpty()) {
            System.out.println("Nem talalhato pozicio adat a naplóban.");
            return;
        }

        System.out.println("Jarmuvek utvonala:");
        utvonalak.forEach((id, poziciok) -> {
            System.out.println("  " + id + ": " + String.join(" -> ", poziciok));
        });
    }

    /**
     * Összehasonlítja a naplót az elvárt kimenettel, és visszaadja a hibás sorokat.
     *
     * @param naplo  A tényleges napló sorai.
     * @param elvart Az elvárt kimenet sorai.
     * @return A hibás sorok listája eltérés-leírással együtt.
     */
    private List<String> szurHibas(List<String> naplo, List<String> elvart) {
        List<String> hibas = new ArrayList<>();
        int maxSor = Math.max(naplo.size(), elvart.size());

        for (int i = 0; i < maxSor; i++) {
            String n = i < naplo.size()  ? naplo.get(i).trim()  : "<HIANYZIK>";
            String e = i < elvart.size() ? elvart.get(i).trim() : "<HIANYZIK>";

            if (!normalizal(n).equals(normalizal(e))) {
                hibas.add(String.format("  Sor %d | Elvart: %-40s | Tenyleges: %s", i + 1, e, n));
            }
        }

        return hibas;
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