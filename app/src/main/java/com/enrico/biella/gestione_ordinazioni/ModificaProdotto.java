package com.enrico.biella.gestione_ordinazioni;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import Objects.Prodotto;
import Objects.Tavolo;

public class ModificaProdotto extends AppCompatActivity {

    private Prodotto prodotto;
    private Prodotto prodottoSalvato;
    private TextView schermo;
    private int contatore;
    private ImageButton clickPlus;
    private ImageButton clickRemove;
    private static final String PRODOTTO = "prodotto";
    private static final String TAVOLO = "tavolo";
    private Tavolo tavolo;
    private FloatingActionButton clickInserisciProdotto;
    private FloatingActionButton clickCancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modifica_prodotto);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getData();
        binding();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(prodotto.getDescrizione());
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            super.onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    private void getData() {
        Intent i = getIntent();
        prodotto=(Prodotto)i.getSerializableExtra(PRODOTTO);
        prodottoSalvato=prodotto;
        tavolo=(Tavolo)i.getSerializableExtra(TAVOLO);
        contatore=prodotto.getQuantità();
    }

    public void clickPlusDialog() {
        ++contatore;
        schermo.setText(String.valueOf(contatore));
    }

    public void clickMinusDialog() {
        if (contatore == 0) {
            schermo.setText(R.string.schermo);
            return;
        } else {
            --contatore;
            schermo.setText(String.valueOf(contatore));
        }
    }

    private void binding() {
        schermo = (TextView) findViewById(R.id.schermo1);
        schermo.setText(String.valueOf(contatore));
        clickPlus=(ImageButton)findViewById(R.id.clickPlus);
        clickRemove=(ImageButton)findViewById(R.id.clickRemove);
        clickCancel=(FloatingActionButton)findViewById(R.id.cancel);
        clickCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tavolo.rimuoviProdotto(prodottoSalvato);
                finishWithResult();
            }
        });
        clickInserisciProdotto=(FloatingActionButton)findViewById(R.id.inserisci);
        clickInserisciProdotto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(contatore>0) {
                    tavolo.rimuoviProdotto(prodottoSalvato);
                    tavolo.inserisciProdotto(getP());
                    finishWithResult();
                }
            }
        });
        clickPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickPlusDialog();
            }
        });
        clickRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickMinusDialog();
            }
        });
    }

    public Prodotto getP() {
        prodotto.setQuantità(contatore);
        return prodotto;
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void finishWithResult()
    {
        Intent intent = new Intent();
        intent.putExtra(TAVOLO, tavolo);
        setResult(RESULT_OK, intent);
        finish();
    }

}
