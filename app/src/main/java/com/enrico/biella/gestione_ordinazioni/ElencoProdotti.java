package com.enrico.biella.gestione_ordinazioni;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.internal.view.ContextThemeWrapper;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.BounceInterpolator;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;

import java.util.ArrayList;
import java.util.Collections;

import Objects.Aggiunta;
import Objects.Cameriere;
import Objects.ConnessioneDB;
import Objects.Prodotto;
import Objects.Tavolo;

public class ElencoProdotti extends AppCompatActivity {

    private static final String TAVOLO_GIA_ESISTENTE = "tavolo_gia_esistente";
    private static boolean tavoloGiaEsistente;
    private static final String CAMERIERE = "cameriere";
    private static final String TAVOLO = "tavolo";
    private static final int REQUEST_MODIFICA_PRODOTTO = 2;
    private static final String START_FROM = "start_from";
    private static final int REQUEST_ACTIVITY_PRODOTTO = 1;
    private static final int REQUEST_MODIFICA_PRODOTTO_SCEGLI_PRODOTTO = 11;
    private static final String PRODOTTO = "prodotto";
    private Cameriere cameriere;
    private Tavolo tavolo;
    private CoordinatorLayout cl;
    private SwipeMenuListView lista;
    private ArrayList<Prodotto> elencoProdotti;
    private ArrayAdapter<Prodotto> itemsAdapter;
    private Menu menu;
    private String ActivityElencoProdotti;
    private long mLastClickTime;
    private ProgressDialog p;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_elenco_prodotti);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setCameriereTavolo();
        binding();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.tavolo)+" "+tavolo.getNomeTavolo());
        getSupportActionBar().setSubtitle("Servizi"+": "+tavolo.getServizi()+"x");
        setupList();
        refreshLista();
    }


    private int dp2px(int dp) {
        float scale = getResources().getDisplayMetrics().density;
        int pixels = (int) (dp * scale + 0.5f);
        return pixels;
    }
    private void setupList() {
        SwipeMenuCreator creator = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {
                // create "delete" item
                SwipeMenuItem deleteItem = new SwipeMenuItem(
                        getApplicationContext());
                // set item background
                deleteItem.setBackground(R.color.red);
                // set item width
                deleteItem.setWidth(dp2px(90));
                // set a icon
                deleteItem.setIcon(R.drawable.ic_delete_white_18dp);
                // add to menu
                menu.addMenuItem(deleteItem);
            }
        };

// set creator
        lista.setMenuCreator(creator);
        lista.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener(){
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                switch (index) {
                    case 0:
                        if(tavolo.getElenco_prodotti().get(position).isEliminabile()){
                            tavolo.rimuoviProdotto(tavolo.getElenco_prodotti().get(position));
                            refreshLista();
                            break;
                        }else{
                            Snackbar.make(cl,getApplicationContext().getResources().getText(R.string.non_puoi_eliminare),Snackbar.LENGTH_LONG).show();
                        }

                }
                // false : close the menu; true : not close the menu
                return false;
            }
        });
        // Close Interpolator
        lista.setCloseInterpolator(new BounceInterpolator());
