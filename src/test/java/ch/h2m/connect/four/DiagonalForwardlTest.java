package ch.h2m.connect.four;


import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import ch.h2m.connect.four.model.Disc;
import ch.h2m.connect.four.model.Result;
import ch.h2m.connect.four.score.ScoreDiagnonalForward;


public class DiagonalForwardlTest {

    private List<List<Disc>> board = Arrays.asList(
            Arrays.asList(Disc.EMPTY, Disc.EMPTY, Disc.EMPTY, Disc.YELLOW, Disc.EMPTY, Disc.EMPTY, Disc.EMPTY),
            Arrays.asList(Disc.EMPTY, Disc.EMPTY, Disc.EMPTY, Disc.YELLOW, Disc.EMPTY, Disc.EMPTY, Disc.EMPTY),
            Arrays.asList(Disc.EMPTY, Disc.EMPTY, Disc.EMPTY, Disc.YELLOW, Disc.EMPTY, Disc.EMPTY, Disc.EMPTY),
            Arrays.asList(Disc.YELLOW, Disc.RED, Disc.EMPTY, Disc.RED, Disc.YELLOW, Disc.EMPTY, Disc.EMPTY),
            Arrays.asList(Disc.YELLOW, Disc.RED, Disc.EMPTY, Disc.RED, Disc.YELLOW, Disc.EMPTY, Disc.RED),
            Arrays.asList(Disc.RED, Disc.YELLOW, Disc.EMPTY, Disc.RED, Disc.YELLOW, Disc.YELLOW, Disc.YELLOW)
    );

    private ScoreDiagnonalForward scoreDiagonalForward;

    @Test
    void calculate() {
        scoreDiagonalForward = new ScoreDiagnonalForward(board);

        List<Result> scores = new ArrayList<>(scoreDiagonalForward.calculate());
        Collections.sort(scores);


    }
}
