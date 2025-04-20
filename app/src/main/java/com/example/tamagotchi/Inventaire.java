package com.example.tamagotchi;
/*
-----------------------------
    Date : 13/04/2025

    Membres qui travaillent dessus : Marwan DENAGNON

    Que fait le code ? : Rien, juste une classe pour l'inventaire d'un tamagotchi
-----------------------------
*/

public class Inventaire {
    private int nbNourritures;
    private int nbBoissons;
    private int nbMedicaments;
    private int nbLits;
    private int nbSavons;
    private int nbJouets;

    // Constructeur
    public Inventaire(int nbNourritures, int nbBoissons, int nbMedicaments, int nbLits, int nbSavons, int nbJouets) {
        this.nbNourritures = nbNourritures;
        this.nbBoissons = nbBoissons;
        this.nbMedicaments = nbMedicaments;
        this.nbLits = nbLits;
        this.nbSavons = nbSavons;
        this.nbJouets = nbJouets;
    }

    public Inventaire(){};

    // Getters et Setters
    public int getNbNourritures() {
        return nbNourritures;
    }

    public void setNbNourritures(int nbNourritures) {
        this.nbNourritures = nbNourritures;
    }

    public int getNbBoissons() {
        return nbBoissons;
    }

    public void setNbBoissons(int nbBoissons) {
        this.nbBoissons = nbBoissons;
    }

    public int getNbMedicaments() {
        return nbMedicaments;
    }

    public void setNbMedicaments(int nbMedicaments) {
        this.nbMedicaments = nbMedicaments;
    }

    public int getNbLits() {
        return nbLits;
    }

    public void setNbLits(int nbLits) {
        this.nbLits = nbLits;
    }

    public int getNbSavons() {
        return nbSavons;
    }

    public void setNbSavons(int nbSavons) {
        this.nbSavons = nbSavons;
    }

    public void setNbJouets(int nbJouets) {
        this.nbJouets = nbJouets;
    }

    public int getNbJouets() {
        return nbJouets;
    }
}
