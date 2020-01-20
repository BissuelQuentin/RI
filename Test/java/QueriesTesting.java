import java.util.ArrayList;
import java.util.List;

public class QueriesTesting {
    //Liste des requetes
    private List<String> requetes[] = new ArrayList[7];
    private List<Integer> idRequetes = new ArrayList();

    public QueriesTesting(String param1, String param2) {
        for (int i = 0; i < 7; i++) {
            this.requetes[i] = new ArrayList<>();
            this.requetes[i].add("a");
            this.requetes[i].add("e");
        }

        this.idRequetes.add(1);
        this.idRequetes.add(2);
        this.idRequetes.add(3);
        this.idRequetes.add(4);
        this.idRequetes.add(5);
        this.idRequetes.add(6);
        this.idRequetes.add(7);

    }

    public QueriesTesting() {
        for (int i = 0; i < 7; i++) {
            this.requetes[i] = new ArrayList<>();
        }

        this.requetes[0].add("olive");
        this.requetes[0].add("oil");
        this.requetes[0].add("health");
        this.requetes[0].add("benefit");

        this.requetes[1].add("notting");
        this.requetes[1].add("hill");
        this.requetes[1].add("film");
        this.requetes[1].add("actors");

        this.requetes[2].add("probabilistic");
        this.requetes[2].add("models");
        this.requetes[2].add("in");
        this.requetes[2].add("information");
        this.requetes[2].add("retrieval");

        this.requetes[3].add("web");
        this.requetes[3].add("link");
        this.requetes[3].add("network");
        this.requetes[3].add("analysis");

        this.requetes[4].add("web");
        this.requetes[4].add("ranking");
        this.requetes[4].add("scoring");
        this.requetes[4].add("algorithm");

        this.requetes[5].add("supervised");
        this.requetes[5].add("machine");
        this.requetes[5].add("learning");
        this.requetes[5].add("algorithm");

        this.requetes[6].add("operating");
        this.requetes[6].add("system");
        this.requetes[6].add("mutual");
        this.requetes[6].add("exclusion");

        this.idRequetes.add(2009011);
        this.idRequetes.add(2009036);
        this.idRequetes.add(2009067);
        this.idRequetes.add(2009073);
        this.idRequetes.add(2009074);
        this.idRequetes.add(2009078);
        this.idRequetes.add(2009085);
    }

    public List<String>[] getRequetes() {
        return requetes;
    }

    public void setRequetes(List<String>[] requetes) {
        this.requetes = requetes;
    }

    public List<Integer> getIdRequetes() {
        return idRequetes;
    }

    public void setIdRequetes(List<Integer> idRequetes) {
        this.idRequetes = idRequetes;
    }

    public void printQueries(){
        for(int i = 0; i<7; i++){
            System.out.println(this.requetes[i].toString());
        }
    }
}
