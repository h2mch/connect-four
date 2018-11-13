package ch.h2m.connect.four;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import ch.h2m.connect.four.model.Disc;

public class PlayerExtended implements Player {


    private final UUID gameId;
    private final String playerId;
    private final Connect4Client connect4Client;
    private final Gson gson;

    private static Logger logger = LoggerFactory.getLogger(Start.class);


    public PlayerExtended(UUID gameId, String playerId) {
        this.gameId = gameId;
        this.playerId = playerId;
        connect4Client = new Connect4Client();
        gson = new Gson();
    }

    @Override
    public String getPlayerId() {
        return playerId;
    }

    @Override
    public void run() {
        try {
            JsonObject game = connect4Client.getGameState(gameId);
            JsonArray players = game.get("players").getAsJsonArray();
            Disc myDisc = getDiscColor(players);

            logger.info("Start Game for {} ({})", playerId, myDisc);
            while (!game.get("finished").getAsBoolean()) {
                if (playerId.equals(game.get("currentPlayerId").getAsString())) {

                    List<List<Disc>> typedBoard = gson.fromJson(game.getAsJsonArray("board"), new TypeToken<ArrayList<ArrayList<Disc>>>() {
                    }.getType());

                    //TODO Try Catch Everything, select random
                    int nextDisc = calc(typedBoard, myDisc);
                    connect4Client.dropDisc(gameId, playerId, nextDisc);
                } else {

                    Thread.sleep(200);
                }
                game = connect4Client.getGameState(gameId);
            }
            logger.info("Game finished. winner '{}'", game.get("winner").getAsString());

        } catch (Exception ex) {
            logger.error("upps", ex);
        }

    }


    private static final int DISC_QUANTIFIER = 1;

    public int calc(List<List<Disc>> board, Disc myDisc) {
        List<Result> scores = new ArrayList<>();
        List<Result> singleScores;

        singleScores = checkVertical(board);
        scores.addAll(singleScores);
        System.out.println("Vertical: " + singleScores);

        singleScores = checkHorizontal(board);
        scores.addAll(singleScores);
        System.out.println("Horizontal: " + singleScores);

        singleScores = checkDiagonalBackward(board);
        scores.addAll(singleScores);
        System.out.println("Diagonal backward: " + singleScores);

        singleScores = checkDiagonalForward(board);
        scores.addAll(singleScores);
        System.out.println("Diagonal forward: " + singleScores);

        return chooseColumn(scores, myDisc);

    }

    public List<Result> checkVertical(List<List<Disc>> board) {
        List<Result> decisionBase = new ArrayList<>();
        int height = board.size();
        int width = board.get(0).size();

        for (int column = 0; column < width; column++) {
            Disc disc, lastDisc = null;
            int score = 0;

            for (int row = height - 1; row >= 0; row--) {
                disc = board.get(row).get(column);
                score = setScore(disc, lastDisc, score);

                if ((score > 0) && (row > 0) && isNextDiscInColumn(board, row - 1, column)) {
                    decisionBase.add(new Result(column, score, disc));

                }
                lastDisc = disc;
            }
        }
        return decisionBase;
    }


    public List<Result> checkHorizontal(List<List<Disc>> board) {
        List<Result> decisionBase = new ArrayList<>();
        int height = board.size();
        int width = board.get(0).size();

        for (int row = 0; row < height; row++) {
            Disc disc, lastDisc = null;
            int score = 0;
            for (int column = 0; column < width; column++) {
                disc = board.get(row).get(column);

                //set current score in-a-row
                score = setScore(disc, lastDisc, score);

                //check if four are possible.
                if (score == 3 * DISC_QUANTIFIER) {
                    //check right
                    if ((column < width - 1) && isNextDiscInColumn(board, row, column + 1)) {
                        decisionBase.add(new Result(column + 1, 3 * DISC_QUANTIFIER, disc));
                    }
                    // check left
                    if ((column > 2) && isNextDiscInColumn(board, row, column - 3)) {
                        decisionBase.add(new Result(column - 3, 3 * DISC_QUANTIFIER, disc));
                    }
                    score = 0;
                }
                if (score > 0) {
                    //check right and next
                    if ((column < width - 2) && isNextDiscInColumn(board, row, column + 1)) {
                        decisionBase.add(new Result(column + 1, score, disc));
                        if (board.get(row).get(column + 2) == disc) {
                            //Empty Disc between
                            decisionBase.add(new Result(column + 1, score + DISC_QUANTIFIER, disc));
                        }
                    }
                    //check left and next
                    if ((column > 2) && isNextDiscInColumn(board, row, column - 2)) {
                        decisionBase.add(new Result(column - 2, score, disc));
                        if (board.get(row).get(column - 3) == disc) {
                            //Empty Disc between
                            decisionBase.add(new Result(column - 2, score + DISC_QUANTIFIER, disc));
                        }
                    }
                }
                lastDisc = disc;
            }
        }
        return decisionBase;
    }

