package com.enrico.biella.gestione_ordinazioni;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.BounceInterpolator;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;

import java.util.ArrayList;

import Objects.Aggiunta;
import Objects.Prodotto;

public class ElencoModifiche extends AppCompatActivity {

    private static final int REQUEST_MODIFICA_PRODOTTO = 4;
    private static final int REQUEST_SCEGLI_AGGIUNTA = 3;
    private static final String PRODOTTO = "prodotto";
    private Prodotto prodotto;
    private ArrayList<Aggiunta> elencoModifiche;
    private SQLiteDatabase mydatabase;
    private ArrayAdapter<Aggiunta> itemsAdapter;
    private SwipeMenuListView lista;
    private CoordinatorLayout cl;
    private FloatingActionButton addAggiunta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_elenco_modifiche);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setValori();
        binding();
        setupList();
        refreshLista();
        getSupportActionBar().setTitle(prodotto.getDescrizione());
    }
    private int dp2px(int dp) {
        float scale = getResources().getDisplayMetrics().density;
        int pixels = (int) (dp * scale + 0.5f);
        return pixels;
    }
    private void setupList() {
        SwipeMenuCreator creator = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {
                // create "delete" item
                SwipeMenuItem deleteItem = new SwipeMenuItem(
                        getApplicationContext());
                // set item background
                deleteItem.setBackground(R.color.red);
                // set item width
                deleteItem.setWidth(dp2px(90));
                // set a icon
                deleteItem.setIcon(R.drawable.ic_delete_white_18dp);
                // add to menu
                menu.addMenuItem(deleteItem);
            }
        };

// set creator
        lista.setMenuCreator(creator);
        lista.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener(){
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                switch (index) {
                    case 0:
                        prodotto.rimuoviAggiunta(prodotto.getAggiunte().get(index));
                        refreshLista();
                        break;
                }
                // false : close the menu; true : not close the menu
                return false;
            }
        });
        // Close Interpolator
        lista.setCloseInterpolator(new BounceInterpolator());
// Open Interpolator
        lista.setOpenInterpolator(new BounceInterpolator());
        lista.setSwipeDirection(SwipeMenuListView.DIRECTION_LEFT);
    }
    private void binding() {
        lista=(SwipeMenuListView)findViewById(R.id.listViewElencoModifiche);
        cl = (CoordinatorLayout) findViewById(R.id.coordinator_layout_elenco_modifiche);
        addAggiunta=(FloatingActionButton)findViewById(R.id.add_aggiunta);
        addAggiunta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResultScegliAggiunta();
            }
        });
    }

    private void startActivityForResultScegliAggiunta() {
        Intent nuovaPaginaScegliAggiunta = new Intent(ElencoModifiche.this, ScegliAggiunta.class);
        nuovaPaginaScegliAggiunta.putExtra(PRODOTTO, prodotto);
        startActivityForResult(nuovaPaginaScegliAggiunta, REQUEST_SCEGLI_AGGIUNTA);
    }

    private void setValori() {
        Intent i = getIntent();
        prodotto= (Prodotto) i.getSerializableExtra(PRODOTTO);
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
        finish();
    }
    private void setupListViewListener() {
        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            }
        });
    }
    private void refreshLista() {
        elencoModifiche = prodotto.getAggiunte();
        itemsAdapter=new ArrayAdapter<Aggiunta>(this, R.layout.my_expandable_list, elencoModifiche);
        lista.setAdapter(itemsAdapter);
        setupListViewListener();
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_SCEGLI_AGGIUNTA && resultCode == RESULT_OK) {
            //TODO: FINIRE QUESTA ACTIVITY SIMILE AD ELENCO PRODOTTI E CREARE UN ALTRA ACTIVITY CHE INTERROGA IL DB
            prodotto=(Prodotto) data.getExtras().getSerializable(PRODOTTO);
            refreshLista();
            Snackbar.make(cl,getResources().getText(R.string.prodotto_modificato_rimosso),Snackbar.LENGTH_SHORT).show();
        }else{
            Snackbar.make(cl,getResources().getText(R.string.prodotto_non_modificato),Snackbar.LENGTH_SHORT).show();
        }
    }
}
