package com.example.p33333;

//import android.annotation.SuppressLint;
//import android.content.DialogInterface;
//import android.database.Cursor;
//import android.database.sqlite.SQLiteDatabase;
//import android.os.Bundle;
//import android.widget.ArrayAdapter;
//import android.widget.Button;
//import android.widget.ListView;
//import android.widget.Toast;
//import androidx.appcompat.app.AlertDialog;
//import androidx.appcompat.app.AppCompatActivity;
//import java.util.ArrayList;
//
//public class UtilizatoriActivity extends AppCompatActivity {
//    ListView listViewUsers;
//    Button btnDeleteUser, btnChangeRole;
//    MyDatabaseHelper databaseHelper;
//    ArrayAdapter<String> adapter;
//    ArrayList<String> usersList, userEmails;
//    int selectedIndex = -1;
//
//    @SuppressLint("MissingInflatedId")
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_utilizatori);
//
//        listViewUsers = findViewById(R.id.listViewUsers);
//        btnDeleteUser = findViewById(R.id.btnDeleteUser);
//        btnChangeRole = findViewById(R.id.btnChangeRole);
//        databaseHelper = new MyDatabaseHelper(this);
//
//        loadUsers();
//
//        listViewUsers.setOnItemClickListener((parent, view, position, id) -> {
//            selectedIndex = position;
//            String selectedEmail = userEmails.get(position);
//            Toast.makeText(this, "Ai selectat: " + selectedEmail, Toast.LENGTH_SHORT).show();
//            adapter.notifyDataSetChanged();
//        });
//
//        btnDeleteUser.setOnClickListener(v -> {
//            if (selectedIndex != -1) {
//                confirmDeletion(userEmails.get(selectedIndex));
//            } else {
//                Toast.makeText(this, "Selectați un utilizator!", Toast.LENGTH_SHORT).show();
//            }
//        });
//
//        btnChangeRole.setOnClickListener(v -> {
//            if (selectedIndex != -1) {
//                changeUserRole(userEmails.get(selectedIndex));
//            } else {
//                Toast.makeText(this, "Selectați un utilizator!", Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
//
//    private void loadUsers() {
//        SQLiteDatabase db = databaseHelper.getReadableDatabase();
//        Cursor cursor = db.rawQuery("SELECT email, role FROM users", null);
//        usersList = new ArrayList<>();
//        userEmails = new ArrayList<>();
//
//        while (cursor.moveToNext()) {
//            String email = cursor.getString(0);
//            String role = cursor.getString(1);
//            usersList.add(email + " - " + role);
//            userEmails.add(email);
//        }
//
//        cursor.close();
//        db.close();
//
//        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_single_choice, usersList);
//        listViewUsers.setAdapter(adapter);
//        listViewUsers.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
//    }
//
//    private void confirmDeletion(String email) {
//        new AlertDialog.Builder(this)
//                .setTitle("Ștergere utilizator")
//                .setMessage("Sigur doriți să ștergeți utilizatorul " + email + "?")
//                .setPositiveButton("Da", (dialog, which) -> deleteUser(email))
//                .setNegativeButton("Nu", null)
//                .show();
//    }
//
//    private void deleteUser(String email) {
//        SQLiteDatabase db = databaseHelper.getWritableDatabase();
//        db.delete("users", "email=?", new String[]{email});
//        db.close();
//        loadUsers();
//        Toast.makeText(this, "Utilizator șters!", Toast.LENGTH_SHORT).show();
//    }
//
//    private void changeUserRole(String email) {
//        SQLiteDatabase db = databaseHelper.getWritableDatabase();
//        Cursor cursor = db.rawQuery("SELECT role FROM users WHERE email=?", new String[]{email});
//
//        if (cursor.moveToFirst()) {
//            String newRole = cursor.getString(0).equals("admin") ? "user" : "admin";
//            db.execSQL("UPDATE users SET role=? WHERE email=?", new Object[]{newRole, email});
//            Toast.makeText(this, "Rol modificat la: " + newRole, Toast.LENGTH_SHORT).show();
//        }
//
//        cursor.close();
//        db.close();
//        loadUsers();
//    }
//}
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;

