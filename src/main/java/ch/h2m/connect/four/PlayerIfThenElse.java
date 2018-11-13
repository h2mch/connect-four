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


    public PlayerIfThenElse(UUID gameId, String playerId) {
        this.gameId = gameId;
        this.playerId = playerId;
        connect4Client = new Connect4Client("https://connect-four-challenge.herokuapp.com/api/v1/players");
        gson = new Gson();
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
        if (otherMaxScore > myMaxScore) {
            Result result = otherMaxScores.get(random.nextInt(otherMaxScores.size()));
            return result.column;
        } else if (!myMaxScores.isEmpty()) {
            Result result = myMaxScores.get(random.nextInt(myMaxScores.size()));
            return result.column;
        }
        int randomColumn = random.nextInt(7);
        return randomColumn;
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


                    List<Result> scores = new ArrayList<>();
                    List<Result> singleScores;

                    singleScores = new ScoreVertical(typedBoard).calculate();
                    scores.addAll(singleScores);
                    logger.debug("Vertical: {}", singleScores);

                    singleScores = new ScoreHorizontal(typedBoard).calculate();
                    scores.addAll(singleScores);
                    logger.debug("Horizontal: {}", singleScores);

                    singleScores = new ScoreDiagnonalBackward(typedBoard).calculate();
                    scores.addAll(singleScores);
                    logger.debug("Diagonal backward: {}", singleScores);

                    singleScores = new ScoreDiagnonalForward(typedBoard).calculate();
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


}


