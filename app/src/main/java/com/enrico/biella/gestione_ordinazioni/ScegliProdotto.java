package com.enrico.biella.gestione_ordinazioni;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
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
import java.util.Collections;
import java.util.Comparator;

import Objects.Cameriere;
import Objects.Prodotto;
import Objects.Tavolo;

public class ScegliProdotto extends AppCompatActivity {

    private static final String CATEGORIA = "categoria";
    private static final String CAMERIERE = "cameriere";
    private static final String TAVOLO = "tavolo";
    private static final String _ID = "codice";
    private static final String DESCRIZIONE = "descrizione";
    private static final String BOTTONE ="bottone" ;
    private static final String TABLE_NAME = "prodotti";
    private static final int REQUEST_ACTIVITY_PRODOTTO = 1;
    private static final String PRODOTTO = "prodotto";
    private Cameriere cameriere;
    private Tavolo tavolo;
    private String categoria;
    private ArrayList<Prodotto> elencoCategoria;
    private SQLiteDatabase mydatabase;
    private ArrayAdapter<Prodotto> itemsAdapter;
    private ListView lista;

    private  SearchView searchView;
    private CoordinatorLayout cl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scegli_prodotto);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setValori();
        setDatabase();
        binding();
        getSupportActionBar().setTitle(getString(R.string.tavolo)+" "+tavolo.getNomeTavolo());
        //refreshLista();
    }
    private void binding() {
        cl=(CoordinatorLayout)findViewById(R.id.coordinator_layout_scegli_prodotto);
        lista = (ListView) findViewById(R.id.listView);
    }
    private void setValori() {
        Intent i = getIntent();
        cameriere= (Cameriere) i.getSerializableExtra(CAMERIERE);
        tavolo=(Tavolo)i.getSerializableExtra(TAVOLO);
        switch (i.getStringExtra(CATEGORIA)){
            case "COCA COLA":
                categoria="COCHE";
                break;
            case "AMARI \nLIQUORI":
                categoria="AMARI";
                break;
            case "CAFFE'":
                categoria="CAFFE";
                break;
            default:
                categoria=i.getStringExtra(CATEGORIA);
                break;
        }
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

    public void startActivityScegliCategoria() {
        Intent nuovaPaginaScegliCategoria = new Intent(ScegliProdotto.this, ScegliCategoria.class);
        nuovaPaginaScegliCategoria.putExtra(CAMERIERE,cameriere);
        nuovaPaginaScegliCategoria.putExtra(TAVOLO,tavolo);
        startActivity(nuovaPaginaScegliCategoria);
        finish();
    }@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            //onBackPressed();
            startActivityScegliCategoria();
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        startActivityScegliCategoria();
        finish();
    }
    private void setupListViewListener() {
        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startActivityForResultActivityProdotto(((Prodotto)(parent.getAdapter().getItem(position))));
            }
        });
    }
    private void refreshLista() {
        elencoCategoria = getAllCategoria();
        itemsAdapter=new ArrayAdapter<Prodotto>(this, R.layout.my_expandable_list, elencoCategoria);
        lista.setAdapter(itemsAdapter);
        setupListViewListener();
    }
    private void refreshLista(String s) {
        elencoCategoria = getAllCategoria(s);
        itemsAdapter=new ArrayAdapter<Prodotto>(this, R.layout.my_expandable_list, elencoCategoria);
        lista.setAdapter(itemsAdapter);
        setupListViewListener();
    }
    private ArrayList<Prodotto> getAllCategoria() {
        ArrayList<Prodotto> ritorno=new ArrayList<>(20);;
        String [] settingsProjection = {
                _ID,
                DESCRIZIONE
        };
        String whereClause = BOTTONE+"=? AND descrizione LIKE ?";
        String [] whereArgs = {categoria, "%"+String.valueOf(searchView.getQuery())+"%"};
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
                String codice = "";
                String descrizione = "";
                codice = cursor.getString(0);
                descrizione = cursor.getString(1);
// Adding contact to list
                ritorno.add( new Prodotto(Integer.valueOf(codice),descrizione));

            } while (cursor.moveToNext());
        }
// return contact list
        return ritorno;
    }
    private ArrayList<Prodotto> getAllCategoria(String s) {
        ArrayList<Prodotto> ritorno=new ArrayList<>(20);;
        String [] settingsProjection = {
                _ID,
                DESCRIZIONE
        };
        String whereClause = BOTTONE+"=? AND descrizione LIKE ?";
        String [] whereArgs = {categoria, "%"+s+"%"};
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
                String codice = "";
                String descrizione = "";
                codice = cursor.getString(0);
                descrizione = cursor.getString(1);
// Adding contact to list
                ritorno.add( new Prodotto(Integer.valueOf(codice),descrizione));

            } while (cursor.moveToNext());
        }
// return contact list
        return ritorno;
    }
    private void setDatabase() {
        mydatabase = openOrCreateDatabase("DB.client", MODE_PRIVATE, null);
    }
    public void startActivityForResultActivityProdotto(Prodotto p) {
        Intent nuovaPaginaActivityProdotto = new Intent(ScegliProdotto.this, ActivityProdotto.class);
        nuovaPaginaActivityProdotto.putExtra(TAVOLO, tavolo);
        nuovaPaginaActivityProdotto.putExtra(PRODOTTO, p);
        startActivityForResult(nuovaPaginaActivityProdotto, REQUEST_ACTIVITY_PRODOTTO);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_ACTIVITY_PRODOTTO && resultCode == RESULT_OK) {
            tavolo.inserisciProdotto((Prodotto) data.getExtras().getSerializable(PRODOTTO));
            Collections.sort(tavolo.getElenco_prodotti(), new Comparator<Prodotto>() {
                @Override
                public int compare(Prodotto p1, Prodotto p2) {
                    int n1=p1.getCodice()-p2.getCodice();
                    boolean n2=p1.getAggiunte().equals(p2.getAggiunte());
                    int n3=p1.getDescrizione().compareTo(p2.getDescrizione());
                    if(n1==0&&n2==true){
                        Prodotto p=p1;
                        p1.setQuantità(p1.getQuantità()+p2.getQuantità());
                        tavolo.getElenco_prodotti().remove(p1);
                        tavolo.getElenco_prodotti().remove(p2);
                        tavolo.getElenco_prodotti().add(p);
                        return 0;
                    }else {
                        return n3;
                    }
                }
            });
            Snackbar.make(cl,getResources().getText(R.string.prodotto_inserito),Snackbar.LENGTH_SHORT).show();
        }else{
            Snackbar.make(cl,getResources().getText(R.string.prodotto_non_inserito),Snackbar.LENGTH_SHORT).show();
        }
    }
}
