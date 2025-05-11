package com.example.tamagotchi.utils;

import static org.junit.Assert.fail;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import com.example.tamagotchi.model.Tamagotchi;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/*
-----------------------------
    Date : 11/05/2025

    Membres qui travaillent dessus : Lina BOUGUETTAYA

    Que fait le code ? : Permet de récuperer la valeur des observables à des fins des tests
    d'instrumentation.
-----------------------------
*/

public class TestUtils {
    public static final String joueurBlebleId = "YszWFlr9M7OwscV2e4aKvAOIETu2";
    public static final String tamagotchiBlyblyId = "F9pc3wi4pbrjbkzagk0g";

    public static <T> T getOrAwaitValue(final LiveData<T> liveData) throws InterruptedException {
        final Object[] data = new Object[1];
        final CountDownLatch latch = new CountDownLatch(1);
        Observer<T> observer = new Observer<T>() {
            @Override
            public void onChanged(@Nullable T o) {
                data[0] = o;
                latch.countDown();
                liveData.removeObserver(this);
            }
        };
        liveData.observeForever(observer);
        // Don't wait indefinitely if the LiveData is not set.
        if (!latch.await(2, TimeUnit.SECONDS)) {
            throw new RuntimeException("LiveData value was never set.");
        }
        //noinspection unchecked
        return (T) data[0];
    }

    public static void resetTamagotchis() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        List<Tamagotchi> tamagotchis = getTamagotchis();

        for (Tamagotchi tamagotchi : tamagotchis) {
            if (!tamagotchi.getIdTamagotchi().equals(tamagotchiBlyblyId)) {
                db.collection("tamagotchis").document(tamagotchi.getIdTamagotchi()).delete();
            }
        }

        db.collection("joueurs").document(joueurBlebleId).update("activeTamagotchiId", tamagotchiBlyblyId);
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
}
