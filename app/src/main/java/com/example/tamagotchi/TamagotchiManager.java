package com.example.tamagotchi;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

import org.checkerframework.checker.units.qual.C;

public class TamagotchiManager {
    private static TamagotchiManager instance;
    private final SharedPreferences pref;
    private final Gson gson = new Gson();

    //constructeur
    private TamagotchiManager(Context context){
        pref= context.getSharedPreferences("TamagotchiPrefs", Context.MODE_PRIVATE);
    }

    //méthodes:
    //1- récuper l'instance unique (Singleton)
    public static synchronized TamagotchiManager getInstance(Context context){
        if (instance ==null){
            //
            instance= new TamagotchiManager(context.getApplicationContext());
        }
        return instance;
    }
    //2-Sauvegarde du tamagotchi en JSON
    public void sauvegarderTamagotchi(Tamagotchi tamagotchi){
        String json = gson.toJson(tamagotchi);
        pref.edit().putString("tamagotchi_data", json).apply();
    }

    //3-charger les données du tamagotchi depuis le json :

    public Tamagotchi chargerTamagotchi(){
        String json = pref.getString("tamagotchi_data", null);
        return json!=null ? gson.fromJson(json, Tamagotchi.class): null;
        /*ici on verif si chaine Json n'est pas vide si oui on la convertit en objet java si non on retourne null*/
    }


}
