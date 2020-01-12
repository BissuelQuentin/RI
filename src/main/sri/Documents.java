package main.sri;

import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;

public class Documents {
    private int docLength;
    private int id;
    private List<String> words;
    private List<Section> section;
    private List<Pair<String, Integer>> postingList;

    public Documents(){

    }

    public Documents(int docid, int docLength, List<String> words){
        List<Section> section = new ArrayList<>();

        this.setDocLength(docLength);
        this.setId(docid);
        this.setWords(words);
        this.setSection(section);
    }

    public Documents(int docid, int docLength, List<String> words, List<Section> section){
        this.setDocLength(docLength);
        this.setId(docid);
        this.setWords(words);
        this.setSection(section);
    }

    public int getDocLength() {
        return docLength;
    }

    public void setDocLength(int docLength) {
        this.docLength = docLength;
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

    public List<Section> getSection() {
        return section;
    }

    public void setSection(List<Section> section) {
        this.section = section;
    }

    public List<Pair<String, Integer>> getPostingList() {
        return postingList;
    }

    public void setPostingList(List<Pair<String, Integer>> postingList) {
        this.postingList = postingList;
    }

    public void initializePostingList(Queries q, List<String> words, int typeElement){
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

        if(typeElement >= 1){
            for(int i=0; i<section.size(); i++){
                Section sec = section.get(i);

                sec.initializePostingList(q,sec.getWords(),typeElement);
            }
        }

        this.setPostingList(postingList);
    }
}
