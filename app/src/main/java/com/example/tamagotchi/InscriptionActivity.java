package com.example.tamagotchi;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class InscriptionActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private EditText emailInput, passwordInput, nomTamagotchiInput, usernameInput;
    private RadioButton radio_male, radio_female;
    private RadioGroup genderRadioGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.inscription);
        mAuth = FirebaseAuth.getInstance();
        emailInput = findViewById(R.id.emailInput);
        passwordInput = findViewById(R.id.passwordInput);
        nomTamagotchiInput = findViewById(R.id.nomTamagotchiInput);
        usernameInput = findViewById(R.id.usernameInput);
    }
    public void inscription(View v) {
        String email = emailInput.getText().toString().trim();
        String password = passwordInput.getText().toString().trim();
        String nomTamagotchi = nomTamagotchiInput.getText().toString().trim();
        String username = usernameInput.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show();
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        if (user != null) {
                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                    .setDisplayName(username)
                                    .build();

                            user.updateProfile(profileUpdates)
                                    .addOnCompleteListener(updateTask -> {
                                        if (updateTask.isSuccessful()) {
                                            Toast.makeText(InscriptionActivity.this, "Inscription réussie" + user.getDisplayName(), Toast.LENGTH_SHORT).show();
                                            Inventaire inventaire = new Inventaire(10,10,10,10,10, Timestamp.now());
                                            Statistique stats = new Statistique(100, 100, 100, 100, 100,100, Timestamp.now());
                                            Tamagotchi tamagotchi = new Tamagotchi(user.getUid(), nomTamagotchi, "temp", Timestamp.now(), stats, inventaire);
                                            FirestoreData firestoreData = new FirestoreData();
                                            firestoreData.sauvegarderTamagotchi(tamagotchi);
                                            Intent intent = new Intent(InscriptionActivity.this, MainActivity.class );
                                            startActivity(intent);
                                        } else {
                                            Toast.makeText(InscriptionActivity.this, "Échec de la mise à jour du profil", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }
                    } else {
                        Toast.makeText(InscriptionActivity.this, "Échec de l'inscription", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void retour(View v){
        Intent intent = new Intent(InscriptionActivity.this,AuthActivity.class);
        startActivity(intent);
    }
}
