package com.example.tamagotchi.model;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import android.content.Context;

import androidx.test.platform.app.InstrumentationRegistry;

import com.google.firebase.auth.FirebaseAuth;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

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
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();

        boolean result = Joueur.connexion(appContext, "blabla@example.com", "bleble");
        assertTrue(result);
        FirebaseAuth.getInstance().signOut();
        assertFalse(Joueur.connexion(appContext, "unknown@example.com", "foobar"));
        FirebaseAuth.getInstance().signOut();
    }

    @Test
    public void testRenitialiserMotDePasse() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();

        //assertTrue(Joueur.renitialiserMotDePasse(appContext, "blabla@example.com"));
        //assertTrue(Joueur.renitialiserMotDePasse(appContext, "unknown@example.com"));
    }
}
