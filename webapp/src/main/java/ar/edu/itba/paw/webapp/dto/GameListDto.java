package ar.edu.itba.paw.webapp.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.ImmutableList;
import org.springframework.hateoas.Link;

import java.util.List;

public class GameListDto {

    private final List<GameDto> games;
    private final List<Link> links;

    public GameListDto(List<GameDto> games, Integer offSet, Integer limit) {
        this.games = games;
        this.links = getHateoasLinks(offSet, limit);
    }

    public static GameListDto from(List<GameDto> games, Integer offSet, Integer limit) {
        return new GameListDto(games, offSet, limit);
    }

    private List<Link> getHateoasLinks(Integer offSet, Integer limit) { //TODO
        return ImmutableList.of(
//                new Link(Ga.getProfileEndpoint(premiumUser.getUserName()), Link.REL_SELF),
//                new Link(UserController.getMatchesEndpoint(premiumUser.getUserName()), "next"),
//                new Link(UserController.getSportsEndpoint(premiumUser.getUserName()), "prev"),
                //TODO update when /matches/id is created
        );
    }

    @JsonProperty("matches")
    public List<GameDto> getGames() {
        return games;
    }
}
