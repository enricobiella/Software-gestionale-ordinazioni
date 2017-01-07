package com.enrico.biella.gestione_ordinazioni;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.concurrent.Callable;

import Objects.Cameriere;
import Objects.DownloadDB;

public class Camerieri extends AppCompatActivity {

    public static String CAMERIERE = "cameriere";
    private Cameriere cameriere;
    private ArrayList<Cameriere> elencoCamerieri;
    private SQLiteDatabase mydatabase;
    private View vecchiaView;
    private int currentListItemIndex;
    private ArrayAdapter<Cameriere> itemsAdapter;
    private ListView lista;
    private CoordinatorLayout cl;
    public static boolean AGGIORNA = true;

    private void binding() {
        //buttonBack=(Button)findViewById(R.id.buttonBack);
        //text = (TextView) findViewById(R.id.text);
        Bundle extras = getIntent().getExtras();
        lista = (ListView) findViewById(R.id.listView);
        cl=(CoordinatorLayout)findViewById(R.id.coordinator_layout);
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
        //setDatabase();
        binding();
        downloadDB();
    }

    public ArrayList<Cameriere> getAllCamerieri() {
        ArrayList<Cameriere> ritorno=new ArrayList<>(20);
        Cursor cursor = mydatabase.query(false, "camerieri", new String[]{"codice", "nome"}, null, null, null, null, "codice", null);
// looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                String id = "";
                String nome = "";
                id = cursor.getString(0);
                nome = cursor.getString(1);
// Adding contact to list
                ritorno.add(new Cameriere(Integer.valueOf(id),nome));
            } while (cursor.moveToNext());
        }
// return contact list
        return ritorno;
    }

    private void refreshLista() {
        elencoCamerieri=getAllCamerieri();
        itemsAdapter=new ArrayAdapter<Cameriere>(this, R.layout.my_expandable_list,elencoCamerieri);
        lista.setAdapter(itemsAdapter);
        setupListViewListener();
    }

    private  void downloadDB()
    {
        if(AGGIORNA) {
            mydatabase = openOrCreateDatabase("DB.client", MODE_PRIVATE, null);
            DownloadDB task = new DownloadDB(mydatabase, AGGIORNA,cl, Camerieri.this, new Callable<Integer>() {
                @Override
                public Integer call() throws Exception {
                    refreshLista();
                    return null;
                }
            },new Callable<Integer>() {
                @Override
                public Integer call() throws Exception {
                    downloadDB();
                    return null;
                }
            } );
            task.execute();

        }else {
            mydatabase = openOrCreateDatabase("DB.client", MODE_PRIVATE, null);
            refreshLista();
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
                cameriere=elencoCamerieri.get(currentListItemIndex);
                vecchiaView.setBackgroundColor(getResources().getColor(R.color.select));

            }
        });
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
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_camerieri, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.menu_refresh) {
            Camerieri.AGGIORNA=true;
            downloadDB();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
