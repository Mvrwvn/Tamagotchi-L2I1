package com.example.tamagotchi;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class TamagotchiViewHolder extends RecyclerView.ViewHolder {
    public TextView nameTextView;
    public Button editButton;
    public Button deleteButton;

    public TamagotchiViewHolder(View itemView) {
        super(itemView);
        nameTextView = itemView.findViewById(R.id.tamagotchiName);
        editButton = itemView.findViewById(R.id.editButton);
        deleteButton = itemView.findViewById(R.id.deleteButton);
    }
}
