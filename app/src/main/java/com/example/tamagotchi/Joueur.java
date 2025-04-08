package com.example.tamagotchi;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class Joueur {

    private String idJoueur;
    private String email;
    private String password;
    private ArrayList<String> idTamagotchis;
    private String activeTamagotchiId;

    // Constructeur vide requis par Firestore
    public Joueur() {
    }

    public Joueur(String idJoueur, String email, String password,
                  ArrayList<String> idTamagotchis, String activeTamagotchiId) {
        this.idJoueur = idJoueur;
        this.email = email;
        this.password = password;
        this.idTamagotchis = idTamagotchis;
        this.activeTamagotchiId = activeTamagotchiId;
    }

    // Getters et Setters
    public String getIdJoueur() {
        return idJoueur;
    }

    public void setIdJoueur(String idJoueur) {
        this.idJoueur = idJoueur;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public static void inscription(Context context, String email, String password) {
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener(authResult -> {
                    FirebaseUser firebaseUser = authResult.getUser();
                    if (firebaseUser != null) {
                        String uid = firebaseUser.getUid();

                        Joueur joueur = new Joueur(uid, email, password, new ArrayList<>(), null);

                        FirebaseFirestore.getInstance()
                                .collection("joueurs")
                                .document(uid)
                                .set(joueur)
                                .addOnSuccessListener(aVoid -> {
                                    Toast.makeText(context, "Inscription réussie", Toast.LENGTH_SHORT).show();
                                    Log.d(TAG, "Utilisateur enregistré dans Firestore");
                                })
                                .addOnFailureListener(e -> {
                                    Toast.makeText(context, "Erreur Firestore", Toast.LENGTH_SHORT).show();
                                    Log.e(TAG, "Erreur Firestore : ", e);
                                });
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(context, "Erreur lors de l'inscription : " + e.getMessage(), Toast.LENGTH_LONG).show();
                    Log.e(TAG, "Erreur inscription : ", e);
                });
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

