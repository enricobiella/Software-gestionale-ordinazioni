package Objects;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by enrico on 31/12/2016.
 */
public class Tavolo implements Serializable, Comparable<Tavolo>{
    private static final String END_FILE = "END OF FILE";
    private static final int CONSTANT = 9;
    private String nomeTavolo;
    private int servizi;
    private ArrayList<Prodotto> elenco_prodotti;
    private Cameriere cameriere;
    private boolean tavoloControlloComanda;//è true se il tavolo esiste di già
    private int serviziOld;

    public Tavolo(String nomeTavolo){
        this.nomeTavolo=nomeTavolo;
        elenco_prodotti=new ArrayList<>(10);
        tavoloControlloComanda=false;
        servizi=0;
        serviziOld=0;
        cameriere=null;
    }

    public Cameriere getCameriere() {
        return cameriere;
    }

    public void inserisciProdotto(Prodotto p){
        elenco_prodotti.add(p);
    }
    public boolean rimuoviProdotto(Prodotto p){
        if(elenco_prodotti.remove(p)){
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
        this.servizi=this.serviziOld= servizi;
    }
    public void setServiziOld(int serviziOld) {
        this.serviziOld = serviziOld;
    }

    public void setCameriere(Cameriere cameriere) {
        this.cameriere=cameriere;
    }

    @Override
    public String toString() {
        return nomeTavolo;
    }

    public ArrayList<String> toComanda() {
        ArrayList<String> ritorno=new ArrayList<>();
        if(!tavoloControlloComanda && (servizi-serviziOld)>0){
            ritorno.add("10;"+(servizi-serviziOld)+";Servizio;"+cameriere.getNome()+";"+nomeTavolo+";Servizio;"+";;;;;;;;;");
        }
        //ritorno.add("10;"+servizi+";Servizio;"+cameriere.getNome()+";"+nomeTavolo+";Servizio;"+";;;;;;;;;");
        for (Prodotto p :elenco_prodotti) {
            if(p.isEliminabile()){
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
        }

        ritorno.add(END_FILE);
        return ritorno;
    }

    @Override
    public int compareTo(Tavolo tavolo) {
        return this.getNomeTavolo().compareTo(tavolo.getNomeTavolo());
    }

    public boolean getControlloComanda() {
        return tavoloControlloComanda;
    }

    public void setControlloComanda(boolean b) {
        tavoloControlloComanda=b;
    }
    public void eliminaDoppioni(){
        ArrayList<Prodotto> daAggiungere=new ArrayList<>();
        ArrayList<Prodotto> daRimuovere=new ArrayList<>();
        Prodotto p1;
        Prodotto p2;
        for (int n=0;n<elenco_prodotti.size()-1;++n) {
            p1=elenco_prodotti.get(n);
            p2=elenco_prodotti.get(n+1);
            int n1=p1.getCodice().compareTo(p2.getCodice());
            boolean n2=p1.getAggiunte().equals(p2.getAggiunte());
            if(n1==0&&n2==true&&p1.isEliminabile()&&p2.isEliminabile()){
                Prodotto p=p1;
                p.setQuantità(p1.getQuantità()+p2.getQuantità());
                daRimuovere.add(p1);
                daRimuovere.add(p2);
                daAggiungere.add(p);
            }
        }
        for(Prodotto p:daRimuovere){
            elenco_prodotti.remove(p);
        }
        for(Prodotto p:daAggiungere){
            elenco_prodotti.add(p);
        }
    }

    public void bloccaTavolo() {
        for (Prodotto p :elenco_prodotti) {
            p.setEliminabile(false);
            for(Aggiunta a:p.getAggiunte()){
                a.setEliminabile(false);
            }
        }
    }

}
