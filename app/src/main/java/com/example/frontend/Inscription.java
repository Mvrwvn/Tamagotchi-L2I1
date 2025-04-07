package com.example.frontend;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

class Inscription extends AppCompatActivity {

    private EditText etEmail, etPassword, etConfirmPassword;
    private Button btnInscription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inscription); // Assure-toi que ce fichier XML existe

        // Liaison avec le XML
        etEmail = findViewById(R.id.tama_email);
        etPassword = findViewById(R.id.editTextNumberPassword);
        btnInscription = findViewById(R.id.inscription);

        btnInscription.setOnClickListener(v -> {
            String email = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString().trim();
            String confirmPassword = etConfirmPassword.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                Toast.makeText(this, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show();
            } else if (!password.equals(confirmPassword)) {
                Toast.makeText(this, "Les mots de passe ne correspondent pas", Toast.LENGTH_SHORT).show();
            } else {
                // Ici, tu peux ajouter la logique pour enregistrer l'utilisateur
                Toast.makeText(this, "Inscription réussie !", Toast.LENGTH_SHORT).show();

                // Redirection vers la page de connexion
                startActivity(new Intent(Inscription.this, MainActivity.class));
                finish();
            }
        });
    }
}
