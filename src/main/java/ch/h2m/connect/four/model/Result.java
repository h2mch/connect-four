package ch.h2m.connect.four.model;


public class Result implements Comparable<Result> {

    public int column;
    public int score;
    public Disc color;

    public Result(int column, int score, Disc disc) {
        this.column = column;
        this.score = score;
        this.color = disc;
    }

    @Override
    public int compareTo(Result other) {
        return this.column - other.column;
    }

    @Override
    public String toString() {
        return column + ":" + color + ": " + score;
    }

}
