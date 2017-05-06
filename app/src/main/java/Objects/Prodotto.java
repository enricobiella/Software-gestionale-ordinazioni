package Objects;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by enrico on 04/01/2017.
 */
public class Prodotto implements Serializable, Comparable<Prodotto> {
    private String codice;
    private String descrizione;
    private int quantità;
    private boolean eliminabile; // se è true è eliminabile, altrimenti è false
    private ArrayList<Aggiunta> aggiunte;

    public Prodotto(String codice, String descrizione) {
        this.codice = codice;
        eliminabile = true;
        this.descrizione = descrizione;
        quantità = 0;
        aggiunte = new ArrayList<>(6);
    }

    public void setQuantità(int quantità) {
        this.quantità = quantità;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public String getCodice() {
        return codice;
    }

    public int getQuantità() {
        return quantità;
    }


    @Override
    public boolean equals(Object o) {
        Prodotto p = (Prodotto) o;
        if (o == null) return false;
        if (p.isEliminabile() == this.isEliminabile() && p.getCodice() == this.getCodice() && p.getDescrizione().equals(this.getDescrizione()) && p.getQuantità() == this.getQuantità() && p.getAggiunte().equals(this.getAggiunte())) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        if (eliminabile) {

        }
        if (quantità == 0) {
            if (eliminabile) {
                return descrizione;
            } else {
                return "<u>" + descrizione + "</u>";
            }

        } else {
            String s = quantità + "x  " + descrizione;
            for (Aggiunta a : aggiunte) {
                s += " " + a.getConsenza() + " " + a.getDescrizione();
            }
            if (eliminabile) {
                return s;
            } else {
                return "|" + s + "|";
            }
        }
    }

    public ArrayList<Aggiunta> getAggiunte() {
        Collections.sort(aggiunte);
        return aggiunte;
    }

    public boolean addAggiunta(Aggiunta aggiunta) {
        if (aggiunte.size() < 7) {
            this.aggiunte.add(aggiunta);
            return true;
        } else {
            return false;
        }

    }

    @Override
    public int compareTo(Prodotto p2) {
        int n3 = this.getDescrizione().compareTo(p2.getDescrizione());
        return n3;

    }

    public boolean rimuoviAggiunta(Aggiunta aggiunta) {
        if (aggiunte.contains(aggiunta)) {
            aggiunte.remove(aggiunta);
            return true;
        } else {
            return false;
        }
    }

    public boolean isEliminabile() {
        return eliminabile;
    }

    public void setEliminabile(boolean eliminabile) {
        this.eliminabile = eliminabile;
    }

    public boolean myIsEquals(Prodotto p) {
        if (p.isEliminabile() == this.isEliminabile() && p.getCodice().compareTo(this.getCodice())==0) {
            if (this.getAggiunte().size() == 0 && p.getAggiunte().size() == 0) {
                return true;
            }
            for (int i = 0; i < this.getAggiunte().size(); i++) {
                if (this.getAggiunte().get(i).getDescrizione().compareTo(p.getAggiunte().get(i).getDescrizione())==0 && this.getAggiunte().get(i).getConsenza().compareTo(p.getAggiunte().get(i).getConsenza())==0) {
                    return true;
                } else {
                    return false;
                }
            }
        } else {
            return false;
        }
        return false;
    }
}
