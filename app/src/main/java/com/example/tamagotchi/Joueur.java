package com.example.tamagotchi;

/*
-----------------------------
    Date : 20/04/2025

    Membres qui travaillent dessus : Marwan DENAGNON

    Que fait le code ? : Fait la passerelle entre les données reçues en java et envoyé à Firebase pour l'authentification et pour le stockages des données sur FireStore
    avec un id de tamagotchi actif pour savoir sur quelle tamagotchis l'utilisateur va modifier les attributs dans le cas où il aurait plusieurs tamagotchis.
    Changement par rapport à la version précédente : Fusion du front end et du back end
    -----------------------------
*/

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class Joueur {

    private String email;
    private ArrayList<String> idTamagotchis;
    private String activeTamagotchiId;

    // Constructeur vide requis par Firestore
    public Joueur() {
    }

    public Joueur(String email,
                  ArrayList<String> idTamagotchis, String activeTamagotchiId) {
        this.email = email;
        this.idTamagotchis = idTamagotchis;
        this.activeTamagotchiId = activeTamagotchiId;
    }

    // Getters et Setters

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public ArrayList<String> getIdTamagotchis() {
        return idTamagotchis;
    }

    public void setIdTamagotchis(ArrayList<String> idTamagotchis) {
        this.idTamagotchis = idTamagotchis;
    }

    public String getActiveTamagotchiId() {
        return activeTamagotchiId;
    }

    public void setActiveTamagotchiId(String activeTamagotchiId) {
        this.activeTamagotchiId = activeTamagotchiId;
    }

    public static void connexion(Context context, String email, String password) {
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                .addOnSuccessListener(authResult -> {
                    Toast.makeText(context, "Connexion réussie", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "Connexion OK");
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(context, "Erreur connexion : " + e.getMessage(), Toast.LENGTH_LONG).show();
                    Log.e(TAG, "Erreur connexion : ", e);
                });
    }

    public static void renitialiserMotDePasse(Context context, String email) {
        FirebaseAuth.getInstance().sendPasswordResetEmail(email)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(context, "E-mail de réinitialisation envoyé", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "Email reset envoyé à : " + email);
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(context, "Erreur : " + e.getMessage(), Toast.LENGTH_LONG).show();
                    Log.e(TAG, "Erreur reset password : ", e);
                });
    }
}

