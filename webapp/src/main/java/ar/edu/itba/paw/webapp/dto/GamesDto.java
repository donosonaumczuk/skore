package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.models.Game;

import java.util.LinkedList;
import java.util.List;

public class GamesDto {

    private List<List<GameDto>> allGames;

    public GamesDto(List<List<Game>> games) {

        this.allGames = new LinkedList<>();
        games.forEach(gameList -> {
            List<GameDto> currentGames = new LinkedList<>();
            gameList.forEach(game -> currentGames.add(GameDto.from(game)));
            allGames.add(currentGames);
        });
    }

    public static GamesDto from(List<List<Game>> games) {
        return new GamesDto(games);
    }

    public List<List<GameDto>> getAllGames() {
        return allGames;
    }
}
