package com.example.myapplication;
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
            if (listeTamagotchis == null) {
                listeTamagotchis = new ArrayList<>();
            }
        } catch (IOException e) {
            System.out.println("Erreur lors du chargement : " + e.getMessage());
            listeTamagotchis = new ArrayList<>();
        }
    }
}