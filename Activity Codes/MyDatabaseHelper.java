package com.example.p33333;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class MyDatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "Library.db";
    private static final int DATABASE_VERSION = 10;
    public static final String TABLE_USERS = "Users";
    public static final String COLUMN_USER_ID = "UserID";
    public static final String COLUMN_EMAIL = "Email";
    public static final String COLUMN_PASSWORD = "Password"; // Parola hash-uită
    public static final String COLUMN_ROLE = "Role"; // "admin" sau "user"



    // Tabela Autori
    public static final String TABLE_AUTORI = "Autori";
    public static final String COLUMN_AUTOR_ID = "AutorID";
    public static final String COLUMN_NUME_AUTOR = "NumeAutor";
    public static final String COLUMN_PRENUME_AUTOR = "PrenumeAutor";
    public static final String COLUMN_TARA_ORIGINE = "TaraOrigine";

    // Tabela Carti
    public static final String TABLE_CARTI = "Carti";
    public static final String COLUMN_CARTE_ID = "CarteID";
    public static final String COLUMN_DENUMIRE = "Denumire";
    public static final String COLUMN_AN_APARITIE = "AnAparitie";
    public static final String COLUMN_EDITURA = "Editura";

    // Tabela AutorCarte
    public static final String TABLE_AUTOR_CARTE = "AutorCarte";
    public static final String COLUMN_AC_AUTOR_ID = "AutorID";
    public static final String COLUMN_AC_CARTE_ID = "CarteID";

    public static final String TABLE_BIBLIOTECA = "Biblioteca";
    public static final String COLUMN_BIBLIOTECA_ID = "BibliotecaID";
    public static final String COLUMN_BIBLIOTECA_DENUMIRE = "Denumire";
    public static final String COLUMN_BIBLIOTECA_ADRESA = "Adresa";
    // Tabela CarteBiblioteca
    public static final String TABLE_CARTE_BIBLIOTECA = "CarteBiblioteca";
    public static final String COLUMN_CB_CARTE_ID = "CarteID";
    public static final String COLUMN_CB_BIBLIOTECA_ID = "BibliotecaID";



    public MyDatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createUsersTableQuery = "CREATE TABLE " + TABLE_USERS + " (" +
                COLUMN_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_EMAIL + " TEXT UNIQUE, " +
                COLUMN_PASSWORD + " TEXT, " +
                COLUMN_ROLE + " TEXT)";
        db.execSQL(createUsersTableQuery);


        String createAutoriTableQuery = "CREATE TABLE " + TABLE_AUTORI + " (" +
                COLUMN_AUTOR_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_NUME_AUTOR + " TEXT, " +
                COLUMN_PRENUME_AUTOR + " TEXT, " +
                COLUMN_TARA_ORIGINE + " TEXT)";
        db.execSQL(createAutoriTableQuery);


        String createCartiTableQuery = "CREATE TABLE " + TABLE_CARTI + " (" +
                COLUMN_CARTE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_DENUMIRE + " TEXT, " +
                COLUMN_AN_APARITIE + " INTEGER, " +
                COLUMN_EDITURA + " TEXT)";
        db.execSQL(createCartiTableQuery);


        String createAutorCarteTableQuery = "CREATE TABLE " + TABLE_AUTOR_CARTE + " (" +
                COLUMN_AC_AUTOR_ID + " INTEGER, " +
                COLUMN_AC_CARTE_ID + " INTEGER, " +
                "PRIMARY KEY (" + COLUMN_AC_AUTOR_ID + ", " + COLUMN_AC_CARTE_ID + "), " +
                "FOREIGN KEY(" + COLUMN_AC_AUTOR_ID + ") REFERENCES " + TABLE_AUTORI + "(" + COLUMN_AUTOR_ID + "), " +
                "FOREIGN KEY(" + COLUMN_AC_CARTE_ID + ") REFERENCES " + TABLE_CARTI + "(" + COLUMN_CARTE_ID + "))";
        db.execSQL(createAutorCarteTableQuery);


        String createBibliotecaTableQuery = "CREATE TABLE " + TABLE_BIBLIOTECA + " (" +
                COLUMN_BIBLIOTECA_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_BIBLIOTECA_DENUMIRE + " TEXT, " +
                COLUMN_BIBLIOTECA_ADRESA + " TEXT)";
        db.execSQL(createBibliotecaTableQuery);


        String createCarteBibliotecaTableQuery = "CREATE TABLE " + TABLE_CARTE_BIBLIOTECA + " (" +
                COLUMN_CB_CARTE_ID + " INTEGER, " +
                COLUMN_CB_BIBLIOTECA_ID + " INTEGER, " +
                "PRIMARY KEY (" + COLUMN_CB_CARTE_ID + ", " + COLUMN_CB_BIBLIOTECA_ID + "), " +
                "FOREIGN KEY(" + COLUMN_CB_CARTE_ID + ") REFERENCES " + TABLE_CARTI + "(" + COLUMN_CARTE_ID + "), " +
                "FOREIGN KEY(" + COLUMN_CB_BIBLIOTECA_ID + ") REFERENCES " + TABLE_BIBLIOTECA + "(" + COLUMN_BIBLIOTECA_ID + "))";
        db.execSQL(createCarteBibliotecaTableQuery);

    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_AUTORI);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CARTI);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_AUTOR_CARTE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BIBLIOTECA);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CARTE_BIBLIOTECA);
        onCreate(db);
    }

    // CRUD pentru Autori
    public ArrayList<String> getAllAutori() {
        ArrayList<String> autoriList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_AUTORI, null);
        if (cursor.moveToFirst()) {
            do {
                autoriList.add(cursor.getInt(0) + " - " +
                        cursor.getString(1) + " " +
                        cursor.getString(2) + " (" +
                        cursor.getString(3) + ")");
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return autoriList;
    }

    public void deleteAutor(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_AUTORI, COLUMN_AUTOR_ID + " = ?", new String[]{String.valueOf(id)});
        db.close();
    }

    // CRUD pentru Carti
    public void addCarte(String denumire, int anAparitie, String editura) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_DENUMIRE, denumire);
        values.put(COLUMN_AN_APARITIE, anAparitie);
        values.put(COLUMN_EDITURA, editura);
        db.insert(TABLE_CARTI, null, values);
        db.close();
    }

    public ArrayList<String> getAllCarti() {
        ArrayList<String> cartiList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_CARTI, null);
        if (cursor.moveToFirst()) {
            do {
                cartiList.add(cursor.getInt(0) + " - " +
                        cursor.getString(1) + ", " +
                        cursor.getInt(2) + ", " +
                        cursor.getString(3));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return cartiList;
    }

    public void updateCarte(int id, String denumire, int anAparitie, String editura) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_DENUMIRE, denumire);
        values.put(COLUMN_AN_APARITIE, anAparitie);
        values.put(COLUMN_EDITURA, editura);
        db.update(TABLE_CARTI, values, COLUMN_CARTE_ID + " = ?", new String[]{String.valueOf(id)});
        db.close();
    }

    public void deleteCarte(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CARTI, COLUMN_CARTE_ID + " = ?", new String[]{String.valueOf(id)});
        db.close();
    }

    public String[] getAutorById(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(
                TABLE_AUTORI,
                new String[]{COLUMN_NUME_AUTOR, COLUMN_PRENUME_AUTOR, COLUMN_TARA_ORIGINE},
                COLUMN_AUTOR_ID + " = ?",
                new String[]{String.valueOf(id)},
                null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            String[] autor = new String[]{
                    cursor.getString(0), // NumeAutor
                    cursor.getString(1), // PrenumeAutor
                    cursor.getString(2)  // TaraOrigine
            };
            cursor.close();
            return autor;
        } else {
            return null; // Autorul nu a fost găsit
        }
    }

    public String[] getCarteById(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(
                TABLE_CARTI,
                new String[]{COLUMN_DENUMIRE, COLUMN_AN_APARITIE, COLUMN_EDITURA},
                COLUMN_CARTE_ID + " = ?",
                new String[]{String.valueOf(id)},
                null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            String[] carte = new String[]{
                    cursor.getString(0), // Denumire
                    String.valueOf(cursor.getInt(1)), // AnAparitie
                    cursor.getString(2)  // Editura
            };
            cursor.close();
            return carte;
        } else {
            return null; // Cartea nu a fost găsită
        }
    }

    //Crud pentru TABELA BIBLIOTECA
    public void addBiblioteca(String denumire, String adresa) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_BIBLIOTECA_DENUMIRE, denumire);
        values.put(COLUMN_BIBLIOTECA_ADRESA, adresa);
        db.insert(TABLE_BIBLIOTECA, null, values);
        db.close();
    }

