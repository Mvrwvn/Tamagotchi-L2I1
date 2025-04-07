package com.example.frontend;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnLogin = findViewById(R.id.main);
        TextView tvInscription = findViewById(R.id.tv_signup);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) TextView tvMdpOublie = findViewById(R.id.mdp_oublie);

        btnLogin.setOnClickListener(v -> {

            Intent intent = new Intent(MainActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });

        tvInscription.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, Inscription.class);
            startActivity(intent);
        });

        tvMdpOublie.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, Mdp_oublie.class);
            startActivity(intent);
        });
    }
}
