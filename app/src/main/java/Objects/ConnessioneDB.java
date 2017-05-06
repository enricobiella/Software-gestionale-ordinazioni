package Objects;

import android.app.ProgressDialog;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Build;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;

import com.enrico.biella.gestione_ordinazioni.Camerieri;
import com.enrico.biella.gestione_ordinazioni.InserisciTavolo;
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
 * Created by Enrico Biella on 05/01/2017.
 */

public class ConnessioneDB extends AsyncTask<Void, Void, Boolean>
{
    //170380
    //TODO: modificare l'ip! - 192.168.2.96
    private static final String ip = "172.17.2.126";
    //TODO: modificare l'utente: echo %username% - ranch
    private static final String username="enrico";
    //TODO: modificare la password - pluto
    private static final String password="Vivainter1";
    private static final String ENCODING = "windows-1252";
    private static final String ERRORE_FILE_NOT_EXISTS = "file_not_exists";
    public static final int DOWNLOAD_ARCHIVI = 0;
    public static final int DOWNLOAD_TAVOLI = 2;
    public static final int CONTROLLO_TAVOLI = 3;
    public static final int UPLOAD = 1;
    private static final String END_OF_FILE = "END OF FILE";
    private static String ERRORE = "";
    private static final String ERRORE_CONNESSIONE = "connessione";
    Callable<Integer> eseguiActivity;
    private ArrayList<Tavolo> elencoTavoli;
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
    public ConnessioneDB(SQLiteDatabase mydatabase, CoordinatorLayout cl, Context ctx, Callable<Integer> refreshLista,
                         Callable<Integer> downloadDB, int downloadUpload, ProgressDialog p)
    {
        this.mydatabase=mydatabase;
        this.cl=cl;
        this.ctx=ctx;
        this.p=p;
        this.refreshLista=refreshLista;
        this.downloadDB=downloadDB;
        this.downloadUpload=downloadUpload;
    }
    public ConnessioneDB(ArrayList<Tavolo> elencoTavoli,CoordinatorLayout cl, Context ctx, Callable<Integer> refreshLista,
                         Callable<Integer> downloadDB, int downloadUpload,ProgressDialog p)
    {
        this.p=p;
        this.elencoTavoli=elencoTavoli;
        this.cl=cl;
        this.ctx=ctx;
        this.refreshLista=refreshLista;
        this.downloadDB=downloadDB;
        this.downloadUpload=downloadUpload;
    }
    public ConnessioneDB(Context ctx,Tavolo tavolo, int downloadUpload, Callable<Integer> eseguiActivity,ProgressDialog p)
    {
        this.p=p;
        this.eseguiActivity=eseguiActivity;
        this.ctx=ctx;
        this.tavolo=tavolo;
        this.downloadUpload=downloadUpload;
    }
    public ConnessioneDB(CoordinatorLayout cl, Context ctx,Tavolo tavolo,int downloadUpload,ProgressDialog p){
        this.cl=cl;
        this.p=p;
        this.ctx=ctx;
        this.tavolo=tavolo;
        this.downloadUpload=downloadUpload;
    }

    /*
        Runs on the UI thread before doInBackground
     */
    @Override
    protected void onPreExecute() {
        if (p == null) {
            if (android.os.Build.VERSION.SDK_INT == Build.VERSION_CODES.JELLY_BEAN_MR1){
                this.p=new ProgressDialog(ctx,R.style.AppTheme_MyDialog);
            } else{
                this.p=new ProgressDialog(ctx);
            }
            p.setMessage(ctx.getResources().getString(R.string.download_db));
            p.setIndeterminate(false);
            p.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            p.setCancelable(false);
        }
        p.show();
    }

