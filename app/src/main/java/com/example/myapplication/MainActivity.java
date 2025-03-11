package com.example.myapplication;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.view.View;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.google.gson.Gson;
import java.io.InputStream;
import java.io.IOException;
import android.util.Log;
import android.os.Bundle;
import java.io.FileInputStream;
import java.io.FileOutputStream;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
    public void nourrir(View v){
        System.out.println("ici");
        Stats stats = new Stats(100,1, 80, 20, 50, 30, 90);
        Tamagotchi tamagotchi = new Tamagotchi(1,"Marwan",20,stats);
        Gson gson = new Gson();
        String json = gson.toJson(tamagotchi);
        Log.d("string tamagotchi",json);
        writeToFile(json);
    }

    public void lecture(View v){
        String json = loadJSONFromFile();
        if(json!=null){
            Log.d("lecture",json);
        }
    }

    private void writeToFile(String json) {
        try {
            // Ouvrir un flux de sortie pour écrire dans un fichier interne
            FileOutputStream fos = openFileOutput("data.json", MODE_PRIVATE);
            fos.write(json.getBytes());
            fos.close();
            Log.d("MainActivity", "Fichier JSON écrit avec succès !");
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("MainActivity", "Erreur lors de l'écriture du fichier JSON");
        }
    }
    // Méthode pour charger le fichier JSON depuis les assets
    private String loadJSONFromFile() {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            // Ouvrir un flux de lecture pour le fichier interne
            FileInputStream fis = openFileInput("data.json");
            int character;
            while ((character = fis.read()) != -1) {
                stringBuilder.append((char) character);
            }
            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return stringBuilder.toString();
    }
}