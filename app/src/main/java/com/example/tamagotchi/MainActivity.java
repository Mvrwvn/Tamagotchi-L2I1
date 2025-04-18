package com.example.tamagotchi;

/*
-----------------------------
    Date : 13/04/2025

    Membres qui travaillent dessus : Marwan DENAGNON

    Que fait le code ? : Permet de lire et écrire des données (attributs de la classe Tamagothci) sur Firestore chaque Tamagotchi est lié à un compte (userId = clé unique générée automatiquement pour chaque compte).
    Changement par rapport à la version précédente : Permet de faire lien entre le front end et la gestion des données, toutes la partie donnée est géré dans la class FirestoreData.java.
    Par exemple la méthode nourrir va être appeler quand l'utilisateur va cliquer sur le bouton "Nourrir"
-----------------------------
*/

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;
import com.airbnb.lottie.LottieDrawable;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;


public class MainActivity extends AppCompatActivity {
    private String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
    private TextView textView;
    private ProgressBar progressSante, progressFaim, progressBonheur, progressEnergie, progressHygiene, progressSoif;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        textView = findViewById(R.id.textView);
        progressSante = findViewById(R.id.progressSante);
        progressFaim = findViewById(R.id.progressFaim);
        progressBonheur = findViewById(R.id.progressBonheur);
        progressEnergie = findViewById(R.id.progressEnergie);
        progressHygiene = findViewById(R.id.progressHygiene);
        progressSoif = findViewById(R.id.progressSoif);
        textView.setVisibility(View.VISIBLE);
        LottieAnimationView lottie = findViewById(R.id.lottie);
        lottie.setRepeatCount(LottieDrawable.INFINITE);
        lottie.playAnimation();
    }
    @Override
    protected void onResume(){
        super.onResume();
        FirestoreData.verifierDernierUpdate();
        FirestoreData.loadStatsFromFirestore(progressFaim,"faim");
        FirestoreData.loadStatsFromFirestore(progressEnergie,"energie");
        FirestoreData.loadStatsFromFirestore(progressSoif,"soif");
        FirestoreData.loadStatsFromFirestore(progressHygiene,"hygiene");
        FirestoreData.loadStatsFromFirestore(progressSante,"sante");
    }

    public void deconnexion(View v){
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(MainActivity.this, InscriptionActivity.class );
        startActivity(intent);
    }

    public void reposer(View v){
        FirestoreData.actionTamagotchi(v,progressEnergie,"Lits","energie");
    }

    public void hydrater(View v){
        FirestoreData.actionTamagotchi(v,progressSoif,"Boissons","soif");
    }

    public void brosser(View v){
        FirestoreData.actionTamagotchi(v,progressHygiene,"Savons","hygiene");
    }

    public void nourrir(View v) {
        FirestoreData.actionTamagotchi(v,progressFaim, "Nourritures", "faim");
    }

    public void soigner(View v) {
        FirestoreData.actionTamagotchi(v,progressSante,"Medicaments","sante");
    }


    public void creer(View v) {
        Inventaire inventaire = new Inventaire(10,0,2,10,10);
        Statistique stats = new Statistique(50,50, 50, 80, 90, 100);
        Tamagotchi tama = new Tamagotchi(userId, "Marwan","male", Timestamp.now(), Timestamp.now(), stats, inventaire);
        FirestoreData.sauvegarderTamagotchi(tama);
    }
}