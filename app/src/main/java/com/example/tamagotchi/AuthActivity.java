package com.example.tamagotchi;

/*
-----------------------------
    Date : 24/03/2025

    Membres qui travaillent dessus : Marwan DENAGNON

    Que fait le code ? : Permet à l'utilisateur de s'inscrire/se connecter avec un email et un mot de passe. Connexion internet requise pour l'inscription et la première connexion.
    Récupération de mot de passe par email
    L'application se connecte automatiquement au compte du dernier utilisateur qui à passer la phase de connexion (même sans internet) grâce à des fichiers en local.
-----------------------------
*/

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class AuthActivity extends AppCompatActivity {
    private EditText emailInput, passwordInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_auth);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        emailInput = findViewById(R.id.usernameInput);
        passwordInput = findViewById(R.id.passwordInput);
    }

    public void connexion(View v) {
        String email = emailInput.getText().toString().trim();
        String password = passwordInput.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show();
            return;
        }
        Joueur.connexion(this,email,password);
        Intent intent = new Intent(AuthActivity.this, MainActivity.class );
        startActivity(intent);
    }

    public void inscription(View v) {
        String email = emailInput.getText().toString().trim();
        String password = passwordInput.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show();
            return;
        }
        Joueur.inscription(this,email,password);
    }

    public void recuperationMotDePasse(View v) {
        String email = emailInput.getText().toString().trim();
        Joueur.renitialiserMotDePasse(this,email);
    }
}
