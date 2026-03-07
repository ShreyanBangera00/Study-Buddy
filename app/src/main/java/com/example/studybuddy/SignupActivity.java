package com.example.studybuddy;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.studybuddy.database.entities.User;
import com.example.studybuddy.viewmodel.UserViewModel;

public class SignupActivity extends AppCompatActivity {

    EditText edtName, edtEmail, edtPassword;
    TextView btnSignup, tvGoToLogin;
    UserViewModel userViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ThemeManager.applyTheme(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        edtName     = findViewById(R.id.edtName);
        edtEmail    = findViewById(R.id.edtEmail);
        edtPassword = findViewById(R.id.edtPassword);
        btnSignup   = findViewById(R.id.btnSignup);
        tvGoToLogin = findViewById(R.id.tvGoToLogin);

        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);

        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name     = edtName.getText().toString().trim();
                String email    = edtEmail.getText().toString().trim().toLowerCase();
                String password = edtPassword.getText().toString().trim();
                if (name.isEmpty())        { edtName.setError("Enter your name"); return; }
                if (email.isEmpty())       { edtEmail.setError("Enter your email"); return; }
                if (password.isEmpty())    { edtPassword.setError("Enter a password"); return; }
                if (password.length() < 6) { edtPassword.setError("Password must be at least 6 characters"); return; }

                userViewModel.getUserByEmail(email, existingUser -> runOnUiThread(() -> {
                    if (existingUser != null) {
                        edtEmail.setError("Email already registered");
                    } else {
                        userViewModel.insertAndConfirm(new User(name, email, password), () -> runOnUiThread(() -> {
                            Session.login(email, name);
                            Toast.makeText(SignupActivity.this, "Welcome, " + name + "!", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(SignupActivity.this, MainActivity.class));
                            finish();
                        }));
                    }
                }));
            }
        });

        tvGoToLogin.setOnClickListener(v -> { startActivity(new Intent(SignupActivity.this, LoginActivity.class)); finish(); });
    }
}