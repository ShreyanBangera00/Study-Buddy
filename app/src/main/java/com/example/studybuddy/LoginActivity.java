package com.example.studybuddy;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.studybuddy.viewmodel.UserViewModel;

public class LoginActivity extends AppCompatActivity {

    EditText edtEmail, edtPassword;
    TextView btnLogin, tvGoToSignup;
    UserViewModel userViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ThemeManager.applyTheme(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edtEmail     = findViewById(R.id.edtEmail);
        edtPassword  = findViewById(R.id.edtPassword);
        btnLogin     = findViewById(R.id.btnLogin);
        tvGoToSignup = findViewById(R.id.tvGoToSignup);

        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email    = edtEmail.getText().toString().trim().toLowerCase();
                String password = edtPassword.getText().toString().trim();
                if (email.isEmpty())    { edtEmail.setError("Enter your email"); return; }
                if (password.isEmpty()) { edtPassword.setError("Enter your password"); return; }

                userViewModel.login(email, password, user -> runOnUiThread(() -> {
                    if (user != null) {
                        Session.login(user.getEmail(), user.getName());
                        Toast.makeText(LoginActivity.this, "Welcome back, " + user.getName() + "!", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        finish();
                    } else {
                        Toast.makeText(LoginActivity.this, "Invalid email or password", Toast.LENGTH_SHORT).show();
                    }
                }));
            }
        });

        tvGoToSignup.setOnClickListener(v -> startActivity(new Intent(LoginActivity.this, SignupActivity.class)));
    }
}