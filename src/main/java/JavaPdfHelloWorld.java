import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Scanner;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;

public class JavaPdfHelloWorld
{
    public static void main(String[] args)
    {
        Scanner sc = new Scanner(System.in);
        System.out.println("Napis cislo na vyber moznosti alebo napis koniec na ukoncenie: ");
        System.out.println("1. Pridaj knihu");
        System.out.println("2. Zobraz všetky knihy");
        System.out.println("3. Zobraz konkrétnu knihu (podľa indexu)");
        System.out.println("4. Vymaž konkrétnu knihu (podľa indexu)");
        System.out.println("5. Zobraz počet všetkých kníh v knižnici");
        System.out.println("6. Vyhladaj knihu podla nazvu");
        System.out.println("7. Nacitaj knihy zo suboru (zadaj názov súboru)");
        System.out.println("8. Uloz knihy do suboru (zadaj názov súboru)");
        System.out.println("9. Uprav knihu (podľa indexu)");
        System.out.println("10. Koniec");

        boolean koniec = false;
        ArrayList<KniznicaDatabaza> knihy = new ArrayList<>();

        //KniznicaDatabaza kniha1 = new KniznicaDatabaza("Harry Potter", "J.K.Rowling", 4774549);
        //knihy.add(kniha1);

        /*

        KniznicaDatabaza kniha2 = new KniznicaDatabaza("Hobit", "J.R.R.Tolkien", 1937);
        KniznicaDatabaza kniha3 = new KniznicaDatabaza("Vlak do stanice nebe", "Karel Čapek", 1934);
        KniznicaDatabaza kniha4 = new KniznicaDatabaza("Pán prsteňov", "J.R.R.Tolkien", 1954);


        knihy.add(kniha2);
        knihy.add(kniha3);
        knihy.add(kniha4);

        */

        while (!koniec){
            String input = sc.nextLine();

            String normalizedString = Normalizer.normalize(input, Normalizer.Form.NFKD)
                    .replaceAll("[^\\p{ASCII}]", "")
                    .toLowerCase()
                    .replaceAll("\\s{2,}", " ")
                    .trim();

            if (normalizedString.equals("koniec")) {
                for(KniznicaDatabaza knihaObjekt:knihy){
                    System.out.println(knihaObjekt);
                }

                koniec = true;


            }else if(normalizedString.equals("1")){
                String knihaVstup = sc.nextLine();

                String[] parts = knihaVstup.split(" ");
                String nazov = "";
                String autor = "";
                int rokVydania = 0;

                for (int i = 0; i < parts.length - 1; i++) {
                    if (parts[i].equals("nazov:") && i < parts.length - 2) {
                        nazov = knihaVstup.substring(knihaVstup.indexOf("nazov:") + 6, knihaVstup.indexOf("autor:")).trim();
                    } else if (parts[i].equals("autor:") && i < parts.length - 2) {
                        autor = knihaVstup.substring(knihaVstup.indexOf("autor:") + 6, knihaVstup.indexOf("rok:")).trim();
                    } else if (parts[i].equals("rok:") && i < parts.length - 1) {
                        try {
                            rokVydania = Integer.parseInt(parts[i + 1]);
                        } catch (NumberFormatException e) {
                            System.out.println("Invalid year format. Please enter a valid year.");
                        }
                    }
                }

                KniznicaDatabaza kniha = new KniznicaDatabaza(nazov, autor, rokVydania);
                knihy.add(kniha);

            } else if (normalizedString.equals("2")) {
                for(KniznicaDatabaza knihaObjekt:knihy){
                    System.out.println(knihaObjekt);
                }

            } else if (normalizedString.equals("3")) {
                System.out.println("Zadaj index knihy: ");
                int index = sc.nextInt();
                System.out.println(knihy.get(index));

            } else if (normalizedString.equals("4")) {
                System.out.println("Zadaj index knihy: ");
                int index = sc.nextInt();
                if (index >= 0 && index < knihy.size()){
                    knihy.remove(index);
                    System.out.println("Kniha bola vymazana");
                } else {
                    System.out.println("Neplatný index knihy.");
                }


            } else if (normalizedString.equals("5")) {
                System.out.println("Pocet knih v kniznici: " + knihy.size());

            } else if (normalizedString.equals("6")) {
                System.out.println("Zadaj nazov knihy: ");
                String nazov = sc.nextLine();
                for (KniznicaDatabaza knihaObjekt : knihy) {
                    if (knihaObjekt.getNazov().equals(nazov)) {
                        System.out.println(knihaObjekt);
                    }
                }

            }else if (normalizedString.equals("9")) {
                System.out.println("Zadaj index knihy, ktorú chceš upraviť: ");
                int index = sc.nextInt();

                if (index >= 0 && index < knihy.size()) {
                    System.out.println("Zadaj nový názov knihy: ");
                    sc.nextLine();
                    String novyNazov = sc.nextLine();
                    knihy.get(index).setNazov(novyNazov);

                    System.out.println("Zadaj nového autora knihy: ");
                    String novyAutor = sc.nextLine();
                    knihy.get(index).setAutor(novyAutor);

                    System.out.println("Zadaj nový rok vydania knihy: ");
                    int novyRok = sc.nextInt();
                    knihy.get(index).setRokVydania(novyRok);

                    System.out.println("Kniha bola upravená.");
                } else {
                    System.out.println("Neplatný index knihy.");
                }
            }else if (normalizedString.equals("8")) {
                System.out.println("Zadaj nazov suboru na ulozenie zoznamu: ");
                String novyNazov = sc.nextLine();
                String nazovSuboru = novyNazov + ".pdf";
                Document document = new Document();

                try {
                    BaseFont baseFont = BaseFont.createFont("ARIAL.TTF", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
                    PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(nazovSuboru));
                    document.open();

                    for(KniznicaDatabaza knihaObjekt:knihy){
                        document.add(new Paragraph(knihaObjekt.toString(), new com.itextpdf.text.Font(baseFont)));

                    }
                    System.out.println("Hotovo.");
                    document.close();
                    writer.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }else if (normalizedString.equals("7")) {
                System.out.println("Zadaj názov súboru na načítanie zoznamu: ");
                String nazovSuboru = sc.nextLine() + ".pdf";
                try {
                    PdfReader reader = new PdfReader(nazovSuboru);

                    for (int i = 1; i <= reader.getNumberOfPages(); i++) {

                        String pageText = PdfTextExtractor.getTextFromPage(reader, i);
                        String[] riadky = pageText.split("\n");

                        for (String line : riadky) {

                            if (line.startsWith("Nazov:") && line.contains("Autor:") && line.contains("Rok vydania:")) {

                                String nazov = line.substring(line.indexOf("Nazov:") + "Nazov:".length(), line.indexOf("Autor:")).trim();
                                String autor = line.substring(line.indexOf("Autor:") + "Autor:".length(), line.indexOf("Rok vydania:")).trim();

                                nazov = nazov.substring(0, nazov.length() - 1);
                                autor = autor.substring(0, autor.length() - 1);

                                int rokVydania = Integer.parseInt(line.substring(line.indexOf("Rok vydania:") + "Rok vydania:".length()).trim());

                                boolean exists = false;

                                for (KniznicaDatabaza existingKniha : knihy) {
                                    if (existingKniha.getNazov().equals(nazov) && existingKniha.getAutor().equals(autor) && existingKniha.getRokVydania() == rokVydania) {
                                        System.out.println("Kniha " + nazov + " už je zapisana.");
                                        exists = true;
                                        break;
                                    }
                                }

                                if (!exists) {
                                    KniznicaDatabaza kniha = new KniznicaDatabaza(nazov, autor, rokVydania);
                                    knihy.add(kniha);
                                }
                            }
                        }
                    }
                    System.out.println("Data loaded from " + nazovSuboru);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if (normalizedString.equals("10")) {
                System.out.println("Koniec programu.");
                koniec = true;
            }
        }
        sc.close();



    }
}