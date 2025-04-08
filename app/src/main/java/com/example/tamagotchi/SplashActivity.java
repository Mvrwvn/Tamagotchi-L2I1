package com.example.tamagotchi;

/*
-----------------------------
    Date : 30/03/2025

    Membres qui travaillent dessus : Marwan DENAGNON

    Que fait le code ? : Affiche un splash screen pendant 3 secondes et, en arrière-plan, vérifie si un utilisateur est connecté
    grâce aux fichiers cache et assure le bon chargement de l'application.
-----------------------------
*/

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import android.os.Handler;
import android.os.Looper;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
            if (currentUser != null) {
                startActivity(new Intent(this, MainActivity.class));
            } else {
                startActivity(new Intent(this, InscriptionActivity.class));
            }
            finish();  // Pour que l'écran de splash ne soit pas accessible après
        }, 3000); // Délai de 3 secondes
    }
}