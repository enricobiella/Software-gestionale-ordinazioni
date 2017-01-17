package com.enrico.biella.gestione_ordinazioni;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

import Objects.Aggiunta;
import Objects.Prodotto;

public class ScegliAggiunta extends AppCompatActivity {
    private static final int REQUEST_SCEGLI_AGGIUNTA = 3;
    private static final String PRODOTTO = "prodotto";
    private static final String DESCRIZIONE = "descrizione";
    private static final String CONSENZA = "consenza";
    private static final String TABLE_NAME = "aggiunte";
    private Prodotto prodotto;
    private SQLiteDatabase mydatabase;
    private CoordinatorLayout cl;
    private ListView lista;
    private SearchView searchView;
    private ArrayList<Aggiunta> elencoAggiunte;
    private ArrayAdapter<Aggiunta> itemsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scegli_aggiunta);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setValori();
        setDatabase();
        binding();
    }
    private void binding() {
        cl=(CoordinatorLayout)findViewById(R.id.coordinator_layout_scegli_aggiunta);
        lista = (ListView) findViewById(R.id.listView);
    }
    private void setDatabase() {
        mydatabase = openOrCreateDatabase("DB.client", MODE_PRIVATE, null);
    }
    private void setValori() {
        Intent i = getIntent();
        prodotto= (Prodotto) i.getSerializableExtra(PRODOTTO);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_scegli_prodotto, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setQueryHint(getResources().getString(R.string.action_search));
        // Configure the search info and add any event listeners
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                refreshLista();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                refreshLista();
                return true;
            }
        });
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                refreshLista("");
                return true;
            }
        });
        refreshLista();
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        finishWithResult();
    }
    private void setupListViewListener() {
        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(prodotto.addAggiunta(((Aggiunta)(parent.getAdapter().getItem(position))))){
                    finishWithResult();
                }else{
                    finish();
                }
            }
        });
    }
    private void refreshLista() {
        elencoAggiunte = getAllAggiunte();
        itemsAdapter=new ArrayAdapter<Aggiunta>(this, R.layout.my_expandable_list, elencoAggiunte);
        lista.setAdapter(itemsAdapter);
        setupListViewListener();
    }
    private void refreshLista(String s) {
        elencoAggiunte = getAllAggiunte(s);
        itemsAdapter=new ArrayAdapter<Aggiunta>(this, R.layout.my_expandable_list, elencoAggiunte);
        lista.setAdapter(itemsAdapter);
        setupListViewListener();
    }
    private ArrayList<Aggiunta> getAllAggiunte() {
        ArrayList<Aggiunta> ritorno=new ArrayList<>(20);;
        String [] settingsProjection = {
                DESCRIZIONE,
                CONSENZA,
        };
        String whereClause = DESCRIZIONE+" not in (?) AND descrizione LIKE ?";
        String aggiunte="";
        for(int i=0;i<prodotto.getAggiunte().size();++i){
            if(i==prodotto.getAggiunte().size()-1){
                aggiunte += "'" + prodotto.getAggiunte().get(i).getDescrizione() + "';";
            }else {
                aggiunte += "'" + prodotto.getAggiunte().get(i).getDescrizione() + "'";
            }
        }
        String [] whereArgs = {aggiunte, "%"+String.valueOf(searchView.getQuery())+"%"};
        Cursor cursor = mydatabase.query(
                TABLE_NAME,
                settingsProjection,
                whereClause,
                whereArgs,
                null,
                null,
                DESCRIZIONE
        );
// looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                String descrizione = "";
                String consenza = "";
                descrizione = cursor.getString(0);
                consenza = cursor.getString(1);
// Adding contact to list
                ritorno.add( new Aggiunta(descrizione,consenza));

            } while (cursor.moveToNext());
        }
// return contact list
        return ritorno;
    }
    private ArrayList<Aggiunta> getAllAggiunte(String s) {
        ArrayList<Aggiunta> ritorno=new ArrayList<>(20);;
        String [] settingsProjection = {
                DESCRIZIONE,
                CONSENZA,
        };
        String whereClause = DESCRIZIONE+" not in (?) AND descrizione LIKE ?";
        String aggiunte="";
        for(int i=0;i<prodotto.getAggiunte().size();++i){
            if(i==prodotto.getAggiunte().size()-1){
                aggiunte += "'" + prodotto.getAggiunte().get(i).getDescrizione() + "';";
            }else {
                aggiunte += "'" + prodotto.getAggiunte().get(i).getDescrizione() + "'";
            }
        }
        String [] whereArgs = {aggiunte, "%"+s+"%"};
        Cursor cursor = mydatabase.query(
                TABLE_NAME,
                settingsProjection,
                whereClause,
                whereArgs,
                null,
                null,
                DESCRIZIONE
        );
// looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                String descrizione = "";
                String consenza = "";
                descrizione = cursor.getString(0);
                consenza = cursor.getString(1);
// Adding contact to list
                ritorno.add( new Aggiunta(descrizione,consenza));

            } while (cursor.moveToNext());
        }
// return contact list
        return ritorno;
    }


    private void finishWithResult()
    {
        Intent intent = new Intent();
        intent.putExtra(PRODOTTO, prodotto);
        setResult(RESULT_OK, intent);
        finish();
    }
}
