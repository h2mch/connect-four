package ch.h2m.connect.four.score;

import java.util.ArrayList;
import java.util.List;

import ch.h2m.connect.four.model.Disc;
import ch.h2m.connect.four.model.Result;

public class ScoreVertical extends ScoreStrategy {

    public ScoreVertical(List<List<Disc>> typedBoard) {
        super(typedBoard);
    }

    @Override
    public List<Result> calculate() {
        List<Result> decisionBase = new ArrayList<>();
        int height = typedBoard.size();
        int width = typedBoard.get(0).size();

        for (int column = 0; column < width; column++) {
            Disc disc, lastDisc = null;
            int score = 0;

            for (int row = height - 1; row >= 0; row--) {
                disc = typedBoard.get(row).get(column);
                score = setScore(disc, lastDisc, score);

                if ((score > 0) && (row > 0) && isNextDiscInColumn(row - 1, column)) {
                    decisionBase.add(new Result(ScoreVertical.class, column, score, disc));
                }
                lastDisc = disc;
            }
        }
        return decisionBase;
    }

}
