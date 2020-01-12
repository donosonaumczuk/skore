package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.models.Game;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.hateoas.Link;

import java.util.LinkedList;
import java.util.List;

public class GameListDto {

    private final List<GameDto> games;
//    private final List<Link> links; TODO update when /matches/id is created

    public GameListDto(List<GameDto> games) {
        this.games = games;
    }

    public static GameListDto from(List<GameDto> games) {
        return new GameListDto(games);
    }

    @JsonProperty("matches")
    public List<GameDto> getGames() {
        return games;
    }
}
