package ch.h2m.connect.four;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;
import java.util.UUID;

import ch.h2m.connect.four.model.Disc;

public interface Player extends Runnable {

    String getPlayerId();

    default Disc getDiscColor(JsonArray players) {
        Disc myDisc = null;
        for (JsonElement player : players) {
            JsonObject asJsonObject = player.getAsJsonObject();
            if (getPlayerId().equals(asJsonObject.get("playerId").getAsString())) {
                myDisc = Disc.valueOf(asJsonObject.get("disc").getAsString());
            }
        }
        return myDisc;
    }


}
