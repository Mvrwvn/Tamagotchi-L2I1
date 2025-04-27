package com.example.tamagotchi.view;
/*
-----------------------------
    Date : 20/04/2025

    Membres qui travaillent dessus : Marwan DENAGNON

    Que fait le code ? : Page d'inscription fusion front end back end
-----------------------------
*/
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.tamagotchi.R;
import com.example.tamagotchi.model.Inventaire;
import com.example.tamagotchi.model.Joueur;
import com.example.tamagotchi.model.Statistique;
import com.example.tamagotchi.model.Tamagotchi;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class InscriptionActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private EditText emailInput, passwordInput, nomTamagotchiInput, usernameInput;
    private RadioButton radio_male, radio_female;
    private RadioGroup genderRadioGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_inscriptionv1);
        mAuth = FirebaseAuth.getInstance();
        emailInput = findViewById(R.id.emailInput);
        passwordInput = findViewById(R.id.passwordInput);
        nomTamagotchiInput = findViewById(R.id.nomTamagotchiInput);
        usernameInput = findViewById(R.id.usernameInput);
        genderRadioGroup = findViewById(R.id.radio_group_sex);
        radio_male = findViewById(R.id.radio_male);
        radio_female = findViewById(R.id.radio_female);
    }
    public void inscription(View v) {
        String email = emailInput.getText().toString().trim();
        String password = passwordInput.getText().toString().trim();
        String nomTamagotchi = nomTamagotchiInput.getText().toString().trim();
        String username = usernameInput.getText().toString().trim();
        String genre;
        int checkedId = genderRadioGroup.getCheckedRadioButtonId();
        if (checkedId == R.id.radio_male) {
            genre = "Mâle";
        } else if (checkedId == R.id.radio_female) {
            genre = "Femelle";
        } else {
            genre = "";
        }
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
                                            Inventaire inventaire = new Inventaire(10,10,10,10,10,10);
                                            Statistique stats = new Statistique(100, 100, 100, 100, 100,100,100);
                                            Tamagotchi tamagotchi = new Tamagotchi(null, user.getUid(), nomTamagotchi, genre, Timestamp.now(), Timestamp.now(), stats, inventaire);
                                            FirebaseFirestore db = FirebaseFirestore.getInstance();
                                            db.collection("tamagotchis")
                                                    .add(tamagotchi)
                                                    .addOnSuccessListener(documentReference -> {
                                                        String newTamagotchiId = documentReference.getId();
                                                        Log.d("Firestore", "Tamagotchi sauvegardé avec ID : " + newTamagotchiId);
                                                        Joueur joueur = new Joueur(email, newTamagotchiId);
                                                        db.collection("joueurs")
                                                                .document(user.getUid())
                                                                .set(joueur)
                                                                .addOnSuccessListener(doc -> {
                                                                    Log.d("Firestore", "Joueur sauvegardé avec email : " + email);
                                                                    Intent intent = new Intent(this, MainActivity.class );
                                                                    startActivity(intent);
                                                                })
                                                                .addOnFailureListener(e ->
                                                                        Log.e("Firestore", "Erreur de sauvegarde", e));
                                                    })
                                                    .addOnFailureListener(e ->
                                                            Log.e("Firestore", "Erreur de sauvegarde", e));
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
        Intent intent = new Intent(this,AuthActivity.class);
        startActivity(intent);
    }
}
