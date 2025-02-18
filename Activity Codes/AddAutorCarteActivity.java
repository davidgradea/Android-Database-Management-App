package com.example.p33333;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class AddAutorCarteActivity extends AppCompatActivity {

    private Spinner spAutor, spCarte;
    private Button btnSaveAdd;
    private MyDatabaseHelper dbHelper;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_autor_carte);

        spAutor = findViewById(R.id.spAutor);
        spCarte = findViewById(R.id.spCarte);
        btnSaveAdd = findViewById(R.id.btnSaveAddAutorCarte);
        dbHelper = new MyDatabaseHelper(this);


        populateSpinner(spAutor, dbHelper.getAllAutori());
        populateSpinner(spCarte, dbHelper.getAllCarti());


        btnSaveAdd.setOnClickListener(v -> {
            try {
                int autorId = Integer.parseInt(spAutor.getSelectedItem().toString().split(" - ")[0].trim());
                int carteId = Integer.parseInt(spCarte.getSelectedItem().toString().split(" - ")[0].trim());


                int result = dbHelper.addAutorCarte(autorId, carteId);

                if (result > 0) {
                    Toast.makeText(this, "Relație adăugată!", Toast.LENGTH_SHORT).show();
                    setResult(RESULT_OK); //
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
