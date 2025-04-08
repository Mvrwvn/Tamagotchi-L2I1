package com.example.tamagotchi;

import android.util.Log;
import android.widget.TextView;

import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import android.content.Context;
import android.content.SharedPreferences;

import java.util.concurrent.CountDownLatch;

public class FirestoreData {
    private final FirebaseFirestore db;
    private final String userId;

    public FirestoreData() {
        db = FirebaseFirestore.getInstance();
        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        // Activer la persistance hors ligne
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(true)
                .build();
        db.setFirestoreSettings(settings);
    }

    public void sauvegarderTamagotchi(Tamagotchi tamagotchi) {
        db.collection("tamagotchis")
                .add(tamagotchi)
                .addOnSuccessListener(documentReference ->
                        Log.d("Firestore", "Tamagotchi sauvegardé avec ID : " + documentReference.getId()))
                .addOnFailureListener(e ->
                        Log.e("Firestore", "Erreur de sauvegarde", e));
    }

    public void chargerTamagotchi(TextView textView) {
        db.collection("tamagotchis")
                .whereEqualTo("userId", userId)
                .get()
                .addOnCompleteListener(task -> {
                    StringBuilder info = new StringBuilder();
                    if (task.isSuccessful()) {
                        for (DocumentSnapshot document : task.getResult()) {
                            Tamagotchi tama = document.toObject(Tamagotchi.class);
                            info.append(tama.toString());
                            /*String nomTamagotchi = document.getString("nomTamagotchi");
                            String userId = document.getString("userId");
                            String genre = document.getString("genre");
                            String dateNaissance = document.getString("dateNaissance");
                            String statsTamagotchi = document.getString("statsTamagotchi");
                            String inventaireTamagotchi = document.getString("inventaireTamagotchi");
                            String nbNourritures = document.getString("nbNourritures");
                            String nbBoissons = document.getString("nbBoissons");
                            String nbLits = document.getString("nbLits");
                            String nbMedicaments = document.getString("nbMedicaments");
                            String nbSavons = document.getString("nbSavons");
                            info.append("Nom : ").append(nomTamagotchi).append("\n");
                            info.append("Faim : ").append(userId).append("\n");
                            info.append("Soif : ").append(genre).append("\n");
                            info.append("Énergie : ").append(dateNaissance).append("\n");
                            info.append("Santé : ").append(statsTamagotchi).append("\n");
                            info.append("Hygiène : ").append(inventaireTamagotchi).append("\n");
                            info.append("Nourritures : ").append(nbNourritures).append("\n");
                            info.append("Boissons : ").append(nbBoissons).append("\n");
                            info.append("Lits : ").append(nbLits).append("\n");
                            info.append("Médicaments : ").append(nbMedicaments).append("\n");
                            info.append("Savons : ").append(nbSavons).append("\n");
                            Log.d("Firestore", nomTamagotchi);*/
                        }
                    }
                    else {
                        info.append("Erreur de récupération des Tamagotchi : ").append(task.getException().getMessage()).append("\n");
                        Log.d("Firestore", "erreur");
                    }
                    textView.setText(info.toString());
                });
    }

