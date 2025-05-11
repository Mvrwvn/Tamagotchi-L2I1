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

/*
-----------------------------
    Date : 10/05/2025

    Membres qui travaillent dessus : Lina BOUGUETTAYA

    Que fait le code ? : Verfie l'intégrité du listing, la mise à jour de l'actif, et la suppression
    des tamagotchis.
-----------------------------
*/
public class ListeTamagotchiViewModelTest {
    private static Context appContext;
    private static Tamagotchi testTamagotchi;
    private static final String joueurBlebleId = "YszWFlr9M7OwscV2e4aKvAOIETu2";
    private static final String tamagotchiBlyblyId = "F9pc3wi4pbrjbkzagk0g";

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @BeforeClass
    public static void setUpClass() {
        /* Création d'un context pour l'execution des tests, et initialisation des données,
         et préparation d'une donnée factice à utiliser. */
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
        //Connexion de l'utilisateur de test avant execution.
        Joueur.connexion(appContext, "blabla@example.com", "bleble");
        ListeTamagotchiViewModel viewModel = new ListeTamagotchiViewModel();

        LiveData<List<Tamagotchi>> tamagotchisLiveData = viewModel.getUserTamagotchis();
        try {
            /* On extrait la liste des tamagotchis en dehors de l'objet LiveData, et on vérfie
            qu'on a bien qu'un seul tamagotchi, à savoir blybly. */
            List<Tamagotchi> tamagotchis = TestUtils.getOrAwaitValue(tamagotchisLiveData);
            assertNotNull(tamagotchisLiveData);
            assertEquals(1, tamagotchis.size());
            assertEquals(tamagotchiBlyblyId, tamagotchis.get(0).getIdTamagotchi());
            assertEquals("blybly", tamagotchis.get(0).getNomTamagotchi());
        } catch (InterruptedException e) {
            // Le test échoue si la connexion à la base de données est coupée.
            fail("Tamagotchis LiveData non initialisée.");
            throw new RuntimeException(e);
        } finally {
            // Se déconnecter dans tous les cas.
            FirebaseAuth.getInstance().signOut();
        }
    }

    @Test
    public void testGetActiveTamagotchi() {
        //Connexion de l'utilisateur de test avant execution.
        Joueur.connexion(appContext, "blabla@example.com", "bleble");
        ListeTamagotchiViewModel viewModel = new ListeTamagotchiViewModel();

        LiveData<Tamagotchi> tamagotchiLiveData = viewModel.getActiveTamagotchi();
        try {
            // On vérifie que le tamagotchi actif est blybly.
            Tamagotchi tamagotchi = TestUtils.getOrAwaitValue(tamagotchiLiveData);
            assertNotNull(tamagotchi);
            assertEquals(tamagotchiBlyblyId, tamagotchi.getIdTamagotchi());
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
    public void testSetActiveTamagotchiId() {
        //Connexion de l'utilisateur de test avant execution.
        Joueur.connexion(appContext, "blabla@example.com", "bleble");
        ListeTamagotchiViewModel viewModel = new ListeTamagotchiViewModel();

        // On verifie que blybly est actif de base
        Joueur joueur = getJoueurTest();
        assertEquals(tamagotchiBlyblyId, joueur.getActiveTamagotchiId());

        // On modifie l'id du tamagotchi actif avec une valeur factice, puis on revérifie.
        String fauxActiveId = "fauxActiveId";

        viewModel.setActiveTamagotchiId(fauxActiveId);
        joueur = getJoueurTest();
        assertEquals(fauxActiveId, joueur.getActiveTamagotchiId());

        // On remets l'identifiant de blybly, et on reverifie une dernière fois.
        viewModel.setActiveTamagotchiId(tamagotchiBlyblyId);
        joueur = getJoueurTest();
        assertEquals(tamagotchiBlyblyId, joueur.getActiveTamagotchiId());
    }

    @Test
    public void testDeleteTamagotchi() {
        //Connexion de l'utilisateur de test avant execution.
        Joueur.connexion(appContext, "blabla@example.com", "bleble");
        ListeTamagotchiViewModel viewModel = new ListeTamagotchiViewModel();

        // On créer un faux tamagotchi de test.
        ajouterTamagotchiTest();
        LiveData<List<Tamagotchi>> tamagotchisLiveData = viewModel.getUserTamagotchis();
        try {
            // On vérifie qu'on a bien 2 tamagotchis à présent
            List<Tamagotchi> tamagotchis = TestUtils.getOrAwaitValue(tamagotchisLiveData);
            assertNotNull(tamagotchisLiveData);
            assertEquals(2, tamagotchis.size());

            // On appelle à présent deleteTamagotchi, et on vérifie que le faux tamagotchi inséré n'existe plus.
            viewModel.deleteTamagotchi("testTamagotchiId");

            tamagotchisLiveData = viewModel.getUserTamagotchis();
            tamagotchis = TestUtils.getOrAwaitValue(tamagotchisLiveData);

            assertEquals(1, tamagotchis.size());
            // S'assurer que le tamagotchi restant est bien blybly
            assertEquals(tamagotchiBlyblyId, tamagotchis.get(0).getIdTamagotchi());
            assertEquals("blybly", tamagotchis.get(0).getNomTamagotchi());
        } catch (InterruptedException e) {
            // Le test échoue si la connexion à la base de données est coupée.
            fail("Tamagotchis LiveData non initialisée.");
            throw new RuntimeException(e);
        } finally {
            // Se déconnecter dans tous les cas.
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
