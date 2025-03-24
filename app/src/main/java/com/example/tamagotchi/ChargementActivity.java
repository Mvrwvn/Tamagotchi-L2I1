package com.example.tamagotchi;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ChargementActivity extends AppCompatActivity {
    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            // L'utilisateur est connecté, rediriger vers l'écran principal
            startActivity(new Intent(this, MainActivity2.class));
            finish();
        } else {
            // L'utilisateur n'est pas connecté, rediriger vers l'écran de connexion
            startActivity(new Intent(this, ConnexionActivity.class));
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.chargement);
    }

}
