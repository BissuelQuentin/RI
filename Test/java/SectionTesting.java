import javafx.util.Pair;
import main.sri.Paragraph;

import java.util.ArrayList;
import java.util.List;

public class SectionTesting {
    private int secLength;
    private int id;
    private List<String> words;
    private List<ParagraphTesting> paragraphs;
    private List<Pair<String, Integer>> postingList;

    public SectionTesting(int seclength, int id, List<String> words){
        this.secLength = seclength;
        this.id = id;
        this.words = words;
    }

    public SectionTesting(int seclength, int id, List<String> words, List<ParagraphTesting> paragraphs){
        this.secLength = seclength;
        this.id = id;
        this.words = words;
        this.paragraphs = paragraphs;
    }

    public int getSecLength() {
        return secLength;
    }

    public void setSecLength(int secLength) {
        this.secLength = secLength;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<String> getWords() {
        return words;
    }

    public void setWords(List<String> words) {
        this.words = words;
    }

    public List<ParagraphTesting> getParagraphs() {
        return paragraphs;
    }

    public void setParagraphs(List<ParagraphTesting> paragraphs) {
        this.paragraphs = paragraphs;
    }

    public List<Pair<String, Integer>> getPostingList() {
        return postingList;
    }

    public void setPostingList(List<Pair<String, Integer>> postingList) {
        this.postingList = postingList;
    }


    public void initializePostingList(QueriesTesting q, List<String> words, int typeElement){
        int index;
        boolean word_to_add;
        Pair<String,Integer> wordFrequency;
        List<Pair<String,Integer>> postingList = new ArrayList<>();
        List<String> listWord = new ArrayList<>();

        for(int k=0; k<words.size(); k++){
            String word = words.get(k);
            index = listWord.indexOf(word);
            word_to_add = false;

            for(int i=0; i<7; i++){
                for(int j=0; j<q.getRequetes()[i].size(); j++){
                    String requestWord = q.getRequetes()[i].get(j);
                    if (word.compareTo(requestWord) == 0) {
                        word_to_add = true;
                    }

                }
            }

            if(word_to_add){
                //Si il a été trouvé
                if(index != -1){
                    Pair<String,Integer> wFToIncrease = postingList.get(index);

                    wordFrequency = new Pair<String,Integer>(word, wFToIncrease.getValue()+1);
                    postingList.set(index,wordFrequency);
                }
                //Si il n'a pas été trouvé
                else{
                    wordFrequency = new Pair<String,Integer>(word, 1);
                    postingList.add(wordFrequency);
                    listWord.add(word);
                }
            }
        }

        if(typeElement == 2){
            for(int i=0; i<paragraphs.size(); i++){
                ParagraphTesting par = paragraphs.get(i);

                par.initializePostingList(q,par.getWords());
            }
        }
        this.setPostingList(postingList);
    }
}
