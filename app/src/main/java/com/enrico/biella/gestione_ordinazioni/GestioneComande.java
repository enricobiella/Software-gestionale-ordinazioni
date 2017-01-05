package com.enrico.biella.gestione_ordinazioni;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import Objects.Cameriere;

public class GestioneComande extends AppCompatActivity {

    public static String CAMERIERE = "cameriere";
    private Cameriere cameriere;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gestione_comande);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setCameriere();
        getSupportActionBar().setTitle(cameriere.getNome());

    }

    private void setCameriere() {
        Intent i = getIntent();
        cameriere= (Cameriere) i.getSerializableExtra(CAMERIERE);
    }

    public void startActivityInserisciTavolo(View v) {
        Intent nuovaActivityInserisciTavolo = new Intent(GestioneComande.this, InserisciTavolo.class);
        nuovaActivityInserisciTavolo.putExtra(CAMERIERE,cameriere);
        startActivity(nuovaActivityInserisciTavolo);
        finish();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            startActivityCamerieri();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        startActivityCamerieri();
    }

    private void startActivityCamerieri() {
        Intent nuovaActivityCameriere =new Intent(GestioneComande.this, Camerieri.class);
        startActivity(nuovaActivityCameriere);
        finish();
    }
}
