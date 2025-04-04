package com.example.tamagotchi;

/*
-----------------------------
    Date : 31/03/2025

    Membres qui travaillent dessus : Marwan DENAGNON

    Que fait le code ? : Permet de lire et écrire des données (attributs de la classe Tamagothci) sur Firestore chaque Tamagotchi est lié à un compte (userId = clé unique générée automatiquement pour chaque compte).
-----------------------------
*/

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import java.time.LocalDate;
import java.time.LocalDateTime;


public class MainActivity extends AppCompatActivity {
    private String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        db = FirebaseFirestore.getInstance();
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(true)
                .build();
        db.setFirestoreSettings(settings);
    }

    public void sauvegarderTamagotchi(Tamagotchi tama) {
        db.collection("tamagotchis")
                .add(tama) // Enregistre l'objet directement
                .addOnSuccessListener(aVoid -> Log.d("Firestore", "Tamagotchi sauvegardé !"))
                .addOnFailureListener(e -> Log.e("Firestore", "Erreur de sauvegarde", e));
    }

    public void chargerTamagotchi() {
        db.collection("tamagotchis")
                .whereEqualTo("userId", userId)  // Filtre pour récupérer les tamagotchis du user
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (DocumentSnapshot document : task.getResult()) {
                            String nomTamagotchi = document.getString("nomTamagotchi");

                            Log.d("Firestore", "Tamagotchi trouvé : " + nomTamagotchi);
                        }
                    } else {
                        Log.e("Firestore", "Erreur de récupération des Tamagotchi", task.getException());
                    }
                });
    }

    public void deconnexion(View v){
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(MainActivity.this, AuthActivity.class );
        startActivity(intent);
    }

    public void ecriture(View v) {
        Statistique stats = new Statistique(100, 100, 100, 100, 100, LocalDateTime.now());
        Tamagotchi tama = new Tamagotchi(userId, "Marwan", LocalDate.now(), stats);
        sauvegarderTamagotchi(tama);
    }


    public void lecture(View v) {
        chargerTamagotchi();
    }
}