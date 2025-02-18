package com.example.p33333;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class AddBibliotecaActivity extends AppCompatActivity {

    private EditText etDenumire, etAdresa;
    private Button btnSaveBiblioteca;
    private MyDatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_biblioteca);

        etDenumire = findViewById(R.id.etDenumire);
        etAdresa = findViewById(R.id.etAdresa);
        btnSaveBiblioteca = findViewById(R.id.btnSaveBiblioteca);

        dbHelper = new MyDatabaseHelper(this);

        btnSaveBiblioteca.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String denumire = etDenumire.getText().toString().trim();
                String adresa = etAdresa.getText().toString().trim();

                if (denumire.isEmpty() || adresa.isEmpty()) {
                    Toast.makeText(AddBibliotecaActivity.this, "Completează toate câmpurile!", Toast.LENGTH_SHORT).show();
                    return;
                }

                try {
                    dbHelper.addBiblioteca(denumire, adresa);
                    Toast.makeText(AddBibliotecaActivity.this, "Bibliotecă adăugată!", Toast.LENGTH_SHORT).show();
                    finish();
                }
                catch (Exception e) {
                    Toast.makeText(AddBibliotecaActivity.this, "Eroare la adăugarea bibliotecii: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
