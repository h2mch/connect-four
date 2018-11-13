package ch.h2m.connect.four;

import java.util.List;

import ch.h2m.connect.four.model.Disc;

public class ScoreVertical extends ScoreStrategy {


    public ScoreVertical(List<List<Disc>> typedBoard) {
        super(typedBoard);
    }

    @Override
    public int[] calculate(Disc myDisc) {
        int width = typedBoard.get(0).size();
        int height = typedBoard.size();
        int[] scores = new int[width];

        for (int i = 0; i < width; i++) {
            int score = 0;
            Disc lastDisc = null;
            for (int j = height - 1; j >= 0; j--) {
                Disc disc = typedBoard.get(j).get(i);

                if (Disc.EMPTY == disc) {
                    break;
                }
                if (lastDisc == null) {
                    lastDisc = disc;
                }

                if (lastDisc == disc) {
                    score += 1;
                } else {
                    score = 1;
                }
                lastDisc = disc;
            }
            //positve if match for my color, negative if for opponent
            scores[i] = (lastDisc == myDisc) ? scores[i] = score : 0 - score;
        }

        return scores;
    }

}
