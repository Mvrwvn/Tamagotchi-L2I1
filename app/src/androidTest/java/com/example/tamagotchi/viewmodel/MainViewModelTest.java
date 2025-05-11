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


public class MainViewModelTest {
    private static Context appContext;

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @BeforeClass
    public static void setUpClass() {
        appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        TestUtils.resetTamagotchis();
    }

    @Test
    public void testGetActiveTamagotchiNoLoggedUser() {
        FirebaseAuth.getInstance().signOut(); // Tester sans utilisateur connecté.
        MainViewModel viewModel = new MainViewModel();

        Tamagotchi tamagotchi = viewModel.getActiveTamagotchi().getValue();

        assertNull(tamagotchi);
    }

    @Test
    public void testGetActiveTamagotchi() {
        Joueur.connexion(appContext, "blabla@example.com", "bleble");
        MainViewModel viewModel = new MainViewModel();

        LiveData<Tamagotchi> tamagotchiLiveData = viewModel.getActiveTamagotchi();
        try {
            Tamagotchi tamagotchi = TestUtils.getOrAwaitValue(tamagotchiLiveData);
            assertNotNull(tamagotchi);
            assertEquals("F9pc3wi4pbrjbkzagk0g", tamagotchi.getIdTamagotchi());
            assertEquals("blybly", tamagotchi.getNomTamagotchi());
        } catch (InterruptedException e) {
            fail("Tamagotchi LiveData non initialisée.");
            throw new RuntimeException(e);
        } finally {
            FirebaseAuth.getInstance().signOut();
        }
    }

    @Test
    public void testUpdateTamagotchi() {
        Joueur.connexion(appContext, "blabla@example.com", "bleble");
        MainViewModel viewModel = new MainViewModel();

        LiveData<Tamagotchi> tamagotchiLiveData = viewModel.getActiveTamagotchi();
        try {
            Timestamp debut = Timestamp.now();

            Tamagotchi tamagotchi = TestUtils.getOrAwaitValue(tamagotchiLiveData);
            assertNotNull(tamagotchi);
            assertEquals("F9pc3wi4pbrjbkzagk0g", tamagotchi.getIdTamagotchi());
            assertEquals("blybly", tamagotchi.getNomTamagotchi());

            viewModel.updateTamagotchi(tamagotchi);
            tamagotchi = TestUtils.getOrAwaitValue(tamagotchiLiveData);

            assertTrue(tamagotchi.getDernierUpdate().compareTo(debut) > 0);
        } catch (InterruptedException e) {
            fail("Tamagotchi LiveData non initialisée.");
            throw new RuntimeException(e);
        } finally {
            FirebaseAuth.getInstance().signOut();
        }
    }
}