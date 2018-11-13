package ch.h2m.connect.four;


import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import ch.h2m.connect.four.model.Disc;
import ch.h2m.connect.four.model.Result;
import ch.h2m.connect.four.score.ScoreVertical;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class VerticalTest {


    private List<List<Disc>> board = Arrays.asList(
            Arrays.asList(Disc.EMPTY, Disc.EMPTY, Disc.EMPTY, Disc.YELLOW, Disc.EMPTY, Disc.EMPTY, Disc.EMPTY),
            Arrays.asList(Disc.EMPTY, Disc.EMPTY, Disc.EMPTY, Disc.YELLOW, Disc.EMPTY, Disc.EMPTY, Disc.EMPTY),
            Arrays.asList(Disc.EMPTY, Disc.EMPTY, Disc.EMPTY, Disc.YELLOW, Disc.EMPTY, Disc.EMPTY, Disc.EMPTY),
            Arrays.asList(Disc.YELLOW, Disc.RED, Disc.EMPTY, Disc.RED, Disc.YELLOW, Disc.EMPTY, Disc.EMPTY),
            Arrays.asList(Disc.YELLOW, Disc.RED, Disc.EMPTY, Disc.RED, Disc.YELLOW, Disc.EMPTY, Disc.RED),
            Arrays.asList(Disc.RED, Disc.YELLOW, Disc.EMPTY, Disc.RED, Disc.YELLOW, Disc.YELLOW, Disc.YELLOW)

    );

    private ScoreVertical scoreVertical;

    @Test
    void calculate() {
        scoreVertical = new ScoreVertical(board);

        List<Result> scores = scoreVertical.calculate();

        assertEquals(0, scores.get(0).column);
        assertEquals(Disc.YELLOW, scores.get(0).color);
        assertEquals(2, scores.get(0).score);

        assertEquals(1, scores.get(1).column);
        assertEquals(Disc.RED, scores.get(1).color);
        assertEquals(2, scores.get(1).score);


        assertEquals(4, scores.get(2).column);
        assertEquals(Disc.YELLOW, scores.get(2).color);
        assertEquals(3, scores.get(2).score);


        assertEquals(5, scores.get(3).column);
        assertEquals(Disc.YELLOW, scores.get(3).color);
        assertEquals(1, scores.get(3).score);
    }
}
