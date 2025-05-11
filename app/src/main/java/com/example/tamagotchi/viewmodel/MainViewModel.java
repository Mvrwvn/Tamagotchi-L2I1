package com.example.tamagotchi.viewmodel;

/*
-----------------------------
    Date : 27/04/2025

    Membres qui travaillent dessus : Marwan DENAGNON

    Que fait le code ? : ViewModel sont but est de gérer les données entre les Model et la View ici MainActivity
    Va gérer toutes les données entre Firestore et l'application pour le MainActivity
-----------------------------
*/

import android.util.Log;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.tamagotchi.model.Tamagotchi;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;


public class MainViewModel extends ViewModel {
    private MutableLiveData<String> messageLiveData = new MutableLiveData<>();

    public LiveData<String> getMessageLiveData() {
        return messageLiveData;
    }

    public void updateTamagotchi(Tamagotchi tamagotchi) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        if (tamagotchi == null || tamagotchi.getIdTamagotchi() == null || tamagotchi.getIdTamagotchi().isEmpty()) {
            String errorMessage = "Objet Tamagotchi invalide.";
            Log.w("TamagotchiViewModel", errorMessage);
            messageLiveData.postValue(errorMessage);
            return;
        }

        tamagotchi.setDernierUpdate(Timestamp.now());
        db.collection("tamagotchis")
                .document(tamagotchi.getIdTamagotchi())
                .set(tamagotchi);
    }

    public LiveData<Tamagotchi> getActiveTamagotchi() {
        final MutableLiveData<Tamagotchi> activeTamagotchiLiveData = new MutableLiveData<>();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user == null) {
            activeTamagotchiLiveData.postValue(null);
            return activeTamagotchiLiveData;
        }
        String uid = user.getUid();
        db.collection("joueurs").document(uid)
                .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot joueurDoc, @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            activeTamagotchiLiveData.postValue(null);
                            return;
                        }

                        if (joueurDoc != null && joueurDoc.exists()) {
                            String activeTamagotchiId = joueurDoc.getString("activeTamagotchiId");
                            if (activeTamagotchiId != null && !activeTamagotchiId.isEmpty()) {
                                db.collection("tamagotchis").document(activeTamagotchiId)
                                        .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                                            @Override
                                            public void onEvent(@Nullable DocumentSnapshot tamagotchiDoc, @Nullable FirebaseFirestoreException e) {
                                                if (e != null) {
                                                    activeTamagotchiLiveData.postValue(null);
                                                    return;
                                                }

                                                if (tamagotchiDoc != null && tamagotchiDoc.exists()) {
                                                    Tamagotchi tamagotchi = tamagotchiDoc.toObject(Tamagotchi.class);
                                                    if (tamagotchi != null) {
                                                        tamagotchi.setIdTamagotchi(tamagotchiDoc.getId());
                                                    }
                                                    activeTamagotchiLiveData.postValue(tamagotchi);
                                                } else {
                                                    activeTamagotchiLiveData.postValue(null); // Tamagotchi non trouvé
                                                }
                                            }
                                        });
                            } else {
                                activeTamagotchiLiveData.postValue(null); // Pas d'ID actif
                            }
                        } else {
                            activeTamagotchiLiveData.postValue(null); // Document joueur inexistant
                        }
                    }
                });

        return activeTamagotchiLiveData;
    }
}
