package com.example.tamagotchi.view;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import android.content.Context;
import android.view.View;

import androidx.test.core.app.ActivityScenario;
import androidx.test.platform.app.InstrumentationRegistry;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;

import com.example.tamagotchi.model.Joueur;
import com.example.tamagotchi.model.Tamagotchi;
import com.example.tamagotchi.utils.TestUtils;

import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;

public class MainActivityTest {
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

    @Test
    public void testReposer0a0() {
        // Tester avec l'utilisateur des test, le Tamagotchi retourné est blybly.
        Joueur.connexion(appContext, "blabla@example.com", "bleble");
        TestUtils.setBlybly(0,0);
        try(ActivityScenario<MainActivity> scenario = ActivityScenario.launch(MainActivity.class)) {
            scenario.onActivity(activity -> {
                Tamagotchi activeTamagotchi = activity.getActiveTamagotchi();
                assertEquals(0, activeTamagotchi.getInventaireTamagotchi().getNbLits());
                assertEquals(0, activeTamagotchi.getStatsTamagotchi().getEnergie(), 0);

                activity.reposer(view);

                activeTamagotchi = activity.getActiveTamagotchi();
                assertEquals(0, activeTamagotchi.getInventaireTamagotchi().getNbLits());
                assertEquals(0, activeTamagotchi.getStatsTamagotchi().getEnergie(), 0);
            });
        }
    }

    @Test
    public void testReposer100a100() {
        // Tester avec l'utilisateur des test, le Tamagotchi retourné est blybly.
        Joueur.connexion(appContext, "blabla@example.com", "bleble");
        TestUtils.setBlybly(100,100);
        try(ActivityScenario<MainActivity> scenario = ActivityScenario.launch(MainActivity.class)) {
            scenario.onActivity(activity -> {
                Tamagotchi activeTamagotchi = activity.getActiveTamagotchi();
                assertEquals(100, activeTamagotchi.getInventaireTamagotchi().getNbLits());
                assertEquals(100, activeTamagotchi.getStatsTamagotchi().getEnergie(), 0);

                activity.reposer(view);

                activeTamagotchi = activity.getActiveTamagotchi();
                assertEquals(100, activeTamagotchi.getInventaireTamagotchi().getNbLits());
                assertEquals(100, activeTamagotchi.getStatsTamagotchi().getEnergie(), 0);
            });
        }
    }

    @Test
    public void testReposer50() {
        // Tester avec l'utilisateur des test, le Tamagotchi retourné est blybly.
        Joueur.connexion(appContext, "blabla@example.com", "bleble");
        TestUtils.setBlybly(50,50);
        try(ActivityScenario<MainActivity> scenario = ActivityScenario.launch(MainActivity.class)) {
            scenario.onActivity(activity -> {
                Tamagotchi activeTamagotchi = activity.getActiveTamagotchi();
                assertEquals(50, activeTamagotchi.getInventaireTamagotchi().getNbLits());
                assertEquals(50, activeTamagotchi.getStatsTamagotchi().getEnergie(), 0);

                activity.reposer(view);

                activeTamagotchi = activity.getActiveTamagotchi();
                assertTrue(50 > activeTamagotchi.getInventaireTamagotchi().getNbLits());
                assertTrue(50 < activeTamagotchi.getStatsTamagotchi().getEnergie());
            });
        }
    }

    @Test
    public void testHydrater0a0() {
        // Tester avec l'utilisateur des test, le Tamagotchi retourné est blybly.
        Joueur.connexion(appContext, "blabla@example.com", "bleble");
        TestUtils.setBlybly(0,0);
        try(ActivityScenario<MainActivity> scenario = ActivityScenario.launch(MainActivity.class)) {
            scenario.onActivity(activity -> {
                Tamagotchi activeTamagotchi = activity.getActiveTamagotchi();
                assertEquals(0, activeTamagotchi.getInventaireTamagotchi().getNbBoissons());
                assertEquals(0, activeTamagotchi.getStatsTamagotchi().getSoif(), 0);

                activity.hydrater(view);

                activeTamagotchi = activity.getActiveTamagotchi();
                assertEquals(0, activeTamagotchi.getInventaireTamagotchi().getNbBoissons());
                assertEquals(0, activeTamagotchi.getStatsTamagotchi().getSoif(), 0);
            });
        }
    }

