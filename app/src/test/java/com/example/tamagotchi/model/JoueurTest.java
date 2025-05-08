package com.example.tamagotchi.model;

import static org.junit.jupiter.api.Assertions.*;


import com.google.firebase.auth.FirebaseAuth;

import org.junit.Assert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class JoueurTest {

    private Joueur joueurInscrit;
    private Joueur joueurInconnu;

    @BeforeEach
    void setUp() {
        this.joueurInscrit = new Joueur("example@test.com", "1");
        this.joueurInconnu = new Joueur("inconnu@test.com", "999999");
    }


    @Test
    void getEmail() {
        assertEquals("example@test.com", this.joueurInscrit.getEmail());
    }

    @Test
    void setEmail() {
        this.joueurInconnu.setEmail("unknown@test.fr");
        assertEquals("unknown@test.fr", this.joueurInconnu.getEmail());

        //réinitialisation
        this.joueurInconnu.setEmail("inconnu@test.com");
 }

    @Test
    void getActiveTamagotchiId() {
        assertEquals("1", this.joueurInscrit.getActiveTamagotchiId());
    }

    @Test
    void setActiveTamagotchiId() {
        this.joueurInconnu.setActiveTamagotchiId("10000");
        assertEquals("10000", this.joueurInconnu.getActiveTamagotchiId());

        //réinitialisation
        this.joueurInconnu.setActiveTamagotchiId("999999");
    }
}