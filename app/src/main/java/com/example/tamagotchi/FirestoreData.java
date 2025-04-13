package com.example.tamagotchi;

/*
-----------------------------
    Date : 08/04/2025

    Membres qui travaillent dessus : Marwan DENAGNON

    Que fait le code ? : Classe avec des méthodes static qui va servir de bascule entre l'échanges des données sur Firestore et les données en Java.
-----------------------------
*/

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.View;


import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import android.widget.Toast;

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

    public static void setActiveTamagotchiId(String activeTamagotchiId){
        db.collection("joueurs")
                .document(userId)
                .update("activeTamagotchiId", activeTamagotchiId)
                .addOnSuccessListener(documentReference ->
                        Log.d("Firestore", "Tamagotchi actif maj" ))
                .addOnFailureListener(e ->
                        Log.e("Firestore", "Erreur de sauvegarde tamagotchi actif", e));
    }

    public static void actionTamagotchi(View v, String nomInventaire, String nomStatistique) {
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
                                        Double statistique = tamagotchiSnapshot.getDouble("statsTamagotchi."+nomStatistique);
                                        Long inventaire = tamagotchiSnapshot.getLong("inventaireTamagotchi.nb"+nomInventaire);

                                        if (statistique == null || inventaire == null) {
                                            Toast.makeText(v.getContext(), "Données manquantes dans le document.", Toast.LENGTH_SHORT).show();
                                            return;
                                        }

                                        if (statistique >= 0 && statistique < 100) {
                                            if (inventaire > 0) {
                                                db.collection("tamagotchis")
                                                        .document(activeTamagotchiId)
                                                        .update(
                                                                "statsTamagotchi."+nomStatistique, FieldValue.increment(1),
                                                                "inventaireTamagotchi.nb"+nomInventaire, FieldValue.increment(-1),
                                                                "dernierUpdate", Timestamp.now()
                                                        )
                                                        .addOnFailureListener(e -> {
                                                            Toast.makeText(v.getContext(), "Erreur lors du soin du Tamagotchi.", Toast.LENGTH_SHORT).show();
                                                        });
                                            } else {
                                                Toast.makeText(v.getContext(), "Pas assez de "+ nomInventaire +" dans l'inventaire.", Toast.LENGTH_SHORT).show();
                                            }
                                        } else {
                                            Toast.makeText(v.getContext(), nomStatistique +" est déjà au maximum.", Toast.LENGTH_SHORT).show();
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
}

    /*public static void verifierDernierUpdate(View v){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        db.collection("joueurs")
                .document(userId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        String activeTamagotchiId = documentSnapshot.getString("activeTamagotchiId");

                        db.collection("tamagotchis")
                                .document(activeTamagotchiId)
                                .get()
                                .addOnSuccessListener(tamagotchiSnapshot -> {
                                    if (tamagotchiSnapshot.exists()) {
                                        long now = Timestamp.now().getSeconds();
                                        long lastUpdate = tamagotchiSnapshot.getTimestamp("dernierUpdate").getSeconds();
                                        long heuresPassees = (now - lastUpdate) / 3600;
                                    if (heuresPassees > 0) {
                                        long variationStats = -2 * heuresPassees;
                                        long variationInventaire = 10 * heuresPassees;

                                        updates.put("statsTamagotchi.sante", FieldValue.increment(variationStats));
                                        updates.put("statsTamagotchi.faim", FieldValue.increment(variationStats));
                                        updates.put("statsTamagotchi.bonheur", FieldValue.increment(variationStats));
                                        updates.put("statsTamagotchi.energie", FieldValue.increment(variationStats));

                                        updates.put("inventaireTamagotchi.nbNourriture", FieldValue.increment(variationInventaire));
                                        updates.put("inventaireTamagotchi.nbJeux", FieldValue.increment(variationInventaire));
                                        updates.put("inventaireTamagotchi.nbMedicaments", FieldValue.increment(variationInventaire));

                                        updates.put("dernierUpdate", Timestamp.now());
                                }

                    }
        }
    }}*/