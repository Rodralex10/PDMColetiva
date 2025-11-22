package com.example.mychat;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";
    private FirebaseAuth auth;

    private EditText editEmail;
    private EditText editPassword;
    private EditText editDisplayName;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseApp.initializeApp(this);
        setContentView(R.layout.activity_login);

        auth = FirebaseAuth.getInstance();

        editEmail = findViewById(R.id.editEmail);
        editPassword = findViewById(R.id.editPassword);
        editDisplayName = findViewById(R.id.editDisplayName);
        Button buttonLogin = findViewById(R.id.buttonLogin);
        Button buttonRegister = findViewById(R.id.buttonRegister);

        // If already signed in, skip login.
        if (auth.getCurrentUser() != null) {
            goToChat();
            return;
        }

        buttonLogin.setOnClickListener(v -> login());
        buttonRegister.setOnClickListener(v -> register());
    }

    private void login() {
        String email = editEmail.getText().toString().trim();
        String pass = editPassword.getText().toString().trim();
        if (!validate(email, pass)) return;

        auth.signInWithEmailAndPassword(email, pass)
                .addOnSuccessListener(result -> {
                    Toast.makeText(this, "Login success", Toast.LENGTH_SHORT).show();
                    goToChat();
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Login failed", e);
                    Toast.makeText(this, "Login failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void register() {
        String email = editEmail.getText().toString().trim();
        String pass = editPassword.getText().toString().trim();
        String displayName = editDisplayName.getText().toString().trim();
        if (!validate(email, pass)) return;

        auth.createUserWithEmailAndPassword(email, pass)
                .addOnSuccessListener(result -> {
                    FirebaseUser user = result.getUser();
                    String nameToUse = !TextUtils.isEmpty(displayName) ? displayName : deriveNameFromEmail(email);
                    updateProfileName(user, nameToUse);
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Register failed", e);
                    Toast.makeText(this, "Register failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private boolean validate(String email, String pass) {
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(this, "Email required", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (TextUtils.isEmpty(pass)) {
            Toast.makeText(this, "Password required", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (pass.length() < 6) {
            Toast.makeText(this, "Password must be at least 6 characters", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private String deriveNameFromEmail(String email) {
        if (TextUtils.isEmpty(email)) return "User";
        int at = email.indexOf("@");
        return at > 0 ? email.substring(0, at) : email;
    }

    private void updateProfileName(FirebaseUser user, String name) {
        if (user == null) {
            Toast.makeText(this, "User missing after register", Toast.LENGTH_SHORT).show();
            return;
        }
        UserProfileChangeRequest request = new UserProfileChangeRequest.Builder()
                .setDisplayName(name)
                .build();
        user.updateProfile(request)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(this, "Registered", Toast.LENGTH_SHORT).show();
                        goToChat();
                    } else {
                        Toast.makeText(this, "Registered sem nome atualizado", Toast.LENGTH_SHORT).show();
                        goToChat();
                    }
                });
    }

    private void goToChat() {
        FirebaseUser user = auth.getCurrentUser();
        Intent intent = new Intent(this, MainActivity.class);
        // Optionally pass email to prefill nickname.
        if (user != null) {
            intent.putExtra("email", user.getEmail());
        }
        startActivity(intent);
        finish();
    }
}
