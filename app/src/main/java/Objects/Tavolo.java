package Objects;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by enrico on 31/12/2016.
 */
public class Tavolo implements Serializable{
    private String nomeTavolo;
    private int servizi;
    private ArrayList<Prodotto> elenco_prodotti;

    public Tavolo(String nomeTavolo){
        this.nomeTavolo=nomeTavolo;
        elenco_prodotti=new ArrayList<>(10);
        servizi=0;
    }

    public void inserisciProdotto(Prodotto p){
        elenco_prodotti.add(p);
    }
    public boolean rimuoviProdotto(Prodotto p){
        if(elenco_prodotti.contains(p)){
            elenco_prodotti.remove(p);
            return true;
        }else{
            return false;
        }
    }


    public ArrayList<Prodotto> getElenco_prodotti() {
        return elenco_prodotti;
    }

    public String getNomeTavolo() {
        return nomeTavolo;
    }

    public int getServizi() {
        return servizi;
    }

    public void setServizi(int servizi) {
        this.servizi = servizi;
    }
}
