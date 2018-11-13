package ch.h2m.connect.four;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
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

public class PlayerRandom implements Player {


    private final UUID gameId;
    private final String playerId;
    private final Connect4Client connect4Client;
    private final Gson gson;

    private static Logger logger = LoggerFactory.getLogger(Start.class);


    public PlayerRandom(UUID gameId, String playerId) {
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
                //    connect4Client.dropDisc(gameId, playerId, new Random().nextInt(4));

                    List<List<Disc>> typedBoard = gson.fromJson(game.getAsJsonArray("board"), new TypeToken<ArrayList<ArrayList<Disc>>>() {
                    }.getType());

                    List<Disc> columns = typedBoard.get(0);
                    //force the middle 4 rows
                    List<Integer> validMoves = IntStream.range(2, 6)
                            .boxed()
                            .filter(column -> columns.get(column).isEmpty())
                            .collect(Collectors.toList());

                    if (validMoves.isEmpty()){
                        validMoves = IntStream.range(0, columns.size())
                                .boxed()
                                .filter(column -> columns.get(column).isEmpty())
                                .collect(Collectors.toList());
                    }

                    Random rand = new Random();
                    int nextMove = validMoves.get(rand.nextInt(validMoves.size()));

                    connect4Client.dropDisc(gameId, playerId, nextMove);
                } else {
                    Thread.sleep(500);
                }
                game = connect4Client.getGameState(gameId);

            }
            logger.info("Game finished. winner '{}'", game.get("winner").toString());
        } catch (Exception ex) {
            logger.error("upps", ex);
        }
    }

}
