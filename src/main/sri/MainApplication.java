package main.sri;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class MainApplication {
    private static int step = 2; // 1 = txt, 2 = XML
    private static int typeElement = 2; //0 = article, 1 = section, 2 = paragraph
    private static boolean useStopWord = false;
    private static boolean useStemmer = false;

    public static void main(String[] args) {

        /** Initialisation du stopword si on le souhaite **/

        List<String> stopWords = new ArrayList<>();
        if(useStopWord) {
            System.out.println("Initialisation stopWord");
            try {
                File file = new File(MainApplication.class.getClassLoader().getResource("stopWords").getFile());
                Scanner input = new Scanner(file);
                input.useDelimiter(" +|\\n|\\r");
                while (input.hasNext()) {
                    stopWords.add(input.next());
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            System.out.println("StopWord initialisé");
        }

        /** Création de la library à partir des fichiers texte ou XML**/

        Queries requete = new Queries();
        Library lib = new Library();
        // parser les documents
        if (step == 1) {
            List<Documents> collectionTXT = ParserTXT.execute(stopWords, useStemmer);

            lib = lib.initiate(collectionTXT, requete, typeElement);
        } else {
            ParserXML parserXML = new ParserXML();
            List<Documents> collectionXML = parserXML.execute(stopWords, useStemmer, typeElement);

            lib = lib.initiate(collectionXML, requete, typeElement);
        }
        requete.printQueries();
        // stocker les 1500 meilleurs scores (a chaque nouveau score l'insérer dans la liste de scores)
        System.out.println("Library initialisée!");

        /** Test du model de ranking **/

        List<List<Score>> result;
        RankingScores rs = new RankingScores();
        List<String> ponderation = new ArrayList<>();

        ponderation.add("nnn");
        ponderation.add("nnc");
        ponderation.add("ltn");
        ponderation.add("ltc");
        ponderation.add("lts");

        int nb_run = 1;

        for (int i = 0; i < ponderation.size(); i++) {
            result = rs.ranking_all_queries(requete, lib, ponderation.get(i), typeElement);
            //System.out.println("result : " + result.toString());
            // Création du fichier avec le bon nom de fichier dans un dossier "out"
            System.out.println("Creation du fichier " + ponderation.get(i));
            switch(typeElement){
                case 0 :
                    RunFileCreator.createRun(step, nb_run, ponderation.get(i), "article", result);
                    break;
                case 1:
                    RunFileCreator.createRun(step, nb_run, ponderation.get(i), "section", result);
                    break;
                case 2:
                    RunFileCreator.createRun(step, nb_run, ponderation.get(i), "paragrph", result);
                    break;
            }

            nb_run++;
            System.out.println("Fichier créé");
        }
/*
        for(int i=0; i<16; i++) {
            result = rs.ranking_bm25(requete, lib, 1+(i*0.05), 0.75);
            //System.out.println("result : " + result.toString());
            // Création du fichier avec le bon nom de fichier dans un dossier "out"
            System.out.println("Creation du fichier bm25");
            RunFileCreator.createRun(step, nb_run, "bm25_k" + (1+(i*0.05)) + "_b0.75", "article", result);
            nb_run++;
            System.out.println("Fichier créé");
        }

        for(int i=0; i<16; i++) {
            result = rs.ranking_bm25(requete, lib, 1.2, 0.75 + (i * 0.05));
            //System.out.println("result : " + result.toString());
            // Création du fichier avec le bon nom de fichier dans un dossier "out"
            System.out.println("Creation du fichier bm25");
            RunFileCreator.createRun(step, nb_run, "bm25_k1.2_b" + (0.75 + (i * 0.05)), "article", result);
            nb_run++;
            System.out.println("Fichier créé");
        }*/
    }
}
