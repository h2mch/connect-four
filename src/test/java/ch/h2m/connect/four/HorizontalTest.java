package ch.h2m.connect.four;


import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import ch.h2m.connect.four.model.Disc;
import ch.h2m.connect.four.model.Result;
import ch.h2m.connect.four.score.ScoreHorizontal;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class HorizontalTest {

    private List<List<Disc>> board = Arrays.asList(
            Arrays.asList(Disc.EMPTY, Disc.EMPTY, Disc.EMPTY, Disc.EMPTY, Disc.EMPTY, Disc.EMPTY, Disc.EMPTY),
            Arrays.asList(Disc.EMPTY, Disc.EMPTY, Disc.EMPTY, Disc.EMPTY, Disc.EMPTY, Disc.EMPTY, Disc.EMPTY),
            Arrays.asList(Disc.EMPTY, Disc.EMPTY, Disc.EMPTY, Disc.EMPTY, Disc.EMPTY, Disc.EMPTY, Disc.EMPTY),
            Arrays.asList(Disc.EMPTY, Disc.EMPTY, Disc.EMPTY, Disc.EMPTY, Disc.EMPTY, Disc.EMPTY, Disc.EMPTY),
            Arrays.asList(Disc.EMPTY, Disc.EMPTY, Disc.EMPTY, Disc.EMPTY, Disc.EMPTY, Disc.EMPTY, Disc.EMPTY),
            Arrays.asList(Disc.EMPTY, Disc.YELLOW, Disc.EMPTY, Disc.EMPTY, Disc.EMPTY, Disc.YELLOW, Disc.EMPTY)
    );


    private ScoreHorizontal scoreHorizontal;

    @Test
    void calculate() {
        scoreHorizontal = new ScoreHorizontal(board);

        List<Result> scores = scoreHorizontal.calculate();
        Collections.sort(scores);

        assertEquals(0, scores.get(0).column);
        assertEquals(Disc.YELLOW, scores.get(0).color);
        assertEquals(1, scores.get(0).score);


        assertEquals(2, scores.get(1).column);
        assertEquals(Disc.YELLOW, scores.get(1).color);
        assertEquals(1, scores.get(1).score);


    }
}
