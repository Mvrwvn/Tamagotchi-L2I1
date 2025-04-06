package com.example.tamagotchi;

import java.time.temporal.ChronoUnit;
import java.time.LocalDate;
import java.time.Period;
/*
-----------------------------
    Date : 06/04/2025

    Membres qui travaillent dessus : Marwan DENAGNON, Lina Bouguettaya

    Que fait le code ? : Classe du tamagotchi qui mets en action les fonctionnalitées pour prendre soin de l'animal

-----------------------------
*/

public class Tamagotchi{

    private String userId;
    private int idTamagotchi;
    private String nomTamagotchi;
    private LocalDate dateNaissance;
    private Statistique statsTamagotchi;
    private Inventaire inventaireTamagotchi;

    public Tamagotchi(int id, String userId, String nomTamagotchi, LocalDate dateNaissance, Statistique statsTamagotchi, Inventaire inventaireTamagotchi){
        this.idTamagotchi = id;
        this.userId = userId;
        this.nomTamagotchi = nomTamagotchi;
        this.dateNaissance = dateNaissance;
        this.statsTamagotchi = statsTamagotchi;
        this.inventaireTamagotchi = inventaireTamagotchi;
    }

    public Tamagotchi(int id, String nom){
        //instanciation des variables de la classe:
        this.idTamagotchi =id;
        this.nomTamagotchi= nom;
        this.dateNaissance= LocalDate.now();
        this.statsTamagotchi = new Statistique();
        this.inventaireTamagotchi= new Inventaire();
    }

    public Tamagotchi() {}  // Obligatoire pour Firestore

    public String getUserId(){
        return userId;
    }

    public void setUserId(String userId){
        this.userId = userId;
    }

    public String getNomTamagotchi() {
        return nomTamagotchi;
    }

    public void setNomTamagotchi(String nomTamagotchi) {
        this.nomTamagotchi = nomTamagotchi;
    }

    public Statistique getStatsTamagotchi() {
        return statsTamagotchi;
    }

    public void setStatsTamagotchi(Statistique statsTamagotchi){
        this.statsTamagotchi = statsTamagotchi;
    }
    //méthodes:
    /*à chaque action (nourrir, jouer.. ect) les stats augmente de 5%*/
    public void nourir() {
        if (inventaireTamagotchi.getNbNourritures() > 0) {
            statsTamagotchi.modifierStatistiques("faim", 5);
            inventaireTamagotchi.modifierInventaires("nbNourritures", -1);
        }
        //else??
    }

    public void jouer(){
        if(inventaireTamagotchi.getNbJouets()>0){
            statsTamagotchi.modifierStatistiques("loisir", 5);
            inventaireTamagotchi.modifierInventaires("nbJouets", -1);
        }
    }

    public void dormir(){
        if(inventaireTamagotchi.getNbLits()>0){
            statsTamagotchi.modifierStatistiques("energie", 5);
            inventaireTamagotchi.modifierInventaires("nbLits", -1);
        }
    }

    public void soigner(){
        if(inventaireTamagotchi.getNbMedicaments()>0){
            statsTamagotchi.modifierStatistiques("sante", 5);
            inventaireTamagotchi.modifierInventaires("NbMedicaments", -1);
        }
    }

    public void doucher(){
        if(inventaireTamagotchi.getNbSavons()>0){
            statsTamagotchi.modifierStatistiques("proprete", 5);
            inventaireTamagotchi.modifierInventaires("NbSavons", -1);
        }
    }

    public void boire(){
        if(inventaireTamagotchi.getNbEau()>0){
            statsTamagotchi.modifierStatistiques("soif", 5);
            inventaireTamagotchi.modifierInventaires("NbEau", -1);
        }
    }

    //méthode getAge:
    public long getAge(){
        return ChronoUnit.DAYS.between(LocalDate.now(), dateNaissance);
        /*jour actuelle - jour de création du compte */
    }
}