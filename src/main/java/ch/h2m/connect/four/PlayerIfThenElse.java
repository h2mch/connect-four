package ch.h2m.connect.four;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import ch.h2m.connect.four.model.Disc;
import ch.h2m.connect.four.model.Result;
import ch.h2m.connect.four.score.ScoreDiagnonalBackward;
import ch.h2m.connect.four.score.ScoreDiagnonalForward;
import ch.h2m.connect.four.score.ScoreHorizontal;
import ch.h2m.connect.four.score.ScoreVertical;

public class PlayerIfThenElse implements Player {


    private static Logger logger = LoggerFactory.getLogger(Start.class);
    private final UUID gameId;
    private final String playerId;
    private final Connect4Client connect4Client;
    private final Gson gson;


    public PlayerIfThenElse(UUID gameId, String playerId, String url) {
        this.gameId = gameId;
        this.playerId = playerId;
        connect4Client = new Connect4Client(url);
        gson = new Gson();
    }

    @Override
    public String getPlayerId() {
        return playerId;
    }

    private int chooseColumn(List<Result> decisionBase, Disc myColor) {

        List<Result> myMaxScores = new ArrayList<>();
        List<Result> otherMaxScores = new ArrayList<>();

        Map<Integer, Integer> columnScoreSum = new HashMap<>();
        int myMaxScore = 0;
        int otherMaxScore = 0;

        for (Result result : decisionBase) {
            if (result.color == myColor) {
                myMaxScore = getMaxScore(myMaxScores, myMaxScore, result);
                Integer sum = columnScoreSum.get(result.column);
                if (sum == null) {
                    sum = 0;
                }
                columnScoreSum.put(result.column, sum + myMaxScore);
            } else {
                otherMaxScore = getMaxScore(otherMaxScores, otherMaxScore, result);
            }
        }

        Random random = new Random();
        if (otherMaxScore > myMaxScore) {
            Result result = otherMaxScores.get(random.nextInt(otherMaxScores.size()));
            return result.column;
        } else if (!myMaxScores.isEmpty()) {

            int maxTotalScoreColumn = 0;
            int maxTotalScore = 0;
            for (Result maxScore : myMaxScores) {
                int currentTotal = columnScoreSum.get(maxScore.column);
                if (currentTotal > maxTotalScore) {
                    maxTotalScoreColumn = maxScore.column;
                    maxTotalScore = currentTotal;
                }
            }

            return maxTotalScoreColumn;
        }
        int randomColumn = random.nextInt(7);
        return randomColumn;
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


                    List<Result> scores = new ArrayList<>();
                    Collection<Result> singleScores;

                    ScoreVertical scoreVertical = new ScoreVertical(typedBoard);
                    singleScores = scoreVertical.calculate();
                    scores.addAll(singleScores);
                    logger.debug("Vertical: {}", singleScores);

                    ScoreHorizontal scoreHorizontal = new ScoreHorizontal(typedBoard);
                    singleScores = scoreHorizontal.calculate();
                    scores.addAll(singleScores);
                    logger.debug("Horizontal: {}", singleScores);

                    ScoreDiagnonalBackward scoreDiagnonalBackward = new ScoreDiagnonalBackward(typedBoard);
                    singleScores = scoreDiagnonalBackward.calculate();
                    scores.addAll(singleScores);
                    logger.debug("Diagonal backward: {}", singleScores);

                    ScoreDiagnonalForward scoreDiagnonalForward = new ScoreDiagnonalForward(typedBoard);
                    singleScores = scoreDiagnonalForward.calculate();
                    scores.addAll(singleScores);
                    logger.debug("Diagonal forward: {}", singleScores);

                    int nextDisc = chooseColumn(scores, myDisc);

                    connect4Client.dropDisc(gameId, playerId, nextDisc);


                } else {

                    Thread.sleep(100);
                }
                game = connect4Client.getGameState(gameId);
            }
            logger.info("Game finished. winner '{}'", game.get("winner").getAsString());

        } catch (Exception ex) {
            logger.error("upps", ex);
        }
    }

    private int getMaxScore(List<Result> scores, int maxScore, Result result) {
        if (scores.isEmpty()) {
            scores.add(result);
            maxScore = result.score;
        } else if (scores.get(0).score == result.score) {
            scores.add(result);
        } else if (scores.get(0).score < result.score) {
            scores.clear();
            scores.add(result);
            maxScore = result.score;
        }
        return maxScore;
    }


}


