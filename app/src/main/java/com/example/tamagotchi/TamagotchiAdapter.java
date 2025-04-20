package com.example.tamagotchi;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;

public class TamagotchiAdapter extends FirestoreRecyclerAdapter<Tamagotchi, TamagotchiViewHolder> {

    public TamagotchiAdapter(@NonNull FirestoreRecyclerOptions<Tamagotchi> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull TamagotchiViewHolder holder, int position, @NonNull Tamagotchi model) {
        holder.nameTextView.setText(model.getNomTamagotchi());

        // Récupérer l'ID du Tamagotchi
        String tamagotchiId = getSnapshots().getSnapshot(position).getId();

        // Bouton "Modifier"
        holder.editButton.setOnClickListener(v -> {
            // Ouvre l'activité pour modifier le Tamagotchi
            Intent intent = new Intent(holder.itemView.getContext(), ModifierMimichiActivity.class);
            intent.putExtra("TAMAGOTCHI_ID", tamagotchiId);
            intent.putExtra("TAMAGOTCHI_NAME", model.getNomTamagotchi());
            // Ajoute d'autres données que tu souhaites passer
            holder.itemView.getContext().startActivity(intent);
        });

        // Bouton "Supprimer"
        holder.deleteButton.setOnClickListener(v -> {
            // Supprimer le Tamagotchi de Firestore
            FirebaseFirestore.getInstance().collection("tamagotchis")
                    .document(tamagotchiId)
                    .delete()
                    .addOnSuccessListener(aVoid -> {
                        // Supprimer l'élément de la liste
                        Toast.makeText(holder.itemView.getContext(), "Tamagotchi supprimé", Toast.LENGTH_SHORT).show();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(holder.itemView.getContext(), "Erreur lors de la suppression", Toast.LENGTH_SHORT).show();
                    });
        });
    }

    @NonNull
    @Override
    public TamagotchiViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_tamagotchi, parent, false);
        return new TamagotchiViewHolder(view);
    }
}
