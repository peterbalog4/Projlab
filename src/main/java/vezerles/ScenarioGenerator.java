package vezerles;

import java.io.*;
import java.nio.file.*;

/**
 * Komplex pályaleíró fájlok automatikus generálásáért felelős osztály.
 *
 * Lehetővé teszi nagy úthálózatok (pl. N×M-es rács) gyors előállítását,
 * amelyek a terheléses teszteléshez szükségesek. A generált fájlok
 * a {@code tests/} mappába kerülnek, és a {@code load} paranccsal tölthetők be.
 */
public class ScenarioGenerator {

    /** A generált fájlok könyvtára. */
    private static final String TESTS_DIR = "tests/";

    /**
     * Generál egy N×M-es rácsos úthálózatot leíró pályafájlt.
     *
     * Az utak azonosítója {@code ut_<sor>_<oszlop>} formátumú.
     * Minden út 1 sávos, és a szomszédos utak össze vannak kötve
     * vízszintesen és függőlegesen is.
     *
     * @param sorok    A rács sorainak száma.
     * @param oszlopok A rács oszlopainak száma.
     * @param fajlnev  A generált fájl neve (pl. {@code grid_5x5.txt}).
     */
    public void generaljRacs(int sorok, int oszlopok, String fajlnev) {
        StringBuilder sb = new StringBuilder();

        // Utak létrehozása
        for (int s = 0; s < sorok; s++) {
            for (int o = 0; o < oszlopok; o++) {
                sb.append(String.format("road ut_%d_%d 2 0 1%n", s, o));
            }
        }

        sb.append("\n");

        // Vízszintes összekötések
        for (int s = 0; s < sorok; s++) {
            for (int o = 0; o < oszlopok - 1; o++) {
                sb.append(String.format("connect ut_%d_%d vegB ut_%d_%d vegA%n", s, o, s, o + 1));
            }
        }

        // Függőleges összekötések
        for (int s = 0; s < sorok - 1; s++) {
            for (int o = 0; o < oszlopok; o++) {
                sb.append(String.format("connect ut_%d_%d vegB ut_%d_%d vegA%n", s, o, s + 1, o));
            }
        }

        sb.append("\n");
        sb.append("depot d1 0\n");

        ment(fajlnev, sb.toString());
    }

    /**
     * Generál egy egyszerű lineáris útvonalat N útból.
     *
     * Az utak azonosítója {@code ut1}, {@code ut2}, ... {@code utN} formátumú,
     * és egymás után össze vannak kötve.
     *
     * @param utakSzama Az egymás után fűzött utak száma.
     * @param fajlnev   A generált fájl neve.
     */
    public void generaljVonal(int utakSzama, String fajlnev) {
        StringBuilder sb = new StringBuilder();

        for (int i = 1; i <= utakSzama; i++) {
            sb.append(String.format("road ut%d 2 0 1%n", i));
        }

        sb.append("\n");

        for (int i = 1; i < utakSzama; i++) {
            sb.append(String.format("connect ut%d vegB ut%d vegA%n", i, i + 1));
        }

        sb.append("\n");
        sb.append("depot d1 0\n");

        ment(fajlnev, sb.toString());
    }

    /**
     * Generálja az összes teszthez szükséges alapértelmezett {@code test_map.txt} pályát.
     *
     * A pálya a részletes tervek 8.2-es pontja alapján a következőket tartalmazza:
     * {@code ut1} (1 sáv), {@code ut2} (1 sáv) összekötve egymással,
     * {@code ut3} (2 sáv), {@code ut4} (4 sáv), és egy {@code d1} telephely.
     */
    public void generaljAlappálya() {
        StringBuilder sb = new StringBuilder();

        sb.append("road ut1 2 0 1\n");
        sb.append("road ut2 2 0 1\n");
        sb.append("connect ut1 vegB ut2 vegA\n");
        sb.append("road ut3 2 0 2\n");
        sb.append("road ut4 2 0 4\n");
        sb.append("depot d1 0\n");

        ment("test_map.txt", sb.toString());
        System.out.println("Generalva: tests/test_map.txt");
    }

    /**
     * Kiírja a megadott tartalmat a {@code tests/} mappába a megadott fájlnévvel.
     *
     * @param fajlnev  A célfájl neve.
     * @param tartalom A fájlba írandó szöveg.
     */
    private void ment(String fajlnev, String tartalom) {
        try {
            Files.createDirectories(Path.of(TESTS_DIR));
            Files.writeString(Path.of(TESTS_DIR + fajlnev), tartalom);
            System.out.println("Generalva: " + TESTS_DIR + fajlnev);
        } catch (IOException e) {
            System.err.println("ERROR: Nem sikerult menteni: " + fajlnev + " – " + e.getMessage());
        }
    }
}