package com.example.tamagotchi.model;
/*
-----------------------------
    Date : 08/04/2025

    Membres qui travaillent dessus : Marwan DENAGNON

    Que fait le code ? : Va permettre de créer un objet tamagotchi et d'enregistrer ses données dans Firestore (tous les getters)
    Changement par rapport à la version précédente : Ajout de l'attribut dernierUpdate qui été précédement dans Inventaire et Statistique
-----------------------------
*/
import com.google.firebase.Timestamp;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;

public class Tamagotchi implements Serializable {

    private String idTamagotchi;
    private String idJoueur;
    private String nomTamagotchi;
    private String genre;
    private Timestamp dateNaissance;
    private Timestamp dernierUpdate;
    private Statistique statsTamagotchi;
    private Inventaire inventaireTamagotchi;
    public Tamagotchi(String idTamagotchi, String idJoueur, String nomTamagotchi, String genre, Timestamp dateNaissance, Timestamp dernierUpdate, Statistique statsTamagotchi, Inventaire inventaireTamagotchi){
        this.idTamagotchi = idTamagotchi;
        this.idJoueur = idJoueur;
        this.nomTamagotchi = nomTamagotchi;
        this.genre = genre;
        this.dateNaissance = dateNaissance;
        this.dernierUpdate = dernierUpdate;
        this.statsTamagotchi = statsTamagotchi;
        this.inventaireTamagotchi = inventaireTamagotchi;
    }
    public Tamagotchi() {}  // Obligatoire pour Firestore

    public String getIdTamagotchi() {
        return idTamagotchi;
    }

    public void setIdTamagotchi(String idTamagotchi) {
        this.idTamagotchi = idTamagotchi;
    }

    public String getIdJoueur(){
        return idJoueur;
    }

    public void setIdJoueur(String idJoueur){
        this.idJoueur = idJoueur;
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

    public long getAge() {
        Timestamp creationTimestamp = dateNaissance;
        long creationMillis = creationTimestamp.toDate().getTime(); // conversion en ms
        long nowMillis = System.currentTimeMillis();
        long diffMillis = nowMillis - creationMillis;
        return TimeUnit.MILLISECONDS.toDays(diffMillis); // âge en jours
    }
}