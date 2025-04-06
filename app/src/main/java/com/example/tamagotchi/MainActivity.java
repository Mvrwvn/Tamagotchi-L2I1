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
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import java.time.LocalDate;


public class MainActivity extends AppCompatActivity {
    private String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
    private FirestoreData firestoreData = new FirestoreData();
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        textView = findViewById(R.id.textView);
        textView.setVisibility(View.VISIBLE);
    }

    public void deconnexion(View v){
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(MainActivity.this, AuthActivity.class );
        startActivity(intent);
    }

    public void brosser(View v){

    }

    public void creer(View v) {
        Inventaire inventaire = new Inventaire(10,10,10,10,10, Timestamp.now());
        Statistique stats = new Statistique(100,100, 100, 100, 100, 100, Timestamp.now());
        Tamagotchi tama = new Tamagotchi(userId, "Marwan","male", LocalDate.now(), stats, inventaire);
        firestoreData.sauvegarderTamagotchi(tama);
    }


    public void lecture(View v) {
        firestoreData.chargerTamagotchi(textView);
    }
}