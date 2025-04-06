package com.example.tamagotchi;

import android.util.Log;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;

import org.w3c.dom.Text;

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
}
