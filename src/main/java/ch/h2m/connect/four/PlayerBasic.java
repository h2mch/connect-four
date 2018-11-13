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
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import ch.h2m.connect.four.model.Disc;

public class PlayerBasic implements Player {


    private final UUID gameId;
    private final String playerId;
    private final Connect4Client connect4Client;
    private final Gson gson;

    private static Logger logger = LoggerFactory.getLogger(Start.class);


    public PlayerBasic(UUID gameId, String playerId) {
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

                    int[] scoreVertical = new ScoreVertical(typedBoard).calculate(myDisc);

                    int columnNumberVertical = 0;
                    int maxValueVertical = scoreVertical[0];
                    for (int i = 1; i < scoreVertical.length; i++) {
                        if (maxValueVertical < Math.abs(scoreVertical[i])){
                            maxValueVertical = Math.abs(scoreVertical[i]);
                            columnNumberVertical = i;
                        }
                    }

                    int[] scoreHorizontal = new ScoreHorizontalBasic(typedBoard).calculate(myDisc);
                    int columnNumberHorizontal = 0;
                    int maxValueHorizontal = scoreHorizontal[0];
                    for (int i = 1; i < scoreHorizontal.length; i++) {
                        if (maxValueHorizontal < Math.abs(scoreHorizontal[i])){
                            maxValueHorizontal = Math.abs(scoreHorizontal[i]);
                            columnNumberHorizontal = i;

                        }
                    }
                    logger.info("vertical {}({}) - horizontal {}({})", maxValueVertical, columnNumberVertical, maxValueHorizontal, columnNumberHorizontal);

                    int nextDisc;
                    if (maxValueHorizontal == maxValueVertical){

                        List<Disc> columns = typedBoard.get(0);
                        List<Integer> validMoves = IntStream.range(0, columns.size())
                                .boxed()
                                .filter(column -> columns.get(column).isEmpty())
                                .collect(Collectors.toList());

                        nextDisc = validMoves.get(new Random().nextInt(validMoves.size()));


                    } else if (maxValueHorizontal > maxValueVertical){
                        nextDisc = columnNumberHorizontal;
                    } else {
                        nextDisc = columnNumberVertical;
                    }
                    connect4Client.dropDisc(gameId, playerId, nextDisc);
                } else {

                    Thread.sleep(500);
                }
                game = connect4Client.getGameState(gameId);

            }
            logger.info("Game finished. winner '{}'", game.get("winner").getAsString());

        } catch (Exception ex) {
            logger.error("upps", ex);
        }

    }

}