    public static void updateTamagotchiStats(Statistique newStats) {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();

            // Récupérer le document de l'utilisateur pour trouver l'ID du Tamagotchi actif
            FirebaseFirestore.getInstance()
                    .collection("joueurs")
                    .document(userId)
                    .get()
                    .addOnSuccessListener(userSnapshot -> {
                        if (userSnapshot.exists()) {
                            String activeTamagotchiId = userSnapshot.getString("activeTamagotchiId");

                            if (activeTamagotchiId != null) {
                                // Récupérer le Tamagotchi actif à partir de Firestore
                                FirebaseFirestore.getInstance()
                                        .collection("tamagotchis")
                                        .document(activeTamagotchiId)
                                        .update(
                                                "stats.vie", newStats.getVie(),
                                                "stats.faim", newStats.getFaim(),
                                                "stats.soif", newStats.getSoif(),
                                                "stats.sante", newStats.getSante(),
                                                "stats.energie", newStats.getEnergie(),
                                                "stats.hygiene", newStats.getHygiene(),
                                                "stats.dernierUpdate", newStats.getDernierUpdate()
                                        )
                                        .addOnSuccessListener(aVoid -> {
                                            Log.d("Firestore", "Statistiques du Tamagotchi mises à jour avec succès !");
                                        })
                                        .addOnFailureListener(e -> {
                                            Log.e("Firestore", "Erreur lors de la mise à jour des statistiques", e);
                                        });
                            }
                        }
                    })
                    .addOnFailureListener(e -> {
                        Log.e("Firestore", "Erreur lors de la récupération de l'utilisateur", e);
                    });
        }
    }
    public static void updateTamagotchiInventaire(Inventaire newInventaire) {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();

            // Récupérer le document de l'utilisateur pour trouver l'ID du Tamagotchi actif
            FirebaseFirestore.getInstance()
                    .collection("joueurs")
                    .document(userId)
                    .get()
                    .addOnSuccessListener(userSnapshot -> {
                        if (userSnapshot.exists()) {
                            String activeTamagotchiId = userSnapshot.getString("activeTamagotchiId");

                            if (activeTamagotchiId != null) {
                                FirebaseFirestore.getInstance()
                                        .collection("tamagotchis")
                                        .document(activeTamagotchiId)
                                        .update(
                                                "inventaire.nbNourritures", newInventaire.getNbNourritures(),
                                                "inventaire.nbBoissons", newInventaire.getNbBoissons(),
                                                "inventaire.nbMedicaments", newInventaire.getNbMedicaments(),
                                                "inventaire.nbLits", newInventaire.getNbLits(),
                                                "inventaire.nbSavons", newInventaire.getNbSavons(),
                                                "inventaire.dernierUpdate", newInventaire.getDernierUpdate()
                                        )
                                        .addOnSuccessListener(aVoid -> {
                                            Log.d("Firestore", "Inventaire du Tamagotchi mis à jour avec succès !");
                                        })
                                        .addOnFailureListener(e -> {
                                            Log.e("Firestore", "Erreur lors de la mise à jour de l'inventaire", e);
                                        });
                            }
                        }
                    })
                    .addOnFailureListener(e -> {
                        Log.e("Firestore", "Erreur lors de la récupération de l'utilisateur", e);
                    });
        }
    }

    public String getActiveTamagotchiId(FirebaseUser currentUser) {
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        final CountDownLatch latch = new CountDownLatch(1);
        final String[] activeTamagotchiId = new String[1];

        firestore.collection("joeuurs")
                .document(currentUser.getUid())
                .get()
                .addOnSuccessListener(userSnapshot -> {
                    if (userSnapshot.exists()) {
                        activeTamagotchiId[0] = userSnapshot.getString("activeTamagotchiId");
                    }
                    latch.countDown();
                })
                .addOnFailureListener(e -> latch.countDown());

        try {
            latch.await();  // Attendre que la réponse de Firestore soit reçue
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return activeTamagotchiId[0];
    }

    // Récupérer toutes les données du Tamagotchi actif
    public Tamagotchi getTamagotchiData(String tamagotchiId) {
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        final CountDownLatch latch = new CountDownLatch(1);
        final Tamagotchi[] tamagotchi = new Tamagotchi[1];

        firestore.collection("tamagotchis")
                .document(tamagotchiId)
                .get()
                .addOnSuccessListener(tamagotchiSnapshot -> {
                    if (tamagotchiSnapshot.exists()) {
                        String userId = tamagotchiSnapshot.getString("userId");
                        String nomTamagotchi = tamagotchiSnapshot.getString("nomTamagotchi");
                        String genre = tamagotchiSnapshot.getString("genre");
                        Timestamp dateNaissance = tamagotchiSnapshot.getTimestamp("dateNaissance");
                        Statistique statsTamagotchi = extractStats(tamagotchiSnapshot);
                        Inventaire inventaireTamagotchi = extractInventaire(tamagotchiSnapshot);

                        tamagotchi[0] = new Tamagotchi(userId, nomTamagotchi, genre, dateNaissance, statsTamagotchi, inventaireTamagotchi);
                    }
                    latch.countDown();
                })
                .addOnFailureListener(e -> latch.countDown());

        try {
            latch.await();  // Attendre que la réponse de Firestore soit reçue
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return tamagotchi[0];
    }

    // Extraire les statistiques d'un Tamagotchi
    private Statistique extractStats(DocumentSnapshot snapshot) {
        double vie = snapshot.getDouble("stats.vie");
        double faim = snapshot.getDouble("stats.faim");
        double soif = snapshot.getDouble("stats.soif");
        double sante = snapshot.getDouble("stats.sante");
        double energie = snapshot.getDouble("stats.energie");
        double hygiene = snapshot.getDouble("stats.hygiene");
        Timestamp dernierUpdate = snapshot.getTimestamp("stats.dernierUpdate");

        return new Statistique(vie, faim, soif, sante, energie, hygiene, dernierUpdate);
    }

    // Extraire l'inventaire d'un Tamagotchi
    private Inventaire extractInventaire(DocumentSnapshot snapshot) {
        int nbNourritures = snapshot.getLong("inventaire.nbNourritures").intValue();
        int nbBoissons = snapshot.getLong("inventaire.nbBoissons").intValue();
        int nbMedicaments = snapshot.getLong("inventaire.nbMedicaments").intValue();
        int nbLits = snapshot.getLong("inventaire.nbLits").intValue();
        int nbSavons = snapshot.getLong("inventaire.nbSavons").intValue();
        Timestamp dernierUpdate = snapshot.getTimestamp("inventaire.dernierUpdate");

        return new Inventaire(nbNourritures, nbBoissons, nbMedicaments, nbLits, nbSavons, dernierUpdate);
    }
}