    /*
         This method to perform a computation on a background thread.
     */
    @Override
    protected Boolean doInBackground(Void... voids) {
        switch (downloadUpload){
            case ConnessioneDB.DOWNLOAD_ARCHIVI:
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
                    Log.d("TAG",e.getMessage());
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
            case ConnessioneDB.DOWNLOAD_TAVOLI:
                try {
                    SmbFile file = getTavoliFromServer();
                    SmbFile[] files=file.listFiles();
                    String line;
                    for(SmbFile f:files){
                        BufferedReader br = new BufferedReader(new InputStreamReader(new SmbFileInputStream(f), ENCODING));
                        String[] h=f.getName().toUpperCase().split(" ");
                        Tavolo t=new Tavolo(h[2].replace(".DAT",""));
                        t.setCameriere(new Cameriere(h[1]));
                        int i=0;
                        while (((line = br.readLine()) != null)&&!line.equals(END_OF_FILE)) {
                            if(i==0){
                                String[] s=line.split(";");
                                t.setServizi(Integer.valueOf(s[1]));
                                //TODO: riparti da qui
                            }else{
                                String[] s=line.split(";");
                                Prodotto p=new Prodotto(s[0],s[2]);
                                p.setQuantità(Integer.valueOf(s[1]));
                                /*int k=6;
                                while(s.length>5&&k<s.length){
                                    String[] o=s[k].split(" ");
                                    p.addAggiunta(new Aggiunta(o[1],o[0]));
                                    k++;
                                }*/
                                t.inserisciProdotto(p);
                            }
                            i++;
                        }
                        elencoTavoli.add(t);
                        br.close();
                    }
                    return true;

                } catch (Exception e) {
                    Log.d("TAG",e.getMessage());
                    ERRORE = ERRORE_CONNESSIONE;
                    return false;
                }
            case ConnessioneDB.CONTROLLO_TAVOLI:
                try {
                    SmbFile file = getTavoliFromServer();
                    SmbFile[] files=file.listFiles();
                    ArrayList<String>nomiTavolo=new ArrayList<>();
                    String line;
                    for(SmbFile f:files){
                        BufferedReader br = new BufferedReader(new InputStreamReader(new SmbFileInputStream(f), ENCODING));
                        String[] h=f.getName().toUpperCase().split(" ");
                        nomiTavolo.add(h[2].replace(".DAT",""));
                        br.close();

                    }
                    if(nomiTavolo.contains(tavolo.getNomeTavolo())){// il nome del tavolo non esiste

                        int p=nomiTavolo.indexOf(tavolo.getNomeTavolo());
                        SmbFile f=files[p];
                        BufferedReader br = new BufferedReader(new InputStreamReader(new SmbFileInputStream(f), ENCODING));
                        String[] h=f.getName().toUpperCase().split(" ");
                        Tavolo t=new Tavolo(h[2].replace(".DAT",""));
                        t.setCameriere(new Cameriere(h[1]));
                        int i=0;
                        while (((line = br.readLine()) != null)&&!line.equals(END_OF_FILE)) {
                            if(i==0){
                                String[] s=line.split(";");
                                t.setServizi(Integer.valueOf(s[1]));
                            }else{
                                /*String[] s=line.split(";");
                                Prodotto pr=new Prodotto(s[0],s[5]);
                                pr.setQuantità(Integer.valueOf(s[1]));
                                int k=6;
                                while(s.length>5 && k<s.length){
                                    String[] o=s[k].split(" ");
                                    pr.addAggiunta(new Aggiunta(o[1],o[0]));
                                    k++;
                                }*/
                                String[] s=line.split(";");
                                Prodotto pr=new Prodotto(s[0],s[2]);
                                pr.setQuantità(Integer.valueOf(s[1]));
                                int k=6;
                                while(s.length>5&&k<s.length){
                                    String[] o=s[k].split(" ");
                                    pr.addAggiunta(new Aggiunta(o[1],o[0]));
                                    k++;
                                }
                                t.inserisciProdotto(pr);
                            }
                            i++;
                        }
                        tavolo=t;
                        br.close();
                        return true;
                    }
                    return false;

                } catch (Exception e) {
                    Log.d("TAG",e.getMessage());
                    ERRORE = ERRORE_CONNESSIONE;
                    return false;
                }
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
        if (p != null && p.isShowing()) {
            p.dismiss();
        }
        switch (downloadUpload){
            case ConnessioneDB.DOWNLOAD_ARCHIVI:
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
                break;
            case ConnessioneDB.DOWNLOAD_TAVOLI:
                if(result)
                {
                    try {
                        refreshLista.call();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    // Do something awesome here
                    Snackbar.make(cl,ctx.getResources().getText(R.string.elementi_inseriti),Snackbar.LENGTH_LONG).show();
                }else{
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
                break;
            case ConnessioneDB.UPLOAD:
                if(result){
                    Snackbar.make(cl,ctx.getResources().getText(R.string.comanda_inviata),Snackbar.LENGTH_LONG).show();
                }else{
                    Snackbar.make(cl,ctx.getResources().getText(R.string.comanda_non_inviata),Snackbar.LENGTH_LONG).show();
                }
                break;
            case ConnessioneDB.CONTROLLO_TAVOLI:
                if(result) {
                    try {
                        tavolo.setControlloComanda(true);
                        tavolo.bloccaTavolo();
                        InserisciTavolo.tavolo=tavolo;
                        eseguiActivity.call();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    // Do something awesome here
                }else{
                    try {
                        eseguiActivity.call();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;
        }
    }

    private SmbFile getFileFromServer() throws MalformedURLException {
        String user = username + ":" + password;
        //TODO: modificare il PATH
        String url = "smb://"+ip+"/Ranch/archivi.dat";
        SmbFile file;
        NtlmPasswordAuthentication auth = new NtlmPasswordAuthentication(user);
        file = new SmbFile(url, auth);
        return file;
    }

    private SmbFile CreateComanda() throws MalformedURLException {
        String user = username + ":" + password;
        //TODO: modificare il PATH
        String url = "smb://"+ip+"/Ranch/"+"COM"+tavolo.getNomeTavolo()+"24"+".txt";
        SmbFile file;
        NtlmPasswordAuthentication auth = new NtlmPasswordAuthentication(user);
        file = new SmbFile(url, auth);
        return file;
    }

    private SmbFile getTavoliFromServer() throws MalformedURLException {
        String user = username + ":" + password;
        //TODO: modificare il PATH
        String url = "smb://"+ip+"/Ranch/SYNCHRO/";
        SmbFile file;
        NtlmPasswordAuthentication auth = new NtlmPasswordAuthentication(user);
        file = new SmbFile(url, auth);
        return file;
    }

}