public class UtilizatoriActivity extends AppCompatActivity {
    ListView listViewUsers;
    Button btnDeleteUser, btnChangeRole;
    MyDatabaseHelper databaseHelper;
    ArrayAdapter<String> adapter;
    ArrayList<String> usersList, userEmails;
    int selectedIndex = -1;
    String loggedInUserEmail; // Email-ul utilizatorului conectat
    String loggedInUserRole;  // Rolul utilizatorului conectat

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_utilizatori);

        listViewUsers = findViewById(R.id.listViewUsers);
        btnDeleteUser = findViewById(R.id.btnDeleteUser);
        btnChangeRole = findViewById(R.id.btnChangeRole);
        databaseHelper = new MyDatabaseHelper(this);

        // 🔹 Preluăm email-ul și rolul utilizatorului conectat din Intent
        loggedInUserEmail = getIntent().getStringExtra("USER_EMAIL");
        loggedInUserRole = getIntent().getStringExtra("USER_ROLE");

        // Asigură-te că email-ul și rolul nu sunt null
        if (loggedInUserEmail == null) loggedInUserEmail = "Necunoscut";
        if (loggedInUserRole == null) loggedInUserRole = "user";

        loadUsers();

        listViewUsers.setOnItemClickListener((parent, view, position, id) -> {
            selectedIndex = position;
            String selectedEmail = userEmails.get(position);
            Toast.makeText(this, "Ai selectat: " + selectedEmail, Toast.LENGTH_SHORT).show();
            adapter.notifyDataSetChanged();
        });

        btnDeleteUser.setOnClickListener(v -> {
            if (selectedIndex != -1) {
                String selectedEmail = userEmails.get(selectedIndex);
                if (selectedEmail.equals(loggedInUserEmail)) {
                    Toast.makeText(this, "Nu poți șterge propriul cont!", Toast.LENGTH_SHORT).show();
                } else {
                    confirmDeletion(selectedEmail);
                }
            } else {
                Toast.makeText(this, "Selectați un utilizator!", Toast.LENGTH_SHORT).show();
            }
        });

        btnChangeRole.setOnClickListener(v -> {
            if (selectedIndex != -1) {
                String selectedEmail = userEmails.get(selectedIndex);
                if (selectedEmail.equals(loggedInUserEmail)) {
                    Toast.makeText(this, "Nu îți poți modifica propriul rol!", Toast.LENGTH_SHORT).show();
                } else {
                    changeUserRole(selectedEmail);
                }
            } else {
                Toast.makeText(this, "Selectați un utilizator!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadUsers() {
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT email, role FROM users", null);
        usersList = new ArrayList<>();
        userEmails = new ArrayList<>();

        while (cursor.moveToNext()) {
            String email = cursor.getString(0);
            String role = cursor.getString(1);
            usersList.add(email + " - " + role);
            userEmails.add(email);
        }

        cursor.close();
        db.close();

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_single_choice, usersList);
        listViewUsers.setAdapter(adapter);
        listViewUsers.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
    }

    private void confirmDeletion(String email) {
        new AlertDialog.Builder(this)
                .setTitle("Ștergere utilizator")
                .setMessage("Sigur doriți să ștergeți utilizatorul " + email + "?")
                .setPositiveButton("Da", (dialog, which) -> deleteUser(email))
                .setNegativeButton("Nu", null)
                .show();
    }

    private void deleteUser(String email) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        db.delete("users", "email=?", new String[]{email});
        db.close();
        loadUsers();
        Toast.makeText(this, "Utilizator șters!", Toast.LENGTH_SHORT).show();
    }

    private void changeUserRole(String email) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT role FROM users WHERE email=?", new String[]{email});

        if (cursor.moveToFirst()) {
            String currentRole = cursor.getString(0);
            String newRole = currentRole.equals("admin") ? "user" : "admin";

            db.execSQL("UPDATE users SET role=? WHERE email=?", new Object[]{newRole, email});
            Toast.makeText(this, "Rol modificat la: " + newRole, Toast.LENGTH_SHORT).show();
        }

        cursor.close();
        db.close();
        loadUsers();
    }
}


