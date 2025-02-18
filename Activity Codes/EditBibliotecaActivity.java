package com.example.p33333;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class EditBibliotecaActivity extends AppCompatActivity {

    private EditText etEditDenumire, etEditAdresa;
    private Button btnUpdateBiblioteca;
    private MyDatabaseHelper dbHelper;
    private int bibliotecaId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_biblioteca);


        etEditDenumire = findViewById(R.id.etEditDenumire);
        etEditAdresa = findViewById(R.id.etEditAdresa);
        btnUpdateBiblioteca = findViewById(R.id.btnUpdateBiblioteca);


        dbHelper = new MyDatabaseHelper(this);


        bibliotecaId = getIntent().getIntExtra("id", -1); // ID-ul bibliotecii
        String bibliotecaDenumire = getIntent().getStringExtra("denumire");
        String bibliotecaAdresa = getIntent().getStringExtra("adresa");


        etEditDenumire.setText(bibliotecaDenumire);
        etEditAdresa.setText(bibliotecaAdresa);


        btnUpdateBiblioteca.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newDenumire = etEditDenumire.getText().toString().trim();
                String newAdresa = etEditAdresa.getText().toString().trim();

                if (newDenumire.isEmpty() || newAdresa.isEmpty()) {
                    Toast.makeText(EditBibliotecaActivity.this, "Completează toate câmpurile!", Toast.LENGTH_SHORT).show();
                } else {
                    try {

                        dbHelper.updateBiblioteca(bibliotecaId, newDenumire, newAdresa);
                        Toast.makeText(EditBibliotecaActivity.this, "Bibliotecă actualizată", Toast.LENGTH_SHORT).show();
                        finish();
                    } catch (Exception e) {
                        Toast.makeText(EditBibliotecaActivity.this, "Eroare la actualizarea bibliotecii: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }
}
