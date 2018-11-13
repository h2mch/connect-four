package ch.h2m.connect.four;

import com.google.gson.JsonObject;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class Connect4ClientTest {

    private static Logger logger = LoggerFactory.getLogger(Connect4ClientTest.class);

    private Connect4Client connect4Client;

    @BeforeEach
    void setUp() {
        connect4Client = new Connect4Client();
    }

    @Test
    void startGame() throws IOException, InterruptedException {

        Optional<UUID> game = connect4Client.join("Heinz");
        assertFalse(game.isPresent(), "No game with only one Player");

        game = connect4Client.join("Manuela");
        assertTrue(game.isPresent(), "Game can be started with two player");

        UUID gameId = game.get();
        logger.info("Game started with id '{}'", gameId);

        JsonObject gameState = connect4Client.getGameState(gameId);
        logger.info(gameState.toString());
    }


    private static UUID currentGame = UUID.fromString("eb9293dd-f56f-4073-a47d-ce81c1d5957f");


    @Test
    void dropDisk() throws IOException, InterruptedException {
        JsonObject round = connect4Client.dropDisc(currentGame, "Heinz", 3);
        logger.info(round.toString());

        round = connect4Client.dropDisc(currentGame, "Heinz", 3);
        logger.info(round.toString());

        round = connect4Client.dropDisc(currentGame, "Manuela", 2);
        logger.info(round.toString());

        round = connect4Client.dropDisc(currentGame, "Heinz", 1);
        logger.info(round.toString());

        round = connect4Client.dropDisc(currentGame, "Manuela", 2);
        logger.info(round.toString());
    }
}