public ArrayList<String> getAllBiblioteci() {
    ArrayList<String> biblioteciList = new ArrayList<>();
    SQLiteDatabase db = this.getReadableDatabase();
    Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_BIBLIOTECA, null);

    if (cursor.moveToFirst()) {
        do {
            int id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_BIBLIOTECA_ID));
            String denumire = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_BIBLIOTECA_DENUMIRE));
            String adresa = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_BIBLIOTECA_ADRESA));
            String item = id + " - " + denumire + ", " + adresa;
            biblioteciList.add(item);
        } while (cursor.moveToNext());
    }

    cursor.close();
    db.close();
    return biblioteciList;
}

    public void updateBiblioteca(int id, String denumire, String adresa) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_BIBLIOTECA_DENUMIRE, denumire);
        values.put(COLUMN_BIBLIOTECA_ADRESA, adresa);
        db.update(TABLE_BIBLIOTECA, values, COLUMN_BIBLIOTECA_ID + " = ?", new String[]{String.valueOf(id)});
        db.close();
    }

    public void deleteBiblioteca(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_BIBLIOTECA, COLUMN_BIBLIOTECA_ID + " = ?", new String[]{String.valueOf(id)});
        db.close();
    }



    ////////////CRUD PENTRU CARTE-BIBLIOTECA///////////////////////

    public int addCarteBiblioteca(int carteId, int bibliotecaId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_CB_CARTE_ID, carteId);
        values.put(COLUMN_CB_BIBLIOTECA_ID, bibliotecaId);
        long result = db.insert(TABLE_CARTE_BIBLIOTECA, null, values);
        return result == -1 ? 0 : 1;
    }

    public int updateCarteBiblioteca(int oldCarteId, int oldBibliotecaId, int newCarteId, int newBibliotecaId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_CB_CARTE_ID, newCarteId);
        values.put(COLUMN_CB_BIBLIOTECA_ID, newBibliotecaId);
        return db.update(TABLE_CARTE_BIBLIOTECA, values,
                COLUMN_CB_CARTE_ID + "=? AND " + COLUMN_CB_BIBLIOTECA_ID + "=?",
                new String[]{String.valueOf(oldCarteId), String.valueOf(oldBibliotecaId)});
    }

    public int deleteCarteBiblioteca(int carteId, int bibliotecaId) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_CARTE_BIBLIOTECA,
                COLUMN_CB_CARTE_ID + "=? AND " + COLUMN_CB_BIBLIOTECA_ID + "=?",
                new String[]{String.valueOf(carteId), String.valueOf(bibliotecaId)});
    }

    public ArrayList<String> getAllCarteBiblioteca() {
        ArrayList<String> lista = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT C." + COLUMN_CARTE_ID + ", C." + COLUMN_DENUMIRE +
                ", C." + COLUMN_EDITURA + ", C." + COLUMN_AN_APARITIE +
                ", B." + COLUMN_BIBLIOTECA_ID + ", B." + COLUMN_BIBLIOTECA_DENUMIRE +
                ", B." + COLUMN_BIBLIOTECA_ADRESA +
                " FROM " + TABLE_CARTE_BIBLIOTECA + " CB" +
                " JOIN " + TABLE_CARTI + " C ON CB." + COLUMN_CB_CARTE_ID + " = C." + COLUMN_CARTE_ID +
                " JOIN " + TABLE_BIBLIOTECA + " B ON CB." + COLUMN_CB_BIBLIOTECA_ID + " = B." + COLUMN_BIBLIOTECA_ID;

        Cursor cursor = db.rawQuery(query, null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                int carteId = cursor.getInt(0);
                String carteDenumire = cursor.getString(1);
                String carteEditura = cursor.getString(2);
                int carteAnAparitie = cursor.getInt(3);
                int bibliotecaId = cursor.getInt(4);
                String bibliotecaDenumire = cursor.getString(5);
                String bibliotecaLocatie = cursor.getString(6);


                String relatie = carteId + "-" + carteDenumire + "," + carteEditura + "," + carteAnAparitie + "|" +
                       bibliotecaId + "-" + bibliotecaDenumire + "," + bibliotecaLocatie;

                lista.add(relatie);
            } while (cursor.moveToNext());
            cursor.close();
        }

        return lista;
    }



    //    //TABELA AUTOR CARTE///////////////////////
    public int addAutorCarte(int autorId, int carteId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_AC_AUTOR_ID, autorId);
        values.put(COLUMN_AC_CARTE_ID, carteId);
        long result = db.insert(TABLE_AUTOR_CARTE, null, values);
        return result == -1 ? 0 : 1;
    }

    // Actualizează o relație între autor și carte
    public int updateAutorCarte(int oldAutorId, int oldCarteId, int newAutorId, int newCarteId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_AC_AUTOR_ID, newAutorId);
        values.put(COLUMN_AC_CARTE_ID, newCarteId);
        return db.update(TABLE_AUTOR_CARTE, values,
                COLUMN_AC_AUTOR_ID + "=? AND " + COLUMN_AC_CARTE_ID + "=?",
                new String[]{String.valueOf(oldAutorId), String.valueOf(oldCarteId)});
    }

    // Șterge o relație între autor și carte
    public int deleteAutorCarte(int autorId, int carteId) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_AUTOR_CARTE,
                COLUMN_AC_AUTOR_ID + "=? AND " + COLUMN_AC_CARTE_ID + "=?",
                new String[]{String.valueOf(autorId), String.valueOf(carteId)});
    }

    // Obține toate relațiile între autori și cărți
    public ArrayList<String> getAllAutorCarte() {
        ArrayList<String> lista = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT A." + COLUMN_AUTOR_ID + ", A." + COLUMN_NUME_AUTOR + ", A." + COLUMN_PRENUME_AUTOR + ", A." + COLUMN_TARA_ORIGINE +
                ", C." + COLUMN_CARTE_ID + ", C." + COLUMN_DENUMIRE + ", C." + COLUMN_EDITURA + ", C." + COLUMN_AN_APARITIE +
                " FROM " + TABLE_AUTOR_CARTE + " AC" +
                " JOIN " + TABLE_AUTORI + " A ON AC." + COLUMN_AC_AUTOR_ID + " = A." + COLUMN_AUTOR_ID +
                " JOIN " + TABLE_CARTI + " C ON AC." + COLUMN_AC_CARTE_ID + " = C." + COLUMN_CARTE_ID;

        Cursor cursor = db.rawQuery(query, null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                int autorId = cursor.getInt(0);
                String numeAutor = cursor.getString(1);
                String prenumeAutor = cursor.getString(2);
                String taraOrigine = cursor.getString(3);
                int carteId = cursor.getInt(4);
                String denumireCarte = cursor.getString(5);
                String edituraCarte = cursor.getString(6);
                int anAparitieCarte = cursor.getInt(7);


                String relatie = autorId + "-" + numeAutor + "," + prenumeAutor + "," + taraOrigine + "|" +
                        carteId + "-" + denumireCarte + "," + edituraCarte + "," + anAparitieCarte;

                lista.add(relatie);
            } while (cursor.moveToNext());
            cursor.close();
        }

        return lista;
    }

    //pentru admin
    public Cursor getUserByEmail(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM Users WHERE Email = ?", new String[]{email});
        return cursor;
    }


}