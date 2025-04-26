package com.example.tamagotchi.view;
/*
-----------------------------
    Date : 20/04/2025

    Membres qui travaillent dessus : Marwan DENAGNON

    Que fait le code ? : Rien en cours de développement
-----------------------------
*/
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.tamagotchi.R;

public class SupprimerMimichiActivity extends AppCompatActivity {

    private ImageView imageViewSupprimerMimichi;
    private Button ajouterButton;
    private TextView retourButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_supprimer_mimichi); // Vérifie le nom du layout ici

        imageViewSupprimerMimichi = findViewById(R.id.imageViewSupprimerMimichi);
        ajouterButton = findViewById(R.id.ajouterButton);
        retourButton = findViewById(R.id.retourButton);

        // Action pour le bouton "Supprimer"
        ajouterButton.setOnClickListener(v -> {
            //en cours de dev
        });

        // Action pour le bouton "Retour"
        retourButton.setOnClickListener(v -> {
            finish(); // Retour à l'écran précédent
        });
    }
}
