package com.enrico.biella.gestione_ordinazioni;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import Objects.Cameriere;
import Objects.Tavolo;

public class InserisciServizi extends AppCompatActivity {

    private static final String CAMERIERE = "cameriere";
    private static final String TAVOLO = "tavolo";
    private Cameriere cameriere;
    private Tavolo tavolo;
    private int contatore;
    private TextView schermo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inserisci_servizi);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setCameriereTavolo();
        getSupportActionBar().setTitle(getString(R.string.tavolo)+" "+tavolo.getNomeTavolo());
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        schermo=(TextView)findViewById(R.id.schermo);
        contatore=tavolo.getServizi();
        schermo.setText(String.valueOf(contatore));
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tavolo.setServizi(contatore);
                startActivityElencoProdotti();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            //onBackPressed();
            if(contatore<1){
                startActivityInserisciTavolo();
            }else{
                tavolo.setServizi(contatore);
                startActivityElencoProdotti();
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    private void setCameriereTavolo() {
        Intent i = getIntent();
        cameriere= (Cameriere) i.getSerializableExtra(CAMERIERE);
        tavolo=(Tavolo)i.getSerializableExtra(TAVOLO);
    }
    public void startActivityElencoProdotti() {
        Intent nuovaPaginaElencoProdotti = new Intent(InserisciServizi.this, ElencoProdotti.class);
        nuovaPaginaElencoProdotti.putExtra(CAMERIERE,cameriere);
        nuovaPaginaElencoProdotti.putExtra(TAVOLO,tavolo);
        startActivity(nuovaPaginaElencoProdotti);
        finish();
    }
    public void clickPlus(View v){
        ++contatore;
        schermo.setText(String.valueOf(contatore));
    }
    public void clickMinus(View v){
        if(contatore==0) {
            schermo.setText(R.string.schermo);
            return;
        }else {
            --contatore;
            schermo.setText(String.valueOf(contatore));
        }
    }
    public void startActivityInserisciTavolo() {
        Intent nuovaActivityInserisciTavolo = new Intent(InserisciServizi.this, InserisciTavolo.class);
        nuovaActivityInserisciTavolo.putExtra(CAMERIERE,cameriere);
        startActivity(nuovaActivityInserisciTavolo);
        finish();
    }

    @Override
    public void onBackPressed() {
        if(contatore<1){
            startActivityInserisciTavolo();
        }else{
            startActivityElencoProdotti();
        }
    }
}
