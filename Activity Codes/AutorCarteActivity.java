package com.example.p33333;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class AutorCarteActivity extends AppCompatActivity {

    private ListView lvAutorCarte;
    private Button btnAddRelation, btnEditRelation, btnDeleteRelation;
    private MyDatabaseHelper dbHelper;

    private int selectedAutorId = -1;
    private int selectedCarteId = -1;

    private ActivityResultLauncher<Intent> addRelationLauncher;
    private ActivityResultLauncher<Intent> editRelationLauncher;
    private String userRole; // Variabilă pentru rolul utilizatorului

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_autor_carte);

        lvAutorCarte = findViewById(R.id.lvAutorCarte);
        btnAddRelation = findViewById(R.id.btnAddRelation);
        btnEditRelation = findViewById(R.id.btnEditRelation);
        btnDeleteRelation = findViewById(R.id.btnDeleteRelation);

        dbHelper = new MyDatabaseHelper(this);

        userRole = getIntent().getStringExtra("USER_ROLE");



        if (userRole == null || !userRole.trim().equalsIgnoreCase("admin")) {
            btnDeleteRelation.setEnabled(false);
            btnDeleteRelation.setAlpha(0.0f);
            btnDeleteRelation.setOnClickListener(v ->
                    Toast.makeText(this, "Acces interzis! Doar administratorul poate șterge relații.", Toast.LENGTH_SHORT).show()
            );
        } else {
            btnDeleteRelation.setEnabled(true);
            btnDeleteRelation.setOnClickListener(v -> {
                if (selectedAutorId == -1 || selectedCarteId == -1) {
                    Toast.makeText(this, "Selectați o relație .", Toast.LENGTH_SHORT).show();
                    return;
                }
                int result = dbHelper.deleteAutorCarte(selectedAutorId, selectedCarteId);
                if (result > 0) {
                    Toast.makeText(this, "Relație  ștearsă .", Toast.LENGTH_SHORT).show();
                    loadRelations();
                    resetSelection();
                } else {
                    Toast.makeText(this, "Eroare la ștergerea relației.", Toast.LENGTH_SHORT).show();
                }
            });
        }


        addRelationLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK) {
                        loadRelations(); // 🔹
                    }
                }
        );


        editRelationLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK) {
                        loadRelations();
                    }
                }
        );

        loadRelations();

        lvAutorCarte.setOnItemClickListener((parent, view, position, id) -> {
            String selectedItem = (String) parent.getItemAtPosition(position);

            try {
                String[] parts = selectedItem.split("\\s*[|,-]\\s*");
                if (parts.length == 8) { // Asigură-te că ai toate câmpurile necesare
                    selectedAutorId = Integer.parseInt(parts[0]); // ID Autor
                    String numeAutor = parts[1];
                    String prenumeAutor = parts[2];
                    String taraOrigine = parts[3];
                    selectedCarteId = Integer.parseInt(parts[4]); // ID Carte
                    String denumireCarte = parts[5];
                    String edituraCarte = parts[6];
                    int anCarte = Integer.parseInt(parts[7]);

                    Toast.makeText(this, "Selecție: " + numeAutor + " - " + denumireCarte, Toast.LENGTH_SHORT).show();
                } else {
                    throw new Exception("Format invalid");
                }
            } catch (Exception e) {
                Toast.makeText(this, "Eroare la selectarea relației.", Toast.LENGTH_SHORT).show();
                Log.e("SelectRelation", "Eroare la parsarea relației: " + selectedItem, e);
            }
        });



        btnAddRelation.setOnClickListener(v -> {
            Intent intent = new Intent(this, AddAutorCarteActivity.class);
            addRelationLauncher.launch(intent);
        });

        btnEditRelation.setOnClickListener(v -> {
            if (selectedAutorId == -1 || selectedCarteId == -1) {
                Toast.makeText(this, "Selectați o relație .", Toast.LENGTH_SHORT).show();
                return;
            }
            Intent intent = new Intent(this, EditAutorCarteActivity.class);
            intent.putExtra("autorId", selectedAutorId);
            intent.putExtra("carteId", selectedCarteId);
            editRelationLauncher.launch(intent);
        });

    }

    private void loadRelations() {
        ArrayList<String> relations = dbHelper.getAllAutorCarte();

        for (String rel : relations) {
            Log.d("Relatie", rel);
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_single_choice, relations);
        lvAutorCarte.setAdapter(adapter);
    }

    private void resetSelection() {
        selectedAutorId = -1;
        selectedCarteId = -1;
    }
}