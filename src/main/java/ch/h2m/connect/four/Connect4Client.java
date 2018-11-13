package ch.h2m.connect.four;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Optional;
import java.util.UUID;


public class Connect4Client {

    private final String URL = "https://connect-four-challenge.herokuapp.com/api/v1/players";
    //        private final String URL = "http://localhost:8080/api/v1/players";
    private final HttpClient httpClient;
    private final JsonParser parser;

    private static Logger logger = LoggerFactory.getLogger(Connect4Client.class);

    public Connect4Client() {
        httpClient = HttpClient.newBuilder().build();
        parser = new JsonParser();
    }

    public JsonObject dropDisc(UUID gameUuid, String playerId, int row) throws IOException, InterruptedException {
        logger.debug("Drop disc for game '{}' on row '{}'.", gameUuid, row);

        JsonObject player = new JsonObject();
        player.addProperty("playerId", playerId);
        player.addProperty("column", row);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(URL + "/games/" + gameUuid.toString()))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(player.toString()))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        String body = response.body();
        logger.debug("Response received with status '{}'\nbody:\n{}", response.statusCode(), body);
        return parser.parse(body).getAsJsonObject();
    }

    public JsonObject getGameState(UUID gameUuid) throws IOException, InterruptedException {
        logger.debug("Get gamestate for game '{}'", gameUuid.toString());

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(URL + "/games/" + gameUuid.toString()))
                .header("Content-Type", "application/json")
                .GET()
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        String body = response.body();
        logger.debug("Response received with status '{}'\nbody:\n{}", response.statusCode(), body);
        return parser.parse(body).getAsJsonObject();
    }

    public Optional<UUID> join(String playerId) throws IOException, InterruptedException {
        logger.info("Join a game with playerId '{}'.", playerId);
        JsonObject player = new JsonObject();
        player.addProperty("playerId", playerId);

        String playerString = player.toString();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(URL + "/join"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(playerString))
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        String body = response.body();
        logger.info("Response received with status '{}'\nbody:\n{}", response.statusCode(), body);
        JsonObject game = parser.parse(body).getAsJsonObject();
        return (game.has("gameId"))
                ? Optional.of(UUID.fromString(game.get("gameId").getAsString()))
                : Optional.empty();
    }


}
