package com.example.tamagotchi.model;
/*
-----------------------------
    Date : 08/04/2025

    Membres qui travaillent dessus : Marwan DENAGNON

    Que fait le code ? : Va permettre de créer un objet tamagotchi et d'enregistrer ses données dans Firestore (tous les getters)
    Changement par rapport à la version précédente : Ajout de l'attribut dernierUpdate qui été précédement dans Inventaire et Statistique
-----------------------------
*/
import com.example.tamagotchi.Inventaire;
import com.google.firebase.Timestamp;

public class Tamagotchi{

    private String userId;
    private String nomTamagotchi;
    private String genre;
    private Timestamp dateNaissance;
    private Timestamp dernierUpdate;
    private Statistique statsTamagotchi;
    private Inventaire inventaireTamagotchi;
    public Tamagotchi(String userId, String nomTamagotchi, String genre, Timestamp dateNaissance, Timestamp dernierUpdate, Statistique statsTamagotchi, Inventaire inventaireTamagotchi){
        this.userId = userId;
        this.nomTamagotchi = nomTamagotchi;
        this.genre = genre;
        this.dateNaissance = dateNaissance;
        this.dernierUpdate = dernierUpdate;
        this.statsTamagotchi = statsTamagotchi;
        this.inventaireTamagotchi = inventaireTamagotchi;
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
    public String getGenre(){
        return genre;
    }
    public void setGenre(String genre){
        this.genre = genre;
    }

    public Timestamp getDateNaissance() {
        return dateNaissance;
    }

    public void setDateNaissance(Timestamp dateNaissance) {
        this.dateNaissance = dateNaissance;
    }

    public Timestamp getDernierUpdate() {
        return dernierUpdate;
    }

    public void setDernierUpdate(Timestamp dernierUpdate) {
        this.dernierUpdate = dernierUpdate;
    }

    public Statistique getStatsTamagotchi() {
        return statsTamagotchi;
    }

    public void setStatsTamagotchi(Statistique statsTamagotchi){
        this.statsTamagotchi = statsTamagotchi;
    }

    public Inventaire getInventaireTamagotchi() {
        return inventaireTamagotchi;
    }

    public void setInventaireTamagotchi(Inventaire inventaireTamagotchi) {
        this.inventaireTamagotchi = inventaireTamagotchi;
    }

}