    @Test
    public void testHydrater100a100() {
        // Tester avec l'utilisateur des test, le Tamagotchi retourné est blybly.
        Joueur.connexion(appContext, "blabla@example.com", "bleble");
        TestUtils.setBlybly(100,100);
        try(ActivityScenario<MainActivity> scenario = ActivityScenario.launch(MainActivity.class)) {
            scenario.onActivity(activity -> {
                Tamagotchi activeTamagotchi = activity.getActiveTamagotchi();
                assertEquals(100, activeTamagotchi.getInventaireTamagotchi().getNbBoissons());
                assertEquals(100, activeTamagotchi.getStatsTamagotchi().getSoif(), 0);

                activity.hydrater(view);

                activeTamagotchi = activity.getActiveTamagotchi();
                assertEquals(100, activeTamagotchi.getInventaireTamagotchi().getNbBoissons());
                assertEquals(100, activeTamagotchi.getStatsTamagotchi().getSoif(), 0);
            });
        }
    }

    @Test
    public void testHydrater50() {
        // Tester avec l'utilisateur des test, le Tamagotchi retourné est blybly.
        Joueur.connexion(appContext, "blabla@example.com", "bleble");
        TestUtils.setBlybly(50,50);
        try(ActivityScenario<MainActivity> scenario = ActivityScenario.launch(MainActivity.class)) {
            scenario.onActivity(activity -> {
                Tamagotchi activeTamagotchi = activity.getActiveTamagotchi();
                assertEquals(50, activeTamagotchi.getInventaireTamagotchi().getNbBoissons());
                assertEquals(50, activeTamagotchi.getStatsTamagotchi().getSoif(), 0);

                activity.hydrater(view);

                activeTamagotchi = activity.getActiveTamagotchi();
                assertTrue(50 > activeTamagotchi.getInventaireTamagotchi().getNbBoissons());
                assertTrue(50 < activeTamagotchi.getStatsTamagotchi().getSoif());
            });
        }
    }

    @Test
    public void testBrosser0a0() {
        // Tester avec l'utilisateur des test, le Tamagotchi retourné est blybly.
        Joueur.connexion(appContext, "blabla@example.com", "bleble");
        TestUtils.setBlybly(0,0);
        try(ActivityScenario<MainActivity> scenario = ActivityScenario.launch(MainActivity.class)) {
            scenario.onActivity(activity -> {
                Tamagotchi activeTamagotchi = activity.getActiveTamagotchi();
                assertEquals(0, activeTamagotchi.getInventaireTamagotchi().getNbSavons());
                assertEquals(0, activeTamagotchi.getStatsTamagotchi().getHygiene(), 0);

                activity.brosser(view);

                activeTamagotchi = activity.getActiveTamagotchi();
                assertEquals(0, activeTamagotchi.getInventaireTamagotchi().getNbSavons());
                assertEquals(0, activeTamagotchi.getStatsTamagotchi().getHygiene(), 0);
            });
        }
    }

    @Test
    public void testBrosser100a100() {
        // Tester avec l'utilisateur des test, le Tamagotchi retourné est blybly.
        Joueur.connexion(appContext, "blabla@example.com", "bleble");
        TestUtils.setBlybly(100,100);
        try(ActivityScenario<MainActivity> scenario = ActivityScenario.launch(MainActivity.class)) {
            scenario.onActivity(activity -> {
                Tamagotchi activeTamagotchi = activity.getActiveTamagotchi();
                assertEquals(100, activeTamagotchi.getInventaireTamagotchi().getNbSavons());
                assertEquals(100, activeTamagotchi.getStatsTamagotchi().getHygiene(), 0);

                activity.brosser(view);

                activeTamagotchi = activity.getActiveTamagotchi();
                assertEquals(100, activeTamagotchi.getInventaireTamagotchi().getNbSavons());
                assertEquals(100, activeTamagotchi.getStatsTamagotchi().getHygiene(), 0);
            });
        }
    }

    @Test
    public void testBrosser50() {
        // Tester avec l'utilisateur des test, le Tamagotchi retourné est blybly.
        Joueur.connexion(appContext, "blabla@example.com", "bleble");
        TestUtils.setBlybly(50,50);
        try(ActivityScenario<MainActivity> scenario = ActivityScenario.launch(MainActivity.class)) {
            scenario.onActivity(activity -> {
                Tamagotchi activeTamagotchi = activity.getActiveTamagotchi();
                assertEquals(50, activeTamagotchi.getInventaireTamagotchi().getNbSavons());
                assertEquals(50, activeTamagotchi.getStatsTamagotchi().getHygiene(), 0);

                activity.brosser(view);

                activeTamagotchi = activity.getActiveTamagotchi();
                assertTrue(50 > activeTamagotchi.getInventaireTamagotchi().getNbSavons());
                assertTrue(50 < activeTamagotchi.getStatsTamagotchi().getHygiene());
            });
        }
    }

