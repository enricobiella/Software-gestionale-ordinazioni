package Objects;

import java.io.Serializable;

/**
 * Created by enrico on 04/01/2017.
 */
public class Prodotto implements Serializable {
    private int codice;
    private String descrizione;
    private int quantità;
    private String aggiunta;
    public Prodotto(int codice,String descrizione){
        this.codice=codice;
        this.descrizione=descrizione;
        quantità=0;
        aggiunta="";
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
        if (p.getCodice() == this.getCodice() && p.getDescrizione().equals(this.getDescrizione()) && p.getQuantità() == this.getQuantità() && p.getAggiunta().equals(this.getAggiunta())) {
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

    public void setAggiunta(String aggiunta) {
        this.aggiunta = aggiunta;
    }

    public String getAggiunta() {
        return aggiunta;
    }
}
