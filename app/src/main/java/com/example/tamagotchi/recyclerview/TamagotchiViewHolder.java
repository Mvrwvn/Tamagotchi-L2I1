package com.example.tamagotchi.recyclerview;
/*
-----------------------------
    Date : 20/04/2025

    Membres qui travaillent dessus : Marwan DENAGNON

    Que fait le code ? : Rien en cours de développement
-----------------------------
*/
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.tamagotchi.R;

public class TamagotchiViewHolder extends RecyclerView.ViewHolder {
    public TextView nameTextView;
    public Button editButton;

    public TamagotchiViewHolder(View itemView) {
        super(itemView);
        nameTextView = itemView.findViewById(R.id.titleTextView);
        editButton = itemView.findViewById(R.id.editButton);
    }
}
