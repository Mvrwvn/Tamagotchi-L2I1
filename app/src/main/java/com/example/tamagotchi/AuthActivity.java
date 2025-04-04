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

    private FirebaseAuth mAuth;
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

        mAuth = FirebaseAuth.getInstance();
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

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        Toast.makeText(AuthActivity.this, "Connexion réussie", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(AuthActivity.this, MainActivity.class );
                        startActivity(intent);
                    } else {
                        Toast.makeText(AuthActivity.this, "Échec de connexion", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void inscription(View v) {
        String email = emailInput.getText().toString().trim();
        String password = passwordInput.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show();
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        Toast.makeText(AuthActivity.this, "Inscription réussie", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(AuthActivity.this, "Échec de l'inscription", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void recuperationMotDePasse(View v) {
        String email = emailInput.getText().toString().trim();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();

        mAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // Le mail de réinitialisation a été envoyé
                        Toast.makeText(AuthActivity.this, "Email de réinitialisation envoyé.", Toast.LENGTH_SHORT).show();
                        Log.d("PasswordReset", "Email de réinitialisation envoyé.");
                        // Vous pouvez ajouter un message pour informer l'utilisateur
                    } else {
                        // En cas d'erreur, vous pouvez afficher un message d'erreur
                        Toast.makeText(AuthActivity.this, "Erreur lors de l'envoi de l'email : " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        Log.e("PasswordReset", "Erreur lors de l'envoi de l'email : " + task.getException().getMessage());
                    }
                });
    }
}
