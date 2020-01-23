package ar.edu.itba.paw.models;

import java.util.Arrays;
import java.util.List;

public class GameSort extends Sort {

    static private List<String> queryFields = Arrays.asList("games.primaryKey.startTime",
            "games.primaryKey.finishTime", "games.primaryKey.team1.sport.playerQuantity",
            "games.place.country", "games.place.state", "games.place.city", "games.primaryKey.team1.sport");
    static private List<String> validFields = Arrays.asList("startTime", "finishTime", "quantity",
            "country", "state", "city", "sport");

    public GameSort(String string) {
        super(string, validFields, queryFields);
    }
}
