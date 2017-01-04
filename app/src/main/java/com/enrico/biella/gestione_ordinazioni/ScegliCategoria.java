package com.enrico.biella.gestione_ordinazioni;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import Objects.Cameriere;
import Objects.Tavolo;

public class ScegliCategoria extends AppCompatActivity {

    private static final String CATEGORIA = "categoria";
    private static String CAMERIERE = "cameriere";
    private Cameriere cameriere;
    private static final String TAVOLO = "tavolo";
    private Tavolo tavolo;
    private String categoria;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scegli_categoria);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setCameriereTavolo();
        getSupportActionBar().setTitle(getString(R.string.tavolo)+" "+tavolo.getNomeTavolo());
        //getSupportActionBar().setTitle(cameriere.getNome());
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            //onBackPressed();
            startActivityInserisciTavolo();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    private void setCameriereTavolo() {
        Intent i = getIntent();
        cameriere= (Cameriere) i.getSerializableExtra(CAMERIERE);
        tavolo=(Tavolo)i.getSerializableExtra(TAVOLO);
        categoria="";
    }
    public void startActivityServizi(View v) {
        Intent activityServizi = new Intent(ScegliCategoria.this, InserisciServizi.class);
        activityServizi.putExtra(CAMERIERE,cameriere);
        activityServizi.putExtra(TAVOLO,tavolo);
        startActivity(activityServizi);
        finish();
    }
    public void startActivityInserisciTavolo() {
        Intent nuovaActivityInserisciTavolo = new Intent(ScegliCategoria.this, InserisciTavolo.class);
        nuovaActivityInserisciTavolo.putExtra(CAMERIERE,cameriere);
        startActivity(nuovaActivityInserisciTavolo);
        finish();
    }

    @Override
    public void onBackPressed() {
        startActivityInserisciTavolo();
    }

    public void startActivityScegliProdotto(View v) {
        categoria=String.valueOf(((Button)v).getText());
        Intent nuovaActivityScegliProdotto = new Intent(ScegliCategoria.this, ScegliProdotto.class);
        nuovaActivityScegliProdotto.putExtra(CAMERIERE,cameriere);
        nuovaActivityScegliProdotto.putExtra(TAVOLO,tavolo);
        nuovaActivityScegliProdotto.putExtra(CATEGORIA,categoria);
        startActivity(nuovaActivityScegliProdotto);
        finish();
    }
}
