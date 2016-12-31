package com.enrico.biella.gestione_ordinazioni;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import Objects.Cameriere;
import Objects.Tavolo;

public class ScegliCategoria extends AppCompatActivity {

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

    public static String CAMERIERE = "cameriere";
    private Cameriere cameriere;
    private static final String TAVOLO = "tavolo";
    private Tavolo tavolo;
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            onBackPressed();  return true;
        }

        return super.onOptionsItemSelected(item);
    }
    private void setCameriereTavolo() {
        Intent i = getIntent();
        cameriere= (Cameriere) i.getSerializableExtra(CAMERIERE);
        tavolo=(Tavolo)i.getSerializableExtra(TAVOLO);
    }
    public void startActivityServizi(View v) {
        Intent activityServizi = new Intent(ScegliCategoria.this, InserisciServizi.class);
        activityServizi.putExtra(CAMERIERE,cameriere);
        activityServizi.putExtra(TAVOLO,tavolo);
        startActivity(activityServizi);
    }
}
