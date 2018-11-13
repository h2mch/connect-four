package ch.h2m.connect.four.score;

import java.util.ArrayList;
import java.util.List;

import ch.h2m.connect.four.model.Disc;
import ch.h2m.connect.four.model.Result;

public class ScoreDiagnonalBackward extends ScoreStrategy {

    public ScoreDiagnonalBackward(List<List<Disc>> typedBoard) {
        super(typedBoard);
    }

    @Override
    public List<Result> calculate() {
        List<Result> decisionBase = new ArrayList<>();

        Disc disc, lastDisc;
        int row;
        int column;
        int score;

        for (int i = 0; i < width + height - 1; i++) {
            row = Math.max(height - 1 - i, 0);
            column = Math.max(i - height, 0);
            score = 0;
            lastDisc = null;
            while (row < height && column < width) {
                disc = typedBoard.get(row).get(column);

                //set current score , same as in-a-row
                score = setScore(disc, lastDisc, score);

                if (score == 3 * DISC_QUANTIFIER) {
                    // check lower right
                    if ((row < height - 1) && (column < width - 1) && isNextDiscInColumn(row + 1, column + 1)) {
                        decisionBase.add(new Result(column + 1, 3 * DISC_QUANTIFIER, disc));
                    }
                    // check upper left
                    if ((row > 2) && (column > 2) && isNextDiscInColumn(row - 3, column - 3)) {
                        decisionBase.add(new Result(column - 3, 3 * DISC_QUANTIFIER, disc));
                    }
                    score = 0;
                }
                if (score > 0) {
                    //check lower right
                    if ((row < height - 2) && (column < width - 2) && isNextDiscInColumn(row + 1, column + 1)) {
                        decisionBase.add(new Result(column + 1, score, disc));
                        if (typedBoard.get(row + 2).get(column + 2) == disc) {
                            decisionBase.add(new Result(column + 1, score + DISC_QUANTIFIER, disc));
                        }
                    }
                    //check upper left
                    if ((row > 2) && (column > 2) && isNextDiscInColumn(row - 2, column - 2)) {
                        decisionBase.add(new Result(column - 2, score, disc));
                        if (typedBoard.get(row - 3).get(column - 3) == disc) {
                            decisionBase.add(new Result(column - 2, score + DISC_QUANTIFIER, disc));
                        }
                    }
                }

                lastDisc = disc;
                row++;
                column++;
            }
        }
        return decisionBase;
    }


}
