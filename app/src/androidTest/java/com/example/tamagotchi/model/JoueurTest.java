package com.example.tamagotchi.model;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import android.content.Context;

import androidx.test.platform.app.InstrumentationRegistry;

import com.google.firebase.auth.FirebaseAuth;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/*
-----------------------------
    Date : 10/05/2025

    Membres qui travaillent dessus : Lina Bouguettaya

    Que fait le code ? : Teste les fonctionnalités d'authentification.
    -----------------------------
*/
public class JoueurTest {
    @Before
    public void setUp() {
        FirebaseAuth.getInstance().signOut();
    }

    @After
    public void destroy() {
        FirebaseAuth.getInstance().signOut();
    }

    @Test
    public void testConnexion() {
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();

        boolean result = Joueur.connexion(appContext, "blabla@example.com", "bleble");
        // Si l'utilisateur existe, on attend un True
        assertTrue(result);
        FirebaseAuth.getInstance().signOut();

        // Si l'utilisateur n'existe pas, on attend un False
        assertFalse(Joueur.connexion(appContext, "unknown@example.com", "foobar"));
        FirebaseAuth.getInstance().signOut();
    }

    @Test
    public void testRenitialiserMotDePasse() {
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();

        /* Peu importe si l'utilsateur existe ou non, la fonction retourne True, l'envoie du mail
         est géré par Firebase, et la verification de l'existence aussi.*/
        assertTrue(Joueur.renitialiserMotDePasse(appContext, "blabla@example.com"));
        assertTrue(Joueur.renitialiserMotDePasse(appContext, "unknown@example.com"));
    }
}
