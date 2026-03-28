package vezerles;


public class Main {

    public static void main(String[] args) {
        boolean kilepes = false;

        while(!kilepes){
            System.out.println("\n___ Skeleton menü ___\n");
            System.out.println("1. Autó vagy Busz elakadása hóban");
            System.out.println("2. Járművek ütközése jégen");
            System.out.println("3. Hókotró utat takarít");
            System.out.println("4. Sáv eljegesedése");
            System.out.println("5. Kotrófej cseréje");
            System.out.println("6. Vásárlás a Boltban");
            System.out.println("7. Busz forduló teljesítése");
            System.out.println("8. Kotrófej újratöltése");
            System.out.println("9. Sávváltás");
            System.out.println("10. Hóesés");
            System.out.println("11. JMF módosítása a Telephelyen");
            System.out.println("12. Kilépés");

            int valasz = Skeleton.kerdezOpcio("Melyik use-case-t szeretnéd tesztelni?", 12);

            System.out.print("\n");
            switch (valasz){
                case 1:
                    TesztFuggvenyek.tesztElakadas();
                    break;
                case 2:
                    TesztFuggvenyek.tesztUtkozes();
                    break;
                case 3:
                    TesztFuggvenyek.tesztHokotroTakaritas();
                    break;
                case 4:
                    TesztFuggvenyek.tesztJegesedes();
                    break;
                case 5:
                    TesztFuggvenyek.tesztFejcsere();
                    break;
                case 6:
                    TesztFuggvenyek.tesztVasarlas();
                    break;
                case 7:
                    TesztFuggvenyek.tesztBuszFordulas();
                    break;
                case 8:
                    TesztFuggvenyek.tesztFejUjratoltes();
                    break;
                case 9:
                    TesztFuggvenyek.tesztSavvaltas();
                    break;
                case 10:
                    TesztFuggvenyek.tesztHoeses();
                    break;
                case 11:
<<<<<<< HEAD
                    TesztFuggvenyek.tesztPenzValtozas();
=======
                    TesztFuggvenyek.tesztJMFModositas();
>>>>>>> a2eda4a534e76a41f32e3aec577c862fbb6e3a66
                    break;
                case 12:
                    kilepes = true;
                    System.out.println("Kilépés...");
                    break;
            }
            System.out.print("\n");
        }
    }
}
