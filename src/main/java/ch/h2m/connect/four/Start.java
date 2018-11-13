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


    public static void main(String[] args) throws IOException, InterruptedException {


        Connect4Client connect4Client = new Connect4Client();

        String namePlayerOne = "heinz";
        String namePlayerTwo = "anna";

        for (int i = 0; i < 10; i++) {
            //Optional<UUID> gameForPlayerOne = connect4Client.join(namePlayerOne);
            Optional<UUID> gameForPlayerTwo = connect4Client.join(namePlayerTwo);

/*            while (gameForPlayerOne.isEmpty()) {
                gameForPlayerOne = connect4Client.join(namePlayerOne);
                Thread.sleep(200);
            }
*/
            while (gameForPlayerTwo.isEmpty()) {
                gameForPlayerTwo = connect4Client.join(namePlayerTwo);
                Thread.sleep(200);
            }

            ExecutorService executor = Executors.newFixedThreadPool(2);
//            Future<?> resultOne = executor.submit(new PlayerRandom(gameForPlayerOne.get(), namePlayerOne));
            Future<?> resultTwo = executor.submit(new PlayerIfThenElse(gameForPlayerTwo.get(), namePlayerTwo));

            while (!resultTwo.isDone()) {
//            while (!resultOne.isDone() && !resultTwo.isDone()) {
                Thread.sleep(1000);
            }
            executor.shutdown();
        }

    }

}
