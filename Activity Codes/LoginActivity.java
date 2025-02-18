package com.example.p33333;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {
    private EditText emailEditText, passwordEditText;
    private Button loginButton;
    private TextView tvCreateAccount;
    private MyDatabaseHelper databaseHelper;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        databaseHelper = new MyDatabaseHelper(this);

        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        loginButton = findViewById(R.id.loginButton);
        tvCreateAccount = findViewById(R.id.tvCreateAccount);

        loginButton.setOnClickListener(v -> loginUser());

        tvCreateAccount.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });
    }

    private void loginUser() {
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Introduceți email și parolă!", Toast.LENGTH_SHORT).show();
            return;
        }

        Cursor cursor = databaseHelper.getUserByEmail(email);
        if (cursor != null && cursor.moveToFirst()) {
            int passwordIndex = cursor.getColumnIndex("Password");
            int roleIndex = cursor.getColumnIndex("Role");

            if (passwordIndex != -1 && roleIndex != -1) {
                String storedHashedPassword = cursor.getString(passwordIndex);
                String role = cursor.getString(roleIndex);

                if (role == null || role.trim().isEmpty()) {
                    role = "user";
                }

                try {
                    String hashedInputPassword = HashingUtil.hashPassword(password);
                    if (storedHashedPassword.equals(hashedInputPassword)) {
                        Toast.makeText(this, "Autentificare reușită!", Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(this, MainActivity.class);
                        intent.putExtra("USER_EMAIL", email);
                        intent.putExtra("USER_ROLE", role);


                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(this, "Parolă incorectă!", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(this, "Eroare la autentificare!", Toast.LENGTH_SHORT).show();
                }
            }
        } else {
            Toast.makeText(this, "Utilizator inexistent!", Toast.LENGTH_SHORT).show();
        }

        if (cursor != null) cursor.close();
    }
}


