package com.example.tamagotchi.view;

import static org.junit.Assert.assertTrue;

import android.content.Context;
import android.view.View;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.test.core.app.ActivityScenario;
import androidx.test.platform.app.InstrumentationRegistry;

import com.example.tamagotchi.utils.TestUtils;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;

public class CreerTamagotchiActivityTest {
    private static Context appContext;
    private static View view;

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();


    @BeforeClass
    public static void setUpClass() {
        // Création d'un context pour l'execution des tests, et initialisation des données.
        appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        view = new View(appContext);
        TestUtils.resetTamagotchis();
    }

    @AfterClass
    public static void tearDownClass() {
        TestUtils.resetTamagotchis();
    }

    @Test
    public void testLoads() {
        try(ActivityScenario<CreerTamagotchiActivity> scenario = ActivityScenario.launch(CreerTamagotchiActivity.class)) {
            scenario.onActivity(activity -> {
                // Verifier que la page charge bien sans erreur.
                assertTrue(true);
            });
        }
    }
}
