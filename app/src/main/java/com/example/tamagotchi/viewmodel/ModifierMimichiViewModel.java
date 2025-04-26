package com.example.tamagotchi.viewmodel;

import android.view.View;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class ModifierMimichiViewModel extends ViewModel {
    private MutableLiveData<String> messageLiveData = new MutableLiveData<>();

    public LiveData<String> getMessageLiveData() {
        return messageLiveData;
    }

    public void modifierTamagotchiActif(String nomTamagotchi, String genre){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) return;
        String userId = user.getUid();
        db.collection("joueurs")
                .document(userId)
                .get()
                .addOnSuccessListener(snapshot -> {
                    String activeTamagotchiId = snapshot.getString("activeTamagotchiId");
                    db.collection("tamagotchis")
                            .document(activeTamagotchiId)
                            .update("nomTamagotchi", nomTamagotchi, "genre", genre)
                            .addOnSuccessListener(snap->{
                                String successMessage = "Mimichi modifié avec succès : " + nomTamagotchi + " (" + genre + ")";
                                messageLiveData.postValue(successMessage);
                            })
                            .addOnFailureListener(snap->{
                                String errorMessage ="Erreur lors de la création du Mimichi.";
                            });
                });

    }
}
