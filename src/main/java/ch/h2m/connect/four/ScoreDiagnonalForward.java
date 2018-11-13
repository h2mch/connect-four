package ch.h2m.connect.four;

import java.util.ArrayList;
import java.util.List;

import ch.h2m.connect.four.model.Disc;
import ch.h2m.connect.four.model.Result;

public class ScoreDiagnonalForward extends ScoreStrategy {

    public ScoreDiagnonalForward(List<List<Disc>> typedBoard) {
        super(typedBoard);
    }

    @Override
    public List<Result> calculate() {
        List<Result> decisionBase = new ArrayList<>();

        Disc disc, lastDisc = null;
        int row;
        int column;
        int score;

        for (int i = 0; i < width + height - 1; i++) {
            row = Math.min(i, height - 1);
            column = Math.max(i - height + 1, 0);
            score = 0;
            lastDisc = null;
            while (row > -1 && column < width) {
                disc = typedBoard.get(row).get(column);

                //set current score , same as in-a-row
                score = setScore(disc, lastDisc, score);

                if (score == 3 * DISC_QUANTIFIER) {
                    // check upper right
                    if ((row > 0) && (column < width - 1) && isNextDiscInColumn(row - 1, column + 1)) {
                        decisionBase.add(new Result(column + 1, 3 * DISC_QUANTIFIER, disc));
                    }
                    // check lower left
                    if ((row < height - 3) && (column > 2) && isNextDiscInColumn(row + 3, column - 3)) {
                        decisionBase.add(new Result(column - 3, 3 * DISC_QUANTIFIER, disc));
                    }
                    score = 0;
                }
                if (score > 0) {
                    //check upper right
                    if ((row > 2) && (column < width - 2) && isNextDiscInColumn(row - 1, column + 1)) {
                        decisionBase.add(new Result(column + 1, score, disc));
                        if (typedBoard.get(row - 2).get(column + 2) == disc) {
                            decisionBase.add(new Result(column + 1, score + DISC_QUANTIFIER, disc));
                        }
                    }
                    //check lower left
                    if ((row < height - 3) && (column > 2) && isNextDiscInColumn(row + 3, column - 3)) {
                        decisionBase.add(new Result(column - 2, score, disc));
                        if (typedBoard.get(row + 3).get(column - 3) == disc) {
                            decisionBase.add(new Result(column - 2, score + DISC_QUANTIFIER, disc));
                        }
                    }
                }

                lastDisc = disc;
                row--;
                column++;
            }
        }
        return decisionBase;
    }


}
