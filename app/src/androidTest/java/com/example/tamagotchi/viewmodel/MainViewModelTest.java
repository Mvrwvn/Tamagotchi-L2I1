package com.example.tamagotchi.viewmodel;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import android.content.Context;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.LiveData;
import androidx.test.platform.app.InstrumentationRegistry;

import com.example.tamagotchi.model.Joueur;
import com.example.tamagotchi.model.Tamagotchi;
import com.example.tamagotchi.utils.TestUtils;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;

import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;


/*
-----------------------------
    Date : 10/05/2025

    Membres qui travaillent dessus : Lina BOUGUETTAYA

    Que fait le code ? : Verfie l'intégrité de la récupération et mise à jour de l'état des tamagotchis.
-----------------------------
*/
public class MainViewModelTest {
    private static Context appContext;

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @BeforeClass
    public static void setUpClass() {
        // Création d'un context pour l'execution des tests, et initialisation des données.
        appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        TestUtils.resetTamagotchis();
    }

    @Test
    public void testGetActiveTamagotchiNoLoggedUser() {
        // Tester sans utilisateur connecté, aucun Tamagotchi n'est renvoyé.
        FirebaseAuth.getInstance().signOut();
        MainViewModel viewModel = new MainViewModel();

        Tamagotchi tamagotchi = viewModel.getActiveTamagotchi().getValue();

        assertNull(tamagotchi);
    }

    @Test
    public void testGetActiveTamagotchi() {
        // Tester avec l'utilisateur des test, le Tamagotchi retourné est blybly.
        Joueur.connexion(appContext, "blabla@example.com", "bleble");
        MainViewModel viewModel = new MainViewModel();

        LiveData<Tamagotchi> tamagotchiLiveData = viewModel.getActiveTamagotchi();
        try {
            // On vérifie que le tamagotchi actif est blybly.
            Tamagotchi tamagotchi = TestUtils.getOrAwaitValue(tamagotchiLiveData);
            assertNotNull(tamagotchi);
            assertEquals("F9pc3wi4pbrjbkzagk0g", tamagotchi.getIdTamagotchi());
            assertEquals("blybly", tamagotchi.getNomTamagotchi());
        } catch (InterruptedException e) {
            // Le test échoue si la connexion à la base de données est coupée.
            fail("Tamagotchi LiveData non initialisée.");
            throw new RuntimeException(e);
        } finally {
            // Se déconnecter dans tous les cas.
            FirebaseAuth.getInstance().signOut();
        }
    }

    @Test
    public void testUpdateTamagotchi() {
        //Connexion de l'utilisateur de test avant execution.
        Joueur.connexion(appContext, "blabla@example.com", "bleble");
        MainViewModel viewModel = new MainViewModel();

        LiveData<Tamagotchi> tamagotchiLiveData = viewModel.getActiveTamagotchi();
        try {
            // On recupère et on s'assure que le tamagotchi est blybly.
            Timestamp debut = Timestamp.now();

            Tamagotchi tamagotchi = TestUtils.getOrAwaitValue(tamagotchiLiveData);
            assertNotNull(tamagotchi);
            assertEquals("F9pc3wi4pbrjbkzagk0g", tamagotchi.getIdTamagotchi());
            assertEquals("blybly", tamagotchi.getNomTamagotchi());

            /* On mets à jour blybly, et on vérifie que la dernière date d'update est supérieure à
            celle enregistrée au début du test */
            viewModel.updateTamagotchi(tamagotchi);
            tamagotchi = TestUtils.getOrAwaitValue(tamagotchiLiveData);

            assertTrue(tamagotchi.getDernierUpdate().compareTo(debut) > 0);
        } catch (InterruptedException e) {
            // Le test échoue si la connexion à la base de données est coupée.
            fail("Tamagotchi LiveData non initialisée.");
            throw new RuntimeException(e);
        } finally {
            // Se déconnecter dans tous les cas.
            FirebaseAuth.getInstance().signOut();
        }
    }
}