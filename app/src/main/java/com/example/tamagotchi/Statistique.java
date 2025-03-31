package com.example.tamagotchi;

import android.util.Log;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;

public class Statistique {
    private int faim;
    private int soif;
    private int sante;
    private int energie;
    private int hygiene;
    private LocalDateTime dernierUpdate;
    public Statistique(int faim, int soif, int sante, int energie, int hygiene, LocalDateTime dernierUpdate){
        this.faim = faim;
        this.soif = soif;
        this.sante = sante;
        this.energie = energie;
        this.hygiene = hygiene;
        this.dernierUpdate = dernierUpdate;
    }
    public void augementerfaim(int valeur){
        this.faim += valeur;
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

    public LocalDateTime getDernierUpdate() {
        return dernierUpdate;
    }

    public void setDernierUpdate(LocalDateTime dernierUpdate) {
        this.dernierUpdate = dernierUpdate;
    }
}




