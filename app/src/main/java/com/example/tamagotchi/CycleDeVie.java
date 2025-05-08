package com.example.tamagotchi;

import android.content.Context;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.WorkManager;
import androidx.work.PeriodicWorkRequest;

import java.util.concurrent.TimeUnit;
/*
-----------------------------
    Date : 04/04/2025

    Membres qui travaillent dessus :  BOUGUETTAYA Lina

    Que fait le code ? : Classe qui lance la dégradation des stats et augmentation de l'inventaire en boucle toutes les heures
-----------------------------
*/
    /**/

    public class CycleDeVie {
        private static final String WORK_TAG= "tamagotchiCycleWorker"; //will see comment utiliser !
        private final Context context;

        //reprendre le contexte de l'application (constructeur)
        public CycleDeVie(Context context){
            this.context= context.getApplicationContext(); //contexte global
        }
        //méthodes:
        //démarrage du cycle automatique :
        public void demarrerCycle(){
            //gestion du timing :
            PeriodicWorkRequest workRequest = new PeriodicWorkRequest.Builder(
                    CycleDeVieWorker.class,
                    1, TimeUnit.HOURS,
                    15, TimeUnit.MINUTES).addTag(WORK_TAG).build();
            //ici on démarre un cycle toute les heures avec une marge de 15min pour l'économisation de batterie

            WorkManager.getInstance(context).enqueueUniquePeriodicWork(
                    WORK_TAG, ExistingPeriodicWorkPolicy.KEEP, workRequest);
            //ici on garde la tâche existante avec le KEEP
        }

        //arrêt de cycle:
        public void arreterCycle(){
            WorkManager.getInstance(context).cancelAllWorkByTag(WORK_TAG);
        }

    }

