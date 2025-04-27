package com.example.tamagotchi.view;

/*
-----------------------------
    Date : 27/04/2025

    Membres qui travaillent dessus : Marwan DENAGNON

    Que fait le code ? : Permet de lire et écrire des données (attributs de la classe Tamagothci) sur Firestore chaque Tamagotchi est lié à un compte (userId = clé unique générée automatiquement pour chaque compte).
    Changement par rapport à la version précédente : Permet de faire lien entre le front end et la gestion des données, toutes la partie donnée est géré dans la class FirestoreData.java.
    Par exemple la méthode nourrir va être appeler quand l'utilisateur va cliquer sur le bouton "Nourrir"
    Changement par rapport à la version précédente : ajout d'un handler pour mettre à jour les données sur la bdd puis les charger dans la progressBar
    Changement par rapport à la version précédente : supression es handler car grâce au modèle MVVM et aux LiveData que le ViewModel renvoie je peut mettre un Observer et avoir les donées modifiée constament.
-----------------------------
*/

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.airbnb.lottie.LottieAnimationView;
import com.airbnb.lottie.LottieDrawable;
import com.example.tamagotchi.R;
import com.example.tamagotchi.model.Tamagotchi;
import com.example.tamagotchi.viewmodel.MainViewModel;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;


public class MainActivity extends AppCompatActivity {
    private ProgressBar progressSante, progressFaim, progressBonheur, progressEnergie, progressHygiene, progressSoif;
    private final Handler handler = new Handler();
    private final int DELAY = 10000; // 10 secondes
    private ImageView parametre;
    private MainViewModel mainViewModel;
    private Tamagotchi activeTamagotchi;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
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
        //Observer les messages de succès ou d'erreur
        mainViewModel.getMessageLiveData().observe(this, message -> {
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        });

