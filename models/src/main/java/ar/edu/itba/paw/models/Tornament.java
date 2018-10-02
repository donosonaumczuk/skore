package ar.edu.itba.paw.models;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Tornament {
    private String name;
    private String type;
    private List<Game> games;

    public Tornament(String name, String type) {
        this.name   = name;
        this.type   = type;
        this.games  = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public List<Game> getGames() {
        return games;
    }

    public void setGames(final List<Game> games) {
        this.games = games;
    }

    public void addGame(Game game) {
        games.add(game);
    }

    @Override
    public boolean equals(Object object) {
        if(object == null || !object.getClass().equals(getClass())) {
            return false;
        }

        Tornament aTornament = ((Tornament) object);
        return getName().equals(aTornament.getName());
    }
    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
