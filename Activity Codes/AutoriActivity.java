package com.example.p33333;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class AutoriActivity extends AppCompatActivity {

    Button btnAdd, btnEdit, btnDelete;
    ListView listViewAutori;
    ArrayList<String> autoriList;
    ArrayAdapter<String> adapter;
    MyDatabaseHelper dbHelper;
    String userRole;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_autori);


        btnAdd = findViewById(R.id.btnAdd);
        btnEdit = findViewById(R.id.btnEdit);
        btnDelete = findViewById(R.id.btnDelete);
        listViewAutori = findViewById(R.id.listViewAutori);

        dbHelper = new MyDatabaseHelper(this);
        autoriList = new ArrayList<>();


        listViewAutori.setChoiceMode(ListView.CHOICE_MODE_SINGLE);


        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_single_choice, autoriList);
        listViewAutori.setAdapter(adapter);


        userRole = getIntent().getStringExtra("USER_ROLE");



        if (userRole == null || !userRole.trim().equalsIgnoreCase("admin")) {
            btnDelete.setEnabled(false);
            btnDelete.setAlpha(0.0f);
            btnDelete.setOnClickListener(v ->
                    Toast.makeText(this, "Acces interzis! Doar administratorul poate șterge.", Toast.LENGTH_SHORT).show()
            );
        } else {
            btnDelete.setEnabled(true);
            btnDelete.setOnClickListener(v -> {
                int position = listViewAutori.getCheckedItemPosition();
                if (position != -1) {
                    String selected = autoriList.get(position);
                    int id = Integer.parseInt(selected.split(" - ")[0]);


                    dbHelper.deleteAutor(id);
                    Toast.makeText(this, "Autor șters!", Toast.LENGTH_SHORT).show();

                    loadData();
                } else {
                    Toast.makeText(this, "Selectați un autor!", Toast.LENGTH_SHORT).show();
                }
            });
        }


        btnAdd.setOnClickListener(v -> {
            Intent intent = new Intent(AutoriActivity.this, AddAutorActivity.class);
            startActivity(intent);
        });


        btnEdit.setOnClickListener(v -> {
            int position = listViewAutori.getCheckedItemPosition();
            if (position != -1) {
                String selected = autoriList.get(position);
                int id = Integer.parseInt(selected.split(" - ")[0]);
                Intent intent = new Intent(AutoriActivity.this, EditAutorActivity.class);
                intent.putExtra("AutorID", id);
                startActivity(intent);
            } else {
                Toast.makeText(this, "Selectați un autor!", Toast.LENGTH_SHORT).show();
            }
        });

        loadData();
    }


    private void loadData() {
        autoriList.clear();
        autoriList.addAll(dbHelper.getAllAutori());
        adapter.notifyDataSetChanged();
    }


    @Override
    protected void onResume() {
        super.onResume();
        loadData();
    }
}

