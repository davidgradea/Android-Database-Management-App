
package com.example.p33333;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class BibliotecaActivity extends AppCompatActivity {

    private Button btnAddBiblioteca, btnEditBiblioteca, btnDeleteBiblioteca;
    private ListView lvBiblioteca;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> bibliotecaList;
    private MyDatabaseHelper dbHelper;
    private int selectedBibliotecaId = -1;
    private String userRole;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_biblioteca);

        btnAddBiblioteca = findViewById(R.id.btnAddBiblioteca);
        btnEditBiblioteca = findViewById(R.id.btnEditBiblioteca);
        btnDeleteBiblioteca = findViewById(R.id.btnDeleteBiblioteca);
        lvBiblioteca = findViewById(R.id.lvBiblioteca);
        dbHelper = new MyDatabaseHelper(this);


        userRole = getIntent().getStringExtra("USER_ROLE");


        bibliotecaList = dbHelper.getAllBiblioteci();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_single_choice, bibliotecaList);
        lvBiblioteca.setAdapter(adapter);


        lvBiblioteca.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = bibliotecaList.get(position);
                String[] parts = selectedItem.split(" - "); // Extragere ID din string-ul formatat
                selectedBibliotecaId = Integer.parseInt(parts[0]); // ID-ul este prima parte
                Toast.makeText(BibliotecaActivity.this, "Biblioteca selectată: " + selectedItem, Toast.LENGTH_SHORT).show();
            }
        });


        btnAddBiblioteca.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BibliotecaActivity.this, AddBibliotecaActivity.class);
                startActivity(intent);
            }
        });


        btnEditBiblioteca.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedBibliotecaId != -1) {
                    Intent intent = new Intent(BibliotecaActivity.this, EditBibliotecaActivity.class);
                    for (String biblioteca : bibliotecaList) {
                        String[] parts = biblioteca.split(" - ");
                        int id = Integer.parseInt(parts[0]);
                        if (id == selectedBibliotecaId) {
                            intent.putExtra("id", id);
                            intent.putExtra("denumire", parts[1].split(",")[0].trim());
                            intent.putExtra("adresa", parts[1].split(",")[1].trim());
                            break;
                        }
                    }
                    startActivity(intent);
                } else {
                    Toast.makeText(BibliotecaActivity.this, "Selectează o bibliotecă !", Toast.LENGTH_SHORT).show();
                }
            }
        });

        if (userRole == null || !userRole.trim().equalsIgnoreCase("admin")) {
            btnDeleteBiblioteca.setEnabled(false);
            btnDeleteBiblioteca.setAlpha(0.0f);
            btnDeleteBiblioteca.setOnClickListener(v ->
                    Toast.makeText(BibliotecaActivity.this, "Acces interzis! Doar administratorul poate șterge biblioteci.", Toast.LENGTH_SHORT).show()
            );
        } else {
            btnDeleteBiblioteca.setEnabled(true);
            btnDeleteBiblioteca.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (selectedBibliotecaId != -1) {
                        dbHelper.deleteBiblioteca(selectedBibliotecaId);
                        updateBibliotecaList();
                        Toast.makeText(BibliotecaActivity.this, "Bibliotecă  ștearsă!", Toast.LENGTH_SHORT).show();
                        selectedBibliotecaId = -1;
                    } else {
                        Toast.makeText(BibliotecaActivity.this, "Selectează o bibliotecă!", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateBibliotecaList();
    }

    private void updateBibliotecaList() {
        bibliotecaList.clear();
        bibliotecaList.addAll(dbHelper.getAllBiblioteci());
        adapter.notifyDataSetChanged();
        Log.d("BibliotecaActivity", "Lista biblioteci actualizată: " + bibliotecaList);
    }
}

