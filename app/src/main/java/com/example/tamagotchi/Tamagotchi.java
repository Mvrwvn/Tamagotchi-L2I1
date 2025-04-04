package com.example.tamagotchi;
/*
-----------------------------
    Date : 31/03/2025

    Membres qui travaillent dessus : Marwan DENAGNON

    Que fait le code ? : Va permettre de créer un objet tamagotchi et d'enregistrer ses données dans Firestore (tous les getters)
-----------------------------
*/
import java.time.LocalDate;
import java.time.Period;

public class Tamagotchi{

    private String userId;
    private String nomTamagotchi;
    private LocalDate dateNaissance;
    private Statistique statsTamagotchi;
    public Tamagotchi(String userId, String nomTamagotchi, LocalDate dateNaissance, Statistique statsTamagotchi){
        this.userId = userId;
        this.nomTamagotchi = nomTamagotchi;
        this.dateNaissance = dateNaissance;
        this.statsTamagotchi = statsTamagotchi;
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
    public int getAge() {
        return Period.between(LocalDate.now(), dateNaissance).getDays();
    }

    public Statistique getStatsTamagotchi() {
        return statsTamagotchi;
    }

    public void setStatsTamagotchi(Statistique statsTamagotchi){
        this.statsTamagotchi = statsTamagotchi;
    }
}