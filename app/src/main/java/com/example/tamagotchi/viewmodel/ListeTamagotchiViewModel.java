package com.example.tamagotchi.viewmodel;

/*
-----------------------------
    Date : 27/04/2025

    Membres qui travaillent dessus : Marwan DENAGNON

    Que fait le code ? : Que fait le code ? : ViewModel sont but est de gérer les données entre les Model et la View ici ListeTamagotchiActivity
    Va gérer toutes les données entre Firestore et l'application pour le  ListeTamagotchiActivity
-----------------------------
*/

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.tamagotchi.model.Tamagotchi;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ListeTamagotchiViewModel extends ViewModel {
    private MutableLiveData<String> messageLiveData = new MutableLiveData<>();

    public LiveData<String> getMessageLiveData() {
        return messageLiveData;
    }

    public LiveData<List<Tamagotchi>> getUserTamagotchis() {
        final MutableLiveData<List<Tamagotchi>> tamagotchiListLiveData = new MutableLiveData<>();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user == null) {
            tamagotchiListLiveData.postValue(Collections.emptyList());
            return tamagotchiListLiveData;
        }

        String uid = user.getUid();

        // Utilisation de addSnapshotListener pour écouter les changements en temps réel
        db.collection("tamagotchis")
                .whereEqualTo("idJoueur", uid)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            tamagotchiListLiveData.postValue(Collections.emptyList());
                            return;
                        }

                        if (queryDocumentSnapshots != null) {
                            List<Tamagotchi> tamagotchiList = new ArrayList<>();
                            for (DocumentSnapshot document : queryDocumentSnapshots) {
                                Tamagotchi tamagotchi = document.toObject(Tamagotchi.class);
                                tamagotchiList.add(tamagotchi);
                            }
                            tamagotchiListLiveData.postValue(tamagotchiList);
                        } else {
                            tamagotchiListLiveData.postValue(Collections.emptyList());
                        }
                    }
                });

        return tamagotchiListLiveData;
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

    public void setActiveTamagotchiId(String activeTamagotchiId) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) return;
        String userId = user.getUid();

        db.collection("joueurs")
                .document(userId)
                .update("activeTamagotchiId", activeTamagotchiId)
                .addOnSuccessListener(documentReference ->{
                    String successMessage = "Tamagotchi actif maj";
                    messageLiveData.postValue(successMessage);
                })
                .addOnFailureListener(e ->{
                    String errorMessage = "Erreur de sauvegarde tamagotchi actif";
                    messageLiveData.postValue(errorMessage);
                });
    }

    public void deleteTamagotchi(String idTamagotchi){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("tamagotchis")
                .document(idTamagotchi)
                .delete()
                .addOnSuccessListener(aVoid -> {
                    // Succès de la suppression
                    messageLiveData.postValue("Tamagotchi supprimé avec succès");
                })
                .addOnFailureListener(e -> {
                    // Échec de la suppression
                    messageLiveData.postValue("Erreur lors de la suppression du Tamagotchi");
                });
    }
}
