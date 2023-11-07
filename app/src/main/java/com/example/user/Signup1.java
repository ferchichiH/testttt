package com.example.user;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import org.mindrot.jbcrypt.BCrypt;

public class Signup1 extends AppCompatActivity {
    private EditText nomEditText;
    private EditText emailEditText;
    private EditText mdpEditText;
    private ImageView signUpButton;

    private AppDatabase appDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        nomEditText = findViewById(R.id.nom);
        emailEditText = findViewById(R.id.email);
        mdpEditText = findViewById(R.id.mdp);
        signUpButton = findViewById(R.id.imageView3);

        appDatabase = Room.databaseBuilder(this, AppDatabase.class, "myapp-database").build();

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nom = nomEditText.getText().toString();
                String email = emailEditText.getText().toString();
                String motDePasse = mdpEditText.getText().toString();



                // Utilisez la bibliothèque BCrypt pour hacher le mot de passe
                String motDePasseHache = hashPassword(motDePasse);


                Intent intent = new Intent(Signup1.this, Signup2.class);
                intent.putExtra("nom", nom);
                intent.putExtra("email", email);
                intent.putExtra("motDePasse", motDePasseHache);
                startActivity(intent);

            }
        });
    }

    private String hashPassword(String password) {
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
        return hashedPassword;
    }

    private class InsertUserTask extends AsyncTask<User, Void, Long> {
        @Override
        protected Long doInBackground(User... users) {
            return appDatabase.userDao().insertUser(users[0]);
        }

        @Override
        protected void onPostExecute(Long userId) {
            if (userId != -1) {
                // L'inscription a réussi
                Toast.makeText(Signup1.this, "Inscription réussie", Toast.LENGTH_SHORT).show();




            } else {
                // Une erreur s'est produite lors de l'inscription
                Toast.makeText(Signup1.this, "Erreur lors de l'inscription", Toast.LENGTH_SHORT).show();
            }
        }
    }}