    @Test
    public void testNourrir0a0() {
        // Tester avec l'utilisateur des test, le Tamagotchi retourné est blybly.
        Joueur.connexion(appContext, "blabla@example.com", "bleble");
        TestUtils.setBlybly(0,0);
        try(ActivityScenario<MainActivity> scenario = ActivityScenario.launch(MainActivity.class)) {
            scenario.onActivity(activity -> {
                Tamagotchi activeTamagotchi = activity.getActiveTamagotchi();
                assertEquals(0, activeTamagotchi.getInventaireTamagotchi().getNbNourritures());
                assertEquals(0, activeTamagotchi.getStatsTamagotchi().getFaim(), 0);

                activity.nourrir(view);

                activeTamagotchi = activity.getActiveTamagotchi();
                assertEquals(0, activeTamagotchi.getInventaireTamagotchi().getNbNourritures());
                assertEquals(0, activeTamagotchi.getStatsTamagotchi().getFaim(), 0);
            });
        }
    }

    @Test
    public void testNourrir100a100() {
        // Tester avec l'utilisateur des test, le Tamagotchi retourné est blybly.
        Joueur.connexion(appContext, "blabla@example.com", "bleble");
        TestUtils.setBlybly(100,100);
        try(ActivityScenario<MainActivity> scenario = ActivityScenario.launch(MainActivity.class)) {
            scenario.onActivity(activity -> {
                Tamagotchi activeTamagotchi = activity.getActiveTamagotchi();
                assertEquals(100, activeTamagotchi.getInventaireTamagotchi().getNbNourritures());
                assertEquals(100, activeTamagotchi.getStatsTamagotchi().getFaim(), 0);

                activity.nourrir(view);

                activeTamagotchi = activity.getActiveTamagotchi();
                assertEquals(100, activeTamagotchi.getInventaireTamagotchi().getNbNourritures());
                assertEquals(100, activeTamagotchi.getStatsTamagotchi().getFaim(), 0);
            });
        }
    }

    @Test
    public void testNourrir50() {
        // Tester avec l'utilisateur des test, le Tamagotchi retourné est blybly.
        Joueur.connexion(appContext, "blabla@example.com", "bleble");
        TestUtils.setBlybly(50,50);
        try(ActivityScenario<MainActivity> scenario = ActivityScenario.launch(MainActivity.class)) {
            scenario.onActivity(activity -> {
                Tamagotchi activeTamagotchi = activity.getActiveTamagotchi();
                assertEquals(50, activeTamagotchi.getInventaireTamagotchi().getNbNourritures());
                assertEquals(50, activeTamagotchi.getStatsTamagotchi().getFaim(), 0);

                activity.nourrir(view);

                activeTamagotchi = activity.getActiveTamagotchi();
                assertTrue(50 > activeTamagotchi.getInventaireTamagotchi().getNbNourritures());
                assertTrue(50 < activeTamagotchi.getStatsTamagotchi().getFaim());
            });
        }
    }

    @Test
    public void testSoigner0a0() {
        // Tester avec l'utilisateur des test, le Tamagotchi retourné est blybly.
        Joueur.connexion(appContext, "blabla@example.com", "bleble");
        TestUtils.setBlybly(0,0);
        try(ActivityScenario<MainActivity> scenario = ActivityScenario.launch(MainActivity.class)) {
            scenario.onActivity(activity -> {
                Tamagotchi activeTamagotchi = activity.getActiveTamagotchi();
                assertEquals(0, activeTamagotchi.getInventaireTamagotchi().getNbMedicaments());
                assertEquals(0, activeTamagotchi.getStatsTamagotchi().getSante(), 0);

                activity.soigner(view);

                activeTamagotchi = activity.getActiveTamagotchi();
                assertEquals(0, activeTamagotchi.getInventaireTamagotchi().getNbMedicaments());
                assertEquals(0, activeTamagotchi.getStatsTamagotchi().getSante(), 0);
            });
        }
    }

