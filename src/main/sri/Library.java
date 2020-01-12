package main.sri;

import java.util.ArrayList;
import java.util.List;

public class Library {
    List<Documents> doclist;

    public Library(){
        List<Documents> doclist = new ArrayList<>();
        this.doclist = doclist;
    }

    public List<Documents> getDoclist() {
        return doclist;
    }

    public void setDoclist(List<Documents> doclist) {
        this.doclist = doclist;
    }

    public Library initiate(List<Documents> collection, Queries q, int typeElement){
        List<Documents> docList = new ArrayList<>();
        Library lib = new Library();

        for(int i=0; i<collection.size(); i++){
            Documents doc = collection.get(i);
            doc.initializePostingList(q,doc.getWords(), typeElement);

            lib.doclist.add(doc);
        }
        return lib;
    }
}
