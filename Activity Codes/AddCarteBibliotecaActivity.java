package com.example.p33333;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class AddCarteBibliotecaActivity extends AppCompatActivity {

    private Spinner spCarte, spBiblioteca;
    private Button btnSaveAdd;
    private MyDatabaseHelper dbHelper;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.adaugare_relatie2);

        spCarte = findViewById(R.id.spCarte);
        spBiblioteca = findViewById(R.id.spBiblioteca);
        btnSaveAdd = findViewById(R.id.btnSaveAddCarteBiblioteca);
        dbHelper = new MyDatabaseHelper(this);

        populateSpinner(spCarte, dbHelper.getAllCarti());
        populateSpinner(spBiblioteca, dbHelper.getAllBiblioteci());

        btnSaveAdd.setOnClickListener(v -> {
            try {
                int carteId = Integer.parseInt(spCarte.getSelectedItem().toString().split(" - ")[0].trim());
                int bibliotecaId = Integer.parseInt(spBiblioteca.getSelectedItem().toString().split(" - ")[0].trim());

                int result = dbHelper.addCarteBiblioteca(carteId, bibliotecaId);

                if (result > 0) {
                    Toast.makeText(this, "Relație  adăugată !", Toast.LENGTH_SHORT).show();
                    setResult(RESULT_OK);
                    finish();
                } else {
                    Toast.makeText(this, "Eroare la adăugarea relației!", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                Toast.makeText(this, "Eroare: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void populateSpinner(Spinner spinner, ArrayList<String> data) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, data);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }
}

