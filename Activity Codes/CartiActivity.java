package com.example.p33333;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class CartiActivity extends AppCompatActivity {

    Button btnAddCarte, btnEditCarte, btnDeleteCarte;
    ListView listViewCarti;
    ArrayList<String> cartiList;
    ArrayAdapter<String> adapter;
    MyDatabaseHelper dbHelper;
    String userRole;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carti);


        btnAddCarte = findViewById(R.id.btnAddCarte);
        btnEditCarte = findViewById(R.id.btnEditCarte);
        btnDeleteCarte = findViewById(R.id.btnDeleteCarte);
        listViewCarti = findViewById(R.id.listViewCarti);

        dbHelper = new MyDatabaseHelper(this);
        cartiList = new ArrayList<>();


        listViewCarti.setChoiceMode(ListView.CHOICE_MODE_SINGLE);


        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_single_choice, cartiList);
        listViewCarti.setAdapter(adapter);


        userRole = getIntent().getStringExtra("USER_ROLE");
        if (userRole == null || !userRole.trim().equalsIgnoreCase("admin")) {
            btnDeleteCarte.setEnabled(false);
            btnDeleteCarte.setAlpha(0.0f);
            btnDeleteCarte.setOnClickListener(v ->
                    Toast.makeText(this, "Acces interzis! Doar administratorul poate șterge.", Toast.LENGTH_SHORT).show()
            );
        } else {
            btnDeleteCarte.setEnabled(true);
            btnDeleteCarte.setOnClickListener(v -> {
                int position = listViewCarti.getCheckedItemPosition();
                if (position != -1) {
                    String selected = cartiList.get(position);
                    int id = Integer.parseInt(selected.split(" - ")[0]);

                    dbHelper.deleteCarte(id);
                    Toast.makeText(this, "Carte ștearsă!", Toast.LENGTH_SHORT).show();

                    loadData();
                } else {
                    Toast.makeText(this, "Selectați o carte!", Toast.LENGTH_SHORT).show();
                }
            });
        }

        btnAddCarte.setOnClickListener(v -> {
            Intent intent = new Intent(CartiActivity.this, AddCarteActivity.class);
            startActivity(intent);
        });


        btnEditCarte.setOnClickListener(v -> {
            int position = listViewCarti.getCheckedItemPosition();
            if (position != -1) {
                String selected = cartiList.get(position);
                int id = Integer.parseInt(selected.split(" - ")[0]);
                Intent intent = new Intent(CartiActivity.this, EditCarteActivity.class);
                intent.putExtra("CarteID", id);
                startActivity(intent);
            } else {
                Toast.makeText(this, "Selectați o carte!", Toast.LENGTH_SHORT).show();
            }
        });

        loadData();
    }


    private void loadData() {
        cartiList.clear();
        cartiList.addAll(dbHelper.getAllCarti());
        adapter.notifyDataSetChanged(); //
    }


    @Override
    protected void onResume() {
        super.onResume();
        loadData();
    }
}