    public List<Result> checkDiagonalBackward(List<List<Disc>> board) {
        List<Result> decisionBase = new ArrayList<>();
        int height = board.size();
        int width = board.get(0).size();

        Disc disc, lastDisc = null;
        int row;
        int column;
        int score;

        for (int i = 0; i < width + height - 1; i++) {
            row = Math.max(height - 1 - i, 0);
            column = Math.max(i - height, 0);
            score = 0;
            lastDisc = null;
            while (row < height && column < width) {
                disc = board.get(row).get(column);

                //set current score , same as in-a-row
                score = setScore(disc, lastDisc, score);

                if (score == 3 * DISC_QUANTIFIER) {
                    // check lower right
                    if ((row < height - 1) && (column < width - 1) && isNextDiscInColumn(board, row + 1, column + 1)) {
                        decisionBase.add(new Result(column + 1, 3 * DISC_QUANTIFIER, disc));
                    }
                    // check upper left
                    if ((row > 2) && (column > 2) && isNextDiscInColumn(board, row - 3, column - 3)) {
                        decisionBase.add(new Result(column - 3, 3 * DISC_QUANTIFIER, disc));
                    }
                    score = 0;
                }
                if (score > 0) {
                    //check lower right
                    if ((row < height - 2) && (column < width - 2) && isNextDiscInColumn(board, row + 1, column + 1)) {
                        decisionBase.add(new Result(column + 1, score, disc));
                        if (board.get(row + 2).get(column + 2) == disc) {
                            decisionBase.add(new Result(column + 1, score + DISC_QUANTIFIER, disc));
                        }
                    }
                    //check upper left
                    if ((row > 2) && (column > 2) && isNextDiscInColumn(board, row - 2, column - 2)) {
                        decisionBase.add(new Result(column - 2, score, disc));
                        if (board.get(row - 3).get(column - 3) == disc) {
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

    public List<Result> checkDiagonalForward(List<List<Disc>> board) {
        List<Result> decisionBase = new ArrayList<>();
        int height = board.size();
        int width = board.get(0).size();

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
                disc = board.get(row).get(column);

                //set current score , same as in-a-row
                score = setScore(disc, lastDisc, score);

                if (score == 3 * DISC_QUANTIFIER) {
                    // check upper right
                    if ((row > 0) && (column < width - 1) && isNextDiscInColumn(board, row - 1, column + 1)) {
                        decisionBase.add(new Result(column + 1, 3 * DISC_QUANTIFIER, disc));
                    }
                    // check lower left
                    if ((row < height - 3) && (column > 2) && isNextDiscInColumn(board, row + 3, column - 3)) {
                        decisionBase.add(new Result(column - 3, 3 * DISC_QUANTIFIER, disc));
                    }
                    score = 0;
                }
                if (score > 0) {
                    //check upper right
                    if ((row > 2) && (column < width - 2) && isNextDiscInColumn(board, row - 1, column + 1)) {
                        decisionBase.add(new Result(column + 1, score, disc));
                        if (board.get(row - 2).get(column + 2) == disc) {
                            decisionBase.add(new Result(column + 1, score + DISC_QUANTIFIER, disc));
                        }
                    }
                    //check lower left
                    if ((row < height - 2) && (column > 2) && isNextDiscInColumn(board, row + 2, column - 2)) {
                        decisionBase.add(new Result(column - 2, score, disc));
                        if (board.get(row + 3).get(column - 3) == disc) {
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

    private boolean isNextDiscInColumn(List<List<Disc>> board, int row, int column) {
        try {
            if (row == board.size() - 1) { // first line, no need to check if row below is empty
                return board.get(row).get(column).isEmpty();
            } else {
                return board.get(row).get(column).isEmpty()
                        && board.get(row + 1).get(column).isNonEmpty();
            }
        } catch (Exception e) {
            logger.error("row:{} column:{}", row, column);
            return false;
        }

    }

    private int setScore(Disc disc, Disc lastDisc, int score) {
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

    private int chooseColumn(List<Result> decisionBase, Disc myColor) {

        List<Result> myMaxScores = new ArrayList<>();
        List<Result> otherMaxScores = new ArrayList<>();

        int myMaxScore = 0;
        int otherMaxScore = 0;

        for (Result result : decisionBase) {
            if (result.color == myColor) {
                if (myMaxScores.isEmpty()) {
                    myMaxScores.add(result);
                    myMaxScore = result.score;
                } else if (myMaxScores.get(0).score == result.score) {
                    myMaxScores.add(result);
                } else if (myMaxScores.get(0).score < result.score) {
                    myMaxScores.clear();
                    myMaxScores.add(result);
                    myMaxScore = result.score;
                }
            } else {
                if (otherMaxScores.isEmpty()) {
                    otherMaxScores.add(result);
                    otherMaxScore = result.score;
                } else if (otherMaxScores.get(0).score == result.score) {
                    otherMaxScores.add(result);
                } else if (otherMaxScores.get(0).score < result.score) {
                    otherMaxScores.clear();
                    otherMaxScores.add(result);
                    otherMaxScore = result.score;
                }
            }
        }

        Random random = new Random();
        System.out.print("Selected: ");
        if (otherMaxScore > myMaxScore) {
            Result result = otherMaxScores.get(random.nextInt(otherMaxScores.size()));
            System.out.println(result);
            return result.column;
        } else if (!myMaxScores.isEmpty()) {
            Result result = myMaxScores.get(random.nextInt(myMaxScores.size()));
            System.out.println(result);
            return result.column;
        }
        int randomColumn = random.nextInt(7);
        System.out.println("random: " + randomColumn);
        return randomColumn;
    }

    private class Result {
        int column;
        int score;
        Disc color;

        public Result(int column, int score, Disc disc) {
            this.column = column;
            this.score = score;
            this.color = disc;
        }

        @Override
        public String toString() {
            return column + "/" + color + ": " + score;
        }

    }
}


