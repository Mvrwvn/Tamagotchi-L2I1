package com.example.tamagotchi.viewmodel;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import android.content.Context;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.test.platform.app.InstrumentationRegistry;

import com.example.tamagotchi.model.Joueur;
import com.example.tamagotchi.model.Tamagotchi;
import com.example.tamagotchi.utils.TestUtils;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;

import java.util.concurrent.ExecutionException;

/*
-----------------------------
    Date : 10/05/2025

    Membres qui travaillent dessus : Lina BOUGUETTAYA

    Que fait le code ? : Verfie l'intégrité de la modification du nom et du genre d'un tamagotchi.
-----------------------------
*/
public class ModifierTamagotchiViewModelTest {
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
    public void testUpdateTamagotchi() {
        //Connexion de l'utilisateur de test avant execution.
        Joueur.connexion(appContext, "blabla@example.com", "bleble");

        ModifierTamagotchiViewModel viewModel = new ModifierTamagotchiViewModel();

        String tamagotchiId = "F9pc3wi4pbrjbkzagk0g";
        String userId = "YszWFlr9M7OwscV2e4aKvAOIETu2";
        String nomTamagotchiOriginal = "blybly";
        String nomTamagotchiModifie = "blyblyTest";
        String genreTamagotchi = "Mâle";

        // On modifie le nom du tamagotchi, et on verifie que le nom a bien changé.
        viewModel.updateTamagotchi(tamagotchiId, nomTamagotchiModifie, genreTamagotchi);

        Tamagotchi tamagotchi = getTamagotchi(tamagotchiId);
        assertEquals(nomTamagotchiModifie, tamagotchi.getNomTamagotchi());

        // On remets le nom du tamagotchi, et on verifie que le nom a bien changé.
        viewModel.updateTamagotchi(tamagotchiId, nomTamagotchiOriginal, genreTamagotchi);
        tamagotchi = getTamagotchi(tamagotchiId);
        assertEquals(nomTamagotchiOriginal, tamagotchi.getNomTamagotchi());

        FirebaseAuth.getInstance().signOut();
    }

    private static Tamagotchi getTamagotchi(String tamagotchiId) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        try {
            Tamagotchi tamagotchi = Tasks.await(
                    db.collection("tamagotchis").document(tamagotchiId).get()
            ).toObject(Tamagotchi.class);

            if (tamagotchi == null) {
                fail("Impossible de récuperer le Tamagotchi de la base de données.");
                throw new RuntimeException();
            }

            tamagotchi.setIdTamagotchi(tamagotchiId);

            return tamagotchi;
        } catch (ExecutionException | InterruptedException e) {
            fail("Impossible de récuperer le Tamagotchi de la base de données.");
            throw new RuntimeException(e);
        }
    }
}
