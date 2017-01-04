package Objects;

import java.io.Serializable;

/**
 * Created by enrico on 04/01/2017.
 */
public class Prodotto implements Serializable {
    private int codice;
    private String descrizione;
    private int quantità;
    public Prodotto(int codice,String descrizione){
        this.codice=codice;
        this.descrizione=descrizione;
        quantità=0;
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
    public String toString() {
        return descrizione;
    }
}
