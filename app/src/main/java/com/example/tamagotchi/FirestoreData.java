package com.example.tamagotchi;

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


import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


public class FirestoreData {
    @SuppressLint("StaticFieldLeak")
    private static final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static final String userId = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();

    public static void sauvegarderTamagotchi(Tamagotchi tamagotchi) {
        db.collection("tamagotchis")
                .add(tamagotchi)
                .addOnSuccessListener(documentReference -> {
                    String newTamagotchiId = documentReference.getId();
                    Log.d("Firestore", "Tamagotchi sauvegardé avec ID : " + newTamagotchiId);
                    setActiveTamagotchiId(newTamagotchiId);
                })
                .addOnFailureListener(e ->
                        Log.e("Firestore", "Erreur de sauvegarde", e));
    }

    public static void setActiveTamagotchiId(String activeTamagotchiId) {
        db.collection("joueurs")
                .document(userId)
                .update("activeTamagotchiId", activeTamagotchiId)
                .addOnSuccessListener(documentReference ->
                        Log.d("Firestore", "Tamagotchi actif maj"))
                .addOnFailureListener(e ->
                        Log.e("Firestore", "Erreur de sauvegarde tamagotchi actif", e));
    }

    public static void actionTamagotchi(View v,ProgressBar progressBar, String nomInventaire, String nomStatistique) {
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
                                                            updateVie();
                                                            loadStatsFromFirestore(progressBar,nomStatistique);
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

    public static void verifierDernierUpdate() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

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
                                                long variationStats = -2 * heuresPassees;
                                                long variationInventaire = 10 * heuresPassees;

                                                long sante = limitBetween(tamagotchiSnapshot.getLong("statsTamagotchi.sante") + variationStats, 0, 100);
                                                long faim = limitBetween(tamagotchiSnapshot.getLong("statsTamagotchi.faim") + variationStats, 0, 100);
                                                long bonheur = limitBetween(tamagotchiSnapshot.getLong("statsTamagotchi.bonheur") + variationStats, 0, 100);
                                                long energie = limitBetween(tamagotchiSnapshot.getLong("statsTamagotchi.energie") + variationStats, 0, 100);
                                                long hygiene = limitBetween(tamagotchiSnapshot.getLong("statsTamagotchi.hygiene") + variationStats, 0, 100);
                                                long soif = limitBetween(tamagotchiSnapshot.getLong("statsTamagotchi.soif") + variationStats, 0, 100);

                                                Map<String, Object> updates = new HashMap<>();
                                                updates.put("statsTamagotchi.sante", sante);
                                                updates.put("statsTamagotchi.faim", faim);
                                                updates.put("statsTamagotchi.bonheur", bonheur);
                                                updates.put("statsTamagotchi.energie", energie);
                                                updates.put("statsTamagotchi.hygiene", hygiene);
                                                updates.put("statsTamagotchi.soif", soif);

                                                updates.put("inventaireTamagotchi.nbNourritures", FieldValue.increment(variationInventaire));
                                                updates.put("inventaireTamagotchi.nbJeux", FieldValue.increment(variationInventaire));
                                                updates.put("inventaireTamagotchi.nbMedicaments", FieldValue.increment(variationInventaire));
                                                updates.put("inventaireTamagotchi.nbLits", FieldValue.increment(variationInventaire));
                                                updates.put("inventaireTamagotchi.nbSavons", FieldValue.increment(variationInventaire));
                                                updates.put("inventaireTamagotchi.nbBoissons", FieldValue.increment(variationInventaire));

                                                updates.put("dernierUpdate", Timestamp.now());

                                                db.collection("tamagotchis")
                                                        .document(activeTamagotchiId)
                                                        .update(updates)
                                                        .addOnSuccessListener(aVoid -> {
                                                            Log.d("Update", "Tamagotchi mis à jour après " + heuresPassees + " heures.");
                                                            updateVie();
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
    public static void updateVie() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

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

    public static void loadStatsFromFirestore(ProgressBar progressBar, String nomStatistique) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        db.collection("joueurs")
                .document(userId)
                .get()
                .addOnSuccessListener(snapshot -> {
                    String activeTamagotchiId = snapshot.getString("activeTamagotchiId");

                    db.collection("tamagotchis")
                            .document(activeTamagotchiId)
                            .get()
                            .addOnSuccessListener(doc -> {
                                if (doc.exists()) {
                                    progressBar.setProgress((int)getSafeLong(doc, "statsTamagotchi."+nomStatistique));
                                }
                            });
                });
    }


    private static long limitBetween(long value, long min, long max) {
        return Math.max(min, Math.min(value, max));
    }

    private static long getSafeLong(DocumentSnapshot doc, String path) {
        Long value = doc.getLong(path);
        return value != null ? value : 0;
    }

}