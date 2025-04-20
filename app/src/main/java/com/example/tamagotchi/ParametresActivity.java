package com.example.tamagotchi;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class ParametresActivity extends AppCompatActivity {

    private Button listeMimichi, modifierMimichi,supprimerMimichi, importerMimichi,exporterMimichi,f, deconnexion, creernouveauTamagotchi,i, reinitialiserMDP;
    private TextView retourButton;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parametres);
        listeMimichi = findViewById(R.id.listeMimichi);
        modifierMimichi = findViewById(R.id.modifierMimichi);
        supprimerMimichi = findViewById(R.id.supprimerMimichi);
        importerMimichi = findViewById(R.id.importerMimichi);
        exporterMimichi = findViewById(R.id.exporterMimichi);
        deconnexion = findViewById(R.id.deconnexion);
        creernouveauTamagotchi = findViewById(R.id.creernouveauTamagotchi);
        reinitialiserMDP = findViewById(R.id.reinitialiserMDP);
        retourButton = findViewById(R.id.retourButton);
        listeMimichi.setOnClickListener(v -> {
            startActivity(new Intent(this, ListeMimichiActivity.class));
        });

        modifierMimichi.setOnClickListener(v -> {
            startActivity(new Intent(this, ModifierMimichiActivity.class));
        });

        supprimerMimichi.setOnClickListener(v -> {
            startActivity(new Intent(this, SupprimerMimichiActivity.class));
        });

        importerMimichi.setOnClickListener(v -> {
            startActivity(new Intent(this, ImporterMimichiActivity.class));
        });

        exporterMimichi.setOnClickListener(v -> {
            startActivity(new Intent(this, ExporterMimichiActivity.class));
        });

        deconnexion.setOnClickListener(v -> {
            startActivity(new Intent(this, AuthActivity.class));
            finish(); // Pour empêcher de revenir avec le bouton retour
        });

        creernouveauTamagotchi.setOnClickListener(v -> {
            startActivity(new Intent(this, CreerMimichiActivity.class));
        });

        reinitialiserMDP.setOnClickListener(v -> {
            startActivity(new Intent(this, RenitialisationMdpActivity.class));
        });

        retourButton.setOnClickListener(v -> {
            finish();
        });
    }

}
