package com.example.tamagotchi.view;

import static org.junit.Assert.assertTrue;

import android.content.Context;
import android.view.View;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.test.core.app.ActivityScenario;
import androidx.test.platform.app.InstrumentationRegistry;

import com.example.tamagotchi.model.Joueur;
import com.example.tamagotchi.utils.TestUtils;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;

/*
-----------------------------
    Date : 19/05/2025

    Membres qui travaillent dessus : Lina BOUGUETTAYA

    Que fait le code ? : Verfie l'execution de la page modification du tamagotchi
-----------------------------
*/
public class ModifierTamagotchiActivityTest {
    private static Context appContext;
    private static View view;

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();


    @BeforeClass
    public static void setUpClass() {
        // Création d'un context pour l'execution des tests, et initialisation des données.
        appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        view = new View(appContext);
    }

    @AfterClass
    public static void tearDownClass() {
        TestUtils.resetTamagotchis();
    }

    @Test
    public void testLoads() {
        // Tester avec l'utilisateur des test, le Tamagotchi retourné est blybly.
        Joueur.connexion(appContext, "blabla@example.com", "bleble");
        try(ActivityScenario<ModifierTamagotchiActivity> scenario = ActivityScenario.launch(ModifierTamagotchiActivity.class)) {
            scenario.onActivity(activity -> {
                // Verifier que la page charge bien sans erreur.
                assertTrue(true);
            });
        }
    }
}
