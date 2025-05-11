package com.example.tamagotchi.model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/*
-----------------------------
    Date : 10/05/2025

    Membres qui travaillent dessus : Lina Bouguettaya

    Que fait le code ? : Test l'intégrité des getters et setters de Statistique
    -----------------------------
*/
class StatistiqueTest {

    private Statistique statistiqueTest;

    @BeforeEach
    void setUp() {
        this.statistiqueTest= new Statistique(50,
                60, 25,
                70, 40,
                30, 75);
    }

    void resetStatistique(){
        this.statistiqueTest.setFaim(60);
        this.statistiqueTest.setSoif(25);
        this.statistiqueTest.setSante(70);
        this.statistiqueTest.setEnergie(40);
        this.statistiqueTest.setHygiene(30);
        this.statistiqueTest.setBonheur(75);
    }

    @Test
    void getVie() {
        assertEquals(50, this.statistiqueTest.getVie());
    }

    @Test
    void getFaim() {
        assertEquals(60,this.statistiqueTest.getFaim());
    }

    @Test
    void setFaim() {
        this.statistiqueTest.setFaim(30);
        assertEquals(30, this.statistiqueTest.getFaim());
        resetStatistique();
    }

    @Test
    void getSoif() {
        assertEquals(25,this.statistiqueTest.getSoif());
    }

    @Test
    void setSoif() {
        this.statistiqueTest.setSoif(85);
        assertEquals(85, this.statistiqueTest.getSoif());
        resetStatistique();
    }

    @Test
    void getSante() {
        assertEquals(70,this.statistiqueTest.getSante());
    }

    @Test
    void setSante() {
        this.statistiqueTest.setSoif(40);
        assertEquals(40, this.statistiqueTest.getSoif());
        resetStatistique();
    }

    @Test
    void getEnergie() {
        assertEquals(40,this.statistiqueTest.getEnergie());
    }

    @Test
    void setEnergie() {
        this.statistiqueTest.setEnergie(95);
        assertEquals(95, this.statistiqueTest.getEnergie());
        resetStatistique();
    }

    @Test
    void getHygiene() {
        assertEquals(30,this.statistiqueTest.getHygiene());
    }

    @Test
    void setHygiene() {
        this.statistiqueTest.setHygiene(67);
        assertEquals(67,this.statistiqueTest.getHygiene());
        resetStatistique();
    }

    @Test
    void setBonheur() {
        this.statistiqueTest.setBonheur(55);
        assertEquals(55, this.statistiqueTest.getBonheur());
        resetStatistique();
    }

    @Test
    void getBonheur() {
        assertEquals(75, this.statistiqueTest.getBonheur());
    }


    @Test
    void degraderStatAutomatiquement() {
        this.statistiqueTest.degraderStatAutomatiquement(3);
        assertEquals(64,this.statistiqueTest.getSante());
        assertEquals(54,this.statistiqueTest.getFaim());
        assertEquals(34,this.statistiqueTest.getEnergie());
        assertEquals(24,this.statistiqueTest.getHygiene());
        assertEquals(19,this.statistiqueTest.getSoif());
        assertEquals(69,this.statistiqueTest.getBonheur());
    }
}