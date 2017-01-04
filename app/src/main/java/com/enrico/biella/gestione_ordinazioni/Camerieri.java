package com.enrico.biella.gestione_ordinazioni;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import Objects.Cameriere;

public class Camerieri extends AppCompatActivity {

    public static String CAMERIERE = "cameriere";
    private Cameriere cameriere;
    private ArrayList<String> elencoCamerieri;
    private SQLiteDatabase mydatabase;
    private View vecchiaView;
    private int currentListItemIndex;
    ArrayAdapter<String> itemsAdapter;
    ListView lista;

    private void binding() {
        //buttonBack=(Button)findViewById(R.id.buttonBack);
        //text = (TextView) findViewById(R.id.text);
        lista = (ListView) findViewById(R.id.listView);
        vecchiaView=null;
        cameriere=null;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camerieri);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton falso = (FloatingActionButton) findViewById(R.id.falso);
        falso.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        FloatingActionButton vero = (FloatingActionButton) findViewById(R.id.vero);
        vero.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(cameriere!=null) {
                    startActivityGestioneComande(cameriere);
                }
            }
        });
        getSupportActionBar().setTitle(R.string.title_activity_camerieri);
        setDatabase();
        binding();
        refreshLista();
    }

    public ArrayList<String> getAllCamerieri() {
        HashMap listaCamerieri = new HashMap();
        ArrayList<String> ritorno=new ArrayList<>(20);
// Select All Query
        //String selectQuery = "SELECT * FROM camerieri;";
        Cursor cursor = mydatabase.query(false, "camerieri", new String[]{"codice", "nome"}, null, null, null, null, "codice", null);
// looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                String id = "";
                String nome = "";
                id = cursor.getString(0);
                nome = cursor.getString(1);
// Adding contact to list
                listaCamerieri.put(id, nome);
            } while (cursor.moveToNext());
        }
// return contact list
        for (int i = 0; i < listaCamerieri.size() ; ++i) {
            ritorno.add(i,(String) listaCamerieri.get(String.valueOf(i+1)));
        }
        return ritorno;
    }


    private void setDatabase() {
        mydatabase = openOrCreateDatabase("DB.client", MODE_PRIVATE, null);
        //mydatabase.execSQL("DROP DATABASE IF EXISTS DB.client;");
        //mydatabase.execSQL("CREATE DATABASE IF NOT EXISTS DB.client;");
        mydatabase.execSQL("DROP TABLE IF EXISTS camerieri;");
        mydatabase.execSQL("DROP TABLE IF EXISTS prodotti;");
        mydatabase.execSQL("DROP TABLE IF EXISTS aggiunte;");
        mydatabase.execSQL("CREATE TABLE IF NOT EXISTS camerieri(codice mediumint(5) NOT NULL,nome VARCHAR(30));");
        mydatabase.execSQL("CREATE TABLE IF NOT EXISTS prodotti(codice mediumint(5) NOT NULL,descrizione VARCHAR(30), bottone VARCHAR(30));");
        mydatabase.execSQL("CREATE TABLE IF NOT EXISTS aggiunte(descrizione VARCHAR(30) NOT NULL,consenza VARCHAR(30));");
        File sdcard = Environment.getExternalStorageDirectory();
        File file = new File(sdcard, "Download//archivi.dat");
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;

            while ((line = br.readLine()) != null) {
                mydatabase.execSQL(line);
            }
            Toast.makeText(getApplicationContext(), "elementi inseriti", Toast.LENGTH_SHORT).show();
            br.close();
        } catch (IOException e) {
            Toast.makeText(getApplicationContext(), "qualcosa è andato storto", Toast.LENGTH_SHORT).show();
        }
    }

    private void setupListViewListener() {
        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (vecchiaView != null) {
                    vecchiaView.setBackgroundColor(getResources().getColor(R.color.trans));
                }
                //view.setBackground(getDrawable(R.drawable.button_circle_pressed_other));
                vecchiaView = view;
                currentListItemIndex = position;
                cameriere=new Cameriere(currentListItemIndex+1,elencoCamerieri.get(currentListItemIndex));
                vecchiaView.setBackgroundColor(getResources().getColor(R.color.select));

            }
        });
    }
    private void refreshLista() {
        elencoCamerieri=getAllCamerieri();
        itemsAdapter=new ArrayAdapter<String>(this, R.layout.my_expandable_list,elencoCamerieri);
        lista.setAdapter(itemsAdapter);
        setupListViewListener();
    }

    public void startActivityGestioneComande(Cameriere c) {
        Intent nuovaPaginaGestioneComande = new Intent(Camerieri.this, GestioneComande.class);
        nuovaPaginaGestioneComande.putExtra(CAMERIERE,c);
        startActivity(nuovaPaginaGestioneComande);
        finish();
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
