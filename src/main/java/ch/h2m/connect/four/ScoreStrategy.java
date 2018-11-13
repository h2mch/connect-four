package ch.h2m.connect.four;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import ch.h2m.connect.four.model.Disc;
import ch.h2m.connect.four.model.Result;

public abstract class ScoreStrategy {


    protected static final int DISC_QUANTIFIER = 1;
    private static Logger logger = LoggerFactory.getLogger(ScoreStrategy.class);
    protected final List<List<Disc>> typedBoard;
    protected final int height;
    protected final int width;


    public ScoreStrategy(List<List<Disc>> typedBoard) {
        this.typedBoard = typedBoard;
        this.height = typedBoard.size();
        this.width = typedBoard.get(0).size();
    }

    public abstract List<Result> calculate();

    protected boolean isNextDiscInColumn(int row, int column) {
        try {
            if (row == typedBoard.size() - 1) {
                // first line, no need to check if row below is empty
                return typedBoard.get(row).get(column).isEmpty();
            } else {
                return typedBoard.get(row).get(column).isEmpty()
                        && typedBoard.get(row + 1).get(column).isNonEmpty();
            }
        } catch (Exception e) {
            logger.error("row:{} column:{}", row, column);
            return false;
        }

    }

    protected int setScore(Disc disc, Disc lastDisc, int score) {
        if (disc.isNonEmpty()) {
            if (lastDisc == null || lastDisc == disc || lastDisc.isEmpty()) {
                score += DISC_QUANTIFIER;
            } else {
                // other color, reset to one disc
                score = DISC_QUANTIFIER;
            }
        } else { // empty, reset to zero
            score = 0;
        }
        return score;
    }

}
