package com.example.tamagotchi;
/*
-----------------------------
    Date : 20/04/2025

    Membres qui travaillent dessus : Marwan DENAGNON

    Que fait le code ? : Page pour modifier le mimichi Modifie uniquement le tamagotchi actif (celui qui est dans l'attribut activeTamagotchiId dans la bdd collection joueurs)
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
import com.google.firebase.firestore.FirebaseFirestore;

public class ModifierMimichiActivity extends AppCompatActivity {

    private ImageView imageViewNouveauMimichi;
    private EditText nomTamagotchiInput;
    private RadioGroup radioGroupSex;
    private RadioButton radioMale;
    private RadioButton radioFemale;
    private Button modifierButton;
    private TextView retourButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modifier_mimichi);

        imageViewNouveauMimichi = findViewById(R.id.imageViewNouveauMimichi);
        nomTamagotchiInput = findViewById(R.id.nomTamagotchiInput);
        radioGroupSex = findViewById(R.id.radio_group_sex);
        radioMale = findViewById(R.id.radio_male);
        radioFemale = findViewById(R.id.radio_female);
        modifierButton = findViewById(R.id.modifierButton);
        retourButton = findViewById(R.id.retourButton);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        modifierButton.setOnClickListener(v -> {
            String nom = nomTamagotchiInput.getText().toString().trim();
            String genre = "";
            if (user == null){
                Toast.makeText(this, "Problème dans le chargement de l'utilisateur connecté firebase", Toast.LENGTH_SHORT).show();
                return;
            }
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

            FirestoreData.modifierTamagotchiActif(user,v,nom,genre);
        });

        retourButton.setOnClickListener(v -> {
            Intent intent = new Intent(this,MainActivity.class); // Retour à l'écran principal
            startActivity(intent);
        });
    }
}

