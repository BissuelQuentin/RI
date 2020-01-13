package main.sri;

public class Score implements Comparable<Score>{
    private float score;
    private int docid;
    private int secid;
    private int parid;

    public Score(){

    }

    public Score(float score, int docid, int secid, int parid) {
        this.score = score;
        this.docid = docid;
        this.secid = secid;
        this.parid = parid;
    }

    public float getScore() {
        return score;
    }

    public void setScore(float score) {
        this.score = score;
    }

    public int getDocid() {
        return docid;
    }

    public void setDocid(int docid) {
        this.docid = docid;
    }

    public int getSecid() {
        return secid;
    }

    public void setSecid(int secid) {
        this.secid = secid;
    }

    public int getParid() {
        return parid;
    }

    public void setParid(int parid) {
        this.parid = parid;
    }

    public String toString(){
        return docid + " : " + score ;
    }

    @Override
    public int compareTo(Score spd) {

        return  Double.compare(this.getScore(), spd.getScore());
    }
}
