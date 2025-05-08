package com.example.tamagotchi.model;

import static org.junit.jupiter.api.Assertions.*;

import com.google.firebase.Timestamp;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;

class TamagotchiTest {


    private Timestamp now;
    private Tamagotchi tamagotchiTest;
    private Timestamp dixJoursPlusTot;
    private Statistique stat;
    private Inventaire invent;



    @BeforeEach
    void setUp() {
        this.dixJoursPlusTot = new Timestamp(System.currentTimeMillis()/1000 - (60 * 60 * 24 * 10), 0);

        this.now = new Timestamp(System.currentTimeMillis()/1000, 0);

        this.stat = new Statistique(50, 10, 45, 70, 30, 40, 60);

        this.invent= new Inventaire(10, 9, 8, 7, 6, 5);


        this.tamagotchiTest = new Tamagotchi(
                "nvTam",
                "1", "abbie",
                "male",
                this.dixJoursPlusTot,
                this.now,
                this.stat,
                this.invent
        );
    }

    void resetTamagotchi(){
        this.tamagotchiTest.setIdTamagotchi("nvTam");
        this.tamagotchiTest.setIdJoueur("1");
        this.tamagotchiTest.setNomTamagotchi("abbie");
        this.tamagotchiTest.setGenre("male");
        this.tamagotchiTest.setDateNaissance(dixJoursPlusTot);
        this.tamagotchiTest.setDernierUpdate(now);
        this.tamagotchiTest.setStatsTamagotchi(stat);
        this.tamagotchiTest.setInventaireTamagotchi(invent);
    }

    @Test
    void getIdTamagotchi() {
        assertEquals("nvTam", this.tamagotchiTest.getIdTamagotchi());
    }

    @Test
    void setIdTamagotchi() {
        this.tamagotchiTest.setIdTamagotchi("nouveauT");
        assertEquals("nouveauT", this.tamagotchiTest.getIdTamagotchi());
        resetTamagotchi();
    }

    @Test
    void getIdJoueur() {
        assertEquals("1", this.tamagotchiTest.getIdJoueur());
    }

    @Test
    void setIdJoueur() {
        this.tamagotchiTest.setIdJoueur("Nb12");
        assertEquals("Nb12", this.tamagotchiTest.getIdJoueur());
        resetTamagotchi();
    }

    @Test
    void getNomTamagotchi() {
        assertEquals("abbie", this.tamagotchiTest.getNomTamagotchi());
    }

    @Test
    void setNomTamagotchi() {
        this.tamagotchiTest.setNomTamagotchi("Cain");
        assertEquals("Cain", this.tamagotchiTest.getNomTamagotchi());
        resetTamagotchi();
    }

    @Test
    void getGenre() {
        assertEquals("male", this.tamagotchiTest.getGenre());
    }

    @Test
    void setGenre() {
        this.tamagotchiTest.setGenre("Non-binaire");
        assertEquals("Non-binaire", this.tamagotchiTest.getGenre());
        resetTamagotchi();
    }

    @Test
    void getDateNaissance() {
        assertEquals(dixJoursPlusTot, this.tamagotchiTest.getDateNaissance());
    }

    @Test
    void setDateNaissance() {
        this.tamagotchiTest.setDateNaissance(new Timestamp((System.currentTimeMillis() / 1000) - (60 * 60 * 24 * 7), 0));
        assertEquals(new Timestamp(System.currentTimeMillis()/1000 - (60 * 60 * 24 * 7), 0), this.tamagotchiTest.getDateNaissance());
        resetTamagotchi();
    }

    @Test
    void getDernierUpdate() {
        assertEquals(now, this.tamagotchiTest.getDernierUpdate());

    }

    @Test
    void setDernierUpdate() {
        Timestamp value = new Timestamp(System.currentTimeMillis()/1000 - (60 ), 0);
        this.tamagotchiTest.setDernierUpdate(value);
        assertEquals(value, this.tamagotchiTest.getDernierUpdate());
        resetTamagotchi();
    }

    @Test
    void getStatsTamagotchi() {
        assertEquals(stat, this.tamagotchiTest.getStatsTamagotchi());
    }

    @Test
    void setStatsTamagotchi() {

         Statistique stat2= new Statistique(27, 5, 12, 15, 40, 80, 10);
         this.tamagotchiTest.setStatsTamagotchi(stat2);
         assertEquals(27, this.tamagotchiTest.getStatsTamagotchi().getVie());
         assertEquals(5,this.tamagotchiTest.getStatsTamagotchi().getFaim() );
         assertEquals(12, this.tamagotchiTest.getStatsTamagotchi().getSoif());
         assertEquals(15, this.tamagotchiTest.getStatsTamagotchi().getSante());
         assertEquals(40, this.tamagotchiTest.getStatsTamagotchi().getEnergie());
         assertEquals(80,this.tamagotchiTest.getStatsTamagotchi().getHygiene());
         assertEquals(10, this.tamagotchiTest.getStatsTamagotchi().getBonheur());

         resetTamagotchi();

    }

    @Test
    void getInventaireTamagotchi() {
        assertEquals(invent, this.tamagotchiTest.getInventaireTamagotchi());
    }

    @Test
    void setInventaireTamagotchi() {
        Inventaire invent2= new Inventaire(5, 6, 7, 8, 9, 10);
        this.tamagotchiTest.setInventaireTamagotchi(invent2);

        assertEquals(5, this.tamagotchiTest.getInventaireTamagotchi().getNbNourritures());
        assertEquals(6,tamagotchiTest.getInventaireTamagotchi().getNbBoissons());
        assertEquals(7,tamagotchiTest.getInventaireTamagotchi().getNbMedicaments());
        assertEquals(8,tamagotchiTest.getInventaireTamagotchi().getNbLits());
        assertEquals(9,tamagotchiTest.getInventaireTamagotchi().getNbSavons());
        assertEquals(10, tamagotchiTest.getInventaireTamagotchi().getNbJouets());
        resetTamagotchi();
    }

    @Test
    void getAge() {
        assertEquals(10,this.tamagotchiTest.getAge());
    }
}