package ch.h2m.connect.four;


import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import ch.h2m.connect.four.model.Disc;
import ch.h2m.connect.four.model.Result;


public class HorizontalTest {

    private List<List<Disc>> board = Arrays.asList(
            Arrays.asList(Disc.EMPTY, Disc.EMPTY, Disc.EMPTY, Disc.YELLOW, Disc.EMPTY, Disc.EMPTY, Disc.EMPTY),
            Arrays.asList(Disc.EMPTY, Disc.EMPTY, Disc.EMPTY, Disc.YELLOW, Disc.EMPTY, Disc.EMPTY, Disc.EMPTY),
            Arrays.asList(Disc.EMPTY, Disc.EMPTY, Disc.EMPTY, Disc.YELLOW, Disc.EMPTY, Disc.EMPTY, Disc.EMPTY),
            Arrays.asList(Disc.YELLOW, Disc.RED, Disc.EMPTY, Disc.RED, Disc.YELLOW, Disc.EMPTY, Disc.EMPTY),
            Arrays.asList(Disc.YELLOW, Disc.RED, Disc.EMPTY, Disc.RED, Disc.YELLOW, Disc.EMPTY, Disc.RED),
            Arrays.asList(Disc.RED, Disc.YELLOW, Disc.EMPTY, Disc.RED, Disc.YELLOW, Disc.YELLOW, Disc.YELLOW)
    );

    private ScoreHorizontal scoreHorizontal;

    @Test
    void calculate() {
        scoreHorizontal = new ScoreHorizontal(board);

        List<Result> scores = scoreHorizontal.calculate();
        Collections.sort(scores);


    }
}
