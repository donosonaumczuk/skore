package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.models.Game;
import org.springframework.hateoas.Link;

import java.util.LinkedList;
import java.util.List;

public class GamesDto {

    private final List<GameDto> games;
//    private final List<Link> links; TODO update when /matches/id is created

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
