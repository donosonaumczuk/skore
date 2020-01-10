package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.models.Game;

import java.util.LinkedList;
import java.util.List;

public class GamesDto {

    private List<GameDto> games;

    public GamesDto(List<GameDto> games) {
        this.games = games;
    }

    public static GamesDto from(List<GameDto> games) {
        return new GamesDto(games);
    }

    public List<GameDto> getGames() {
        return games;
    }
}