// Open Interpolator
        lista.setOpenInterpolator(new BounceInterpolator());
        lista.setSwipeDirection(SwipeMenuListView.DIRECTION_LEFT);
    }

    public void startActivityScegliCategoria(View v) {
        Intent nuovaPaginaScegliCategoria = new Intent(ElencoProdotti.this, ScegliCategoria.class);
        nuovaPaginaScegliCategoria.putExtra(CAMERIERE,cameriere);
        nuovaPaginaScegliCategoria.putExtra(TAVOLO,tavolo);
        startActivity(nuovaPaginaScegliCategoria);
        finish();
    }
    private void binding() {
        lista=(SwipeMenuListView)findViewById(R.id.listViewElencoProdotti);
        cl = (CoordinatorLayout) findViewById(R.id.coordinator_layout_elenco_prodotti);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_elenco_prodotti, menu);
        this.menu=menu;
        return true;
    }


    private void refreshLista() {
        elencoProdotti=tavolo.getElenco_prodotti();
        Collections.sort(tavolo.getElenco_prodotti());
        itemsAdapter=new ArrayAdapter<Prodotto>(this, R.layout.my_expandable_list,elencoProdotti);
        lista.setAdapter(itemsAdapter);
        setupListViewListener();
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

    private void setupListViewListener() {
        lista.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long l) {
                Prodotto p=((Prodotto)(parent.getAdapter().getItem(position)));
                if(p.isEliminabile()){
                    //TODO: aggiungere il pallino.
                    ((Prodotto)(parent.getAdapter().getItem(position))).addAggiunta(new Aggiunta(getString(R.string.pallino),"con"));
                    refreshLista();
                }
                return true;
            }
        });
        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(((Prodotto)(parent.getAdapter().getItem(position))).isEliminabile()){
                    startActivityForResultModificaProdotto(((Prodotto)(parent.getAdapter().getItem(position))),position);
                }
            }
        });
    }
    public void startActivityForResultModificaProdotto(Prodotto p,int pos) {
        Intent nuovaPaginaActivityProdotto = new Intent(ElencoProdotti.this, ModificaProdotto.class);
        nuovaPaginaActivityProdotto.putExtra(TAVOLO, tavolo);
        nuovaPaginaActivityProdotto.putExtra(PRODOTTO, p);
        nuovaPaginaActivityProdotto.putExtra("posizione", pos);
        nuovaPaginaActivityProdotto.putExtra(CAMERIERE, cameriere);
        nuovaPaginaActivityProdotto.putExtra(START_FROM, ActivityElencoProdotti);
        startActivityForResult(nuovaPaginaActivityProdotto, REQUEST_MODIFICA_PRODOTTO);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case android.R.id.home:
                //startActivityInserisciTavolo();
                onBackPressed();
                return true;
            case R.id.menu_done:
                creaDialogInviaComanda();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        //startActivityInserisciTavolo();
        creaDialogBack();
    }

    private void creaDialogInviaComanda() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.AlertDialogCustom));
        alertDialogBuilder
                .setCancelable(false)
                .setTitle(R.string.title_dialog_invia_comanda)
                .setMessage(R.string.messaggio_dialog_invia_comanda)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        inviaComanda();
                        //onBackPressed();
                        startActivityInserisciTavolo();
                    }
                })
                .setNegativeButton(R.string.indietro,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

        AlertDialog alertD = alertDialogBuilder.create();
        alertD.show();
    }
    private void creaDialogBack() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.AlertDialogCustom));
        alertDialogBuilder
                .setCancelable(false)
                .setTitle(R.string.title_dialog)
                .setMessage(R.string.messaggio_dialog)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        ElencoProdotti.super.onBackPressed();
                        startActivityInserisciTavolo();
                    }
                })
                .setNegativeButton(R.string.indietro,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

        AlertDialog alertD = alertDialogBuilder.create();
        alertD.show();
    }
    private void dismissProgressDialog() {
        if (p != null && p.isShowing()) {
            p.dismiss();
        }
    }

    @Override
    protected void onDestroy() {
        dismissProgressDialog();
        super.onDestroy();
    }
    private void inviaComanda() {
        ConnessioneDB task=new ConnessioneDB(cl,ElencoProdotti.this,tavolo,ConnessioneDB.UPLOAD,p);
        task.execute();
    }
    public void startActivityInserisciTavolo() {
        Intent nuovaActivityInserisciTavolo = new Intent(ElencoProdotti.this, InserisciTavolo.class);
        nuovaActivityInserisciTavolo.putExtra(CAMERIERE,cameriere);
        startActivity(nuovaActivityInserisciTavolo);
        finish();
    }
    private void setCameriereTavolo() {
        ActivityElencoProdotti="ActivityElencoProdotti";
        Intent i = getIntent();
        mLastClickTime=0;
        //cameriere= (Cameriere) i.getSerializableExtra(CAMERIERE);
        tavoloGiaEsistente=(boolean)i.getBooleanExtra(TAVOLO_GIA_ESISTENTE,false);
        tavolo=(Tavolo)i.getSerializableExtra(TAVOLO);
        cameriere=tavolo.getCameriere();
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_MODIFICA_PRODOTTO && resultCode == RESULT_OK) {
            tavolo=(Tavolo) data.getExtras().getSerializable(TAVOLO);
            refreshLista();
            //Snackbar.make(cl,getResources().getText(R.string.prodotto_modificato_rimosso),Snackbar.LENGTH_SHORT).show();
        }else if (requestCode == REQUEST_ACTIVITY_PRODOTTO && resultCode == RESULT_OK) {
            tavolo=(Tavolo) data.getExtras().getSerializable(TAVOLO);
            refreshLista();
            //Snackbar.make(cl,getResources().getText(R.string.prodotto_modificato_rimosso),Snackbar.LENGTH_SHORT).show();
        }
        else{
            //Snackbar.make(cl,getResources().getText(R.string.prodotto_non_modificato),Snackbar.LENGTH_SHORT).show();
        }
    }
}
