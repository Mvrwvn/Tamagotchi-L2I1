package com.example.tamagotchi.model;

/*
-----------------------------
    Date : 27/04/2025

    Membres qui travaillent dessus : Marwan DENAGNON

    Que fait le code ? : Fait la passerelle entre les données reçues en java et envoyé à Firebase pour l'authentification et pour le stockages des données sur FireStore
    avec un id de tamagotchi actif pour savoir sur quelle tamagotchis l'utilisateur va modifier les attributs dans le cas où il aurait plusieurs tamagotchis.
    Changement par rapport à la version précédente : Fusion du front end et du back end
    Changement par rapport à la version précédente : suppression de l'attribut idTamagotchis qui était une liste sensé servir à savoir tous les tamagotchis que l'utilisateur avait créer
    cette attribut est obsolète car grâce à la méthode whereEqualsTo de firestore nous pouvons directement obtenir tous les documents liés à un utilisateur précis
    -----------------------------
*/

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.concurrent.ExecutionException;

public class Joueur {

    private String email;
    private String activeTamagotchiId;

    // Constructeur vide requis par Firestore
    public Joueur() {
    }

    public Joueur(String email, String activeTamagotchiId) {
        this.email = email;
        this.activeTamagotchiId = activeTamagotchiId;
    }

    // Getters et Setters

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getActiveTamagotchiId() {
        return activeTamagotchiId;
    }

    public void setActiveTamagotchiId(String activeTamagotchiId) {
        this.activeTamagotchiId = activeTamagotchiId;
    }

    public static boolean connexion(Context context, String email, String password) {
        Task<AuthResult> loginTask = FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                .addOnSuccessListener(authResult -> {
                    Toast.makeText(context, "Connexion réussie", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "Connexion OK");
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(context, "Erreur connexion : " + e.getMessage(), Toast.LENGTH_LONG).show();
                    Log.e(TAG, "Erreur connexion : ", e);
                });
        try {
            Tasks.await(loginTask);
            return true;
        } catch (ExecutionException e) {
            return false;
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean renitialiserMotDePasse(Context context, String email) {
        Task<Void> passwordResetEmailTask = FirebaseAuth.getInstance().sendPasswordResetEmail(email)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(context, "E-mail de réinitialisation envoyé", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "Email reset envoyé à : " + email);
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(context, "Erreur : " + e.getMessage(), Toast.LENGTH_LONG).show();
                    Log.e(TAG, "Erreur reset password : ", e);
                });
        try {
            Tasks.await(passwordResetEmailTask);
            return true;
        } catch (ExecutionException e) {
            return false;
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}

