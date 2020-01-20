import main.sri.Documents;
import main.sri.Queries;

import java.util.ArrayList;
import java.util.List;

public class LibraryTesting {
    List<DocumentsTesting> doclist;

    public LibraryTesting(){
        List<DocumentsTesting> doclist = new ArrayList<>();
        this.doclist = doclist;
    }

    public List<DocumentsTesting> getDoclist() {
        return doclist;
    }

    public void setDoclist(List<DocumentsTesting> doclist) {
        this.doclist = doclist;
    }

    public LibraryTesting initiate(List<DocumentsTesting> collection, QueriesTesting q, int typeElement){
        List<DocumentsTesting> docList = new ArrayList<>();
        LibraryTesting lib = new LibraryTesting();

        for(int i=0; i<collection.size(); i++){
            DocumentsTesting doc = collection.get(i);
            doc.initializePostingList(q,doc.getWords(), typeElement);

            lib.doclist.add(doc);
        }
        return lib;
    }
}
