package vezerles;

import funkcionalisElemek.KorSzamlalo;
import funkcionalisElemek.Telephely;
import funkcionalisElemek.Ut;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Map_generator {
    private Map<String, Ut> utak = new HashMap<>();
    private Map<String, Telephely> telephelyek = new HashMap<>();
    private KorSzamlalo korszamlalo;

    public Map_generator(KorSzamlalo korszamlalo) {
        this.korszamlalo = korszamlalo;
    }

    public void load(String filename) {
        try (Scanner scanner = new Scanner(new File(filename))) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine().trim();
                
                // Üres sorok és kommentek átugrása
                if (line.isEmpty() || line.startsWith("#")) {
                    continue;
                }

                String[] parts = line.split("\\s+");
                switch (parts[0]) {
                    case "depot":
                        // depot <telep_id> <jmf_kezdotoke>
                        int telepId = Integer.parseInt(parts[1]);
                        int jmf = Integer.parseInt(parts[2]);
                        
                        Telephely t = new Telephely(telepId);
                        t.JMFmodosit(jmf);
                        telephelyek.put(parts[1], t);
                        break;

                    case "road":
                        // road <út_id> <hossz> <sávok_száma_A> <sávok_száma_B>
                        String utId = parts[1];
                        int hossz = Integer.parseInt(parts[2]);
                        int savokA = Integer.parseInt(parts[3]);
                        int savokB = Integer.parseInt(parts[4]);
                        
                        Ut u = new Ut(utId, hossz, savokA, savokB);
                        utak.put(utId, u);
                        korszamlalo.addUt(u);
                        break;

                    case "connect":
                        // connect <út1_id> <út1_vége> <út2_id> <út2_vége>
                        Ut u1 = utak.get(parts[1]);
                        Ut u2 = utak.get(parts[3]);
                        
                        if (u1 != null && u2 != null) {
                            u1.addKapcsolat(parts[2], u2, parts[4]);
                        }
                        break;
                        
                    default:
                        System.out.println("Ismeretlen parancs: " + parts[0]);
                        break;
                }
            }
        } catch (FileNotFoundException e) {
            System.err.println("Hiba: A konfigurációs fájl nem található (" + filename + ")");
        } catch (NumberFormatException e) {
            System.err.println("Hiba: Hibás számformátum a konfigurációs fájlban!");
        }
    }

    public Map<String, Ut> getUtak() {
        return utak;
    }

    public Map<String, Telephely> getTelephelyek() {
        return telephelyek;
    }
}