package com.example.p33333;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class AddCarteActivity extends AppCompatActivity {

    EditText etDenumire, etAnAparitie, etEditura;
    Button btnSaveCarte;
    MyDatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_carte);

        etDenumire = findViewById(R.id.etDenumireCarte);
        etAnAparitie = findViewById(R.id.etAnAparitieCarte);
        etEditura = findViewById(R.id.etEdituraCarte);
        btnSaveCarte = findViewById(R.id.btnSaveCarte);

        dbHelper = new MyDatabaseHelper(this);

        btnSaveCarte.setOnClickListener(v -> {
            String denumire = etDenumire.getText().toString().trim();
            String anAparitie = etAnAparitie.getText().toString().trim();
            String editura = etEditura.getText().toString().trim();

            if (denumire.isEmpty() || anAparitie.isEmpty() || editura.isEmpty()) {
                Toast.makeText(this, "Completează toate câmpurile!", Toast.LENGTH_SHORT).show();
            } else {
                dbHelper.addCarte(denumire, Integer.parseInt(anAparitie), editura);
                Toast.makeText(this, "Carte adăugată!", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }
}
