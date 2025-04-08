package com.example.tamagotchi;
import android.util.Log;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.Timestamp;
import java.util.HashMap;
import java.util.Map;

public class TamagotchiManager {

    private FirebaseFirestore firestore = FirebaseFirestore.getInstance();

    // Méthode pour récupérer l'ID du Tamagotchi actif de l'utilisateur
    public String getActiveTamagotchiId(FirebaseUser currentUser) {
        final String[] activeTamagotchiId = new String[1];

        firestore.collection("users")
                .document(currentUser.getUid())
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        activeTamagotchiId[0] = documentSnapshot.getString("activeTamagotchiId");
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e("Tamagotchi", "Erreur lors de la récupération du Tamagotchi actif", e);
                });

        return activeTamagotchiId[0];
    }

    // Méthode pour récupérer les statistiques d'un Tamagotchi sous forme de HashMap
    public void getTamagotchiStats(String tamagotchiId, final OnStatsLoadedListener listener) {
        firestore.collection("tamagotchis")
                .document(tamagotchiId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        Map<String, Object> statsMap = (Map<String, Object>) documentSnapshot.get("stats");
                        listener.onStatsLoaded(statsMap); // Retourner la Map des statistiques
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e("Tamagotchi", "Erreur lors de la récupération des statistiques", e);
                    listener.onStatsLoaded(null);
                });
    }

    // Interface pour écouter les résultats des stats
    public interface OnStatsLoadedListener {
        void onStatsLoaded(Map<String, Object> statsMap);
    }

    // Méthode pour mettre à jour les statistiques d'un Tamagotchi avec des changements dynamiques
    public void updateTamagotchiStats(String tamagotchiId, Map<String, Double> deltaStats) {
        firestore.collection("tamagotchis")
                .document(tamagotchiId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        // Récupérer les valeurs existantes des statistiques
                        Map<String, Object> currentStats = (Map<String, Object>) documentSnapshot.get("stats");

                        // Créer une nouvelle Map pour les nouvelles statistiques
                        Map<String, Object> updatedStats = new HashMap<>();

                        // Appliquer les ajouts ou soustractions des nouvelles valeurs
                        for (Map.Entry<String, Object> entry : currentStats.entrySet()) {
                            String key = entry.getKey();
                            Double currentValue = (Double) entry.getValue();
                            Double deltaValue = deltaStats.get(key); // Récupérer la valeur du delta (ajout/soustraction)

                            if (deltaValue != null) {
                                updatedStats.put(key, currentValue + deltaValue); // Appliquer le changement
                            } else {
                                updatedStats.put(key, currentValue); // Si pas de changement, garder la valeur existante
                            }
                        }

                        // Mettre à jour les statistiques dans Firestore
                        firestore.collection("tamagotchis")
                                .document(tamagotchiId)
                                .update(
                                        "stats", updatedStats,
                                        "stats.dernierUpdate", Timestamp.now()
                                )
                                .addOnSuccessListener(aVoid -> {
                                    Log.d("Tamagotchi", "Statistiques du Tamagotchi mises à jour.");
                                })
                                .addOnFailureListener(e -> {
                                    Log.e("Tamagotchi", "Erreur lors de la mise à jour des statistiques.", e);
                                });
                    }
                })
                .addOnFailureListener(e -> Log.e("Tamagotchi", "Erreur lors de la récupération des statistiques", e));
    }

    // Exemple d'utilisation dans une activité
    public void modifyActiveTamagotchiStats(FirebaseUser currentUser, Map<String, Double> deltaStats) {
        String activeTamagotchiId = getActiveTamagotchiId(currentUser);

        if (activeTamagotchiId != null) {
            // Mettre à jour les statistiques du Tamagotchi actif
            updateTamagotchiStats(activeTamagotchiId, deltaStats);
        }
    }
}
