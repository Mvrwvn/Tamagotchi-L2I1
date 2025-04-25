package com.example.tamagotchi.viewmodel;

/*
-----------------------------
    Date : 18/04/2025

    Membres qui travaillent dessus : Marwan DENAGNON

    Que fait le code ? : Classe avec des méthodes static qui va servir de bascule entre l'échanges des données sur Firestore et les données en Java.
    Changement par rapport à la version précédente : Ajout d'une fonction pour mettre à jour les statistiques en fonction du Timestamp de la dernière update (-2% à toutes les statistiques par heure, +10 à chaque objets de l'inventaire)
    fonction pour augmenter la robustesse des données (s'assurer que toutes les statistiques soit compris entre 0 et 100 et non null)
    fonction pour màj la statistique "vie" qui représente la moyenne de toutes les autres statistiques
    Changement par rapport à la version précédente 2.0 : correction de la fonction verifierDernierUpdate
    ajout d'une fonction pour mettre les données de statistique de la bdd dans une progressbar
-----------------------------
*/

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.tamagotchi.model.Tamagotchi;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class MainViewModel extends ViewModel {

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
                .whereEqualTo("userId", uid)
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

        // Étape 1 : Écouter en temps réel le document joueur
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
                                // Étape 2 : Écouter en temps réel le Tamagotchi actif
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


    @SuppressLint("StaticFieldLeak")
    private static final FirebaseFirestore db = FirebaseFirestore.getInstance();

    public static void ajouterTamagotchi(FirebaseUser user, View v, Tamagotchi tamagotchi) {
        db.collection("tamagotchis")
                .add(tamagotchi)
                .addOnSuccessListener(documentReference -> {
                    String newTamagotchiId = documentReference.getId();
                    setActiveTamagotchiId(newTamagotchiId);
                    Toast.makeText(v.getContext(), "Mimichi créer avec succès : " + tamagotchi.getNomTamagotchi() + " (" + tamagotchi.getGenre() + ")", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e ->
                        Log.e("Firestore", "Erreur de sauvegarde", e));
    }
    public static void setActiveTamagotchiId(String activeTamagotchiId) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) return;
        String userId = user.getUid();

        db.collection("joueurs")
                .document(userId)
                .update("activeTamagotchiId", activeTamagotchiId, "idTamagotchis", FieldValue.arrayUnion(activeTamagotchiId))
                .addOnSuccessListener(documentReference ->
                        Log.d("Firestore", "Tamagotchi actif maj"))
                .addOnFailureListener(e ->
                        Log.e("Firestore", "Erreur de sauvegarde tamagotchi actif", e));
    }

    public static void actionTamagotchi(FirebaseUser user, View v, ProgressBar progressBar, String nomInventaire, String nomStatistique) {
        String userId = user.getUid();

        db.collection("joueurs")
                .document(userId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        String activeTamagotchiId = documentSnapshot.getString("activeTamagotchiId");

                        db.collection("tamagotchis")
                                .document(Objects.requireNonNull(activeTamagotchiId))
                                .get()
                                .addOnSuccessListener(tamagotchiSnapshot -> {
                                    if (tamagotchiSnapshot.exists()) {
                                        Double statistique = tamagotchiSnapshot.getDouble("statsTamagotchi." + nomStatistique);
                                        Long inventaire = tamagotchiSnapshot.getLong("inventaireTamagotchi.nb" + nomInventaire);

                                        if (statistique == null || inventaire == null) {
                                            Toast.makeText(v.getContext(), "Données manquantes dans le document.", Toast.LENGTH_SHORT).show();
                                            return;
                                        }

                                        if (statistique >= 0 && statistique < 100) {
                                            if (inventaire > 0) {
                                                db.collection("tamagotchis")
                                                        .document(activeTamagotchiId)
                                                        .update(
                                                                "statsTamagotchi." + nomStatistique, FieldValue.increment(1),
                                                                "inventaireTamagotchi.nb" + nomInventaire, FieldValue.increment(-1),
                                                                "dernierUpdate", Timestamp.now()
                                                        )
                                                        .addOnSuccessListener(aVoid -> {
                                                            updateVie(user);
                                                            loadStatsFromFirestore(user, progressBar, nomStatistique);
                                                        })
                                                        .addOnFailureListener(e -> {
                                                            Toast.makeText(v.getContext(), "Erreur lors du soin du Tamagotchi.", Toast.LENGTH_SHORT).show();
                                                        });
                                            } else {
                                                Toast.makeText(v.getContext(), "Pas assez de " + nomInventaire + " dans l'inventaire.", Toast.LENGTH_SHORT).show();
                                            }
                                        } else {
                                            Toast.makeText(v.getContext(), nomStatistique + " est déjà au maximum.", Toast.LENGTH_SHORT).show();
                                        }
                                    } else {
                                        Toast.makeText(v.getContext(), "Tamagotchi introuvable.", Toast.LENGTH_SHORT).show();
                                    }
                                })
                                .addOnFailureListener(e -> {
                                    Toast.makeText(v.getContext(), "Erreur lors de la lecture du Tamagotchi.", Toast.LENGTH_SHORT).show();
                                });

                    } else {
                        Toast.makeText(v.getContext(), "Joueur introuvable.", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(v.getContext(), "Erreur lors de la lecture du joueur.", Toast.LENGTH_SHORT).show();
                });
    }

    public static void verifierDernierUpdate(FirebaseUser user) {
        String userId = user.getUid();

        db.collection("joueurs")
                .document(userId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        String activeTamagotchiId = documentSnapshot.getString("activeTamagotchiId");

                        if (activeTamagotchiId != null && !activeTamagotchiId.isEmpty()) {
                            db.collection("tamagotchis")
                                    .document(activeTamagotchiId)
                                    .get()
                                    .addOnSuccessListener(tamagotchiSnapshot -> {
                                        if (tamagotchiSnapshot.exists() && tamagotchiSnapshot.getTimestamp("dernierUpdate") != null) {
                                            long now = Timestamp.now().getSeconds();
                                            long lastUpdate = tamagotchiSnapshot.getTimestamp("dernierUpdate").getSeconds();
                                            long heuresPassees = (now - lastUpdate) / 3600;

                                            if (heuresPassees > 0) {
                                                Log.d("Ici","test2");
                                                Log.d("Firestore", "Document: " + tamagotchiSnapshot.getData());
                                                long variationStats = -2 * heuresPassees;
                                                long variationInventaire = 10 * heuresPassees;

                                                long sante = limitBetween(tamagotchiSnapshot.getLong("statsTamagotchi.sante") + variationStats, 0, 100);
                                                long faim = limitBetween(tamagotchiSnapshot.getLong("statsTamagotchi.faim") + variationStats, 0, 100);
                                                long energie = limitBetween(tamagotchiSnapshot.getLong("statsTamagotchi.energie") + variationStats, 0, 100);
                                                long hygiene = limitBetween(tamagotchiSnapshot.getLong("statsTamagotchi.hygiene") + variationStats, 0, 100);
                                                long soif = limitBetween(tamagotchiSnapshot.getLong("statsTamagotchi.soif") + variationStats, 0, 100);
                                                long bonheur = limitBetween(tamagotchiSnapshot.getLong("statsTamagotchi.bonheur") + variationStats, 0, 100);


                                                Map<String, Object> updates = new HashMap<>();
                                                updates.put("statsTamagotchi.sante", sante);
                                                updates.put("statsTamagotchi.faim", faim);
                                                updates.put("statsTamagotchi.energie", energie);
                                                updates.put("statsTamagotchi.hygiene", hygiene);
                                                updates.put("statsTamagotchi.soif", soif);
                                                updates.put("statsTamagotchi.bonheur", bonheur);

                                                updates.put("inventaireTamagotchi.nbNourritures", FieldValue.increment(variationInventaire));
                                                updates.put("inventaireTamagotchi.nbMedicaments", FieldValue.increment(variationInventaire));
                                                updates.put("inventaireTamagotchi.nbLits", FieldValue.increment(variationInventaire));
                                                updates.put("inventaireTamagotchi.nbSavons", FieldValue.increment(variationInventaire));
                                                updates.put("inventaireTamagotchi.nbBoissons", FieldValue.increment(variationInventaire));
                                                updates.put("inventaireTamagotchi.nbJouets", FieldValue.increment(variationInventaire));

                                                updates.put("dernierUpdate", Timestamp.now());

                                                db.collection("tamagotchis")
                                                        .document(activeTamagotchiId)
                                                        .update(updates)
                                                        .addOnSuccessListener(aVoid -> {
                                                            Log.d("Update", "Tamagotchi mis à jour après " + heuresPassees + " heures.");
                                                            updateVie(user);
                                                        })
                                                        .addOnFailureListener(e -> {
                                                            Log.e("Update", "Erreur lors de la mise à jour du tamagotchi", e);
                                                        });
                                            }
                                        }
                                    });
                        }
                    }
                });
    }

    public static void updateVie(FirebaseUser user) {
        String userId = user.getUid();

        db.collection("joueurs")
                .document(userId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        String activeTamagotchiId = documentSnapshot.getString("activeTamagotchiId");

                        if (activeTamagotchiId != null && !activeTamagotchiId.isEmpty()) {
                            db.collection("tamagotchis")
                                    .document(activeTamagotchiId)
                                    .get()
                                    .addOnSuccessListener(document -> {
                                        if (document.exists()) {
                                            long sante = getSafeLong(document, "statsTamagotchi.sante");
                                            long faim = getSafeLong(document, "statsTamagotchi.faim");
                                            long bonheur = getSafeLong(document, "statsTamagotchi.bonheur");
                                            long energie = getSafeLong(document, "statsTamagotchi.energie");
                                            long hygiene = getSafeLong(document, "statsTamagotchi.hygiene");
                                            long soif = getSafeLong(document, "statsTamagotchi.soif");

                                            long vie = (sante + faim + bonheur + energie + hygiene + soif) / 6;

                                            Map<String, Object> updateVie = new HashMap<>();
                                            updateVie.put("statsTamagotchi.vie", vie);

                                            db.collection("tamagotchis")
                                                    .document(activeTamagotchiId)
                                                    .update(updateVie)
                                                    .addOnSuccessListener(aVoid -> Log.d("Vie", "Vie mise à jour : " + vie))
                                                    .addOnFailureListener(e -> Log.e("Vie", "Erreur mise à jour de la vie", e));
                                        }
                                    });
                        }
                    }
                });
    }

    public static void loadStatsFromFirestore(FirebaseUser user, ProgressBar progressBar, String nomStatistique) {
        String userId = user.getUid();
        Log.d("userId",userId);
        db.collection("joueurs")
                .document(userId)
                .get()
                .addOnSuccessListener(snapshot -> {
                    Log.d("Ici","test1");
                    String activeTamagotchiId = snapshot.getString("activeTamagotchiId");
                    Log.d("tamaactif",activeTamagotchiId);
                    db.collection("tamagotchis")
                            .document(activeTamagotchiId)
                            .get()
                            .addOnSuccessListener(doc -> {
                                Log.d("Ici1","ici");
                                if (doc.exists()) {
                                    progressBar.setProgress((int) getSafeLong(doc, "statsTamagotchi." + nomStatistique));
                                }
                            });
                })
                .addOnFailureListener(e -> Log.e("Erreur", "Erreur ici", e));
    }

    private static long limitBetween(long value, long min, long max) {
        return Math.max(min, Math.min(value, max));
    }

    private static long getSafeLong(DocumentSnapshot doc, String path) {
        Long value = doc.getLong(path);
        return value != null ? value : 0;
    }

    public static void modifierTamagotchiActif(FirebaseUser user, View v, String nomTamagotchi, String genre){
        String userId = user.getUid();
        db.collection("joueurs")
                .document(userId)
                .get()
                .addOnSuccessListener(snapshot -> {
                    Log.d("Ici","test1");
                    String activeTamagotchiId = snapshot.getString("activeTamagotchiId");
                    Log.d("tamaactif",activeTamagotchiId);
                    db.collection("tamagotchis")
                            .document(activeTamagotchiId)
                            .update("nomTamagotchi", nomTamagotchi, "genre", genre)
                            .addOnSuccessListener(snap->{
                                Toast.makeText(v.getContext(), "Mimichi modifié avec succès : " + nomTamagotchi + " (" + genre + ")", Toast.LENGTH_SHORT).show();
                            })
                            .addOnFailureListener(snap->{
                                Toast.makeText(v.getContext(), "Erreur lors de la création du Mimichi.", Toast.LENGTH_SHORT).show();
                            });
                });

    }
}