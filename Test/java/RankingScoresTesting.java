import javafx.util.Pair;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RankingScoresTesting {
    List<Float> df = new ArrayList<>();
    int tf = 0;
    double avLength = 0;
    boolean calc_avLenght = false;


    public List<List<ScoreTesting>> ranking_all_queries(QueriesTesting q, LibraryTesting lib, String codeSMART, int typeElement) {
        List<List<ScoreTesting>> result = new ArrayList<>();
        List<ScoreTesting> score_per_request = new ArrayList<>();
        List<List<Float>> all_tf_per_doc = new ArrayList<>();
        List<List<List<Float>>> all_tf_per_sec = new ArrayList<>();
        List<List<List<List<Float>>>> all_tf_per_par = new ArrayList<>();

        switch (typeElement){
            case 0: //Article
                    for (int i = 0; i < 7; i++) {
                        for (int k = 0; k < q.getRequetes()[i].size(); k++) {
                            df.add((float) 0);
                        }
                        //Calculer le tf de chaque mot de la requête par document
                        for (int j = 0; j < lib.getDoclist().size(); j++) {
                            List<Float> tf_per_doc = calculate_tf_per_request_per_document(q.getRequetes()[i], lib.getDoclist().get(j), codeSMART.charAt(0));
                            //System.out.println("doc id : " + i + " tf : " + tf_per_doc);
                            all_tf_per_doc.add(tf_per_doc);
                            //System.out.println(tf_per_doc);
                        }
                        //Calculer l'idf
                        for (int j = 0; j < all_tf_per_doc.size(); j++) {
                            List<Float> tf_times_idf_per_doc = calculate_idf_per_tf(all_tf_per_doc.get(j), lib.getDoclist().size(), codeSMART.charAt(1));
                            all_tf_per_doc.set(j, tf_times_idf_per_doc);
                            //System.out.println(tf_times_idf_per_doc);
                        }

                        //Normalisation
                        List<Float> normalised_scores = normalisation(all_tf_per_doc, codeSMART.charAt(2));

                        for (int j = 0; j < normalised_scores.size(); j++) {
                            ScoreTesting tmp = new ScoreTesting(normalised_scores.get(j), lib.getDoclist().get(j).getId(),-1, -1);
                            score_per_request.add(tmp);
                        }
                        Collections.sort(score_per_request, Collections.reverseOrder());
                        result.add(score_per_request);
                        score_per_request = new ArrayList<>();
                        all_tf_per_doc.clear();
                        df.clear();
                    }
                    return result;
            case 1: // Section
                List<List<Float>> tf_per_doc_sec = new ArrayList<>();

                for (int i = 0; i < 7; i++) {
                    for (int k = 0; k < q.getRequetes()[i].size(); k++) {
                        df.add((float) 0);
                    }
                    //Calculer le tf de chaque document
                    for (int j = 0; j < lib.doclist.size(); j++) {
                        //Pour chaque section
                        for (int l=0; l < lib.doclist.get(j).getSection().size(); l++ ){
                            List<Float> tf_per_sec = calculate_tf_per_request_per_section(q.getRequetes()[i], lib.getDoclist().get(j).getSection().get(l), codeSMART.charAt(0));
                            //System.out.println("doc id : " + i + " tf : " + tf_per_doc);
                            tf_per_doc_sec.add(tf_per_sec);
                            //System.out.println(tf_per_doc);
                        }
                        all_tf_per_sec.add(tf_per_doc_sec);
                        tf_per_doc_sec = new ArrayList<>();
                    }
                    //Calculer l'idf
                    for (int j = 0; j < all_tf_per_sec.size(); j++) {
                        for (int l = 0; l < all_tf_per_sec.get(j).size(); l++) {
                            List<Float> tf_times_idf_per_sec = calculate_idf_per_tf(all_tf_per_sec.get(j).get(l), lib.getDoclist().size(), codeSMART.charAt(1));
                            all_tf_per_sec.get(j).set(l, tf_times_idf_per_sec);
                            //System.out.println(tf_times_idf_per_doc);
                        }
                    }

                    //Normalisation
                    List<List<Float>> all_normalized_score = new ArrayList<>();

                    for(int j= 0; j < all_tf_per_sec.size(); j++) {
                        List<Float> normalised_scores = normalisation(all_tf_per_sec.get(j), codeSMART.charAt(2));
                        all_normalized_score.add(normalised_scores);
                    }

                    for (int j = 0; j < all_normalized_score.size(); j++) {
                        for (int l = 0; l < all_normalized_score.get(j).size(); l++) {
                            ScoreTesting tmp = new ScoreTesting(all_normalized_score.get(j).get(l), lib.getDoclist().get(j).getId(), lib.getDoclist().get(j).getSection().get(l).getId(), -1);
                            score_per_request.add(tmp);
                        }
                    }
                    Collections.sort(score_per_request, Collections.reverseOrder());
                    result.add(score_per_request);
                    score_per_request = new ArrayList<>();
                    all_tf_per_sec.clear();
                    df.clear();
                }
                return result;
            case 2: // Section
                List<List<Float>> tf_per_sec_par = new ArrayList<>();
                List<List<List<Float>>> tf_per_doc_sec_par = new ArrayList<>();

                for (int i = 0; i < 7; i++) {
                    for (int k = 0; k < q.getRequetes()[i].size(); k++) {
                        df.add((float) 0);
                    }
                    //Calculer le tf de chaque document
                    for (int j = 0; j < lib.doclist.size(); j++) {
                        //Pour chaque section
                        for (int l=0; l < lib.doclist.get(j).getSection().size(); l++ ) {
                            for (int k = 0; k < lib.doclist.get(j).getSection().get(l).getParagraphs().size(); k++) {
                                List<Float> tf_per_par = calculate_tf_per_request_per_paragraph(q.getRequetes()[i], lib.getDoclist().get(j).getSection().get(l).getParagraphs().get(k), codeSMART.charAt(0));
                                //System.out.println("doc id : " + i + " tf : " + tf_per_doc);
                                tf_per_sec_par.add(tf_per_par);
                                //System.out.println(tf_per_doc);
                            }
                            tf_per_doc_sec_par.add(tf_per_sec_par);
                            tf_per_sec_par = new ArrayList<>();
                        }
                        all_tf_per_par.add(tf_per_doc_sec_par);
                        tf_per_doc_sec_par = new ArrayList<>();
                    }
                    //Calculer l'idf
                    for (int j = 0; j < all_tf_per_par.size(); j++) {
                        for (int l = 0; l < all_tf_per_par.get(j).size(); l++) {
                            for (int k = 0; k < all_tf_per_par.get(j).get(l).size(); k++) {
                                List<Float> tf_times_idf_per_par = calculate_idf_per_tf(all_tf_per_par.get(j).get(l).get(k), lib.getDoclist().size(), codeSMART.charAt(1));
                                all_tf_per_par.get(j).get(l).set(k, tf_times_idf_per_par);
                                //System.out.println(tf_times_idf_per_doc);
                            }
                        }
                    }

                    //Normalisation
                    List<List<List<Float>>> all_normalized_score = new ArrayList<>();
                    List<List<Float>> normalized_score_sec_par = new ArrayList<>();

                    for(int j= 0; j < all_tf_per_par.size(); j++) {
                        for(int k=0; k < all_tf_per_par.get(j).size();k++) {
                            List<Float> normalised_scores = normalisation(all_tf_per_par.get(j).get(k), codeSMART.charAt(2));
                            normalized_score_sec_par.add(normalised_scores);
                        }
                        all_normalized_score.add(normalized_score_sec_par);
                        normalized_score_sec_par = new ArrayList<>();
                    }

                    for (int j = 0; j < all_normalized_score.size(); j++) {
                        for (int l = 0; l < all_normalized_score.get(j).size(); l++) {
                            for(int k=0; k < all_normalized_score.get(j).get(l).size(); k++) {
                                ScoreTesting tmp = new ScoreTesting(all_normalized_score.get(j).get(l).get(k), lib.getDoclist().get(j).getId(), lib.getDoclist().get(j).getSection().get(l).getId(), lib.getDoclist().get(j).getSection().get(l).getParagraphs().get(k).getId());
                                score_per_request.add(tmp);
                            }
                        }
                    }
                    Collections.sort(score_per_request, Collections.reverseOrder());
                    result.add(score_per_request);
                    score_per_request = new ArrayList<>();
                    all_tf_per_par.clear();
                    df.clear();
                }
                return result;
        }

        return result;

    }

    private List<Float> calculate_tf_per_request_per_paragraph(List<String> request, ParagraphTesting paragraph, char code_tf) {
        List<Float> tf_per_word = new ArrayList<>();

        for (int i = 0; i < request.size(); i++) {
            String word = request.get(i);
            tf_per_word.add((float) (0));

            for (int k = 0; k < paragraph.getPostingList().size(); k++) {
                if (word.compareTo(paragraph.getPostingList().get(k).getKey()) == 0) {
                    tf_per_word.set(i, (float) paragraph.getPostingList().get(k).getValue());

                    df.set(i, df.get(i) + 1);
                }
            }
        }


        if (code_tf == 'l') {
            for (int i = 0; i < tf_per_word.size(); i++) {
                float tmp = (float) (1 + Math.log10(tf_per_word.get(i)));
                if (Float.isInfinite(tmp)) {
                    tmp = 0;
                }
                tf_per_word.set(i, tmp);
            }
        }

        return tf_per_word;
    }

    private List<Float> calculate_tf_per_request_per_section(List<String> request, SectionTesting section, char code_tf) {
        List<Float> tf_per_word = new ArrayList<>();

        for (int i = 0; i < request.size(); i++) {
            String word = request.get(i);
            tf_per_word.add((float) (0));

            for (int k = 0; k < section.getPostingList().size(); k++) {
                if (word.compareTo(section.getPostingList().get(k).getKey()) == 0) {
                    tf_per_word.set(i, (float) section.getPostingList().get(k).getValue());

                    df.set(i, df.get(i) + 1);
                }
            }
        }


        if (code_tf == 'l') {
            for (int i = 0; i < tf_per_word.size(); i++) {
                float tmp = (float) (1 + Math.log10(tf_per_word.get(i)));
                if (Float.isInfinite(tmp)) {
                    tmp = 0;
                }
                tf_per_word.set(i, tmp);
            }
        }

        return tf_per_word;
    }

    public List<Float> calculate_tf_per_request_per_document(List<String> request, DocumentsTesting dict, char code_tf) {
        List<Float> tf_per_word = new ArrayList<>();

        for (int i = 0; i < request.size(); i++) {
            String word = request.get(i);
            tf_per_word.add((float) (0));

            for (int k = 0; k < dict.getPostingList().size(); k++) {
                if (word.compareTo(dict.getPostingList().get(k).getKey()) == 0) {
                    tf_per_word.set(i, (float) dict.getPostingList().get(k).getValue());

                    df.set(i, df.get(i) + 1);
                }
            }
        }


        if (code_tf == 'l') {
            for (int i = 0; i < tf_per_word.size(); i++) {
                float tmp = (float) (1 + Math.log10(tf_per_word.get(i)));
                if (Float.isInfinite(tmp)) {
                    tmp = 0;
                }
                tf_per_word.set(i, tmp);
            }
        }

        return tf_per_word;
    }

    public List<Float> calculate_idf_per_tf(List<Float> tf_per_doc, int lib_size, char code_idf) {
        List<Float> idf_per_doc = new ArrayList<>();

        if (code_idf == 'n') {
            idf_per_doc = tf_per_doc;
        }

        if (code_idf == 't') {
            for (int i = 0; i < tf_per_doc.size(); i++) {
                if (df.get(i) == 0) {
                    idf_per_doc.add((float) 0);
                } else {
                    idf_per_doc.add((float) (tf_per_doc.get(i) * Math.log10(lib_size / df.get(i))));
                }
            }
        }

        return idf_per_doc;
    }

    public List<Float> normalisation(List<List<Float>> all_idf_per_doc, char code_norm) {
        List<Float> normalised_score = new ArrayList<>();
        float weight;
        switch (code_norm) {
            case ('c'):
                float sum_square;
                for (int i = 0; i < all_idf_per_doc.size(); i++) {
                    weight = 0;
                    sum_square = 0;
                    for (int j = 0; j < all_idf_per_doc.get(i).size(); j++) {
                        sum_square += Math.pow(all_idf_per_doc.get(i).get(j), 2);
                    }
                    for (int j = 0; j < all_idf_per_doc.get(i).size(); j++) {
                        weight += all_idf_per_doc.get(i).get(j) / (float) Math.sqrt(sum_square);
                    }
                    if (Float.isNaN(weight)) {
                        weight = 0;
                    }
                    normalised_score.add(weight);
                }
                break;
            case ('s'):
                float sum_idf = 0;
                for (int i = 0; i < all_idf_per_doc.size(); i++) {
                    for (int j = 0; j < all_idf_per_doc.get(i).size(); j++) {
                        sum_idf += all_idf_per_doc.get(i).get(j);
                    }
                }
                for (int i = 0; i < all_idf_per_doc.size(); i++) {
                    weight = 0;
                    for (int j = 0; j < all_idf_per_doc.get(i).size(); j++) {
                        weight += all_idf_per_doc.get(i).get(j) / sum_idf;
                    }
                    normalised_score.add(weight);
                }
                break;
            case ('n'):
                for (int i = 0; i < all_idf_per_doc.size(); i++) {
                    weight = 0;
                    for (int j = 0; j < all_idf_per_doc.get(i).size(); j++) {
                        weight += all_idf_per_doc.get(i).get(j);
                    }
                    normalised_score.add(weight);
                }
                break;
            default:
                break;
        }
        return normalised_score;
    }

    public List<List<ScoreTesting>> keep_max_score(List<List<ScoreTesting>> list_score){
        List<List<ScoreTesting>> result = new ArrayList<>();
        List<ScoreTesting> scores_for_one_request = new ArrayList<>();
        List<Integer> id_visited = new ArrayList<>();

        for( int i=0; i < list_score.size(); i++){
            for(int j=0; j < list_score.get(i).size(); j++){
                if (!id_visited.contains(list_score.get(i).get(j).getDocid())){
                    scores_for_one_request.add(list_score.get(i).get(j));
                    id_visited.add(list_score.get(i).get(j).getDocid());
                }

            }
            Collections.sort(scores_for_one_request, Collections.reverseOrder());
            result.add(scores_for_one_request);
            scores_for_one_request = new ArrayList<>();
            id_visited = new ArrayList<>();
        }
        return result;
    }

    public List<List<ScoreTesting>> ranking_bm25(QueriesTesting q, LibraryTesting lib, double k, double b) {

        List<List<ScoreTesting>> result = new ArrayList<>();
        List<ScoreTesting> score_per_request = new ArrayList<>();
        List<Pair<Integer, Double>> score_per_doc = new ArrayList<>();
        boolean all_pairs_initialized = false;

        ScoreTesting tmp;

        int doc_frequency_for_one_term;
        int term_frequency_for_one_term;
        double bm25_log;
        double bm25_tf = 0;
        double bm25_score = 0;
        List<Float> bm25_per_doc = new ArrayList<>();

        //Pour chaque requête
        for (int i = 0; i < 7; i++) {
            //pour chaque mot de la requête
            for (int j = 0; j < q.getRequetes()[i].size(); j++) {
                String mot_requete = q.getRequetes()[i].get(j);
                //TODO : On la calcule pour chaque requête alors que c'est la même !
                doc_frequency_for_one_term = calculate_doc_frequency(mot_requete, lib);
                //System.out.println(mot_requete + ", " + doc_frequency_for_one_term);
                bm25_log = Math.log10(((double) lib.getDoclist().size() - (double) doc_frequency_for_one_term + 0.5) / ((double) doc_frequency_for_one_term + 0.5));

                System.out.println(mot_requete + " log :  "+ bm25_log);

                //Pour chaque document
                for (int l = 0; l < lib.getDoclist().size(); l++) {
                    //On calcule le tf pour ce document
                    System.out.println(" doc " +l );
                    term_frequency_for_one_term = calculate_term_frequency(q.getRequetes()[i].get(j), lib.getDoclist().get(l));
                    System.out.println("tf de " +mot_requete + " : " + term_frequency_for_one_term);

                    //On calcule la partie avec le tf
                    bm25_tf = ((double) term_frequency_for_one_term * ((double) k + 1)) / (double) (k * (((double)1 - (double) b) + (double) b * ((double) lib.getDoclist().get(l).getDocLength() / 20)) + (double) term_frequency_for_one_term);

                    System.out.println(mot_requete + " tf :  " + bm25_tf);
                    //On calcule le score de bm25 pour le document
                    bm25_score = bm25_log * bm25_tf;
                    System.out.println(mot_requete + " tf :  " + bm25_tf);

                    if (all_pairs_initialized) {
                        Pair<Integer, Double> pair_found = score_per_doc.get(l);
                        Pair<Integer, Double> pair_tmp = new Pair<>(lib.getDoclist().get(l).getId(), bm25_score + pair_found.getValue());

                        score_per_doc.set(l, pair_tmp);
                    } else {
                        //On crée une Pair du score et du docid
                        Pair<Integer, Double> pair_tmp = new Pair<>(lib.getDoclist().get(l).getId(), bm25_score);
                        score_per_doc.add(pair_tmp);
                    }
                }

                all_pairs_initialized = true;
                tf = 0;
            }

            //pour toutes les pair docid, score bm25
            for(int m=0; m<score_per_doc.size();m++){
                tmp = new ScoreTesting(score_per_doc.get(m).getValue().floatValue(), score_per_doc.get(m).getKey(), -1, -1);

                score_per_request.add(tmp);
            }

            Collections.sort(score_per_request, Collections.reverseOrder());
            result.add(score_per_request);
            score_per_request = new ArrayList<>();
        }
        return result;
    }

    private int calculate_term_frequency(String word, DocumentsTesting dic) {
        int term_frequency_for_one_term = 0;

        //pour chaque doc
        for (int i = 0; i < dic.getPostingList().size(); i++) {
            //Pour chaque mot
            if (dic.getPostingList().get(i).getKey().compareTo(word) == 0) {
                term_frequency_for_one_term  += dic.getPostingList().get(i).getValue();


            }
        }

        return term_frequency_for_one_term;
    }

    private int calculate_doc_frequency(String word, LibraryTesting lib) {
        int doc_frequency = 0;

        //pour chaque doc
        for (int i = 0; i < lib.getDoclist().size(); i++) {
            //Pour chaque mot
            for (int j = 0; j < lib.getDoclist().get(i).getPostingList().size(); j++) {
                if (lib.getDoclist().get(i).getPostingList().get(j).getKey().compareTo(word) == 0) {
                    doc_frequency++;
                }

            }

            if(!calc_avLenght) {
                avLength += lib.getDoclist().get(i).getDocLength();
            }
        }

        if(!calc_avLenght) {
            avLength = avLength / lib.getDoclist().size();
            calc_avLenght = true;
        }


        return doc_frequency;
    }
}
