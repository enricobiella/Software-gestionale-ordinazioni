package com.enrico.biella.gestione_ordinazioni;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import Objects.Cameriere;

public class InserisciServizi extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inserisci_servizi);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityScegliCategoria();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
    public void startActivityScegliCategoria() {
        Intent nuovaPaginaScegliCategoria = new Intent(InserisciServizi.this, ScegliCategoria.class);
        nuovaPaginaScegliCategoria.putExtra("cameriere",new Cameriere(2,"UTENTE_STATICO"));
        //nuovaPaginaScegliCategoria.putExtra(TAVOLO,tavolo);
        startActivity(nuovaPaginaScegliCategoria);
    }
}
