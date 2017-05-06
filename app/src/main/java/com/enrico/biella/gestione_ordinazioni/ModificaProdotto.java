package com.enrico.biella.gestione_ordinazioni;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.Collections;

import Objects.Cameriere;
import Objects.Prodotto;
import Objects.Tavolo;

public class ModificaProdotto extends AppCompatActivity {

    private static final int REQUEST_ELENCO_MODIFICHE = 3;
    private static String CAMERIERE = "cameriere";
    private Cameriere cameriere;
    private Prodotto prodotto;
    private Prodotto prodottoSalvato;
    private TextView schermo;
    private int contatore;
    private ImageButton clickPlus;
    private ImageButton clickRemove;
    private static final String PRODOTTO = "prodotto";
    private static final String TAVOLO = "tavolo";
    private static final String START_FROM = "start_from";
    private Tavolo tavolo;
    private FloatingActionButton clickInserisciProdotto;
    private FloatingActionButton clickModify;
    private CoordinatorLayout cl;
    private String activity;
    private int posizione;

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
        activity = (String) i.getSerializableExtra(START_FROM);
        prodottoSalvato = (Prodotto) i.getSerializableExtra(PRODOTTO);
        posizione = i.getIntExtra("posizione", 0);
        cameriere = (Cameriere) i.getSerializableExtra(CAMERIERE);
        prodotto = prodottoSalvato;
        tavolo = (Tavolo) i.getSerializableExtra(TAVOLO);
        contatore = prodottoSalvato.getQuantità();
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
        cl = (CoordinatorLayout) findViewById(R.id.coordinator_layout_modifica_prodotto);
        schermo = (TextView) findViewById(R.id.schermo1);
        schermo.setText(String.valueOf(contatore));
        clickPlus = (ImageButton) findViewById(R.id.clickPlus);
        clickRemove = (ImageButton) findViewById(R.id.clickRemove);
        clickModify = (FloatingActionButton) findViewById(R.id.modify);
        clickModify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResultElencoModifiche();
            }
        });
        clickInserisciProdotto = (FloatingActionButton) findViewById(R.id.inserisci);
        clickInserisciProdotto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (contatore > 0) {
                    if (activity.equals("ActivityElencoProdotti")) {
                        tavolo.getElenco_prodotti().remove(posizione);
                    }

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

    public void startActivityForResultElencoModifiche() {
        Intent nuovaPaginaElencoModifiche = new Intent(ModificaProdotto.this, ElencoModifiche.class);
        nuovaPaginaElencoModifiche.putExtra(PRODOTTO, prodotto);
        startActivityForResult(nuovaPaginaElencoModifiche, REQUEST_ELENCO_MODIFICHE);
    }

    private void finishWithResult() {
        Intent intent = new Intent();
        switch (activity) {
            case "ActivityScegliProdotto":
                Collections.sort(tavolo.getElenco_prodotti());
                sistemaDoppioni();
                startActivityElencoProdotti();
                finish();
                break;
            case "ActivityElencoProdotti":
                intent.putExtra(TAVOLO, tavolo);
                setResult(RESULT_OK, intent);
                finish();
                break;
        }

    }

    private void sistemaDoppioni() {
        if (tavolo.getElenco_prodotti().size()>1) {
            Prodotto p1;
            Prodotto p2;
            for (int n = 0; n < tavolo.getElenco_prodotti().size()-1; n++) {
                p1=tavolo.getElenco_prodotti().get(n);
                p2=tavolo.getElenco_prodotti().get(n+1);
                if(p1.myIsEquals(p2)){
                    p1.setQuantità(p1.getQuantità()+p2.getQuantità());
                    tavolo.getElenco_prodotti().remove(n+1);
                    break;
                }
            }
        }
    }

    public void startActivityElencoProdotti() {
        Intent nuovaPaginaElencoProdotti = new Intent(ModificaProdotto.this, ElencoProdotti.class);
        nuovaPaginaElencoProdotti.putExtra(CAMERIERE, cameriere);
        nuovaPaginaElencoProdotti.putExtra(TAVOLO, tavolo);
        startActivity(nuovaPaginaElencoProdotti);
        ScegliProdotto.ScegliProdotto.finish();
        ScegliCategoria.ScegliCategoria.finish();
        finish();
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_ELENCO_MODIFICHE && resultCode == RESULT_OK) {
            prodotto = (Prodotto) data.getExtras().getSerializable(PRODOTTO);
            //Snackbar.make(cl,getResources().getText(R.string.prodotto_modificato_rimosso),Snackbar.LENGTH_SHORT).show();
        } else {
            //Snackbar.make(cl,getResources().getText(R.string.prodotto_non_modificato),Snackbar.LENGTH_SHORT).show();
        }
    }
}