    @Test
    public void testSoigner100a100() {
        // Tester avec l'utilisateur des test, le Tamagotchi retourné est blybly.
        Joueur.connexion(appContext, "blabla@example.com", "bleble");
        TestUtils.setBlybly(100,100);
        try(ActivityScenario<MainActivity> scenario = ActivityScenario.launch(MainActivity.class)) {
            scenario.onActivity(activity -> {
                Tamagotchi activeTamagotchi = activity.getActiveTamagotchi();
                assertEquals(100, activeTamagotchi.getInventaireTamagotchi().getNbMedicaments());
                assertEquals(100, activeTamagotchi.getStatsTamagotchi().getSante(), 0);

                activity.soigner(view);

                activeTamagotchi = activity.getActiveTamagotchi();
                assertEquals(100, activeTamagotchi.getInventaireTamagotchi().getNbMedicaments());
                assertEquals(100, activeTamagotchi.getStatsTamagotchi().getSante(), 0);
            });
        }
    }

    @Test
    public void testSoigner50() {
        // Tester avec l'utilisateur des test, le Tamagotchi retourné est blybly.
        Joueur.connexion(appContext, "blabla@example.com", "bleble");
        TestUtils.setBlybly(50,50);
        try(ActivityScenario<MainActivity> scenario = ActivityScenario.launch(MainActivity.class)) {
            scenario.onActivity(activity -> {
                Tamagotchi activeTamagotchi = activity.getActiveTamagotchi();
                assertEquals(50, activeTamagotchi.getInventaireTamagotchi().getNbMedicaments());
                assertEquals(50, activeTamagotchi.getStatsTamagotchi().getSante(), 0);

                activity.soigner(view);

                activeTamagotchi = activity.getActiveTamagotchi();
                assertTrue(50 > activeTamagotchi.getInventaireTamagotchi().getNbMedicaments());
                assertTrue(50 < activeTamagotchi.getStatsTamagotchi().getSante());
            });
        }
    }

    @Test
    public void testJouer0a0() {
        // Tester avec l'utilisateur des test, le Tamagotchi retourné est blybly.
        Joueur.connexion(appContext, "blabla@example.com", "bleble");
        TestUtils.setBlybly(0,0);
        try(ActivityScenario<MainActivity> scenario = ActivityScenario.launch(MainActivity.class)) {
            scenario.onActivity(activity -> {
                Tamagotchi activeTamagotchi = activity.getActiveTamagotchi();
                assertEquals(0, activeTamagotchi.getInventaireTamagotchi().getNbJouets());
                assertEquals(0, activeTamagotchi.getStatsTamagotchi().getBonheur(), 0);

                activity.jouer(view);

                activeTamagotchi = activity.getActiveTamagotchi();
                assertEquals(0, activeTamagotchi.getInventaireTamagotchi().getNbJouets());
                assertEquals(0, activeTamagotchi.getStatsTamagotchi().getBonheur(), 0);
            });
        }
    }

    @Test
    public void testJouer100a100() {
        // Tester avec l'utilisateur des test, le Tamagotchi retourné est blybly.
        Joueur.connexion(appContext, "blabla@example.com", "bleble");
        TestUtils.setBlybly(100,100);
        try(ActivityScenario<MainActivity> scenario = ActivityScenario.launch(MainActivity.class)) {
            scenario.onActivity(activity -> {
                Tamagotchi activeTamagotchi = activity.getActiveTamagotchi();
                assertEquals(100, activeTamagotchi.getInventaireTamagotchi().getNbJouets());
                assertEquals(100, activeTamagotchi.getStatsTamagotchi().getBonheur(), 0);

                activity.jouer(view);

                activeTamagotchi = activity.getActiveTamagotchi();
                assertEquals(100, activeTamagotchi.getInventaireTamagotchi().getNbJouets());
                assertEquals(100, activeTamagotchi.getStatsTamagotchi().getBonheur(), 0);
            });
        }
    }

    @Test
    public void testJouer50() {
        // Tester avec l'utilisateur des test, le Tamagotchi retourné est blybly.
        Joueur.connexion(appContext, "blabla@example.com", "bleble");
        TestUtils.setBlybly(50,50);
        try(ActivityScenario<MainActivity> scenario = ActivityScenario.launch(MainActivity.class)) {
            scenario.onActivity(activity -> {
                Tamagotchi activeTamagotchi = activity.getActiveTamagotchi();
                assertEquals(50, activeTamagotchi.getInventaireTamagotchi().getNbJouets());
                assertEquals(50, activeTamagotchi.getStatsTamagotchi().getBonheur(), 0);

                activity.jouer(view);

                activeTamagotchi = activity.getActiveTamagotchi();
                assertTrue(50 > activeTamagotchi.getInventaireTamagotchi().getNbJouets());
                assertTrue(50 < activeTamagotchi.getStatsTamagotchi().getBonheur());
            });
        }
    }
}
