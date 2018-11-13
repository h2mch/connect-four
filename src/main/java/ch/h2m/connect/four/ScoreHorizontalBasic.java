package ch.h2m.connect.four;

import java.util.List;

import ch.h2m.connect.four.model.Disc;

public class ScoreHorizontalBasic extends ScoreStrategy {

    public ScoreHorizontalBasic(List<List<Disc>> typedBoard) {
        super(typedBoard);
    }

    @Override
    public int[] calculate(Disc myDisc) {
        int[] scoreHorizontal = new int[typedBoard.get(0).size()];

        int[] scoreHorizontalWining = checkHorizontalSimplified(typedBoard, myDisc);
        // double[] scoreHorizontalLoosing = checkHorizontalSimplified(typedBoard, Disc.converse(myDisc));
        for (int i = 0; i < scoreHorizontal.length; i++) {
            scoreHorizontal[i] = scoreHorizontalWining[i];
            //scoreHorizontal[i] = scoreHorizontalWining[i] - scoreHorizontalLoosing[i];
        }
        return scoreHorizontal;
    }

    public int[] checkHorizontalSimplified(List<List<Disc>> typedBoard, Disc myDisc) {
        int width = typedBoard.get(0).size();
        int[] scores = new int[width];
        int height = typedBoard.size();

        for (int columnNumber = 0; columnNumber < width; columnNumber++) {
            int rowToWatch = 0;
            for (int rowNumber = height - 1; rowNumber >= 0; rowNumber--) {
                Disc disc = typedBoard.get(rowNumber).get(columnNumber);
                if (disc != Disc.EMPTY) {
                    continue;
                }
                rowToWatch = rowNumber;
                break;
            }

            int sameColor = 0;
            for (Disc disc : typedBoard.get(rowToWatch)) {
                if (disc == myDisc) {
                    sameColor++;
                }
            }
            int simplyfiedScore = sameColor;
            scores[columnNumber] = simplyfiedScore;
        }

        return scores;
    }

}
