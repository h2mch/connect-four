package ch.h2m.connect.four;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Test with new httprequest api from java11.
 *
 * https://dzone.com/articles/java-11-standardized-http-client-api
 */
public class Start {

    private static Logger logger = LoggerFactory.getLogger(Start.class);

    public static void main(String[] args) throws IOException, InterruptedException {


        Connect4Client connect4Client = new Connect4Client();

        String namePlayerOne = "Random";
        String namePlayerTwo = "Basic";

        for (int i = 0; i < 200; i++) {
            Optional<UUID> gameForPlayerOne = connect4Client.join(namePlayerOne);
            Optional<UUID> gameForPlayerTwo = connect4Client.join(namePlayerTwo);

            UUID gameId = gameForPlayerTwo.get();

            while(gameForPlayerOne.isEmpty()) {
                gameForPlayerOne = connect4Client.join(namePlayerOne);
                Thread.sleep(200);
            }

            while(gameForPlayerTwo.isEmpty()) {
                gameForPlayerTwo = connect4Client.join(namePlayerTwo);
                Thread.sleep(200);
            }

            ExecutorService executor = Executors.newFixedThreadPool(2);
            Future<?> resultOne = executor.submit(new PlayerRandom(gameForPlayerOne.get(), "Random"));
            Future<?> resultTwo = executor.submit(new PlayerIfThenElse(gameForPlayerTwo.get(), "IfThenElse"));

            while (!resultOne.isDone() && !resultTwo.isDone()) {
                Thread.sleep(1000);
            }
            executor.shutdown();
        }

    }

}
