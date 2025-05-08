package com.example.tamagotchi.model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class InventaireTest {

    private Inventaire inventaire;

    @BeforeEach
    void setUp() {
        this.inventaire = new Inventaire(10, 9, 8, 7, 6, 5);
    }

    void resetInventaire() {
        this.inventaire.setNbNourritures(10);
        this.inventaire.setNbBoissons(9);
        this.inventaire.setNbMedicaments(8);
        this.inventaire.setNbLits(7);
        this.inventaire.setNbSavons(6);
        this.inventaire.setNbJouets(5);
    }

    @Test
    void getNbNourritures() {
        assertEquals(10, this.inventaire.getNbNourritures());
    }

    @Test
    void setNbNourritures() {
        this.inventaire.setNbNourritures(1);
        assertEquals(1, this.inventaire.getNbNourritures());
        this.resetInventaire();
    }

    @Test
    void getNbBoissons() {
        assertEquals(9, this.inventaire.getNbBoissons());
    }

    @Test
    void setNbBoissons() {
        this.inventaire.setNbBoissons(2);
        assertEquals(2,this.inventaire.getNbBoissons());
    }

    @Test
    void getNbMedicaments() {
        assertEquals(8, this.inventaire.getNbMedicaments());
    }

    @Test
    void setNbMedicaments() {
        this.inventaire.setNbMedicaments(3);
        assertEquals(3, this.inventaire.getNbMedicaments());
    }

    @Test
    void getNbLits() {
        assertEquals(7, this.inventaire.getNbLits());
    }

    @Test
    void setNbLits() {
        this.inventaire.setNbLits(4);
        assertEquals(4, this.inventaire.getNbLits());
    }

    @Test
    void getNbSavons() {
        assertEquals(6, this.inventaire.getNbSavons());
    }

    @Test
    void setNbSavons() {
        this.inventaire.setNbSavons(5);
        assertEquals(5, this.inventaire.getNbSavons());
    }

    @Test
    void getNbJouets() {
        assertEquals(5, this.inventaire.getNbJouets());
    }
    @Test
    void setNbJouets() {
        this.inventaire.setNbJouets(6);
        assertEquals(6, this.inventaire.getNbJouets());
    }



    @Test
    void augmenterInventairesAutomatiquement() {
        this.inventaire.augmenterInventairesAutomatiquement(2);

        assertEquals(30, this.inventaire.getNbNourritures());
        assertEquals(29, this.inventaire.getNbBoissons());
        assertEquals(28, this.inventaire.getNbMedicaments());
        assertEquals(27, this.inventaire.getNbLits());
        assertEquals(26, this.inventaire.getNbSavons());
        assertEquals(25, this.inventaire.getNbJouets());
    }
}