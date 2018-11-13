package ch.h2m.connect.four.score;

import java.util.ArrayList;
import java.util.List;

import ch.h2m.connect.four.model.Disc;
import ch.h2m.connect.four.model.Result;

public class ScoreHorizontal extends ScoreStrategy {

    public ScoreHorizontal(List<List<Disc>> typedBoard) {
        super(typedBoard);
    }

    @Override
    public List<Result> calculate() {
        List<Result> decisionBase = new ArrayList<>();

        for (int row = 0; row < height; row++) {
            Disc disc, lastDisc = null;
            int score = 0;
            for (int column = 0; column < width; column++) {
                disc = typedBoard.get(row).get(column);

                //set current score in-a-row
                score = setScore(disc, lastDisc, score);

                //check if four are possible.
                if (score == 3 * DISC_QUANTIFIER) {
                    //check right
                    if ((column < width - 1) && isNextDiscInColumn(row, column + 1)) {
                        decisionBase.add(new Result(column + 1, 3 * DISC_QUANTIFIER, disc));
                    }
                    // check left
                    if ((column > 2) && isNextDiscInColumn(row, column - 3)) {
                        decisionBase.add(new Result(column - 3, 3 * DISC_QUANTIFIER, disc));
                    }
                    score = 0;
                }
                if (score > 0) {
                    //check right and next
                    if ((column < width - 2) && isNextDiscInColumn(row, column + 1)) {
                        decisionBase.add(new Result(column + 1, score, disc));
                        if (typedBoard.get(row).get(column + 2) == disc) {
                            //Empty Disc between
                            decisionBase.add(new Result(column + 1, score + DISC_QUANTIFIER, disc));
                        }
                    }
                    //check left and next
                    if ((column > 2) && isNextDiscInColumn(row, column - 2)) {
                        decisionBase.add(new Result(column - 2, score, disc));
                        if (typedBoard.get(row).get(column - 3) == disc) {
                            //Empty Disc between
                            decisionBase.add(new Result(column - 2, score + DISC_QUANTIFIER, disc));
                        }
                    }
                }
                lastDisc = disc;
            }
        }
        return decisionBase;
    }


}
