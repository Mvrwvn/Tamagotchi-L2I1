package com.example.tamagotchi;
/*
-----------------------------
    Date : 31/03/2025

    Membres qui travaillent dessus : Marwan DENAGNON

    Que fait le code ? : Va permettre de créer un objet tamagotchi et d'enregistrer ses données dans Firestore (tous les getters)
-----------------------------
*/
import com.google.firebase.Timestamp;

import java.time.LocalDate;
import java.time.Period;

public class Tamagotchi{

    private String userId;
    private String nomTamagotchi;
    private String genre;
    private Timestamp dateNaissance;
    private Statistique statsTamagotchi;
    private Inventaire inventaireTamagotchi;
    public Tamagotchi(String userId, String nomTamagotchi, String genre, Timestamp dateNaissance, Statistique statsTamagotchi, Inventaire inventaireTamagotchi){
        this.userId = userId;
        this.nomTamagotchi = nomTamagotchi;
        this.genre = genre;
        this.dateNaissance = dateNaissance;
        this.statsTamagotchi = statsTamagotchi;
        this.inventaireTamagotchi = inventaireTamagotchi;
    }
    public Tamagotchi() {}  // Obligatoire pour Firestore

    public void nourir(double valeur){
        statsTamagotchi.setFaim(valeur);
        inventaireTamagotchi.setNbNourritures(inventaireTamagotchi.getNbNourritures()-1);
    }
    public void soif(double valeur) {
        statsTamagotchi.setSoif(valeur);
        inventaireTamagotchi.setNbBoissons(inventaireTamagotchi.getNbBoissons() - 1);
    }

    public void dormir(double valeur) {
        statsTamagotchi.setEnergie(valeur);
        inventaireTamagotchi.setNbLits(inventaireTamagotchi.getNbLits() - 1);
    }

    public void soigner(double valeur) {
        statsTamagotchi.setSante(valeur);
        inventaireTamagotchi.setNbMedicaments(inventaireTamagotchi.getNbMedicaments() - 1);
    }

    public void doucher(double valeur) {
        statsTamagotchi.setHygiene(valeur);
        inventaireTamagotchi.setNbSavons(inventaireTamagotchi.getNbSavons() - 1);
    }


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

    @Override
    public String toString() {
        return "Nom : " + nomTamagotchi + "\n" +
                "Genre : " + genre + "\n" +
                "Date de naissance : " + dateNaissance + "\n" +
                "User ID : " + userId + "\n\n" +
                "===== Statistiques =====\n" +
                "Vie : " + statsTamagotchi.getVie() + "\n" +
                "Faim : " + statsTamagotchi.getFaim() + "\n" +
                "Soif : " + statsTamagotchi.getSoif() + "\n" +
                "Santé : " + statsTamagotchi.getSante() + "\n" +
                "Énergie : " + statsTamagotchi.getEnergie() + "\n" +
                "Hygiène : " + statsTamagotchi.getHygiene() + "\n" +
                "Dernière MAJ (stats) : " + statsTamagotchi.getDernierUpdate() + "\n\n" +
                "===== Inventaire =====\n" +
                "Nourritures : " + inventaireTamagotchi.getNbNourritures() + "\n" +
                "Boissons : " + inventaireTamagotchi.getNbBoissons() + "\n" +
                "Médicaments : " + inventaireTamagotchi.getNbMedicaments() + "\n" +
                "Lits : " + inventaireTamagotchi.getNbLits() + "\n" +
                "Savons : " + inventaireTamagotchi.getNbSavons() + "\n" +
                "Dernière MAJ (inventaire) : " + inventaireTamagotchi.getDernierUpdate();
    }

}