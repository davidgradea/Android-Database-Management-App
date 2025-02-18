
package com.example.p33333;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.security.NoSuchAlgorithmException;

public class MainActivity extends AppCompatActivity {
    Button btnAutori, btnCarti, btnRelatie, btnBiblioteca, btnCarteBiblioteca, btnUtilizatori, btnLogout;
    TextView tvUserInfo;
    MyDatabaseHelper databaseHelper;
    String userRole, userEmail;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        databaseHelper = new MyDatabaseHelper(this);
        userEmail = getIntent().getStringExtra("USER_EMAIL");
        userRole = getIntent().getStringExtra("USER_ROLE");

        if (userRole == null || userRole.trim().isEmpty()) {
            userRole = "user";
        }

        if (userEmail == null || userEmail.trim().isEmpty()) {
            userEmail = "Necunoscut";
        }

        tvUserInfo = findViewById(R.id.tvUserInfo);
        tvUserInfo.setText("Ești conectat ca " + userRole + ": " + userEmail);

        btnAutori = findViewById(R.id.btnAutori);
        btnCarti = findViewById(R.id.btnCarti);
        btnRelatie = findViewById(R.id.btnRelatie);
        btnBiblioteca = findViewById(R.id.btnBiblioteca);
        btnCarteBiblioteca = findViewById(R.id.btnCarteBiblioteca);
        btnUtilizatori = findViewById(R.id.btnUtilizatori);
        btnLogout = findViewById(R.id.btnLogout);

        btnAutori.setOnClickListener(v -> openActivity(AutoriActivity.class));
        btnCarti.setOnClickListener(v -> openActivity(CartiActivity.class));
        btnRelatie.setOnClickListener(v -> openActivity(AutorCarteActivity.class));
        btnBiblioteca.setOnClickListener(v -> openActivity(BibliotecaActivity.class));
        btnCarteBiblioteca.setOnClickListener(v -> openActivity(CarteBibliotecaActivity.class));


        if ("admin".equals(userRole)) {
            btnUtilizatori.setOnClickListener(v -> openUsersActivity());
        } else {
            btnUtilizatori.setEnabled(false);
            btnUtilizatori.setAlpha(0.0f); // Dezactivează vizual butonul
        }


        btnLogout.setOnClickListener(v -> {
            getSharedPreferences("UserPrefs", MODE_PRIVATE)
                    .edit()
                    .clear()
                    .apply();

            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });

        try {
            addTestUser();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    private void openActivity(Class<?> activityClass) {
        Intent intent = new Intent(MainActivity.this, activityClass);
        intent.putExtra("USER_ROLE", userRole);
        intent.putExtra("USER_EMAIL", userEmail); // Trimitem și email-ul
        startActivity(intent);
    }

    private void openUsersActivity() {
        Intent intent = new Intent(MainActivity.this, UtilizatoriActivity.class);
        intent.putExtra("USER_ROLE", userRole);
        intent.putExtra("USER_EMAIL", userEmail);
        startActivity(intent);
    }
    

    private void addTestUser() throws NoSuchAlgorithmException {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        String email = "admin123";

        Cursor cursor = db.rawQuery("SELECT * FROM users WHERE email=?", new String[]{email});
        if (cursor.getCount() == 0) {
            String passwordHashAdmin = HashingUtil.hashPassword("admin123");
            db.execSQL("INSERT INTO users (email, password, role) VALUES (?, ?, ?)",
                    new String[]{"admin123", passwordHashAdmin, "admin"});
        }

        cursor.close();
        db.close();
    }
}




