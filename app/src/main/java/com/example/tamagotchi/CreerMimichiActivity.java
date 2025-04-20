package com.example.tamagotchi;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class CreerMimichiActivity extends AppCompatActivity {

    private ImageView imageViewNouveauMimichi;
    private EditText nomTamagotchiInput;
    private RadioGroup radioGroupSex;
    private RadioButton radioMale;
    private RadioButton radioFemale;
    private Button ajouterButton;
    private TextView retourButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nouveau_mimichi); // Mets ici le bon nom de ton layout XML

        // Liaison des vues avec leur ID
        imageViewNouveauMimichi = findViewById(R.id.imageViewNouveauMimichi);
        nomTamagotchiInput = findViewById(R.id.nomTamagotchiInput);
        radioGroupSex = findViewById(R.id.radio_group_sex);
        radioMale = findViewById(R.id.radio_male);
        radioFemale = findViewById(R.id.radio_female);
        ajouterButton = findViewById(R.id.ajouterButton);
        retourButton = findViewById(R.id.retourButton);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        // Action bouton "Ajouter"
        ajouterButton.setOnClickListener(v -> {
            String nom = nomTamagotchiInput.getText().toString().trim();
            String genre = "";

            int checkedId = radioGroupSex.getCheckedRadioButtonId();
            if (checkedId == R.id.radio_male) {
                genre = "Mâle";
            } else if (checkedId == R.id.radio_female) {
                genre = "Femelle";
            }

            // Vérfification de la présence de données
            if (nom.isEmpty() || genre.isEmpty()) {
                Toast.makeText(this, "Veuillez renseigner un nom et un genre", Toast.LENGTH_SHORT).show();
                return;
            }

            // Tu peux ici insérer dans Firestore ou aller vers une autre activité
            Inventaire inventaire = new Inventaire(10,10,10,10,10);
            Statistique stats = new Statistique(100, 100, 100, 100, 100,100);
            Tamagotchi tamagotchi = new Tamagotchi(user.getUid(), nom, genre, Timestamp.now(), Timestamp.now(), stats, inventaire);
            FirestoreData.sauvegarderTamagotchi(tamagotchi);
        });

        retourButton.setOnClickListener(v -> {
            Intent intent = new Intent(this,MainActivity.class); // Retour à l'écran principal
            startActivity(intent);
        });
    }
}

