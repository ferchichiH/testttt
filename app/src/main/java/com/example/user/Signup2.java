package com.example.user;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

public class Signup2 extends AppCompatActivity {
    private AppDatabase appDatabase;
    private EditText ageEditText;
    private EditText tailleEditText;
    private EditText poidsEditText;
    private EditText sexeEditText;
    private ImageView btn_image;
@Override
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.signup2); // Remplacez "votre_fichier_layout" par le nom de votre fichier XML

    appDatabase = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "myapp-database").build();
    ageEditText = findViewById(R.id.age);
    tailleEditText = findViewById(R.id.taille);
    poidsEditText = findViewById(R.id.poids);
    sexeEditText = findViewById(R.id.sexe);
    btn_image=findViewById(R.id.next);

    Intent intent = getIntent();
    String nom = intent.getStringExtra("nom");
    String email = intent.getStringExtra("email");
    String motDePasse = intent.getStringExtra("motDePasse");
    btn_image.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            String age = ageEditText.getText().toString();
            String taille = tailleEditText.getText().toString();
            String poids = poidsEditText.getText().toString();
            String sexe = sexeEditText.getText().toString();

            if (TextUtils.isEmpty(age) || TextUtils.isEmpty(taille) || TextUtils.isEmpty(poids)) {
                Toast.makeText(getApplicationContext(), "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show();
                return;
            }

            try {
                int ageValue = Integer.parseInt(age);
                int tailleValue = Integer.parseInt(taille);
                int poidsValue = Integer.parseInt(poids);

                User newUser = new User();
                newUser.setName(nom);
                newUser.setUsername(email);
                newUser.setPassword(motDePasse);
                newUser.setAge(ageValue);
                newUser.setTaille(tailleValue);
                newUser.setPoids(poidsValue);
                newUser.setSexe(sexe);

                // Insérer l'utilisateur dans la base de données en utilisant AsyncTask
                new InsertUserTask().execute(newUser);
            } catch (NumberFormatException e) {
                Toast.makeText(getApplicationContext(), "Veuillez entrer des valeurs numériques valides", Toast.LENGTH_SHORT).show();
            }


        }
    });

}

    private class InsertUserTask extends AsyncTask<User, Void, Void> {
        @Override
        protected Void doInBackground(User... users) {
            appDatabase.userDao().insertUser(users[0]);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            // Action à effectuer après l'insertion de l'utilisateur (par exemple, redirection vers une autre activité)
            Toast.makeText(getApplicationContext(), "Utilisateur inséré avec succès", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(Signup2.this, Login.class);
            startActivity(intent);

        }

}}
