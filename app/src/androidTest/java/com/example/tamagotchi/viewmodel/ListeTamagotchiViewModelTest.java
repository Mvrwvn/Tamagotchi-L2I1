package com.example.tamagotchi.viewmodel;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import android.content.Context;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.LiveData;
import androidx.test.platform.app.InstrumentationRegistry;

import com.example.tamagotchi.model.Inventaire;
import com.example.tamagotchi.model.Joueur;
import com.example.tamagotchi.model.Statistique;
import com.example.tamagotchi.model.Tamagotchi;
import com.example.tamagotchi.utils.TestUtils;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class ListeTamagotchiViewModelTest {
    private static Context appContext;
    private static Tamagotchi testTamagotchi;
    private static final String joueurBlebleId = "YszWFlr9M7OwscV2e4aKvAOIETu2";
    private static final String tamagotchiBlyblyId = "F9pc3wi4pbrjbkzagk0g";

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @BeforeClass
    public static void setUpClass() {
        appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        testTamagotchi = new Tamagotchi(
                "testTamagotchiId",
                joueurBlebleId,
                "testBleblyblu",
                "Mâle",
                Timestamp.now(),
                Timestamp.now(),
                new Statistique(),
                new Inventaire()
            );
        TestUtils.resetTamagotchis();
    }

    @Test
    public void testGetUserTamagotchis() {
        Joueur.connexion(appContext, "blabla@example.com", "bleble");
        ListeTamagotchiViewModel viewModel = new ListeTamagotchiViewModel();

        LiveData<List<Tamagotchi>> tamagotchisLiveData = viewModel.getUserTamagotchis();
        try {
            List<Tamagotchi> tamagotchis = TestUtils.getOrAwaitValue(tamagotchisLiveData);
            assertNotNull(tamagotchisLiveData);
            assertEquals(1, tamagotchis.size());
            assertEquals(tamagotchiBlyblyId, tamagotchis.get(0).getIdTamagotchi());
            assertEquals("blybly", tamagotchis.get(0).getNomTamagotchi());
        } catch (InterruptedException e) {
            fail("Tamagotchis LiveData non initialisée.");
            throw new RuntimeException(e);
        } finally {
            FirebaseAuth.getInstance().signOut();
        }
    }

    @Test
    public void testGetActiveTamagotchi() {
        Joueur.connexion(appContext, "blabla@example.com", "bleble");
        ListeTamagotchiViewModel viewModel = new ListeTamagotchiViewModel();

        LiveData<Tamagotchi> tamagotchiLiveData = viewModel.getActiveTamagotchi();
        try {
            Tamagotchi tamagotchi = TestUtils.getOrAwaitValue(tamagotchiLiveData);
            assertNotNull(tamagotchi);
            assertEquals(tamagotchiBlyblyId, tamagotchi.getIdTamagotchi());
            assertEquals("blybly", tamagotchi.getNomTamagotchi());
        } catch (InterruptedException e) {
            fail("Tamagotchi LiveData non initialisée.");
            throw new RuntimeException(e);
        } finally {
            FirebaseAuth.getInstance().signOut();
        }
    }

    @Test
    public void testSetActiveTamagotchiId() {
        Joueur.connexion(appContext, "blabla@example.com", "bleble");
        ListeTamagotchiViewModel viewModel = new ListeTamagotchiViewModel();

        Joueur joueur = getJoueurTest();
        assertEquals(tamagotchiBlyblyId, joueur.getActiveTamagotchiId());

        String fauxActiveId = "fauxActiveId";

        viewModel.setActiveTamagotchiId(fauxActiveId);
        joueur = getJoueurTest();
        assertEquals(fauxActiveId, joueur.getActiveTamagotchiId());

        viewModel.setActiveTamagotchiId(tamagotchiBlyblyId);
        joueur = getJoueurTest();
        assertEquals(tamagotchiBlyblyId, joueur.getActiveTamagotchiId());
    }

    @Test
    public void testDeleteTamagotchi() {
        Joueur.connexion(appContext, "blabla@example.com", "bleble");
        ListeTamagotchiViewModel viewModel = new ListeTamagotchiViewModel();

        ajouterTamagotchiTest();
        LiveData<List<Tamagotchi>> tamagotchisLiveData = viewModel.getUserTamagotchis();
        try {
            List<Tamagotchi> tamagotchis = TestUtils.getOrAwaitValue(tamagotchisLiveData);
            assertNotNull(tamagotchisLiveData);
            assertEquals(2, tamagotchis.size());

            viewModel.deleteTamagotchi("testTamagotchiId");

            tamagotchisLiveData = viewModel.getUserTamagotchis();
            tamagotchis = TestUtils.getOrAwaitValue(tamagotchisLiveData);

            assertEquals(1, tamagotchis.size());
            assertEquals(tamagotchiBlyblyId, tamagotchis.get(0).getIdTamagotchi());
            assertEquals("blybly", tamagotchis.get(0).getNomTamagotchi());
        } catch (InterruptedException e) {
            fail("Tamagotchis LiveData non initialisée.");
            throw new RuntimeException(e);
        } finally {
            FirebaseAuth.getInstance().signOut();
        }
    }

    private static Joueur getJoueurTest() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        try {
            Joueur joueur = Tasks.await(
                    db.collection("joueurs").document(joueurBlebleId).get()
            ).toObject(Joueur.class);

            if (joueur == null) {
                fail("Impossible de récuperer le Tamagotchi de la base de données.");
                throw new RuntimeException();
            }

            return joueur;
        } catch (ExecutionException | InterruptedException e) {
            fail("Impossible de récuperer le Joueur de la base de données.");
            throw new RuntimeException(e);
        }
    }

    private static void ajouterTamagotchiTest() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("tamagotchis").document("testTamagotchiId").set(testTamagotchi);
    }
}
