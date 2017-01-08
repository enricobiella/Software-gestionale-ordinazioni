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
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.util.concurrent.Callable;

import jcifs.smb.NtlmPasswordAuthentication;
import jcifs.smb.SmbFile;
import jcifs.smb.SmbFileInputStream;

/**
 * Created by enrico on 05/01/2017.
 */

public class DownloadDB extends AsyncTask<Void, Void, Boolean>
{
    private static final String ip = "192.168.1.25";
    private static final String username="enrico";
    private static final String password="Vivainter1";
    private static final String ENCODING = "windows-1252";
    private static String ERRORE = "";
    private static final String CONNESSIONE = "connessione";
    private static final String LETTURA = "lettura";
    private Context ctx;
    private ProgressDialog p;
    private SQLiteDatabase mydatabase;
    private File file;
    private boolean aggiorna;
    private CoordinatorLayout cl;
    Callable<Integer> refreshLista;
    Callable<Integer> downloadDB;
    /*
         Constructor
     */
    public DownloadDB(SQLiteDatabase mydatabase, boolean aggiorna, CoordinatorLayout cl, Context ctx, Callable<Integer> refreshLista, Callable<Integer> downloadDB)
    {
        this.mydatabase=mydatabase;
        this.aggiorna=aggiorna;
        this.cl=cl;
        this.ctx=ctx;
        this.refreshLista=refreshLista;
        this.downloadDB=downloadDB;
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
        mydatabase.execSQL("DROP TABLE IF EXISTS camerieri;");
        mydatabase.execSQL("DROP TABLE IF EXISTS prodotti;");
        mydatabase.execSQL("DROP TABLE IF EXISTS aggiunte;");
        mydatabase.execSQL("CREATE TABLE IF NOT EXISTS camerieri(codice mediumint(5) NOT NULL,nome VARCHAR(30));");
        mydatabase.execSQL("CREATE TABLE IF NOT EXISTS prodotti(codice mediumint(5) NOT NULL,descrizione VARCHAR(30), bottone VARCHAR(30));");
        mydatabase.execSQL("CREATE TABLE IF NOT EXISTS aggiunte(descrizione VARCHAR(30) NOT NULL,consenza VARCHAR(30));");
        try {
            SmbFile file=getFileFromServer();
            BufferedReader br =new BufferedReader(new InputStreamReader(new SmbFileInputStream(file),ENCODING));
            String line;
            while ((line = br.readLine()) != null) {
                mydatabase.execSQL(line);
            }
            br.close();
            return true;
        } catch (IOException e) {
            ERRORE=CONNESSIONE;
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
                case CONNESSIONE:
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
    }

    private SmbFile getFileFromServer() throws MalformedURLException {
        String user = username + ":" + password;
        String url = "smb://"+ip+"/Dati/archivi.dat";
        SmbFile file = null;
        NtlmPasswordAuthentication auth = new NtlmPasswordAuthentication(user);
        file = new SmbFile(url, auth);
        return file;
    }
}