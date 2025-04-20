package com.example.tamagotchi;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class SupprimerMimichiActivity extends AppCompatActivity {

    private ImageView imageViewSupprimerMimichi;
    private Button ajouterButton;
    private TextView retourButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_supprimer_mimichi); // Vérifie le nom du layout ici

        // Liaison des vues
        imageViewSupprimerMimichi = findViewById(R.id.imageViewSupprimerMimichi);
        ajouterButton = findViewById(R.id.ajouterButton);
        retourButton = findViewById(R.id.retourButton);

        // Action pour le bouton "Supprimer"
        ajouterButton.setOnClickListener(v -> {
            // À adapter avec la logique de suppression réelle
            Toast.makeText(this, "Mimichi supprimé 😢", Toast.LENGTH_SHORT).show();
            // finish(); ou redirection selon ce que tu veux
        });

        // Action pour le bouton "Retour"
        retourButton.setOnClickListener(v -> {
            finish(); // Retour à l'écran précédent
        });
    }
}
