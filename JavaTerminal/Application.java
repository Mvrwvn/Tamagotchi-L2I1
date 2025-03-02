import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class Application {
    private static final String FICHIER_JSON = "tamagotchis.json";
    private static int idTamagotchi = 0;
    private static int idStatistique = 1000;
    private static int ageTamagotchi = 0;
    private static ArrayList<Tamagotchi> listeTamagotchis = new ArrayList<>();
    private static final Gson gson = new Gson();

    public static void CreerTamagotchi(String nomTamagotchi) {
        idStatistique++;
        idTamagotchi++;

        Stats statsTama = new Stats(idStatistique, idTamagotchi, 80, 20, 50, 30, 90);
        Tamagotchi tama = new Tamagotchi(idTamagotchi, nomTamagotchi, ageTamagotchi, statsTama);

        listeTamagotchis.add(tama);
        sauvegarderTamagotchis();

        System.out.println("Tamagotchi " + nomTamagotchi + " créé avec succès !");
    }

    public static void sauvegarderTamagotchis() {
        try (Writer writer = new FileWriter(FICHIER_JSON)) {
            gson.toJson(listeTamagotchis, writer);
        } catch (IOException e) {
            System.out.println("Erreur lors de la sauvegarde : " + e.getMessage());
        }
    }

    public static void chargerTamagotchis() {
        File fichier = new File(FICHIER_JSON);
        if (!fichier.exists()) {
            listeTamagotchis = new ArrayList<>();
            return;
        }

        try (Reader reader = new FileReader(FICHIER_JSON)) {
            listeTamagotchis = gson.fromJson(reader, new TypeToken<ArrayList<Tamagotchi>>() {}.getType());

            // Vérifie si la liste est encore null après le chargement
            if (listeTamagotchis == null) {
                listeTamagotchis = new ArrayList<>();
            }
        } catch (IOException e) {
            System.out.println("Erreur lors du chargement : " + e.getMessage());
            listeTamagotchis = new ArrayList<>();
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        chargerTamagotchis();
        int choix;

        do {
            System.out.println("\n=== Menu ===");
            System.out.println("1. Créer un Tamagotchi");
            System.out.println("2. Nourrir un Tamagotchi");
            System.out.println("0. Quitter");
            System.out.print("Choisissez une option : ");

            while (!scanner.hasNextInt()) {
                System.out.println("Entrée invalide. Veuillez entrer un nombre.");
                scanner.next();
            }

            choix = scanner.nextInt();
            scanner.nextLine();

            switch (choix) {
                case 1:
                    System.out.println("Quel est le nom de votre Tamagotchi ?");
                    String nomTamagotchi = scanner.nextLine();
                    CreerTamagotchi(nomTamagotchi);
                    break;

                case 2:
                    if (listeTamagotchis.isEmpty()) {
                        System.out.println("Aucun Tamagotchi n'a été créé !");
                        break;
                    }

                    System.out.println("Quel Tamagotchi voulez-vous nourrir ?");
                    for (int i = 0; i < listeTamagotchis.size(); i++) {
                        System.out.println((i + 1) + ". " + listeTamagotchis.get(i).nom);
                    }

                    int indexTama;
                    do {
                        System.out.print("Entrez le numéro : ");
                        while (!scanner.hasNextInt()) {
                            System.out.println("Entrée invalide. Veuillez entrer un nombre.");
                            scanner.next();
                        }
                        indexTama = scanner.nextInt();
                    } while (indexTama < 1 || indexTama > listeTamagotchis.size());

                    Tamagotchi tama = listeTamagotchis.get(indexTama - 1);

                    System.out.println("Votre Tamagotchi " + tama.nom + " a " + tama.statsTama.faim + " points de faim.");
                    System.out.print("Combien de points de nourriture voulez-vous lui donner ? ");

                    int nbPointFaim;
                    while (!scanner.hasNextInt()) {
                        System.out.println("Entrée invalide. Veuillez entrer un nombre.");
                        scanner.next();
                    }
                    nbPointFaim = scanner.nextInt();
                    scanner.nextLine();

                    tama.manger(nbPointFaim);
                    sauvegarderTamagotchis();
                    break;

                case 0:
                    System.out.println("Au revoir !");
                    break;

                default:
                    System.out.println("Option invalide, veuillez réessayer.");
            }
        } while (choix != 0);

        scanner.close();
    }
}