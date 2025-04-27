package com.example.tamagotchi.view.recyclerview;
/*
-----------------------------
    Date : 27/04/2025

    Membres qui travaillent dessus : Marwan DENAGNON

    Que fait le code ? : Adapter du RecyclerView permet de gérer chaque item de ce RecyclerView à savoir bouton modifier/supprimer
    permettre d'afficher un bookmark pour le tamagotchi actif est de changer le tamagotchi actif (rappel le tamagotchi actif est le tamagotchi
    sur lequel l'utilisateur va pouvoir jouer
    Ce RecyclerView dépend d'un ViewHolder qui est directement créer en tant que class static, ce ViewHolder permet entre autre de récupérer
    tous les itemViews dont l'adapter aura besoin depuis le fichier item_tamagotchi.xml
-----------------------------
*/
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tamagotchi.R;
import com.example.tamagotchi.model.Tamagotchi;
import com.example.tamagotchi.view.ModifierTamagotchiActivity;
import com.example.tamagotchi.viewmodel.ListeTamagotchiViewModel;

import java.util.ArrayList;
import java.util.List;

public class TamagotchiAdapter extends RecyclerView.Adapter<TamagotchiAdapter.TamagotchiViewHolder> {

    private List<Tamagotchi> tamagotchiList = new ArrayList<>();
    private String idActiveTamagotchi;
    private ListeTamagotchiViewModel listeTamagotchiViewModel;

    public TamagotchiAdapter(ListeTamagotchiViewModel listeTamagotchiViewModel) {
        this.listeTamagotchiViewModel = listeTamagotchiViewModel;
    }

    public void setTamagotchis(List<Tamagotchi> tamagotchis) {
        this.tamagotchiList = tamagotchis;
        notifyDataSetChanged();
    }

    public void setActiveTamagotchiId(String idActiveTamagotchi) {
        this.idActiveTamagotchi = idActiveTamagotchi;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public TamagotchiViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tamagotchi, parent, false);
        return new TamagotchiViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TamagotchiViewHolder holder, int position) {
        Tamagotchi tamagotchi = tamagotchiList.get(position);
        holder.nomTamagotchiTextView.setText(tamagotchi.getNomTamagotchi());
        holder.vieTamagotchiTextView.setText("Point de vie : " + (int)tamagotchi.getStatsTamagotchi().getVie());
        holder.optionsMenuButton.setOnClickListener(v -> {
            PopupMenu popup = new PopupMenu(v.getContext(), holder.optionsMenuButton);
            popup.getMenuInflater().inflate(R.menu.menu_tamagotchi_options, popup.getMenu());
            popup.setOnMenuItemClickListener(item -> {
                if (item.getItemId() == R.id.action_edit) {
                    Intent intent = new Intent(v.getContext(), ModifierTamagotchiActivity.class);
                    intent.putExtra("KEY_STRING", tamagotchi.getIdTamagotchi());
                    v.getContext().startActivity(intent);
                    return true;
                } else if (item.getItemId() == R.id.action_delete) {
                    listeTamagotchiViewModel.deleteTamagotchi(tamagotchi.getIdTamagotchi());
                    return true;
                }
                return false;
            });
            popup.show();
            Log.d("Adapter",idActiveTamagotchi);
        });

        holder.setActifButton.setOnClickListener(v -> {
            listeTamagotchiViewModel.setActiveTamagotchiId(tamagotchi.getIdTamagotchi());
        });

        // Affichage de l'icône bookmark si c'est le tamagotchi actif
        if (tamagotchi.getIdTamagotchi() != null && tamagotchi.getIdTamagotchi().equals(idActiveTamagotchi)) {
            holder.bookmarkImageView.setVisibility(View.VISIBLE);
        } else {
            holder.bookmarkImageView.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return tamagotchiList.size();
    }

    static class TamagotchiViewHolder extends RecyclerView.ViewHolder {
        TextView nomTamagotchiTextView;
        TextView vieTamagotchiTextView;
        ImageView optionsMenuButton;
        ImageView bookmarkImageView;
        Button setActifButton;

        TamagotchiViewHolder(@NonNull View itemView) {
            super(itemView);
            vieTamagotchiTextView = itemView.findViewById(R.id.descriptionTextView);
            nomTamagotchiTextView = itemView.findViewById(R.id.titleTextView);
            optionsMenuButton = itemView.findViewById(R.id.optionsMenuButton);
            bookmarkImageView = itemView.findViewById(R.id.bookmarkImageView);
            setActifButton = itemView.findViewById(R.id.setActifButton);
        }
    }
}

