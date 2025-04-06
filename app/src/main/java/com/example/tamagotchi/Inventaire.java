package com.example.tamagotchi;

import java.time.Duration;
import java.time.LocalDateTime;
/*
-----------------------------
    Date : 05/04/2025

    Membres qui travaillent dessus :  BOUGUETTAYA Lina

    Que fait le code ? : Classe d'inventaire qui définit le système d'augmentation et de diminution des stocks.
-----------------------------
*/

public class Inventaire {
    //10 objets par catégorie à la connexion initiale
    private int nbNourritures = 10;

    private int nbEau= 10;
    private int nbJouets= 10;
    private int nbMedicaments =10;
    private int nbLits= 10;
    private int nbSavons= 10;
    private LocalDateTime dernierUpdate;


    //constructeur:
    public Inventaire(){
        this.dernierUpdate= LocalDateTime.now();
        //démarrage?
    }
    public void augmenterInventairesAutomatiquement(){
        //var de temps iminant:
        LocalDateTime maintenant= LocalDateTime.now();
        //pour chaque heure on gagne 10 objets dans l'inventaire:

        //on calcul le nb d'heures écoulées depuis le dernier update:

        long heuresEcoules= Duration.between(dernierUpdate, maintenant).toHours();

        if(heuresEcoules>0){
            int augment = (int) heuresEcoules * 10 ;

            nbNourritures += augment;
            nbJouets+=augment;
            nbMedicaments+=augment;
            nbLits+=augment;
            nbSavons+=augment;
            nbEau+=augment;

            dernierUpdate= maintenant;
        }
    }

    public void modifierInventaires(String categorie, int valeur){
        switch(categorie){
            case "nbNourritures": nbNourritures +=valeur;
                break;
            case "nbJouets": nbJouets+=valeur;
                break;
            case "nbMedicaments": nbMedicaments+= valeur;
                break;
            case "nbLits": nbLits+=valeur;
                break;
            case "nbSavons": nbSavons+=valeur;
                break;
            case "nbEau": nbEau+=valeur;
                break;
        }
        dernierUpdate= LocalDateTime.now();
    }

    //les getters:
    public int getNbNourritures(){
        return nbNourritures;
    }
    public int getNbJouets(){
        return nbJouets;
    }
    public int getNbMedicaments(){
        return nbMedicaments;
    }
    public int getNbLits(){
        return nbLits;
    }
    public int getNbSavons(){
        return nbSavons;
    }
    public int getNbEau(){
        return nbEau;
    }
}
