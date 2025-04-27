package com.example.tamagotchi.view;
/*
-----------------------------
    Date : 27/04/2025

    Membres qui travaillent dessus : Marwan DENAGNON

    Que fait le code ? : Page des parametres
    Changement par rapport à la version précédente : Refonte de l'ui les boutons pour modifier et supprimer un tamagotchi sont directement intégrée au RecyclerView
-----------------------------
*/
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.tamagotchi.R;
import com.example.tamagotchi.view.recyclerview.ListeTamagotchiActivity;
import com.google.firebase.auth.FirebaseAuth;

public class ParametresActivity extends AppCompatActivity {

    private Button listeMimichi, importerMimichi,exporterMimichi, deconnexion, creernouveauTamagotchi, reinitialiserMDP;
    private TextView retourButton;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parametres);
        listeMimichi = findViewById(R.id.listeMimichi);
        importerMimichi = findViewById(R.id.importerMimichi);
        exporterMimichi = findViewById(R.id.exporterMimichi);
        deconnexion = findViewById(R.id.deconnexion);
        creernouveauTamagotchi = findViewById(R.id.creernouveauTamagotchi);
        reinitialiserMDP = findViewById(R.id.reinitialiserMDP);
        retourButton = findViewById(R.id.retourButton);
        listeMimichi.setOnClickListener(v -> {
            startActivity(new Intent(this, ListeTamagotchiActivity.class));
        });

        importerMimichi.setOnClickListener(v -> {
            Toast.makeText(this, "En cours de développement", Toast.LENGTH_SHORT).show();
        });

        exporterMimichi.setOnClickListener(v -> {
            Toast.makeText(this, "En cours de développement", Toast.LENGTH_SHORT).show();
        });

        deconnexion.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut(); // Déconnexion Firebase
            startActivity(new Intent(this, AuthActivity.class));
            finish(); // Pour empêcher de revenir avec le bouton retour
        });

        creernouveauTamagotchi.setOnClickListener(v -> {
            startActivity(new Intent(this, CreerTamagotchiActivity.class));
        });

        reinitialiserMDP.setOnClickListener(v -> {
            startActivity(new Intent(this, RenitialisationMdpActivity.class));
        });

        retourButton.setOnClickListener(v -> {
            finish();
        });
    }

}
