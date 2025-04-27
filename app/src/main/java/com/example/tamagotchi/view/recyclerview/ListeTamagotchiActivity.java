package com.example.tamagotchi.view.recyclerview;
/*
-----------------------------
    Date : 20/04/2025

    Membres qui travaillent dessus : Marwan DENAGNON

    Que fait le code ? : Permet d'afficher la page de liste des tamagotchis et de gérer les actions que l'on peut faire sur ces derniers grâce à son Adapter,ViewHolder et son ViewModel
    Grâce au modèle MVVM et aux LiveData que le ViewModel renvoie je peut mettre un Observer et avoir les donées modifiée constament.
-----------------------------
*/
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tamagotchi.R;
import com.example.tamagotchi.viewmodel.ListeTamagotchiViewModel;

public class ListeTamagotchiActivity extends AppCompatActivity {
    private TamagotchiAdapter adapter;
    private ListeTamagotchiViewModel listeTamagotchiViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_liste_tamagotchis);

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        listeTamagotchiViewModel = new ViewModelProvider(this).get(ListeTamagotchiViewModel.class);
        adapter = new TamagotchiAdapter(listeTamagotchiViewModel);
        recyclerView.setAdapter(adapter);

        listeTamagotchiViewModel.getMessageLiveData().observe(this, message -> {
            if (message != null && !message.isEmpty()) {
                // Affiche un Toast avec le message
                Toast.makeText(ListeTamagotchiActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        });

        listeTamagotchiViewModel.getUserTamagotchis().observe(this, tamagotchis -> {
            adapter.setTamagotchis(tamagotchis);
        });

        listeTamagotchiViewModel.getActiveTamagotchi().observe(this, activeTamagotchi -> {
            if (activeTamagotchi != null) {
                adapter.setActiveTamagotchiId(activeTamagotchi.getIdTamagotchi());
            } else {
                adapter.setActiveTamagotchiId(null);
            }
        });

    }
}
