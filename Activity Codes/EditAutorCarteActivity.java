package com.example.p33333;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class EditAutorCarteActivity extends AppCompatActivity {

    private Spinner spAutor, spCarte;
    private Button btnSave;
    private MyDatabaseHelper dbHelper;

    private int autorIdOriginal = -1;
    private int carteIdOriginal = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_autor_carte);

        spAutor = findViewById(R.id.spAutorEdit);
        spCarte = findViewById(R.id.spCarteEdit);
        btnSave = findViewById(R.id.btnSaveEditAutorCarte);

        dbHelper = new MyDatabaseHelper(this);

        autorIdOriginal = getIntent().getIntExtra("autorId", -1);
        carteIdOriginal = getIntent().getIntExtra("carteId", -1);


        populateSpinner(spAutor, dbHelper.getAllAutori());
        populateSpinner(spCarte, dbHelper.getAllCarti());


        if (autorIdOriginal != -1 && carteIdOriginal != -1) {
            setInitialSelection(spAutor, autorIdOriginal);
            setInitialSelection(spCarte, carteIdOriginal);
        }

        btnSave.setOnClickListener(v -> {
            try {

                int newAutorId = Integer.parseInt(spAutor.getSelectedItem().toString().split(" - ")[0].trim());
                int newCarteId = Integer.parseInt(spCarte.getSelectedItem().toString().split(" - ")[0].trim());


                if (autorIdOriginal == -1 || carteIdOriginal == -1) {

                    dbHelper.addAutorCarte(newAutorId, newCarteId);
                    Toast.makeText(this, "Relație  adăugată! ", Toast.LENGTH_SHORT).show();
                } else {

                    dbHelper.updateAutorCarte(autorIdOriginal, carteIdOriginal, newAutorId, newCarteId);
                    Toast.makeText(this, "Relație actualizată!", Toast.LENGTH_SHORT).show();
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
