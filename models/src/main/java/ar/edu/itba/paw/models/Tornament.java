package ar.edu.itba.paw.models;

import java.util.List;

public class Tornament {
    private String name;
    private String type;
    private List<Game> games;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public List<Game> getGames() {
        return games;
    }

    public void setGames(List<Game> games) {
        this.games = games;
    }
}
