package ch.h2m.connect.four;

import java.util.ArrayList;
import java.util.List;

import ch.h2m.connect.four.model.Disc;

public class ScoreHorizontalExtended extends ScoreStrategy {


    private static final int DISC_COUNT = 1;

    public ScoreHorizontalExtended(List<List<Disc>> typedBoard) {
        super(typedBoard);
    }



    @Override
    public int[] calculate(Disc myDisc) {

        List<List<Result>> decisionBase = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            decisionBase.add(new ArrayList<>());
        }
        cal(this.typedBoard, decisionBase);

        return new int[7];
    }

    public void cal(List<List<Disc>> board, List<List<Result>> decisionBase) {
        int height = board.size();
        int width = board.get(0).size();

        for (int row = 0; row < height; row++) {
            Disc lastDisc = null;
            double score = 0;
            for (int column = 0; column < width; column++) {
                Disc disc = board.get(row).get(column);

                //set current score in-a-row
                score = setScore(disc, lastDisc, score);

                //check if four are possible.
                if (score == 3 * DISC_COUNT) {
                    //check right
                    if ((column < width - 1) && isNextDiscInColumn(board, row, column + 1)) {
                        decisionBase.get(column + 1).add(new Result(3 * DISC_COUNT, disc));
                    }
                    // check left
                    if ((column > 2) && isNextDiscInColumn(board, row, column - 3)) {
                        decisionBase.get(column - 3).add(new Result(3 * DISC_COUNT, disc));
                    }
                    score = 0;
                }
                if (score == 2 * DISC_COUNT) {
                    //check right and next
                    if ((column < width - 2) && isNextDiscInColumn(board, row, column + 1) && board.get(row).get(column + 2) == disc) {
                        decisionBase.get(column + 1).add(new Result(3 * DISC_COUNT, disc));
                    }
                    //check left and next
                    if ((column > 2) && isNextDiscInColumn(board, row, column - 2) && board.get(row).get(column - 3) == disc) {
                        decisionBase.get(column - 2).add(new Result(3 * DISC_COUNT, disc));
                    }
                }
                lastDisc = disc;
            }
        }
    }

    private class Result {
        double score;
        Disc color;

        public Result(double score, Disc disc) {
            this.score = score;
            this.color = disc;
        }

        @Override
        public String toString() {
            return color + ": " + score;
        }
    }
    private double setScore(Disc disc, Disc lastDisc, double score) {
        if (disc.isNonEmpty()) {
            if (lastDisc == null || lastDisc == disc || lastDisc.isEmpty()) {
                score += DISC_COUNT;
            } else {
                // other color, reset to one disc
                score = DISC_COUNT;
            }
        } else { // empty, reset to zero
            score = 0;
        }
        return score;
    }

    public boolean isNextDiscInColumn(List<List<Disc>> board, int row, int column) {
        if (row == board.size() - 1) { // first line, no need to check if row below is empty
            return board.get(row).get(column).isEmpty();
        } else {
            return board.get(row).get(column).isEmpty()
                    && board.get(row + 1).get(column).isNonEmpty();
        }

    }


}
