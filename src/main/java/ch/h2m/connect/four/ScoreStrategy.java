package ch.h2m.connect.four;

import java.util.List;

import ch.h2m.connect.four.model.Disc;

public abstract class ScoreStrategy {


    final List<List<Disc>> typedBoard;

    public ScoreStrategy(List<List<Disc>> typedBoard) {
        this.typedBoard = typedBoard;
    }

    public abstract int[] calculate(Disc myDisc);


}
