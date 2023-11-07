package com.example.user;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.snackbar.Snackbar;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Login extends AppCompatActivity {

    private EditText emailEditText;
    private EditText passwordEditText;
    private ImageView loginButton;
    private TextView registerTextView;

    private SharedPreferences sharedPreferences;
    private UserDao userDao;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        emailEditText = findViewById(R.id.email);
        passwordEditText = findViewById(R.id.mdp);
        loginButton = findViewById(R.id.imageView3);
        registerTextView = findViewById(R.id.textView5);

        sharedPreferences = getSharedPreferences("auth", Context.MODE_PRIVATE);
        userDao = AppDatabase.getInstance(this).userDao();

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = emailEditText.getText().toString();
                String password = passwordEditText.getText().toString();

                if (isValidCredentials(email, password)) {
                    showSnackbar("Authentification réussie");
                } else {
                    showErrorToast();
                }
            }
        });

        registerTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Login.this, Signup1.class));
            }
        });
    }

    private boolean isValidCredentials(String email, String password) {
        String storedEmail = sharedPreferences.getString("email", "");
        String storedHashedPassword = sharedPreferences.getString("hashed_password", "");

        User user = userDao.getUserByEmail(email);
        if (user != null) {
            String hashedPasswordFromDatabase = user.getPassword();
            String hashedPassword = hashPassword(password);
            return storedEmail.equals(email) && storedHashedPassword.equals(hashedPassword) && hashedPasswordFromDatabase.equals(hashedPassword);
        }
        return false;
    }

    private String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashedBytes = md.digest(password.getBytes());
            StringBuilder sb = new StringBuilder();

            for (byte b : hashedBytes) {
                sb.append(String.format("%02x", b));
            }

            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return null;
    }

    private void showSnackbar(String message) {
        Snackbar.make(loginButton, message, Snackbar.LENGTH_SHORT).show();
    }

    private void showErrorToast() {
        Toast.makeText(Login.this, "Identifiants incorrects. Veuillez réessayer.", Toast.LENGTH_SHORT).show();
    }
}
