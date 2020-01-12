package main.sri;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RankingScores {
    List<Float> df = new ArrayList<>();
    int tf = 0;
    double avLength = 0;
    boolean calc_avLenght = false;
    int dl = 0;




    public List<List<Score>> ranking_all_queries(Queries q, Library lib, String codeSMART, int typeElement) {
        List<List<Score>> result = new ArrayList<>();
        List<Score> score_per_request = new ArrayList<>();
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
                            Score tmp = new Score(normalised_scores.get(j), lib.getDoclist().get(j).getId(),-1, -1);
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
                            List<Float> tf_times_idf_per_doc = calculate_idf_per_tf(all_tf_per_sec.get(j).get(l), lib.getDoclist().size(), codeSMART.charAt(1));
                            all_tf_per_sec.get(j).set(l, tf_times_idf_per_doc);
                            //System.out.println(tf_times_idf_per_doc);
                        }
                    }

                    //Normalisation
                    List<List<Float>> all_normalized_score = new ArrayList<>();

                    for(int j= 0; j < all_tf_per_sec.size(); j++) {
                        System.out.println(all_tf_per_sec.get(j));
                        List<Float> normalised_scores = normalisation(all_tf_per_sec.get(j), codeSMART.charAt(2));
                        all_normalized_score.add(normalised_scores);
                    }

                    for (int j = 0; j < all_normalized_score.size(); j++) {
                        for (int l = 0; l < all_normalized_score.get(j).size(); l++) {
                            Score tmp = new Score(all_normalized_score.get(j).get(l), lib.getDoclist().get(j).getId(), lib.getDoclist().get(j).getSection().get(l).getId(), -1);
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
            case 2: //Paragraph
                List<List<List<Float>>> tf_per_doc_sec_par = new ArrayList<>();
                List<List<Float>>  tf_per_sec_par = new ArrayList<>();

                for (int i = 0; i < 7; i++) {
                    for (int k = 0; k < q.getRequetes()[i].size(); k++) {
                        df.add((float) 0);
                    }
                    //Calculer le tf de chaque mot de la requête par document
                    for (int j = 0; j < lib.doclist.size(); j++) {
                        for (int k = 0; k < lib.doclist.get(j).getSection().size(); k++) {
                            for (int l = 0; l < lib.doclist.get(j).getSection().get(k).getParagraphs().size(); l++) {
                                List<Float> tf_per_sec = calculate_tf_per_request_per_paragraph(q.getRequetes()[i], lib.getDoclist().get(j).getSection().get(k).getParagraphs().get(l), codeSMART.charAt(0));
                                //System.out.println("doc id : " + i + " tf : " + tf_per_doc);
                                tf_per_sec_par.add(tf_per_sec);
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
                        for (int k = 0; k < all_tf_per_sec.get(j).size(); k++) {
                            for (int l = 0; l < all_tf_per_sec.get(j).get(k).size(); l++) {
                                List<Float> tf_times_idf_per_doc = calculate_idf_per_tf(all_tf_per_par.get(j).get(k).get(l), lib.getDoclist().size(), codeSMART.charAt(1));
                                all_tf_per_par.get(j).get(k).set(l, tf_times_idf_per_doc);
                                //System.out.println(tf_times_idf_per_doc);
                            }
                        }
                    }

                    //Normalisation
                    List<List<List<Float>>> all_normalized_score_par = new ArrayList<>();
                    List<List<Float>> normalize_score_for_sec = new ArrayList<>();

                    for(int j= 0; j < all_tf_per_par.size(); j++) {
                        for (int k = 0; k < all_tf_per_par.get(j).size(); k++) {
                            List<Float> normalised_scores = normalisation(all_tf_per_sec.get(j), codeSMART.charAt(2));
                            normalize_score_for_sec.add(normalised_scores);
                        }
                        all_normalized_score_par.add(normalize_score_for_sec);
                    }

                    for (int j = 0; j < all_normalized_score_par.size(); j++) {
                        for (int k = 0; k < all_normalized_score_par.get(j).size(); k++) {
                            for (int l = 0; l < all_normalized_score_par.get(j).get(k).size(); l++) {
                                Score tmp = new Score(all_normalized_score_par.get(j).get(k).get(l), lib.getDoclist().get(j).getId(), lib.getDoclist().get(j).getSection().get(k).getId(), lib.getDoclist().get(j).getSection().get(k).getParagraphs().get(l).getId());
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

    private List<Float> calculate_tf_per_request_per_paragraph(List<String> request, Paragraph par, char code_tf) {
        List<Float> tf_per_word = new ArrayList<>();

        for (int i = 0; i < request.size(); i++) {
            String word = request.get(i);
            tf_per_word.add((float) (0));

            for (int k = 0; k < par.getPostingList().size(); k++) {
                if (word.compareTo(par.getPostingList().get(k).getKey()) == 0) {
                    tf_per_word.set(i, (float) par.getPostingList().get(k).getValue());

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

    private List<Float> calculate_tf_per_request_per_section(List<String> request, Section section, char code_tf) {
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

    public List<Float> calculate_tf_per_request_per_document(List<String> request, Documents dict, char code_tf) {
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
}
