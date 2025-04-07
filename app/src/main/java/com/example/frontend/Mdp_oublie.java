package com.example.frontend;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class Mdp_oublie extends AppCompatActivity {

    private EditText emailEditText;
    private Button resetPasswordButton;
    private TextView backToLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mdp_oublie);

        emailEditText = findViewById(R.id.mimi_email);
        resetPasswordButton = findViewById(R.id.send_reset_link);
        backToLogin = findViewById(R.id.tv_back_to_login);

        resetPasswordButton.setOnClickListener(v -> {
            String email = emailEditText.getText().toString().trim();

            if (!email.isEmpty()) {
                Toast.makeText(Mdp_oublie.this,
                        "Un lien de réinitialisation a été envoyé à " + email,
                        Toast.LENGTH_LONG).show();
                startActivity(new Intent(Mdp_oublie.this, MainActivity.class));
                finish();
            } else {
                Toast.makeText(Mdp_oublie.this,
                        "Veuillez entrer une adresse e-mail",
                        Toast.LENGTH_SHORT).show();
            }
        });

        backToLogin.setOnClickListener(v -> {
            startActivity(new Intent(Mdp_oublie.this, MainActivity.class));
            finish();
        });
    }
}
