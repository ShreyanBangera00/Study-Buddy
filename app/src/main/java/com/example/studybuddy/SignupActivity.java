package com.example.studybuddy;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class SignupActivity extends AppCompatActivity {

    EditText edtName, edtEmail, edtPassword;
    Button btnSignup;
    TextView tvGoToLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        edtName      = findViewById(R.id.edtName);
        edtEmail     = findViewById(R.id.edtEmail);
        edtPassword  = findViewById(R.id.edtPassword);
        btnSignup   = findViewById(R.id.btnSignup);
        tvGoToLogin = findViewById(R.id.tvGoToLogin);

        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name     = edtName.getText().toString().trim();
                String email    = edtEmail.getText().toString().trim();
                String password = edtPassword.getText().toString().trim();

                if (name.isEmpty()) {
                    edtName.setError("Enter your name");
                    return;
                }
                if (email.isEmpty()) {
                    edtEmail.setError("Enter your email");
                    return;
                }
                if (password.isEmpty()) {
                    edtPassword.setError("Enter a password");
                    return;
                }
                if (password.length() < 6) {
                    edtPassword.setError("Password must be at least 6 characters");
                    return;
                }

                Toast.makeText(SignupActivity.this, "Account created! Welcome, " + name + "!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(SignupActivity.this, MainActivity.class));
                finish();
            }
        });

        tvGoToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignupActivity.this, LoginActivity.class));
                finish();
            }
        });
    }
}