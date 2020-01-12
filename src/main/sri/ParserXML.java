package main.sri;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.sound.sampled.Port;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class ParserXML {
    /*nouvel objet avec id, liste de mots et taille du document (/section/trucs a ajouter)*/

    public ParserXML() {

    }

    public List<Documents> execute(List<String> stopWords, boolean useStemmer, int typeElement){
        File directory = new File("coll");
        File[] list_file = directory.listFiles();
        List<Documents> collection = new ArrayList<>();
        for(File file : list_file) {
            Documents dp = this.execute_bis(file, stopWords, useStemmer, typeElement);

            List<String> words = dp.getWords();
            words.forEach(word -> {
                if(useStemmer){
                    word = PorterStemmer.stemWord(word);
                }
                if(stopWords.contains(word)){
                    words.remove(word);
                }
            });
            dp.setWords(words);
            collection.add(dp);
        }
        System.out.println("Parsing fini!");
        return collection;
    }


    private Documents execute_bis(File file, List<String> stopWords, boolean useStemmer, int typeElement) {
        DocumentBuilderFactory fabric = DocumentBuilderFactory.newInstance();
        DocumentBuilder constructor;

        try {
            fabric.setNamespaceAware(false);
            fabric.setValidating(false);

            constructor = fabric.newDocumentBuilder();
            constructor.setEntityResolver(new EntityResolver() {
                @Override
                public InputSource resolveEntity(String publicId, String systemId)
                        throws SAXException, IOException {
                    if (systemId.contains("article.dtd")) {
                        return new InputSource(new StringReader(""));
                    } else {
                        return null;
                    }
                }
            });
            Document document = (Document) constructor.parse(file);
            document.normalize();
            String article = document.getElementsByTagName("article").item(0).getTextContent();
            article.trim();
            article = article.replaceAll("\n", " ");
            article = article.replaceAll("[^A-Za-z0-9 ]", " ");
            article = article.replaceAll("^ +| +$|( )+", "$1");
            article = article.toLowerCase();
            String[] tmp = article.split(" ");
            String[] tmp_ep;
            Documents dp = new Documents(Integer.parseInt(file.getName().substring(0, file.getName().length() - 4)), tmp.length, Arrays.asList(tmp));

            if(typeElement >= 1) {
                //Gestion des sections
                Section sec;
                NodeList element = document.getElementsByTagName("sec");
                List<Section> sections = new ArrayList<>();
                for (int i = 0; i < element.getLength(); i++) {
                    String e = element.item(i).getTextContent();
                    e.trim();
                    e = e.replaceAll("\n", " ");
                    e = e.replaceAll("[^A-Za-z0-9 ]", " ");
                    e = e.replaceAll("^ +| +$|( )+", "$1");
                    e = e.toLowerCase();
                    tmp = e.split(" ");

                    if (typeElement >= 2 ){
                        NodeList elementPar = document.getElementsByTagName("p");
                        List<Paragraph> paragraphs = new ArrayList<>();
                        for (int j = 0; j < elementPar.getLength(); j++) {
                            String ep = elementPar.item(j).getTextContent();
                            ep.trim();
                            ep = ep.replaceAll("\n", " ");
                            ep = ep.replaceAll("[^A-Za-z0-9 ]", " ");
                            ep = ep.replaceAll("^ +| +$|( )+", "$1");
                            ep = ep.toLowerCase();
                            tmp_ep = ep.split(" ");

                            Paragraph par = new Paragraph(tmp_ep.length, j, Arrays.asList(tmp_ep));
                            paragraphs.add(par);
                        }

                        sec = new Section(tmp.length, i, Arrays.asList(tmp), paragraphs);

                    }
                    else {
                        sec = new Section(tmp.length, i, Arrays.asList(tmp));
                    }
                    sections.add(sec);

                }

                dp.setSection(sections);
            }
            return dp;
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }

        return null;

    }
}
