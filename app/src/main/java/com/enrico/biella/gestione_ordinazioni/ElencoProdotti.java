package com.enrico.biella.gestione_ordinazioni;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

import Objects.Cameriere;
import Objects.ConnessioneDB;
import Objects.Prodotto;
import Objects.Tavolo;

public class ElencoProdotti extends AppCompatActivity {

    private static final String CAMERIERE = "cameriere";
    private static final String TAVOLO = "tavolo";
    private static final int REQUEST_MODIFICA_PRODOTTO = 2;
    private static final int REQUEST_ACTIVITY_PRODOTTO = 1;
    private static final String PRODOTTO = "prodotto";
    private Cameriere cameriere;
    private Tavolo tavolo;
    private CoordinatorLayout cl;
    private ListView lista;
    private ArrayList<Prodotto> elencoProdotti;
    private ArrayAdapter<Prodotto> itemsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_elenco_prodotti);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setCameriereTavolo();
        binding();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        refreshLista();
    }
    public void startActivityScegliCategoria(View v) {
        Intent nuovaPaginaScegliCategoria = new Intent(ElencoProdotti.this, ScegliCategoria.class);
        nuovaPaginaScegliCategoria.putExtra(CAMERIERE,cameriere);
        nuovaPaginaScegliCategoria.putExtra(TAVOLO,tavolo);
        startActivity(nuovaPaginaScegliCategoria);
        finish();
    }
    private void binding() {
        lista=(ListView)findViewById(R.id.listViewElencoProdotti);
        cl = (CoordinatorLayout) findViewById(R.id.coordinator_layout_elenco_prodotti);
        if(tavolo.getCameriere()==null){
        tavolo.setCameriere(cameriere);
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_elenco_prodotti, menu);
        return true;
    }

    private void refreshLista() {
        elencoProdotti=tavolo.getElenco_prodotti();
        itemsAdapter=new ArrayAdapter<Prodotto>(this, R.layout.my_expandable_list,elencoProdotti);
        lista.setAdapter(itemsAdapter);
        setupListViewListener();
    }
    private void setupListViewListener() {
        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startActivityForResultModificaProdotto(((Prodotto)(parent.getAdapter().getItem(position))));
            }
        });
    }
    public void startActivityForResultModificaProdotto(Prodotto p) {
        Intent nuovaPaginaActivityProdotto = new Intent(ElencoProdotti.this, ModificaProdotto.class);
        nuovaPaginaActivityProdotto.putExtra(TAVOLO, tavolo);
        nuovaPaginaActivityProdotto.putExtra(PRODOTTO, p);
        startActivityForResult(nuovaPaginaActivityProdotto, REQUEST_MODIFICA_PRODOTTO);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case android.R.id.home:
                startActivityInserisciTavolo();
                return true;
            case R.id.menu_done:
                creaDialog();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivityInserisciTavolo();
    }

    private void creaDialog() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder
                .setCancelable(false)
                .setTitle(R.string.title_dialog)
                .setMessage(R.string.messaggio_dialog)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        inviaComanda();
                    }
                })
                .setNegativeButton(R.string.indietro,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

        AlertDialog alertD = alertDialogBuilder.create();
        alertD.show();
    }
    private void inviaComanda() {
        ConnessioneDB task=new ConnessioneDB(cl,ElencoProdotti.this,tavolo,ConnessioneDB.UPLOAD);
        task.execute();
    }
    public void startActivityInserisciTavolo() {
        Intent nuovaActivityInserisciTavolo = new Intent(ElencoProdotti.this, InserisciTavolo.class);
        nuovaActivityInserisciTavolo.putExtra(CAMERIERE,cameriere);
        startActivity(nuovaActivityInserisciTavolo);
        finish();
    }
    private void setCameriereTavolo() {
        Intent i = getIntent();
        cameriere= (Cameriere) i.getSerializableExtra(CAMERIERE);
        tavolo=(Tavolo)i.getSerializableExtra(TAVOLO);
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_MODIFICA_PRODOTTO && resultCode == RESULT_OK) {
            tavolo=(Tavolo) data.getExtras().getSerializable(TAVOLO);
            refreshLista();
            Snackbar.make(cl,getResources().getText(R.string.prodotto_modificato_rimosso),Snackbar.LENGTH_SHORT).show();
        }else{
            Snackbar.make(cl,getResources().getText(R.string.prodotto_non_modificato),Snackbar.LENGTH_SHORT).show();
        }
    }
}
