import main.sri.Documents;
import main.sri.PorterStemmer;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ParserTXTTesting {

    public ParserTXTTesting() {

    }

    public static List<DocumentsTesting> execute(List<String> stopWords, boolean useStemmer) {
        File file = new File("ressouces/Text_Only_Ascii_Coll_MWI_NoSem");

        DocumentBuilderFactory fabric = DocumentBuilderFactory.newInstance();
        DocumentBuilder constructor;
        try {
            fabric.setNamespaceAware(false);
            fabric.setValidating(false);

            constructor = fabric.newDocumentBuilder();
            Document document = (Document) constructor.parse(file);
            document.normalize();
            int size = document.getElementsByTagName("doc").getLength();
            List<DocumentsTesting> collection = new ArrayList<>();
            for (int j = 0; j < size; j++) {
                Element e = (Element) document.getElementsByTagName("doc").item(j);
                String doc = e.getTextContent();
                doc.trim();
                doc = doc.replaceAll("\n", " ");
                doc = doc.replaceAll("[^A-Za-z0-9 ]", " ");
                doc = doc.replaceAll("^ +| +$|( )+", "$1");
                doc = doc.toLowerCase();
                String[] tmp = doc.split(" ");
                DocumentsTesting dp = new DocumentsTesting(Integer.parseInt(tmp[0]), tmp.length, Arrays.asList(tmp));
                List<String> words = dp.getWords();
                words.forEach(word -> {
                    if (!stopWords.contains(word)) {
                        if (useStemmer) {
                            word = PorterStemmer.stemWord(word);
                        }
                    } else {
                        words.remove(word);
                    }
                });
                dp.setWords(words);
                collection.add(dp);
            }
            System.out.println("Parsing fini!");
            return collection;
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }

        return null;
    }

}
