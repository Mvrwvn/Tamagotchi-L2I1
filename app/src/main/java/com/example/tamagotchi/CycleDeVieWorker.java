package com.example.tamagotchi;

/*
-----------------------------
    Date : 04/04/2025

    Membres qui travaillent dessus :  BOUGUETTAYA Lina

    Que fait le code ? : Classe qui éxecute la dégradation des stats et augmentation de l'inventaire en boucle toutes les heures

-----------------------------
*/
/**/

import android.content.Context;

import androidx.work.Worker;
import androidx.work.WorkerParameters;

import javax.annotation.Nonnull;

import android.app.Notification.*;

public class CycleDeVieWorker extends Worker {

    //constructeur obligatoire pour classe qui hérite de Worker:
    public CycleDeVieWorker(@Nonnull Context context, @Nonnull WorkerParameters parametres){
        super(context, parametres);
        //stockage du context et des paramètres dans des champs protégés
    }

    //méthode
    public Result doWork() {
        // charger le tamagotchi depuis le stockage:
        TamagotchiManager manager = TamagotchiManager.getInstance(getApplicationContext());
        Tamagotchi tamagotchi = manager.chargerTamagotchi();

        if (tamagotchi != null && tamagotchi.getStatsTamagotchi().estVivant()) {
            //mettre à jour les statistiques (-2/h)
            tamagotchi.getStatsTamagotchi().degraderStatsAutomatiquement();

            //restock inventaire (+1/h)
            tamagotchi.getInventaireTamagotchi().augmenterInventairesAutomatiquement();

            //sauvegarde des modifications:
            manager.sauvegarderTamagotchi(tamagotchi);

            /*si le tamagotchi est mort : on reçoit une notif

            if (!tamagotchi.getStatsTamagotchi().estVivant()) {
                NotificationHelper.notifierMort(getApplicationContext()); //il faut écrire la classe NotificationHelper
            }*/

        }
        return Result.success();

    }

}
