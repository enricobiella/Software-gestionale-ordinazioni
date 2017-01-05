package Objects;

import android.app.ProgressDialog;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.enrico.biella.gestione_ordinazioni.Camerieri;
import com.enrico.biella.gestione_ordinazioni.R;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.Callable;

/**
 * Created by enrico on 05/01/2017.
 */

public class DownloadDB extends AsyncTask<Void, Void, Boolean>
{
    private static final String ENCODING = "windows-1252";
    private Context ctx;
    private String TAG="DownloadDB.java";
    //private String url;
    private ProgressDialog p;
    private SQLiteDatabase mydatabase;
    private File file;
    private boolean aggiorna;
    Callable<Integer> refreshLista;


    /*
         Constructor
     */
    public DownloadDB(File file, SQLiteDatabase mydatabase, boolean aggiorna, Context ctx, Callable<Integer> refreshLista)
    {
        this.refreshLista=refreshLista;
        Log.v(TAG, "Url Passed");
        this.file=file;
        this.ctx=ctx;
        this.aggiorna=aggiorna;
        this.mydatabase=mydatabase;
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
        //mydatabase.execSQL("DROP DATABASE IF EXISTS DB.client;");
        //mydatabase.execSQL("CREATE DATABASE IF NOT EXISTS DB.client;");
        mydatabase.execSQL("DROP TABLE IF EXISTS camerieri;");
        mydatabase.execSQL("DROP TABLE IF EXISTS prodotti;");
        mydatabase.execSQL("DROP TABLE IF EXISTS aggiunte;");
        mydatabase.execSQL("CREATE TABLE IF NOT EXISTS camerieri(codice mediumint(5) NOT NULL,nome VARCHAR(30));");
        mydatabase.execSQL("CREATE TABLE IF NOT EXISTS prodotti(codice mediumint(5) NOT NULL,descrizione VARCHAR(30), bottone VARCHAR(30));");
        mydatabase.execSQL("CREATE TABLE IF NOT EXISTS aggiunte(descrizione VARCHAR(30) NOT NULL,consenza VARCHAR(30));");
        File sdcard = Environment.getExternalStorageDirectory();
        File file = new File(sdcard, "Download//archivi.dat");
        try {
            BufferedReader br =new BufferedReader(new InputStreamReader(new FileInputStream(file),ENCODING));
            //BufferedReader br = new BufferedReader(new FileReader(file));
            String line;

            while ((line = br.readLine()) != null) {
                mydatabase.execSQL(line);
            }
            br.close();
            return true;
        } catch (IOException e) {
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
            Toast.makeText(ctx, "elementi inseriti", Toast.LENGTH_SHORT).show();
        }
        else
        {
            Toast.makeText(ctx, "qualcosa è andato storto", Toast.LENGTH_SHORT).show();
        }
    }
}