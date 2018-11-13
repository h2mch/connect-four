package ch.h2m.connect.four.model;


public class Result implements Comparable<Result> {

    public int column;
    public int score;
    public Disc color;
    public Class strategy;


    public Result(Class strategy, int column, int score, Disc disc) {
        this.strategy = strategy;
        this.column = column;
        this.score = score;
        this.color = disc;
    }

    @Override
    public int compareTo(Result other) {
        return this.column - other.column;
    }

    @Override
    public boolean equals(Object obj) {
        return this.toString().equals(obj.toString());
    }

    @Override
    public String toString() {
        return strategy.getName() + ":" + column + ":" + color + ":" + score;
    }
}
