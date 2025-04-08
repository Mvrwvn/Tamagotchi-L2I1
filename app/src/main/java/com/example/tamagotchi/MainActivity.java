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
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;
import com.airbnb.lottie.LottieDrawable;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;


public class MainActivity extends AppCompatActivity {
    private String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
    private FirestoreData firestoreData = new FirestoreData();
    private TextView textView;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        db = FirebaseFirestore.getInstance();
        textView = findViewById(R.id.textView);
        textView.setVisibility(View.VISIBLE);
        LottieAnimationView lottie = findViewById(R.id.lottie);
        lottie.setRepeatCount(LottieDrawable.INFINITE);
        lottie.playAnimation();
    }

    public void deconnexion(View v){
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(MainActivity.this, InscriptionActivity.class );
        startActivity(intent);
    }

    public void brosser(View v){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("tamagotchis")
                .document("6TScsrL5iBBmIvdNbHD9")
                .update("statsTamagotchi.hygiene", FieldValue.increment(10))
                .addOnSuccessListener(aVoid -> {
                    // Succès de la mise à jour
                    Log.d("Firestore", "Valeur incrémentée avec succès !");
                })
                .addOnFailureListener(e -> {
                    // Erreur lors de la mise à jour
                    Log.e("Firestore", "Erreur lors de l'incrémentation", e);
                });
    }

    public void creer(View v) {
        Inventaire inventaire = new Inventaire(10,10,10,10,10, Timestamp.now());
        Statistique stats = new Statistique(100,100, 100, 100, 100, 100, Timestamp.now());
        Tamagotchi tama = new Tamagotchi(userId, "Marwan","male", Timestamp.now(), stats, inventaire);
        firestoreData.sauvegarderTamagotchi(tama);
    }


    public void lecture(View v) {
        firestoreData.chargerTamagotchi(textView);
    }
}