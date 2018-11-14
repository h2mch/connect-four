package ch.h2m.connect.four;

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

    private static final String remoteUrl = "https://connect-four-challenge.herokuapp.com/api/v1/players";
    private static final String localUrl = "http://localhost:8080/api/v1/players";


    public static void main(String[] args) throws IOException, InterruptedException {

        String url = localUrl;
        Connect4Client connect4Client = new Connect4Client(url);

        String namePlayerOne = "heinz";
        String namePlayerTwo = "anna";

        for (int i = 0; i < 200; i++) {
            Optional<UUID> gameForPlayerOne = connect4Client.join(namePlayerOne);
            Optional<UUID> gameForPlayerTwo = connect4Client.join(namePlayerTwo);

            while (gameForPlayerOne.isEmpty()) {
                gameForPlayerOne = connect4Client.join(namePlayerOne);
                Thread.sleep(200);
            }

            while (gameForPlayerTwo.isEmpty()) {
                gameForPlayerTwo = connect4Client.join(namePlayerTwo);
                Thread.sleep(200);
            }

            ExecutorService executor = Executors.newFixedThreadPool(2);
            Future<?> resultOne = executor.submit(new PlayerRandom(gameForPlayerOne.get(), namePlayerOne, url));
            Future<?> resultTwo = executor.submit(new PlayerIfThenElse(gameForPlayerTwo.get(), namePlayerTwo, url));

//            while (!resultTwo.isDone()) {
            while (!resultOne.isDone() && !resultTwo.isDone()) {
                Thread.sleep(1000);
            }
            executor.shutdown();
        }

    }

}
