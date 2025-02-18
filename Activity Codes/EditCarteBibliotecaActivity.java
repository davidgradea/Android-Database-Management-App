package com.example.p33333;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class EditCarteBibliotecaActivity extends AppCompatActivity {

    private Spinner spCarte, spBiblioteca;
    private Button btnSave;
    private MyDatabaseHelper dbHelper;

    private int carteIdOriginal = -1;
    private int bibliotecaIdOriginal = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_carte_biblioteca);

        spCarte = findViewById(R.id.spCarteEdit);
        spBiblioteca = findViewById(R.id.spBibliotecaEdit);
        btnSave = findViewById(R.id.btnSaveEditCarteBiblioteca);

        dbHelper = new MyDatabaseHelper(this);

        // Obține valorile originale transmise (dacă sunt prezente)
        carteIdOriginal = getIntent().getIntExtra("carteId", -1);
        bibliotecaIdOriginal = getIntent().getIntExtra("bibliotecaId", -1);


        populateSpinner(spCarte, dbHelper.getAllCarti());
        populateSpinner(spBiblioteca, dbHelper.getAllBiblioteci());

        if (carteIdOriginal != -1 && bibliotecaIdOriginal != -1) {
            setInitialSelection(spCarte, carteIdOriginal);
            setInitialSelection(spBiblioteca, bibliotecaIdOriginal);
        }

        btnSave.setOnClickListener(v -> {
            try {
                int newCarteId = Integer.parseInt(spCarte.getSelectedItem().toString().split(" - ")[0].trim());
                int newBibliotecaId = Integer.parseInt(spBiblioteca.getSelectedItem().toString().split(" - ")[0].trim());


                if (carteIdOriginal == -1 || bibliotecaIdOriginal == -1) {
                    dbHelper.addCarteBiblioteca(newCarteId, newBibliotecaId);
                    Toast.makeText(this, "Relație adăugată!", Toast.LENGTH_SHORT).show();
                } else {
                    dbHelper.updateCarteBiblioteca(carteIdOriginal, bibliotecaIdOriginal, newCarteId, newBibliotecaId);
                    Toast.makeText(this, "Relație  actualizată !", Toast.LENGTH_SHORT).show();
                }


                Intent intent = new Intent();
                setResult(RESULT_OK, intent);
                finish();
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

    private void setInitialSelection(Spinner spinner, int id) {
        ArrayAdapter<String> adapter = (ArrayAdapter<String>) spinner.getAdapter();
        for (int i = 0; i < adapter.getCount(); i++) {
            String item = adapter.getItem(i);
            if (item != null && Integer.parseInt(item.split(" - ")[0].trim()) == id) {
                spinner.setSelection(i);
                break;
            }
        }
    }
}
