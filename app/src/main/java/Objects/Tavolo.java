package Objects;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by enrico on 31/12/2016.
 */
public class Tavolo implements Serializable{
    private static final String END_FILE = "END OF FILE";
    private static final int CONSTANT = 9;
    private String nomeTavolo;
    private int servizi;
    private ArrayList<Prodotto> elenco_prodotti;
    private Cameriere cameriere;

    public Tavolo(String nomeTavolo){
        this.nomeTavolo=nomeTavolo;
        elenco_prodotti=new ArrayList<>(10);
        servizi=0;
        cameriere=null;
    }

    public Cameriere getCameriere() {
        return cameriere;
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

    public void setCameriere(Cameriere cameriere) {
        this.cameriere=cameriere;
    }
//3
    public ArrayList<String> toComanda() {
        ArrayList<String> ritorno=new ArrayList<>();
        ritorno.add("10;"+servizi+";Servizio;"+cameriere.getNome()+";"+nomeTavolo+";Servizio;"+";;;;;;;;;");
        for (Prodotto p :elenco_prodotti) {
            String s=p.getCodice()+";"+p.getQuantità()+";"+p.getDescrizione();
            int size=p.getAggiunte().size();
            for(int i=0;i<size;++i){
                s+=" "+p.getAggiunte().get(i);

            }
            s+=";"+cameriere.getNome()+";"+nomeTavolo+";"+p.getDescrizione()+";";
            for(int i=0;i<size;++i){
                s+=p.getAggiunte().get(i)+";";
            }
            //ritorno.add(p.getCodice()+";"+p.getQuantità()+";"+p.getDescrizione()+p.getAggiunta()+";"+cameriere.getNome()+";"+nomeTavolo+";"+p.getDescrizione()+";"
              //      +p.getAggiunta()+";;;;;;;;;");
            for(int i=0;i<(CONSTANT-size);++i){
            s+=";";
            }
            ritorno.add(s);
        }

        ritorno.add(END_FILE);
        return ritorno;
    }
}
