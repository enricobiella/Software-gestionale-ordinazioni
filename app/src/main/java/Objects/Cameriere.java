package Objects;

import java.io.Serializable;

/**
 * Created by enrico on 29/12/2016.
 */
public class Cameriere implements Serializable {
    private String nome;
    private int id;
    public Cameriere(int id, String nome){
        this.id=id;
        this.nome=nome;
    }

    public String getNome() {
        return nome;
    }

    public int getId() {
        return id;
    }
}
