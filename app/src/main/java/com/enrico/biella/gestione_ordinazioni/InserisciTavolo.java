package com.enrico.biella.gestione_ordinazioni;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import Objects.Cameriere;

public class InserisciTavolo extends AppCompatActivity {

    private static final String CAMERIERE = "cameriere";
    private static final String TAVOLO = "tavolo";
    private Cameriere cameriere;
    private String tavolo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inserisci_tavolo);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.conferma);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityInserisciServizi();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_inserisci_tavolo, menu);

        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            onBackPressed();  return true;
        }

        return super.onOptionsItemSelected(item);
    }
    public void startActivityInserisciServizi() {
        Intent nuovaPaginaInserisciServizi = new Intent(InserisciTavolo.this, InserisciServizi.class);
        nuovaPaginaInserisciServizi.putExtra(CAMERIERE,cameriere);
        nuovaPaginaInserisciServizi.putExtra(TAVOLO,tavolo);
        startActivity(nuovaPaginaInserisciServizi);
    }
}
