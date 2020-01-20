package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.models.Game;
import ar.edu.itba.paw.models.Page;
import ar.edu.itba.paw.webapp.controller.GameController;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.ImmutableList;
import org.springframework.hateoas.Link;

import javax.ws.rs.core.MultivaluedMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class GameListDto {

    private final List<GameDto> games;
    private final List<Link> links;

    public GameListDto(Page<Game> page, String query, List<GameDto> gameDtos) {
        this.games = gameDtos;
        this.links = getHateoasLinks(page, query);
    }

    public static GameListDto from(Page<Game> page, List<GameDto> gameDtos,
                                   MultivaluedMap<String, String> queryParameters) {
        return new GameListDto(page, getQuery(queryParameters), gameDtos);
    }

    private List<Link> getHateoasLinks(Page<Game> page, String query) { //TODO
        ImmutableList.Builder<Link> builder = ImmutableList.builder();
        builder = builder.add(new Link(GameController.getGamesEndpoint(page, query), Link.REL_SELF));
        Optional<Page<Game>> aux = page.getNextPage();
        if (aux.isPresent()) {
            builder = builder.add(new Link(GameController.getGamesEndpoint(aux.get(), query), "next"));
        }
        aux = page.getPrevPage();
        if (aux.isPresent()) {
            builder = builder.add(new Link(GameController.getGamesEndpoint(aux.get(), query), "prev"));
        }
        return builder.build();
    }

    @JsonProperty("matches")
    public List<GameDto> getGames() {
        return games;
    }

    public List<Link> getLinks() {
        return links;
    }

    private static String getQuery(MultivaluedMap<String, String> queryParameters) {
        StringBuilder ans = new StringBuilder();
        boolean isFirstValue, isFirstKey = true;
        for (Map.Entry<String, List<String>> entry: queryParameters.entrySet()) {
            if (!entry.getKey().equals("limit") && !entry.getKey().equals("offSet")) {
                if (isFirstKey) {
                    isFirstKey = false;
                } else {
                    ans.append('&');
                }
                ans.append(entry.getKey()).append('=');
                isFirstValue = true;
                for (String value : entry.getValue()) {
                    if (isFirstValue) {
                        isFirstValue = false;
                    } else {
                        ans.append(',');
                    }
                    ans.append(value);
                }
            }
        }
        return ans.toString();
    }
}
