package com.example.tamagotchi;
/*
-----------------------------
    Date : 20/04/2025

    Membres qui travaillent dessus : Marwan DENAGNON

    Que fait le code ? : Rien en cours de développement
-----------------------------
*/
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tamagotchi.model.Tamagotchi;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class ListeMimichiActivity extends AppCompatActivity {

    private FirebaseFirestore db;
    private TamagotchiAdapter adapter;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_liste_tamagotchis);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        db = FirebaseFirestore.getInstance();

        // Vérifie si un utilisateur est connecté et récupère les Tamagotchis associés
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            fetchTamagotchis(currentUser.getUid()); // Passer l'ID de l'utilisateur connecté
        } else {
            // Si l'utilisateur n'est pas connecté, affiche un message
            Toast.makeText(this, "Veuillez vous connecter", Toast.LENGTH_SHORT).show();
        }
    }

    private void fetchTamagotchis(String userId) {
        // Requête Firestore pour récupérer uniquement les Tamagotchis avec le userId correspondant
        Query query = db.collection("tamagotchis").whereEqualTo("userId", userId);

        // Configuration de l'adaptateur FirestoreRecyclerAdapter
        FirestoreRecyclerOptions<Tamagotchi> options = new FirestoreRecyclerOptions.Builder<Tamagotchi>()
                .setQuery(query, Tamagotchi.class)
                .build();

        // Instancier l'adaptateur avec les options configurées
        adapter = new TamagotchiAdapter(options);
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            adapter.startListening(); // Démarre l'écouteur Firestore
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening(); // Arrête l'écouteur Firestore
    }

    private void showOptionsDialog(String tamagotchiId) {
        // Implémente la logique pour modifier ou supprimer
    }
}
