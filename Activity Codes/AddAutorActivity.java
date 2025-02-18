package com.example.p33333;
import android.content.ContentValues;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class AddAutorActivity extends AppCompatActivity {

    EditText etNumeAutor, etPrenumeAutor, etTaraOrigine;
    Button btnSave;
    MyDatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_autor);

        etNumeAutor = findViewById(R.id.etNumeAutor);
        etPrenumeAutor = findViewById(R.id.etPrenumeAutor);
        etTaraOrigine = findViewById(R.id.etTaraOrigine);
        btnSave = findViewById(R.id.btnSave);

        dbHelper = new MyDatabaseHelper(this);

        btnSave.setOnClickListener(v -> {
            String nume = etNumeAutor.getText().toString().trim();
            String prenume = etPrenumeAutor.getText().toString().trim();
            String tara = etTaraOrigine.getText().toString().trim();

            if (!nume.isEmpty() && !prenume.isEmpty() && !tara.isEmpty()) {
                ContentValues values = new ContentValues();
                values.put("NumeAutor", nume);
                values.put("PrenumeAutor", prenume);
                values.put("TaraOrigine", tara);
                dbHelper.getWritableDatabase().insert("Autori", null, values);
                Toast.makeText(this, "Autor adăugat!", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Completează toate câmpurile!!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
