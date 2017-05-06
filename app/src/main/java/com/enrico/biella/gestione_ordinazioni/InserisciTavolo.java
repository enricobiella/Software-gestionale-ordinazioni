package com.enrico.biella.gestione_ordinazioni;

import android.app.ProgressDialog;
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
import java.util.concurrent.Callable;

import Objects.Cameriere;
import Objects.ConnessioneDB;
import Objects.Tavolo;

public class InserisciTavolo extends AppCompatActivity {

    private static final String CAMERIERE = "cameriere";
    private static final String TAVOLO = "tavolo";
    private static final String TAVOLO_GIA_ESISTENTE = "tavolo_gia_esistente";
    private Cameriere cameriere;
    public static Tavolo tavolo;
    private ArrayList<String> array_parole;
    private TextView editText;

    private Button zero;
    private Button one;
    private Button two;
    private Button three;
    private Button four;
    private Button five;
    private Button six;
    private Button seven;
    private Button eight;
    private Button nine;

    private Button cap;
    private Button b;
    private Button c;
    private Button p;
    private Button t;
    private Button v;
    private ProgressDialog progressDialog;

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
        setButtons(false);
    }

    private void setCameriere() {
        Intent i = getIntent();
        cameriere= (Cameriere) i.getSerializableExtra(CAMERIERE);
    }
    private void binding() {
        editText=(TextView) findViewById(R.id.textView);
        array_parole=new ArrayList<>(5);
        zero=(Button)findViewById(R.id.zero);
        one=(Button)findViewById(R.id.one);
        two=(Button)findViewById(R.id.two);
        three=(Button)findViewById(R.id.three);
        four=(Button)findViewById(R.id.four);
        five=(Button)findViewById(R.id.five);
        six=(Button)findViewById(R.id.six);
        seven=(Button)findViewById(R.id.seven);
        eight=(Button)findViewById(R.id.eight);
        nine=(Button)findViewById(R.id.nine);
        cap=(Button)findViewById(R.id.cap);
        b=(Button)findViewById(R.id.b);
        c=(Button)findViewById(R.id.c);
        p=(Button)findViewById(R.id.p);
        t=(Button)findViewById(R.id.t);
        v=(Button)findViewById(R.id.v);
    }

    private void setButtons(boolean b){
        zero.setEnabled(b);
        one.setEnabled(b);
        two.setEnabled(b);
        three.setEnabled(b);
        four.setEnabled(b);
        five.setEnabled(b);
        six.setEnabled(b);
        seven.setEnabled(b);
        eight.setEnabled(b);
        nine.setEnabled(b);
        cap.setEnabled(!b);
        this.b.setEnabled(!b);
        c.setEnabled(!b);
        p.setEnabled(!b);
        t.setEnabled(!b);
        v.setEnabled(!b);

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_inserisci_tavolo, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            //onBackPressed();
            startActivityCamerieri();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    public void startActivityInserisciServizi(View v) {
        if(array_parole.size()>0) {
            tavolo=new Tavolo(String.valueOf(editText.getText()));
            controlloComanda();
        }
    }

    private void eseguiActivity() {
        if(tavolo.getControlloComanda()){
            Intent nuovaPaginaElencoProdotti = new Intent(InserisciTavolo.this, ElencoProdotti.class);
            //nuovaPaginaElencoProdotti.putExtra(CAMERIERE,cameriere);
            tavolo.setCameriere(cameriere);
            nuovaPaginaElencoProdotti.putExtra(TAVOLO_GIA_ESISTENTE,true);
            nuovaPaginaElencoProdotti.putExtra(TAVOLO,tavolo);
            startActivity(nuovaPaginaElencoProdotti);
            finish();
        }else{
            //tavolo=new Tavolo(String.valueOf(editText.getText()));
            tavolo.setCameriere(cameriere);
            Intent nuovaPaginaInserisciServizi = new Intent(InserisciTavolo.this, InserisciServizi.class);
            nuovaPaginaInserisciServizi.putExtra(CAMERIERE, cameriere);
            nuovaPaginaInserisciServizi.putExtra(TAVOLO, tavolo);
            startActivity(nuovaPaginaInserisciServizi);
            finish();
        }
    }
    private void dismissProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    @Override
    protected void onDestroy() {
        dismissProgressDialog();
        super.onDestroy();
    }

    private  void controlloComanda() {
        ConnessioneDB task = new ConnessioneDB(InserisciTavolo.this, tavolo, ConnessioneDB.CONTROLLO_TAVOLI, new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                eseguiActivity();
                return null;
            }
        },progressDialog);
        task.execute();
    }

    public void clickButton(View v){
        if(editText.getText().equals("")) {
            setButtons(true);
            editText.setText("");
            array_parole.add((String) ((Button) v).getText());
            for(int i=0;i<array_parole.size();++i){
                editText.append(array_parole.get(i));
            }
        }else{
            setButtons(true);
            editText.setText("");
            array_parole.add((String) ((Button) v).getText());
            for(int i=0;i<array_parole.size();++i){
                editText.append(array_parole.get(i));
            }
        }
    }
    public void clickCancel(View v){
        if(array_parole.size()==0) {
            setButtons(false);
            editText.setText("");
            return;
        }else if (array_parole.size()==1) {
            setButtons(false);
            editText.setText("");
            array_parole.remove(array_parole.size() - 1);
            return;
        }else{
            setButtons(true);
            editText.setText("");
            array_parole.remove(array_parole.size() - 1);
            for(int i=0;i<array_parole.size();++i){
                editText.append(array_parole.get(i));
            }
            return;
        }
    }
    private void startActivityCamerieri() {
        Intent nuovaActivityCameriere =new Intent(InserisciTavolo.this, Camerieri.class);
        startActivity(nuovaActivityCameriere);
        finish();
    }

    public void clickElencoTavoli(View v){
        startActivityElencoTavoli();
    }

    private void startActivityElencoTavoli() {
        Intent nuovaActivityElencoTavoli= new Intent(InserisciTavolo.this,ElencoTavoli.class);
        nuovaActivityElencoTavoli.putExtra(CAMERIERE, cameriere);
        startActivity(nuovaActivityElencoTavoli);
        finish();
    }

    @Override
    public void onBackPressed() {
        startActivityCamerieri();
    }
}
