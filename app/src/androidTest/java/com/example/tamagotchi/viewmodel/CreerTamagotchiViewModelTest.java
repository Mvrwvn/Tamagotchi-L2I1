package com.example.tamagotchi.viewmodel;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import android.content.Context;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.test.platform.app.InstrumentationRegistry;

import com.example.tamagotchi.model.Inventaire;
import com.example.tamagotchi.model.Joueur;
import com.example.tamagotchi.model.Statistique;
import com.example.tamagotchi.model.Tamagotchi;
import com.example.tamagotchi.utils.TestUtils;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class CreerTamagotchiViewModelTest {
    private static Context appContext;
    private static Tamagotchi testTamagotchi;
    private static final String joueurBlebleId = "YszWFlr9M7OwscV2e4aKvAOIETu2";
    private static final String tamagotchiBlyblyId = "F9pc3wi4pbrjbkzagk0g";

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @BeforeClass
    public static void setUpClass() {
        appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        TestUtils.resetTamagotchis();
    }

    @Test
    public void testAjouterTamagotchi() {
        Joueur.connexion(appContext, "blabla@example.com", "bleble");
        CreerTamagotchiViewModel viewModel = new CreerTamagotchiViewModel();
        Tamagotchi testTamagotchi = new Tamagotchi(
            null,
            joueurBlebleId,
            "testBleblyblu",
            "Mâle",
            Timestamp.now(),
            Timestamp.now(),
            new Statistique(),
            new Inventaire()
        );

        List<Tamagotchi> tamagotchis = getTamagotchis();
        assertEquals(1, tamagotchis.size());

        viewModel.ajouterTamagotchi(testTamagotchi);
        tamagotchis = getTamagotchis();
        assertEquals(2, tamagotchis.size());

        TestUtils.resetTamagotchis();
        tamagotchis = getTamagotchis();
        assertEquals(1, tamagotchis.size());
    }

    @Test
    public void testSetActiveTamagotchiId() {
        Joueur.connexion(appContext, "blabla@example.com", "bleble");

        Joueur joueur = getJoueurTest();
        assertEquals(tamagotchiBlyblyId, joueur.getActiveTamagotchiId());

        String fauxActiveId = "fauxActiveId";

        CreerTamagotchiViewModel.setActiveTamagotchiId(fauxActiveId);
        joueur = getJoueurTest();
        assertEquals(fauxActiveId, joueur.getActiveTamagotchiId());

        CreerTamagotchiViewModel.setActiveTamagotchiId(tamagotchiBlyblyId);
        joueur = getJoueurTest();
        assertEquals(tamagotchiBlyblyId, joueur.getActiveTamagotchiId());
    }

    private static List<Tamagotchi> getTamagotchis() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        try {
            QuerySnapshot tamagotchisQuery = Tasks.await(
                    db.collection("tamagotchis").whereEqualTo("idJoueur", joueurBlebleId).get()
            );

            List<Tamagotchi> tamagotchiList = new ArrayList<>();
            for (DocumentSnapshot document : tamagotchisQuery) {
                Tamagotchi tamagotchi = document.toObject(Tamagotchi.class);
                if (tamagotchi != null) {
                    tamagotchi.setIdTamagotchi(document.getId());
                }
                tamagotchiList.add(tamagotchi);
            }

            return tamagotchiList;
        } catch (ExecutionException | InterruptedException e) {
            fail("Impossible de récuperer la liste des tamagotchis de la base de données.");
            throw new RuntimeException(e);
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
}
