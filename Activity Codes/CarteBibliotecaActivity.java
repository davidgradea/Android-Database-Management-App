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

public class CarteBibliotecaActivity extends AppCompatActivity {

    private ListView lvBibliotecaCarte;
    private Button btnAddRelation, btnEditRelation, btnDeleteRelation;
    private MyDatabaseHelper dbHelper;

    private int selectedCarteId = -1;
    private int selectedBibliotecaId = -1;
    private String userRole;

    private ActivityResultLauncher<Intent> addRelationLauncher;
    private ActivityResultLauncher<Intent> editRelationLauncher;

        @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carte_biblioteca);

        lvBibliotecaCarte = findViewById(R.id.lvBibliotecaCarte);
        btnAddRelation = findViewById(R.id.btnAddRelation);
        btnEditRelation = findViewById(R.id.btnEditRelation);
        btnDeleteRelation = findViewById(R.id.btnDeleteRelation);

        dbHelper = new MyDatabaseHelper(this);

        userRole = getIntent().getStringExtra("USER_ROLE");


        addRelationLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK) {
                        loadRelations();
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

        lvBibliotecaCarte.setOnItemClickListener((parent, view, position, id) -> {
            String selectedItem = (String) parent.getItemAtPosition(position);

            try {

                String[] parts = selectedItem.split("\\s*[|,-]\\s*");


                if (parts.length == 7) {
                    selectedCarteId = Integer.parseInt(parts[0]); // ID Carte
                    String numeCarte = parts[1];
                    String edituraCarte = parts[2];
                    int anCarte = Integer.parseInt(parts[3]);
                    selectedBibliotecaId = Integer.parseInt(parts[4]); // ID Bibliotecă
                    String numeBiblioteca = parts[5];
                    String bibliotecaLocatie = parts[6];

                    Toast.makeText(this, "Selecție: " + numeCarte + ", " + numeBiblioteca, Toast.LENGTH_SHORT).show();
                } else {
                    throw new Exception("Format invalid");
                }
            } catch (Exception e) {
                Toast.makeText(this, "Eroare la selectarea relației.", Toast.LENGTH_SHORT).show();
            }
        });

            btnAddRelation.setOnClickListener(v -> {
                Intent intent = new Intent(this, AddCarteBibliotecaActivity.class);
            addRelationLauncher.launch(intent);
        });

        btnEditRelation.setOnClickListener(v -> {
            if (selectedCarteId == -1 || selectedBibliotecaId == -1) {
                Toast.makeText(this, "Selectați o relație .", Toast.LENGTH_SHORT).show();
                return;
            }
            Intent intent = new Intent(this, EditCarteBibliotecaActivity.class);
            intent.putExtra("carteId", selectedCarteId);
            intent.putExtra("bibliotecaId", selectedBibliotecaId);
            editRelationLauncher.launch(intent);
        });


        if (userRole == null || !userRole.trim().equalsIgnoreCase("admin")) {
            btnDeleteRelation.setEnabled(false);
            btnDeleteRelation.setAlpha(0.0f);
            btnDeleteRelation.setOnClickListener(v ->
                    Toast.makeText(CarteBibliotecaActivity.this, "Acces interzis! Doar administratorul poate șterge relații.", Toast.LENGTH_SHORT).show()
            );
        } else {
            btnDeleteRelation.setEnabled(true);
            btnDeleteRelation.setOnClickListener(v -> {
                if (selectedCarteId == -1 || selectedBibliotecaId == -1) {
                    Toast.makeText(this, "Selectați o relație .", Toast.LENGTH_SHORT).show();
                    return;
                }
                int result = dbHelper.deleteCarteBiblioteca(selectedCarteId, selectedBibliotecaId);
                if (result > 0) {
                    Toast.makeText(this, "Relație  ștearsă .", Toast.LENGTH_SHORT).show();
                    loadRelations();
                    resetSelection();
                } else {
                    Toast.makeText(this, "Eroare la ștergerea relației.", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void loadRelations() {
        ArrayList<String> relations = dbHelper.getAllCarteBiblioteca();

        for (String rel : relations) {
            Log.d("Relatie", rel);
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_single_choice, relations);
        lvBibliotecaCarte.setAdapter(adapter);
    }

    private void resetSelection() {
        selectedCarteId = -1;
        selectedBibliotecaId = -1;
    }
}


