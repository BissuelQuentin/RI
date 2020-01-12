package main.sri;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RunFileCreator {

    private static final String teamName = "MaleineQuentinTaoSebastien_";
    private static final String extention = ".txt";
    private static final String directory = "./out/";
    private static final String queriesID = "2009011";
    private static final String column2 = "Q0";
    private static final String runTeamName = "MaleineQuentinTaoSebastien";
    private static String path;


    public static void createRun(Integer step, Integer run, String ponderation, String granularity, List<List<Score>> result) {
        String stepNum = "";
        String runNum = "";
        if (step < 10) {
            stepNum = "0" + step;
        }
        if (run < 10) {
            runNum = "0" + run;
        }
        else {
            runNum = "" + run;
        }

        String fileName = teamName.concat(stepNum)
                .concat("_")
                .concat(runNum)
                .concat("_")
                .concat(ponderation)
                .concat("_")
                .concat(granularity)
                .concat(extention);

        File runFile = new File(directory + fileName);
        try {
            runFile.createNewFile();
        } catch (IOException ex) {
            Logger.getLogger(RunFileCreator.class.getName()).log(Level.SEVERE, null, ex);
        }

        try {

                writeInFile(runFile, result );


        } catch (IOException ex) {
            Logger.getLogger(RunFileCreator.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void writeInFile(File runFile, List<List<Score>> result) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(runFile));
        Queries q = new Queries();
        for (int i=0; i<7; i++) {
            for (int j = 0; j < 1500; j++) {
                System.out.println(result.get(i).get(j).getParid());
                if(result.get(i).get(j).getParid() != -1){
                    path = "/article[1]/bdy/sec[" + result.get(i).get(j).getSecid() + "]/p["+ result.get(i).get(j).getParid() + "]";
                }
                else {
                    if (result.get(i).get(j).getSecid() != -1){
                        path = "/article[1]/bdy/sec[" + result.get(i).get(j).getSecid() + "]";
                    }
                    else {
                        if(result.get(i).get(j).getDocid() != -1) {
                            path = "/article[1]";
                        }
                    }
                }

                writer.write(q.getIdRequetes().get(i) + " " + column2 + " " + result.get(i).get(j).getDocid() + " " + (j + 1) + " " + result.get(i).get(j).getScore() + " " + runTeamName + " " + path);
                writer.newLine();

            }
        }
        writer.close();
    }
}
