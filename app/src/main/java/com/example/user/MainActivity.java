package com.example.user;

import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;
import androidx.room.Room;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.example.user.Login;
import android.view.View;


import com.example.user.R;

import java.util.concurrent.Executor;

public class MainActivity extends AppCompatActivity {
    Button btn_pn, btn_ppin, btn_face;
    private BiometricPrompt biometricPrompt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.empreinte);

        btn_pn = findViewById(R.id.btn_pn);
        btn_ppin = findViewById(R.id.btn_ppin);
        btn_face = findViewById(R.id.face);

        CheckBiometricSupported();
        Executor executor = ContextCompat.getMainExecutor(this);
        biometricPrompt = new BiometricPrompt(MainActivity.this, executor, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationSucceeded(BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                Toast.makeText(MainActivity.this, "Auth succeeded", Toast.LENGTH_LONG).show();
                // Handle successful authentication here
                // You can start a new activity or perform other actions
                Intent intent = new Intent(MainActivity.this, Signup1.class);
                startActivity(intent);
            }

            @Override
            public void onAuthenticationError(int errorCode, CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
                Toast.makeText(MainActivity.this, "Auth error: " + errString, Toast.LENGTH_LONG).show();
                // Handle authentication error here

            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
                Toast.makeText(MainActivity.this, "Auth Failed", Toast.LENGTH_LONG).show();
                // Handle authentication failure here
                Intent intent = new Intent(MainActivity.this, Login.class);
                startActivity(intent);
            }
        });

        // Handle fingerprint button click
        btn_pn.setOnClickListener(view -> {
            BiometricPrompt.PromptInfo.Builder promptInfo = dialogMetric();
            promptInfo.setNegativeButtonText("Cancel");
            biometricPrompt.authenticate(promptInfo.build());
        });

        // Handle fingerprint + PIN button click
        btn_ppin.setOnClickListener(view -> {
            BiometricPrompt.PromptInfo.Builder promptInfo = dialogMetric();
            promptInfo.setDeviceCredentialAllowed(true);
            biometricPrompt.authenticate(promptInfo.build());
        });

        // Handle Face ID button click
        btn_face.setOnClickListener(this::authenticateWithFaceID);
     /*   btn_face.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Signup1.class);
                startActivity(intent);
            }
        });*/
    }

    BiometricPrompt.PromptInfo.Builder dialogMetric() {
        return new BiometricPrompt.PromptInfo.Builder()
                .setTitle("Biometric login")
                .setSubtitle("Login with your Biometric credential");
    }

    private void CheckBiometricSupported() {
        String info = "";
        BiometricManager manager = BiometricManager.from(this);
        boolean isFaceIDSupported = (manager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_STRONG) == BiometricManager.BIOMETRIC_SUCCESS);

        if (manager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_WEAK | BiometricManager.Authenticators.BIOMETRIC_STRONG) == BiometricManager.BIOMETRIC_SUCCESS) {
            info = "App can Authenticate using biometrics";
            enableButton(true, true, isFaceIDSupported);
        } else if (isFaceIDSupported) {
            info = "App can Authenticate using Face ID";
            enableButton(false, true, isFaceIDSupported);
        } else {
            info = "Biometrics features are currently unavailable";
            enableButton(false, false, false);
        }

        TextView text = findViewById(R.id.txt_info);
        text.setText(info);
    }

    void enableButton(boolean enable, boolean enroll, boolean enableFaceID) {
        btn_pn.setEnabled(enable);
        btn_ppin.setEnabled(true);
        btn_face.setEnabled(enableFaceID);
    }

    void authenticateWithFaceID(View view) {
        BiometricPrompt.PromptInfo.Builder promptInfo = dialogMetric();
        promptInfo.setNegativeButtonText("Cancel");
        biometricPrompt.authenticate(promptInfo.build());
    }
}