        // Observer le Tamagotchi actif
        mainViewModel.getActiveTamagotchi().observe(this, new Observer<Tamagotchi>() {
            @Override
            public void onChanged(Tamagotchi tamagotchi) {
                if (tamagotchi != null) {
                    activeTamagotchi = tamagotchi;
                    long now = Timestamp.now().getSeconds();
                    long lastUpdate = tamagotchi.getDernierUpdate().getSeconds();
                    long heuresPassees = (now - lastUpdate) / 3600;
                    double faim  = tamagotchi.getStatsTamagotchi().getFaim(); // lecture du champ
                    double soif = tamagotchi.getStatsTamagotchi().getSoif(); // lecture du champ
                    double sante = tamagotchi.getStatsTamagotchi().getSante(); // lecture du champ
                    double energie = tamagotchi.getStatsTamagotchi().getEnergie(); // lecture du champ
                    double hygiene = tamagotchi.getStatsTamagotchi().getHygiene(); // lecture du champ
                    double bonheur = tamagotchi.getStatsTamagotchi().getBonheur(); // lecture du champ
                    progressFaim.setProgress((int)faim); // mise à jour de la barre
                    progressSoif.setProgress((int)soif); // mise à jour de la barre
                    progressSante.setProgress((int)sante); // mise à jour de la barre
                    progressEnergie.setProgress((int)energie); // mise à jour de la barre
                    progressHygiene.setProgress((int)hygiene); // mise à jour de la barre
                    progressBonheur.setProgress((int)bonheur); // mise à jour de la barre
                    if(heuresPassees > 0){
                        tamagotchi.getStatsTamagotchi().degraderStatAutomatiquement(heuresPassees);
                        tamagotchi.getInventaireTamagotchi().augmenterInventairesAutomatiquement(heuresPassees);
                        mainViewModel.updateTamagotchi(tamagotchi);
                    }

                } else {
                    progressFaim.setProgress(0);
                    progressSoif.setProgress(0);
                    progressSante.setProgress(0);
                    progressEnergie.setProgress(0);
                    progressHygiene.setProgress(0);
                    progressBonheur.setProgress(0);// au cas où
                }
            }
        });
        LottieAnimationView lottie = findViewById(R.id.lottie);
        lottie.setRepeatCount(LottieDrawable.INFINITE);
        lottie.playAnimation();
    }

    public void deconnexion(View v){
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(MainActivity.this, InscriptionActivity.class );
        startActivity(intent);
    }

    public void reposer(View v){
        int nbLits = activeTamagotchi.getInventaireTamagotchi().getNbLits();
        double energie = activeTamagotchi.getStatsTamagotchi().getEnergie();
        activeTamagotchi.getInventaireTamagotchi().setNbLits(nbLits-1);
        activeTamagotchi.getStatsTamagotchi().setEnergie(energie+1);
        if (energie >= 0 && energie < 100) {
            if (nbLits > 0) {
                mainViewModel.updateTamagotchi(activeTamagotchi);
            } else {
                Toast.makeText(this, "Vous n'avez plus de lits", Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(this, "L'énergie est déjà au maximum", Toast.LENGTH_SHORT).show();
        }
    }

    public void hydrater(View v){
        int nbBoissons = activeTamagotchi.getInventaireTamagotchi().getNbBoissons();
        double soif = activeTamagotchi.getStatsTamagotchi().getSoif();
        activeTamagotchi.getInventaireTamagotchi().setNbBoissons(nbBoissons-1);
        activeTamagotchi.getStatsTamagotchi().setSoif(soif+1);
        if (soif >= 0 && soif < 100) {
            if (nbBoissons > 0) {
                mainViewModel.updateTamagotchi(activeTamagotchi);
            } else {
                Toast.makeText(this, "Vous n'avez plus de boissons", Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(this, "La soif est déjà au maximum", Toast.LENGTH_SHORT).show();
        }
    }

    public void brosser(View v){
        int nbSavons = activeTamagotchi.getInventaireTamagotchi().getNbSavons();
        double hygiene = activeTamagotchi.getStatsTamagotchi().getHygiene();
        activeTamagotchi.getInventaireTamagotchi().setNbSavons(nbSavons-1);
        activeTamagotchi.getStatsTamagotchi().setHygiene(hygiene+1);
        if (hygiene >= 0 && hygiene < 100) {
            if (nbSavons > 0) {
                mainViewModel.updateTamagotchi(activeTamagotchi);
            } else {
                Toast.makeText(this, "Vous n'avez plus de savons", Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(this, "L'hygiène est déjà au maximum", Toast.LENGTH_SHORT).show();
        }
    }

    public void nourrir(View v) {
        int nbNourritures = activeTamagotchi.getInventaireTamagotchi().getNbNourritures();
        double faim = activeTamagotchi.getStatsTamagotchi().getFaim();
        activeTamagotchi.getInventaireTamagotchi().setNbNourritures(nbNourritures-1);
        activeTamagotchi.getStatsTamagotchi().setFaim(faim+1);
        if (faim >= 0 && faim < 100) {
            if (nbNourritures > 0) {
                mainViewModel.updateTamagotchi(activeTamagotchi);
            } else {
                Toast.makeText(this, "Vous n'avez plus de nourritures", Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(this, "La faim est déjà au maximum", Toast.LENGTH_SHORT).show();
        }
    }

    public void soigner(View v) {
        int nbMedicaments = activeTamagotchi.getInventaireTamagotchi().getNbMedicaments();
        double sante = activeTamagotchi.getStatsTamagotchi().getSante();
        activeTamagotchi.getInventaireTamagotchi().setNbMedicaments(nbMedicaments-1);
        activeTamagotchi.getStatsTamagotchi().setSante(sante+1);
        if (sante >= 0 && sante < 100) {
            if (nbMedicaments > 0) {
                mainViewModel.updateTamagotchi(activeTamagotchi);
            } else {
                Toast.makeText(this, "Vous n'avez plus de médicaments", Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(this, "La santé est déjà au maximum", Toast.LENGTH_SHORT).show();
        }
    }

    public void jouer(View v){
        int nbJouets = activeTamagotchi.getInventaireTamagotchi().getNbJouets();
        double bonheur = activeTamagotchi.getStatsTamagotchi().getBonheur();
        activeTamagotchi.getInventaireTamagotchi().setNbJouets(nbJouets-1);
        activeTamagotchi.getStatsTamagotchi().setBonheur(bonheur+1);
        if (bonheur >= 0 && bonheur < 100) {
            if (nbJouets > 0) {
                mainViewModel.updateTamagotchi(activeTamagotchi);
            } else {
                Toast.makeText(this, "Vous n'avez plus de jouets", Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(this, "Le bonheur est déjà au maximum", Toast.LENGTH_SHORT).show();
        }
    }
}