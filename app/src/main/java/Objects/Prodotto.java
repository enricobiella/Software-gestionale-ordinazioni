package Objects;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by enrico on 04/01/2017.
 */
public class Prodotto implements Serializable,Comparable<Prodotto>{
    private int codice;
    private String descrizione;
    private int quantità;
    private ArrayList<Aggiunta> aggiunte;
    public Prodotto(int codice,String descrizione){
        this.codice=codice;
        this.descrizione=descrizione;
        quantità=0;
        aggiunte=new ArrayList<>(6);
    }

    public void setQuantità(int quantità) {
        this.quantità = quantità;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public int getCodice() {
        return codice;
    }

    public int getQuantità() {
        return quantità;
    }


    @Override
    public boolean equals(Object o) {
        Prodotto p = (Prodotto) o;
        if (o == null) return false;
        if (p.getCodice() == this.getCodice() && p.getDescrizione().equals(this.getDescrizione()) && p.getQuantità() == this.getQuantità() && p.getAggiunte().equals(this.getAggiunte())) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        if(quantità==0){
            return descrizione;
        }else {
            return quantità+"x  "+descrizione;
        }
    }

    public ArrayList<Aggiunta> getAggiunte() {
        Collections.sort(aggiunte);
        return aggiunte;
    }

    public boolean addAggiunta(Aggiunta aggiunta) {
        if(aggiunte.size()<7){
            this.aggiunte.add(aggiunta);
            return true;
        }else{
            return false;
        }

    }

    @Override
    public int compareTo(Prodotto p2) {
        int n3=this.getDescrizione().compareTo(p2.getDescrizione());
         return n3;

    }

    public boolean rimuoviAggiunta(Aggiunta aggiunta) {
            if(aggiunte.contains(aggiunta)){
                aggiunte.remove(aggiunta);
                return true;
            }else{
                return false;
            }
    }
}
