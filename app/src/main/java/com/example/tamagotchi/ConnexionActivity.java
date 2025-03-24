package com.example.tamagotchi;

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

public class ConnexionActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private EditText emailInput, passwordInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.connexion_inscription);

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
                        Toast.makeText(ConnexionActivity.this, "Connexion réussie", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(ConnexionActivity.this, MainActivity2.class );
                        startActivity(intent);
                    } else {
                        Toast.makeText(ConnexionActivity.this, "Échec de connexion", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(ConnexionActivity.this, "Inscription réussie", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(ConnexionActivity.this, "Échec de l'inscription", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(ConnexionActivity.this, "Email de réinitialisation envoyé.", Toast.LENGTH_SHORT).show();
                        Log.d("PasswordReset", "Email de réinitialisation envoyé.");
                        // Vous pouvez ajouter un message pour informer l'utilisateur
                    } else {
                        // En cas d'erreur, vous pouvez afficher un message d'erreur
                        Toast.makeText(ConnexionActivity.this, "Erreur lors de l'envoi de l'email : " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        Log.e("PasswordReset", "Erreur lors de l'envoi de l'email : " + task.getException().getMessage());
                    }
                });
    }

}
