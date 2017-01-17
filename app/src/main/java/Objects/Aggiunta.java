package Objects;

import java.io.Serializable;

/**
 * Created by enrico on 12/01/2017.
 */
public class Aggiunta implements Serializable,Comparable<Aggiunta>{
    private String descrizione;
    private String consenza;
    public Aggiunta(String descrizione,String consenza){
        this.descrizione=descrizione;
        this.consenza=consenza;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public String getConsenza() {
        return consenza;
    }

    public void setConsenza(String consenza) {
        this.consenza = consenza;
    }

    @Override
    public boolean equals(Object o) {
        Aggiunta a = (Aggiunta) o;
        if (o == null) return false;
        if (this.getDescrizione().equals(a.getDescrizione()) && this.getConsenza().equals(a.getConsenza())) {
            return true;
        } else {
            return false;
        }
    }
    @Override
    public int compareTo(Aggiunta aggiunta) {
        return this.getDescrizione().compareTo(aggiunta.getDescrizione());
    }
    @Override
    public String toString() {
            return descrizione;
    }
}
