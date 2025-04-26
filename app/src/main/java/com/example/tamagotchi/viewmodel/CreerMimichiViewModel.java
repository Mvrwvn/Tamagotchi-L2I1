package com.example.tamagotchi.viewmodel;

import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.tamagotchi.model.Tamagotchi;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class CreerMimichiViewModel extends ViewModel {

    private MutableLiveData<String> messageLiveData = new MutableLiveData<>();

    public LiveData<String> getMessageLiveData() {
        return messageLiveData;
    }

    public static void setActiveTamagotchiId(String activeTamagotchiId) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) return;
        String userId = user.getUid();

        db.collection("joueurs")
                .document(userId)
                .update("activeTamagotchiId", activeTamagotchiId, "idTamagotchis", FieldValue.arrayUnion(activeTamagotchiId))
                .addOnSuccessListener(documentReference ->
                        Log.d("Firestore", "Tamagotchi actif maj"))
                .addOnFailureListener(e ->
                        Log.e("Firestore", "Erreur de sauvegarde tamagotchi actif", e));
    }

    public void ajouterTamagotchi(Tamagotchi tamagotchi) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("tamagotchis")
                .add(tamagotchi)
                .addOnSuccessListener(documentReference -> {
                    String newTamagotchiId = documentReference.getId();

                    // Mettre à jour l'attribut idTamagotchi de l'objet local
                    tamagotchi.setIdTamagotchi(newTamagotchiId);

                    // mettre aussi à jour dans Firestore
                    db.collection("tamagotchis").document(newTamagotchiId)
                            .update("idTamagotchi", newTamagotchiId)
                            .addOnSuccessListener(aVoid -> {
                                setActiveTamagotchiId(newTamagotchiId);
                                String successMessage = "Mimichi créé avec succès : "
                                        + tamagotchi.getNomTamagotchi() + " (" + tamagotchi.getGenre() + ")";
                                Log.d("CreerMimichiViewModel", successMessage);
                                messageLiveData.postValue(successMessage);
                            })
                            .addOnFailureListener(e -> {
                                String errorMessage = "Erreur lors de l'ajout de l'idTamagotchi : " + e.getMessage();
                                Log.e("Firestore", errorMessage, e);
                                messageLiveData.postValue(errorMessage);
                            });

                })
                .addOnFailureListener(e -> {
                    String errorMessage = "Erreur de sauvegarde : " + e.getMessage();
                    Log.e("Firestore", errorMessage, e);
                    messageLiveData.postValue(errorMessage);
                });
    }
}
