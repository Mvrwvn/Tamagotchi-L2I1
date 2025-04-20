package com.example.tamagotchi;
/*
-----------------------------
    Date : 20/04/2025

    Membres qui travaillent dessus : Marwan DENAGNON

    Que fait le code ? : Page d'ajout de tamagotchi permet d'en ajouter un à la bdd et il va automatiquement le mettre en actif après l'avoir ajouté
-----------------------------
*/
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
        setContentView(R.layout.activity_nouveau_mimichi);

        imageViewNouveauMimichi = findViewById(R.id.imageViewNouveauMimichi);
        nomTamagotchiInput = findViewById(R.id.nomTamagotchiInput);
        radioGroupSex = findViewById(R.id.radio_group_sex);
        radioMale = findViewById(R.id.radio_male);
        radioFemale = findViewById(R.id.radio_female);
        ajouterButton = findViewById(R.id.ajouterButton);
        retourButton = findViewById(R.id.retourButton);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        ajouterButton.setOnClickListener(v -> {
            String nom = nomTamagotchiInput.getText().toString().trim();
            String genre = "";

            int checkedId = radioGroupSex.getCheckedRadioButtonId();
            if (user == null){
                Toast.makeText(this, "Problème dans le chargement de l'utilisateur connecté firebase", Toast.LENGTH_SHORT).show();
                return;
            }
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

            Inventaire inventaire = new Inventaire(10,10,10,10,10,10);
            Statistique stats = new Statistique(100, 100, 100, 100, 100,100,100);
            Tamagotchi tamagotchi = new Tamagotchi(user.getUid(), nom, genre, Timestamp.now(), Timestamp.now(), stats, inventaire);
            FirestoreData.ajouterTamagotchi(user,v,tamagotchi);
        });

        retourButton.setOnClickListener(v -> {
            Intent intent = new Intent(this,MainActivity.class); // Retour à l'écran principal
            startActivity(intent);
        });
    }
}

