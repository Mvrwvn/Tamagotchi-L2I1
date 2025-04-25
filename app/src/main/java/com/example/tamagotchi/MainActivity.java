package com.example.tamagotchi;

/*
-----------------------------
    Date : 20/04/2025

    Membres qui travaillent dessus : Marwan DENAGNON

    Que fait le code ? : Permet de lire et écrire des données (attributs de la classe Tamagothci) sur Firestore chaque Tamagotchi est lié à un compte (userId = clé unique générée automatiquement pour chaque compte).
    Changement par rapport à la version précédente : Permet de faire lien entre le front end et la gestion des données, toutes la partie donnée est géré dans la class FirestoreData.java.
    Par exemple la méthode nourrir va être appeler quand l'utilisateur va cliquer sur le bouton "Nourrir"
    Changement par rapport à la version précédente : ajout d'un handler pour mettre à jour les données sur la bdd puis les charger dans la progressBar
-----------------------------
*/

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.airbnb.lottie.LottieAnimationView;
import com.airbnb.lottie.LottieDrawable;
import com.example.tamagotchi.model.Tamagotchi;
import com.example.tamagotchi.viewmodel.MainViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class MainActivity extends AppCompatActivity {
    private ProgressBar progressSante, progressFaim, progressBonheur, progressEnergie, progressHygiene, progressSoif;
    private final Handler handler = new Handler();
    private final int DELAY = 10000; // 10 secondes
    private ImageView parametre;
    private MainViewModel mainViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_accueil2);
        progressSante = findViewById(R.id.progressMedicament);
        progressFaim = findViewById(R.id.progressNourriture);
        progressBonheur = findViewById(R.id.progressJouet);
        progressEnergie = findViewById(R.id.progressSommeil);
        progressHygiene = findViewById(R.id.progressDouche);
        progressSoif = findViewById(R.id.progressBoisson);
        parametre = findViewById(R.id.imageViewIconeParametres);
        parametre.setOnClickListener(v -> {
            startActivity(new Intent(this, ParametresActivity.class));
        });
        mainViewModel = new ViewModelProvider(this).get(MainViewModel.class);

        // Observer le Tamagotchi actif
        mainViewModel.getActiveTamagotchi().observe(this, new Observer<Tamagotchi>() {
            @Override
            public void onChanged(Tamagotchi tamagotchi) {
                if (tamagotchi != null) {
                    double faim = tamagotchi.getStatsTamagotchi().getFaim(); // lecture du champ
                    progressFaim.setProgress((int)faim*10); // mise à jour de la barre
                } else {
                    progressFaim.setProgress(0); // au cas où
                }
            }
        });
        LottieAnimationView lottie = findViewById(R.id.lottie);
        lottie.setRepeatCount(LottieDrawable.INFINITE);
        lottie.playAnimation();
    }

    @Override
    protected void onResume(){
        super.onResume();
        handler.post(updateRunnable);
    }

    @Override
    protected void onPause() {
        super.onPause();
        handler.removeCallbacks(updateRunnable); // évite les fuites mémoire
    }

    private final Runnable updateRunnable = new Runnable() {
        @Override
        public void run() {
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            if (user != null) {
                MainViewModel.verifierDernierUpdate(user);
                MainViewModel.loadStatsFromFirestore(user, progressEnergie,"energie");
                MainViewModel.loadStatsFromFirestore(user, progressSoif,"soif");
                MainViewModel.loadStatsFromFirestore(user, progressHygiene,"hygiene");
                MainViewModel.loadStatsFromFirestore(user, progressSante,"sante");
                MainViewModel.loadStatsFromFirestore(user, progressBonheur,"bonheur");
            }
            handler.postDelayed(this, DELAY);
        }
    };

    public void deconnexion(View v){
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(MainActivity.this, InscriptionActivity.class );
        startActivity(intent);
    }

    public void reposer(View v){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {return;}
        MainViewModel.actionTamagotchi(user, v,progressEnergie,"Lits","energie");
    }

    public void hydrater(View v){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {return;}
        MainViewModel.actionTamagotchi(user, v,progressSoif,"Boissons","soif");
    }

    public void brosser(View v){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {return;}
        MainViewModel.actionTamagotchi(user, v,progressHygiene,"Savons","hygiene");
    }

    public void nourrir(View v) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {return;}
        MainViewModel.actionTamagotchi(user, v,progressFaim, "Nourritures", "faim");
    }

    public void soigner(View v) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {return;}
        MainViewModel.actionTamagotchi(user, v,progressSante,"Medicaments","sante");
    }

    public void jouer(View v){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {return;}
        MainViewModel.actionTamagotchi(user, v,progressBonheur,"Jouets","bonheur");
    }
}