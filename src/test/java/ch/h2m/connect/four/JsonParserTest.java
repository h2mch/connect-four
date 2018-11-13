package ch.h2m.connect.four;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import ch.h2m.connect.four.model.Disc;

public class JsonParserTest {

    private String game = "{\n" +
            "  \"currentPlayerId\": \"Bob\",\n" +
            "  \"players\": [\n" +
            "      {\n" +
            "        \"playerId\": \"Alice\",\n" +
            "        \"disc\": \"RED\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"playerId\": \"Bob\",\n" +
            "        \"disc\": \"YELLOW\"\n" +
            "      }\n" +
            "  ],\n" +
            "  \"board\": [\n" +
            "    [\"EMPTY\",\"EMPTY\",\"EMPTY\",    \"EMPTY\",\"EMPTY\",\"EMPTY\",\"EMPTY\"],\n" +
            "    [\"EMPTY\",\"EMPTY\",\"EMPTY\",    \"EMPTY\",\"EMPTY\",\"EMPTY\",\"EMPTY\"],\n" +
            "    [\"EMPTY\",\"EMPTY\",\"EMPTY\",    \"EMPTY\",\"EMPTY\",\"EMPTY\",\"YELLOW\"],\n" +
            "    [\"YELLOW\",\"EMPTY\",\"EMPTY\",   \"EMPTY\",\"EMPTY\",\"EMPTY\",\"RED\"],\n" +
            "    [\"YELLOW\",\"EMPTY\",\"YELLOW\",  \"EMPTY\",\"EMPTY\",\"EMPTY\",\"RED\"],\n" +
            "    [\"RED\"   ,\"RED\"  ,\"RED\",     \"EMPTY\",\"EMPTY\",\"EMPTY\",\"RED\"]\n" +
            "  ],\n" +
            "  \"finished\": false\n" +
            "}";

    @Test
    void parseGame() {

        JsonParser parser = new JsonParser();
        JsonElement element = parser.parse(game);
        JsonObject json = element.getAsJsonObject();

        System.out.println(json.getAsJsonArray("board").toString());

        Gson gson = new Gson();

        List<List<Disc>> typedBoard = gson.fromJson(json.getAsJsonArray("board"), new TypeToken<ArrayList<ArrayList<Disc>>>() {
        }.getType());
/*
        int[] score = new ScoreVertical(typedBoard).calculate(Disc.RED);
        System.out.println("Vertical");
        for (int v : score) {
            System.out.println(v);
        }

        System.out.println("Horizinal");

        score = new ScoreHorizontalBasic(typedBoard).calculate(Disc.RED);
        for (int v : score) {
            System.out.println(v);
        }
*/

        //checkDiagonalSlash(typedBoard);
        //checkDiagonalBackSlash(typedBoard);


    }


    private double[] checkHorizontalSecondTry(List<List<Disc>> typedBoard, Disc myDisc) {
        int width = typedBoard.get(0).size();
        double[] scores = new double[width];
        int height = typedBoard.size();

        for (int columnNumber = 0; columnNumber < width; columnNumber++) {
            int rowToWatch = 0;
            for (int rowNumber = height; rowNumber >= 0; rowNumber--) {
                if (typedBoard.get(columnNumber).get(rowNumber) != Disc.EMPTY) {
                    continue;
                }
                rowToWatch = rowNumber;
                break;
            }

            //check Right Side
            try {
                if (Disc.EMPTY == typedBoard.get(columnNumber + 1).get(rowToWatch)) {
                    if (Disc.EMPTY != typedBoard.get(columnNumber + 1).get(rowToWatch - 1)) {

                    }
                }
            } catch (IndexOutOfBoundsException ie) {
                // out of boarder abort
            }

            //check Left Side
            try {
                if (Disc.EMPTY == typedBoard.get(columnNumber - 1).get(rowToWatch)) {
                    if (Disc.EMPTY != typedBoard.get(columnNumber - 1).get(rowToWatch - 1)) {

                    }
                }
            } catch (IndexOutOfBoundsException ie) {
                // out of boarder abort
            }
        }

        return scores;
    }

    private double[] checkHorizontalWrongTry(List<List<Disc>> typedBoard) {
        int width = typedBoard.get(0).size();
        int height = typedBoard.size();
        double[] scores = new double[width];

        for (int columnNumber = height - 1; columnNumber >= 0; columnNumber--) {

            List<Disc> discs = typedBoard.get(columnNumber);

            for (int startRow = 0; startRow < width; startRow++) {
                double score = 0;
                Disc lastDisc = null;
                for (int i = startRow; i < width; i++) {

                    Disc currentDisc = discs.get(i);
                    if (currentDisc == Disc.EMPTY) {
                        continue;
                    }
                    if (lastDisc == null) {
                        lastDisc = currentDisc;
                    }
                    if (currentDisc == lastDisc) {
                        score += 0.33;
                    } else {
                        score = 0.00;
                        continue;
                    }

                    if (score == 0.99) {
                        int rightSide = i + 1;
                        if (rightSide < width && discs.get(rightSide) == Disc.EMPTY) {
                            if (columnNumber == height - 1) {
                                // you can finish the game
                                scores[rightSide] = score;
                                continue;
                            }

                            if (typedBoard.get(columnNumber - 1).get(rightSide) != Disc.EMPTY) {
                                // you can finish the game
                                scores[rightSide] = score;
                                continue;
                            }
                        }

                        int leftSide = i - 3;
                        if (leftSide > 0 && discs.get(leftSide) == Disc.EMPTY) {
                            if (columnNumber == height - 1) {
                                scores[leftSide] = score;
                                continue;
                                // you can finish the game
                            }
                            if (typedBoard.get(columnNumber - 1).get(leftSide) != Disc.EMPTY) {
                                // you can finish the game
                                scores[leftSide] = score;
                                continue;
                            }
                        }
                    }
                }
            }

        }
        return scores;
    }


}
