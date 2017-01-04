package com.enrico.biella.gestione_ordinazioni;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

import Objects.Cameriere;
import Objects.Tavolo;

public class InserisciTavolo extends AppCompatActivity {

    private static final String CAMERIERE = "cameriere";
    private static final String TAVOLO = "tavolo";
    private Cameriere cameriere;
    private Tavolo tavolo;
    private ArrayList<String> array_parole;

    private TextView editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inserisci_tavolo);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        binding();
        setCameriere();
        getSupportActionBar().setTitle(cameriere.getNome());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void setCameriere() {
        Intent i = getIntent();
        cameriere= (Cameriere) i.getSerializableExtra(CAMERIERE);
    }
    private void binding() {
        editText=(TextView) findViewById(R.id.textView);
        array_parole=new ArrayList<>(5);
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
            //onBackPressed();
            startActivityGestioneComande();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    public void startActivityInserisciServizi(View v) {
        if(array_parole.size()>0) {
            tavolo=new Tavolo(String.valueOf(editText.getText()));
            Intent nuovaPaginaInserisciServizi = new Intent(InserisciTavolo.this, InserisciServizi.class);
            nuovaPaginaInserisciServizi.putExtra(CAMERIERE, cameriere);
            nuovaPaginaInserisciServizi.putExtra(TAVOLO, tavolo);
            startActivity(nuovaPaginaInserisciServizi);
            finish();
        }
    }

    public void clickButton(View v){
        if(editText.getText()==getString(R.string.numero_tavolo)) {
            editText.setText("");
            array_parole.add((String) ((Button) v).getText());
            for(int i=0;i<array_parole.size();++i){
                editText.append(array_parole.get(i));
            }
        }else{
            editText.setText("");
            array_parole.add((String) ((Button) v).getText());
            for(int i=0;i<array_parole.size();++i){
                editText.append(array_parole.get(i));
            }
        }
    }
    public void clickCancel(View v){
        if(array_parole.size()==0) {
            editText.setText(R.string.numero_tavolo);
            return;
        }else if (array_parole.size()==1) {
            editText.setText(R.string.numero_tavolo);
            array_parole.remove(array_parole.size() - 1);
            return;
        }else{
            editText.setText("");
            array_parole.remove(array_parole.size() - 1);
            for(int i=0;i<array_parole.size();++i){
                editText.append(array_parole.get(i));
            }
            return;
        }
    }
    public void startActivityGestioneComande() {
        Intent nuovaPaginaGestioneComande = new Intent(InserisciTavolo.this, GestioneComande.class);
        nuovaPaginaGestioneComande.putExtra(CAMERIERE,cameriere);
        startActivity(nuovaPaginaGestioneComande);
        finish();
    }

    @Override
    public void onBackPressed() {
        startActivityGestioneComande();
    }
}
