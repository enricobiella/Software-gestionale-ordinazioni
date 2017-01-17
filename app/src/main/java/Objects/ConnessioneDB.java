package Objects;

import android.app.ProgressDialog;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.view.View;

import com.enrico.biella.gestione_ordinazioni.Camerieri;
import com.enrico.biella.gestione_ordinazioni.R;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.concurrent.Callable;

import jcifs.smb.NtlmPasswordAuthentication;
import jcifs.smb.SmbFile;
import jcifs.smb.SmbFileInputStream;
import jcifs.smb.SmbFileOutputStream;

/**
 * Created by SUPERUOMO_BIELLA_HASHMAP_WOOOO on 05/01/2017.
 */

public class ConnessioneDB extends AsyncTask<Void, Void, Boolean>
{
    private static final String ip = "172.17.2.126";

    private static final String username="enrico";
    private static final String password="Vivainter1";
    private static final String ENCODING = "windows-1252";
    private static final String ERRORE_FILE_NOT_EXISTS = "file_not_exists";
    public static final int DOWNLOAD = 0;
    public static final int UPLOAD = 1;
    private static String ERRORE = "";
    private static final String ERRORE_CONNESSIONE = "connessione";
    private Context ctx;
    private ProgressDialog p;
    private SQLiteDatabase mydatabase;
    private File file;
    private CoordinatorLayout cl;
    private int downloadUpload;
    private Tavolo tavolo;
    Callable<Integer> refreshLista;
    Callable<Integer> downloadDB;
    /*
         Constructor
     */
    public ConnessioneDB(SQLiteDatabase mydatabase, CoordinatorLayout cl, Context ctx, Callable<Integer> refreshLista, Callable<Integer> downloadDB, int downloadUpload)
    {
        this.mydatabase=mydatabase;
        this.cl=cl;
        this.ctx=ctx;
        this.refreshLista=refreshLista;
        this.downloadDB=downloadDB;
        this.downloadUpload=downloadUpload;
        this.p=new ProgressDialog(ctx);
    }

    public ConnessioneDB(CoordinatorLayout cl, Context ctx,Tavolo tavolo,int downloadUpload){
        this.cl=cl;
        this.ctx=ctx;
        this.tavolo=tavolo;
        this.downloadUpload=downloadUpload;
        this.p=new ProgressDialog(ctx);
    }
    /*
        Runs on the UI thread before doInBackground
     */
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        p.setMessage(ctx.getResources().getString(R.string.download_db));
        p.setIndeterminate(false);
        p.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        p.setCancelable(false);
        p.show();
    }
    /*
         This method to perform a computation on a background thread.
     */
    @Override
    protected Boolean doInBackground(Void... voids) {
        switch (downloadUpload){
            case ConnessioneDB.DOWNLOAD:
                mydatabase.execSQL("DROP TABLE IF EXISTS camerieri;");
                mydatabase.execSQL("DROP TABLE IF EXISTS prodotti;");
                mydatabase.execSQL("DROP TABLE IF EXISTS aggiunte;");
                mydatabase.execSQL("CREATE TABLE IF NOT EXISTS camerieri(codice mediumint(5) NOT NULL,nome VARCHAR(30));");
                mydatabase.execSQL("CREATE TABLE IF NOT EXISTS prodotti(codice mediumint(5) NOT NULL,descrizione VARCHAR(30), bottone VARCHAR(30));");
                mydatabase.execSQL("CREATE TABLE IF NOT EXISTS aggiunte(descrizione VARCHAR(30) NOT NULL,consenza VARCHAR(2));");
                try {
                    SmbFile file = getFileFromServer();
                    BufferedReader br = new BufferedReader(new InputStreamReader(new SmbFileInputStream(file), ENCODING));
                    String line;
                    while ((line = br.readLine()) != null) {
                        mydatabase.execSQL(line);
                    }
                    br.close();
                    return true;
                } catch (IOException e) {
                    ERRORE = ERRORE_CONNESSIONE;
                    return false;
                }

            case ConnessioneDB.UPLOAD:
                try {
                    SmbFile file=CreateComanda();
                    BufferedWriter bw=new BufferedWriter(new OutputStreamWriter(new SmbFileOutputStream(file),ENCODING));
                    ArrayList<String> lista=tavolo.toComanda();
                    for (String s:lista) {
                        bw.write(s);
                        bw.write("\r\n");
                    }
                    bw.close();
                    return true;
                } catch (Exception e){return false;}
            default:
                return false;
        }

    }

    /*
        Runs on the UI thread after doInBackground
     */
    @Override
    protected void onPostExecute(Boolean result) {
        super.onPostExecute(result);
        p.dismiss();
        switch (downloadUpload){
            case ConnessioneDB.DOWNLOAD:
                if(result)
                {
                    Camerieri.AGGIORNA=false;
                    try {
                        refreshLista.call();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    // Do something awesome here
                    Snackbar.make(cl,ctx.getResources().getText(R.string.elementi_inseriti),Snackbar.LENGTH_LONG).show();
                }
                else
                {
                    switch (ERRORE){
                        case ERRORE_CONNESSIONE:
                            Snackbar.make(cl,ctx.getResources().getText(R.string.errore_connesione),Snackbar.LENGTH_LONG)
                                    .setAction(ctx.getResources().getText(R.string.refresh), new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            try {
                                                downloadDB.call();
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    }).show();
                            break;
                        case ERRORE_FILE_NOT_EXISTS:
                            Snackbar.make(cl,ctx.getResources().getText(R.string.file_not_exists),Snackbar.LENGTH_LONG)
                                    .setAction(ctx.getResources().getText(R.string.refresh), new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            try {
                                                downloadDB.call();
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    }).show();
                        default:
                            Snackbar.make(cl,ctx.getResources().getText(R.string.elementi_non_inseriti),Snackbar.LENGTH_LONG)
                                    .setAction(ctx.getResources().getText(R.string.refresh), new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            try {
                                                downloadDB.call();
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    }).show();
                    }
                }

            case ConnessioneDB.UPLOAD:
                if(result){
                    Snackbar.make(cl,ctx.getResources().getText(R.string.elementi_inseriti),Snackbar.LENGTH_LONG).show();
                }else{
                    Snackbar.make(cl,ctx.getResources().getText(R.string.elementi_non_inseriti),Snackbar.LENGTH_LONG).show();
                }
        }

    }

    private SmbFile getFileFromServer() throws MalformedURLException {
        String user = username + ":" + password;
        String url = "smb://"+ip+"/Dati/archivi.dat";
        SmbFile file = null;
        NtlmPasswordAuthentication auth = new NtlmPasswordAuthentication(user);
        file = new SmbFile(url, auth);
        return file;
    }
    private SmbFile CreateComanda() throws MalformedURLException {
        String user = username + ":" + password;
        String url = "smb://"+ip+"/Dati/"+"comanda_"+tavolo.getNomeTavolo()+".txt";
        SmbFile file = null;
        NtlmPasswordAuthentication auth = new NtlmPasswordAuthentication(user);
        file = new SmbFile(url, auth);
        return file;
    }
}