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

public class ActivityProdotto extends AppCompatActivity {
    private Prodotto prodotto;
    private TextView schermo;
    private int contatore;
    private ImageButton clickPlus;
    private ImageButton clickRemove;
    private static final String PRODOTTO = "prodotto";
    private FloatingActionButton clickInserisciProdotto;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity_prodotto);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        binding();
        getData();
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
        clickInserisciProdotto=(FloatingActionButton)findViewById(R.id.clickInserisciProdotto);
        clickInserisciProdotto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(contatore>0) {
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
        if(contatore>0) {
            finishWithResult();
        }
        super.onBackPressed();
    }

    private void finishWithResult()
    {
        Intent intent = new Intent();
        intent.putExtra(PRODOTTO, getP());
        setResult(RESULT_OK, intent);
        finish();
    }

}
