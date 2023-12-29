package bullscows;

public class Score {
    private int bulls;
    private int cows;


    public Score(int bulls, int cows) {
        this.bulls = bulls;
        this.cows = cows;
    }

    public Score() {
        this.bulls = 0;
        this.cows = 0;
    }

    public void increaseBulls(){
        this.bulls++;
    }

    public void increaseCows(){
        this.cows++;
    }

    public int getBulls() {
        return bulls;
    }

    public int getCows() {
        return cows;
    }
}
