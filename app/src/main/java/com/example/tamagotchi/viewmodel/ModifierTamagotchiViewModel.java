package com.example.tamagotchi.viewmodel;
/*
-----------------------------
    Date : 27/04/2025

    Membres qui travaillent dessus : Marwan DENAGNON

    Que fait le code ? : Que fait le code ? : ViewModel sont but est de gérer les données entre les Model et la View ici ModifierTamagotchiActivity
    Va gérer toutes les données entre Firestore et l'application pour le ModifierTamagotchiActivity
-----------------------------
*/

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.google.firebase.firestore.FirebaseFirestore;

public class ModifierTamagotchiViewModel extends ViewModel {
    private MutableLiveData<String> messageLiveData = new MutableLiveData<>();

    public LiveData<String> getMessageLiveData() {
        return messageLiveData;
    }

    public void updateTamagotchi(String idTamagotchi, String nomTamagotchi, String genre){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("tamagotchis")
                .document(idTamagotchi)
                .update("nomTamagotchi", nomTamagotchi, "genre", genre)
                .addOnSuccessListener(snap->{
                    String successMessage = "Mimichi modifié avec succès : " + nomTamagotchi + " (" + genre + ")";
                    messageLiveData.postValue(successMessage);
                })
                .addOnFailureListener(snap->{
                    String errorMessage ="Erreur lors de la modification du Mimichi.";
                    messageLiveData.postValue(errorMessage);
                });
    }
}
