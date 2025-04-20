package com.example.tamagotchi;
/*
-----------------------------
    Date : 20/04/2025

    Membres qui travaillent dessus : Marwan DENAGNON

    Que fait le code ? : Page de réinitialisation de mot de passe
-----------------------------
*/
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class RenitialisationMdpActivity extends AppCompatActivity {

    private EditText emailInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_reinitialisation_mdpv1);
        emailInput = findViewById(R.id.email_input);
    }

    public void recuperationMotDePasse(View v) {
        String email = emailInput.getText().toString().trim();
        Joueur.renitialiserMotDePasse(this,email);
    }

    public void load_authActivity(View v){
        Intent intent = new Intent(this, AuthActivity.class);
        startActivity(intent);
    }
}
