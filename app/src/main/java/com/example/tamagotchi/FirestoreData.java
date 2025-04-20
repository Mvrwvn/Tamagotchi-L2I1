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
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class FirestoreData {
    @SuppressLint("StaticFieldLeak")
    private static final FirebaseFirestore db = FirebaseFirestore.getInstance();

    public static void sauvegarderTamagotchi(Tamagotchi tamagotchi) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) return;
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

    public static void inscription(Tamagotchi tamagotchi, String email){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) return;
        db.collection("tamagotchis")
                .add(tamagotchi)
                .addOnSuccessListener(documentReference -> {
                    String newTamagotchiId = documentReference.getId();
                    Log.d("Firestore", "Tamagotchi sauvegardé avec ID : " + newTamagotchiId);
                    ArrayList<String> tamagotchiIds = new ArrayList<>();
                    tamagotchiIds.add(newTamagotchiId);
                    Joueur joueur = new Joueur(email, tamagotchiIds, newTamagotchiId);
                    db.collection("joueurs")
                            .add(joueur)
                            .addOnSuccessListener(doc -> {
                                Log.d("Firestore", "Joueur sauvegardé avec email : " + email);
                            })
                            .addOnFailureListener(e ->
                                    Log.e("Firestore", "Erreur de sauvegarde", e));
                })
                .addOnFailureListener(e ->
                        Log.e("Firestore", "Erreur de sauvegarde", e));
    }

    public static void sauvegarderJoueur(Joueur joueur){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) return;
        db.collection("joueurs")
                .add(joueur)
                .addOnSuccessListener(documentReference -> {
                    String newTamagotchiId = documentReference.getId();
                    Log.d("Firestore", "Tamagotchi sauvegardé avec ID : " + newTamagotchiId);
                    setActiveTamagotchiId(newTamagotchiId);
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

                                                Map<String, Object> updates = new HashMap<>();
                                                updates.put("statsTamagotchi.sante", sante);
                                                updates.put("statsTamagotchi.faim", faim);
                                                updates.put("statsTamagotchi.energie", energie);
                                                updates.put("statsTamagotchi.hygiene", hygiene);
                                                updates.put("statsTamagotchi.soif", soif);

                                                updates.put("inventaireTamagotchi.nbNourritures", FieldValue.increment(variationInventaire));
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
                                Toast.makeText(v.getContext(), "Mimichi modifié : " + nomTamagotchi + " (" + genre + ")", Toast.LENGTH_SHORT).show();
                            })
                            .addOnFailureListener(snap->{
                                Toast.makeText(v.getContext(), "Erreur lors de la création du Mimichi.", Toast.LENGTH_SHORT).show();
                            });
                });

    }
}