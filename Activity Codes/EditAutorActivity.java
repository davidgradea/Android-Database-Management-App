package com.example.p33333;
import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class EditAutorActivity extends AppCompatActivity {

    EditText etNumeAutor, etPrenumeAutor, etTaraOrigine;
    Button btnSave;
    MyDatabaseHelper dbHelper;
    int autorId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_autor);

        etNumeAutor = findViewById(R.id.etNumeAutor);
        etPrenumeAutor = findViewById(R.id.etPrenumeAutor);
        etTaraOrigine = findViewById(R.id.etTaraOrigine);
        btnSave = findViewById(R.id.btnSave);

        dbHelper = new MyDatabaseHelper(this);

        Intent intent = getIntent();
        autorId = intent.getIntExtra("AutorID", -1);

        if (autorId != -1) {
            String[] autor = dbHelper.getAutorById(autorId);
            etNumeAutor.setText(autor[0]);
            etPrenumeAutor.setText(autor[1]);
            etTaraOrigine.setText(autor[2]);
        } else {
            Toast.makeText(this, "Eroare la procesarea datelor autorului!", Toast.LENGTH_SHORT).show();
            finish();
        }

        btnSave.setOnClickListener(v -> {
            String nume = etNumeAutor.getText().toString().trim();
            String prenume = etPrenumeAutor.getText().toString().trim();
            String tara = etTaraOrigine.getText().toString().trim();

            if (!nume.isEmpty() && !prenume.isEmpty() && !tara.isEmpty()) {
                ContentValues values = new ContentValues();
                values.put("NumeAutor", nume);
                values.put("PrenumeAutor", prenume);
                values.put("TaraOrigine", tara);

                dbHelper.getWritableDatabase().update("Autori", values, "AutorID = ?", new String[]{String.valueOf(autorId)});
                Toast.makeText(this, "Autor actualizat!", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Completează toate câmpurile!", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
