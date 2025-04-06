package com.example.tamagotchi;

import java.time.Duration;
import java.time.LocalDateTime;

/*
-----------------------------
    Date : 05/04/2025

    Membres qui travaillent dessus : Marwan DENAGNON, Lina BOUGUETTAYA

    Que fait le code ? : Classe des Stats qui définissent la dégradation, modification des stats au fil du temps
                         Définition de quand meurt le tamagotchi
-----------------------------
*/
public class Statistique {
    private int soif=100;
    private int sante=100;
    private int energie=100;
    private int hygiene=100;
    private int loisir=100;
    private int faim=100;
    private LocalDateTime dernierUpdate;
    public Statistique(int faim, int soif, int sante, int energie, int hygiene, int loisir, LocalDateTime dernierUpdate){
        this.faim = faim;
        this.soif = soif;
        this.sante = sante;
        this.energie = energie;
        this.hygiene = hygiene;
        this.loisir= loisir;
        this.dernierUpdate= dernierUpdate;
    }

    public Statistique(){
        this.dernierUpdate=LocalDateTime.now();
    }


    public int getFaim() {
        return faim;
    }

    public void setFaim(int faim) {
        this.faim = faim;
    }

    public int getSoif() {
        return soif;
    }

    public void setSoif(int soif) {
        this.soif = soif;
    }

    public int getSante() {
        return sante;
    }

    public void setSante(int sante) {
        this.sante = sante;
    }

    public int getEnergie() {
        return energie;
    }

    public void setEnergie(int energie) {
        this.energie = energie;
    }

    public int getHygiene() {
        return hygiene;
    }

    public void setHygiene(int hygiene) {
        this.hygiene = hygiene;
    }

    public int getLoisir(){
        return loisir;
    }
    public void setLoisir(int loisir){
        this.loisir= loisir;
    }

    public LocalDateTime getDernierUpdate() {
        return dernierUpdate;
    }

    public void setDernierUpdate(LocalDateTime dernierUpdate) {
        this.dernierUpdate = dernierUpdate;
    }

    //méthodes sur le diag de classes:
    public void degraderStatsAutomatiquement() {
        //même système que dans inventaire
        /*les stats se dégradent de 2% toutes les heures*/
        LocalDateTime maintenant = LocalDateTime.now();
        long heuresEcoules= Duration.between(dernierUpdate, maintenant).toHours();

        if(heuresEcoules>0){
            int reduc = (int) heuresEcoules * 2 ;

            //les stats sont limité entre 100 et 0 :
            soif= borner(soif-reduc);
            sante= borner(sante -reduc);
            energie = borner(energie - reduc);
            hygiene = borner(energie - reduc);
            loisir = borner(energie - reduc);
            faim= borner(faim-reduc);

            dernierUpdate = maintenant;
        }
    }

    //méthode modifier stat:
    public void modifierStatistiques(String categorie, int valeur){
        switch (categorie){
            case "soif": soif= borner(soif + valeur);
                break;
            case "sante": sante= borner(sante + valeur);
                break;
            case "energie": energie= borner(energie + valeur);
                break;
            case "propreté": hygiene= borner(hygiene + valeur);
                break;
            case "faim": faim = borner(faim + valeur);
                break;
            case "loisir": loisir= borner(loisir + valeur);
                break;
        }
        dernierUpdate= LocalDateTime.now();
    }

    //vie: moyenne de tous les stats
    public float vie(){
        return  (float)(soif+ sante+energie+hygiene+loisir+faim)/6;
    }

    //méthode pour savoir si le tamagotchi est en vie :
    public boolean estVivant(){
        return  this.vie()> 0 && this.sante>0 ;
        //meurt si sa santé est 0 même si les autres sont au max
        //meurt si toutes les vitalités sont à 0 aussi
    }

    /*méthode qui aide à borner les stats:
        - limite minimale à 0
        - limite maximale à 100
        - lorsqu'on atteint 100 on peut continuer à utiliser prendre soin de l'animal ex le nourir ect
        mais ça ne se montre pas en stat*/
    private int borner(int valeur){
        if (valeur<0) {
            return 0;
        }
        if (valeur>100){
            return 100;
        }
        return valeur ;
    }
}




