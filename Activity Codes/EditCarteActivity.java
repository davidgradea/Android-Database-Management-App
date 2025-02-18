package com.example.p33333;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class EditCarteActivity extends AppCompatActivity {

    EditText etDenumire, etAnAparitie, etEditura;
    Button btnUpdateCarte;
    MyDatabaseHelper dbHelper;
    int carteId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_carte);

        etDenumire = findViewById(R.id.etDenumireCarte);
        etAnAparitie = findViewById(R.id.etAnAparitieCarte);
        etEditura = findViewById(R.id.etEdituraCarte);
        btnUpdateCarte = findViewById(R.id.btnUpdateCarte);

        dbHelper = new MyDatabaseHelper(this);
        carteId = getIntent().getIntExtra("CarteID", -1);

        String[] carte = dbHelper.getCarteById(carteId);
        if (carte != null) {
            etDenumire.setText(carte[0]);
            etAnAparitie.setText(carte[1]);
            etEditura.setText(carte[2]);
        }

        btnUpdateCarte.setOnClickListener(v -> {
            String denumire = etDenumire.getText().toString().trim();
            String anAparitie = etAnAparitie.getText().toString().trim();
            String editura = etEditura.getText().toString().trim();

            if (denumire.isEmpty() || anAparitie.isEmpty() || editura.isEmpty()) {
                Toast.makeText(this, "Completează toate câmpurile!", Toast.LENGTH_SHORT).show();
            } else {
                dbHelper.updateCarte(carteId, denumire, Integer.parseInt(anAparitie), editura);
                Toast.makeText(this, "Carte actualizată!", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }
}
