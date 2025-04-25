package com.example.tamagotchi.model;
/*
-----------------------------
    Date : 13/04/2025

    Membres qui travaillent dessus : Marwan DENAGNON

    Que fait le code ? : Rien, juste une classe pour les statistiques d'un tamagotchi
    Changement par rapport à la version précédente : Suppression de l'attribut dernierUpdate pour le mettre directement dans l'objet Tamagotchi
-----------------------------
*/

public class Statistique {
    private double vie;
    private double faim;
    private double soif;
    private double sante;
    private double energie;
    private double hygiene;
    private double bonheur;

    public Statistique(double vie, double faim, double soif, double sante, double energie, double hygiene, double bonheur){
        this.vie = vie;
        this.faim = faim;
        this.soif = soif;
        this.sante = sante;
        this.energie = energie;
        this.hygiene = hygiene;
        this.bonheur = bonheur;
    }

    public Statistique(){}; // Obligatoire pour Firestore

    public double getVie(){
        return vie;
    }

    public void setVie(double vie) {
        this.vie = vie;
    }

    public double getFaim() {
        return faim;
    }

    public void setFaim(double faim) {
        this.faim = faim;
    }

    public double getSoif() {
        return soif;
    }

    public void setSoif(double soif) {
        this.soif = soif;
    }

    public double getSante() {
        return sante;
    }

    public void setSante(double sante) {
        this.sante = sante;
    }

    public double getEnergie() {
        return energie;
    }

    public void setEnergie(double energie) {
        this.energie = energie;
    }

    public double getHygiene() {
        return hygiene;
    }

    public void setHygiene(double hygiene) {
        this.hygiene = hygiene;
    }

    public void setBonheur(double bonheur) {
        this.bonheur = bonheur;
    }

    public double getBonheur() {
        return bonheur;
    }
}




