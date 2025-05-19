package com.example.tamagotchi.view;

import static org.junit.Assert.assertTrue;

import android.content.Context;
import android.view.View;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.test.core.app.ActivityScenario;
import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;

public class SplashActivityTest {
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

    @Test
    public void testLoads() {
        try(ActivityScenario<SplashActivity> scenario = ActivityScenario.launch(SplashActivity.class)) {
            scenario.onActivity(activity -> {
                // Verifier que la page charge bien sans erreur.
                assertTrue(true);
            });
        }
    }
}
