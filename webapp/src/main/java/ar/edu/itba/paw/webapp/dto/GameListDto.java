package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.models.Game;
import ar.edu.itba.paw.models.Page;
import ar.edu.itba.paw.webapp.constants.URLConstants;
import ar.edu.itba.paw.webapp.controller.GameController;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.ImmutableList;
import org.springframework.hateoas.Link;

import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.UriInfo;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class GameListDto {

    private final List<GameDto> games;
    private final List<Link> links;

    public GameListDto(Page<Game> page, UriInfo uriInfo, List<GameDto> gameDtos) {
        this.games = gameDtos;
        this.links = getHateoasLinks(page, getQuery(uriInfo.getQueryParameters(false)),
                uriInfo.getPath());
    }

    public static GameListDto from(Page<Game> page, List<GameDto> gameDtos,
                                   UriInfo uriInfo) {
        return new GameListDto(page, uriInfo, gameDtos);
    }

    private List<Link> getHateoasLinks(Page<Game> page, String query, String base) {
        ImmutableList.Builder<Link> builder = ImmutableList.builder();
        builder = builder.add(new Link(getEndpoint(page, query, base), Link.REL_SELF));
        Optional<Page<Game>> aux = page.getNextPage();
        if (aux.isPresent()) {
            builder = builder.add(new Link(getEndpoint(aux.get(), query, base), "next"));
        }
        aux = page.getPrevPage();
        if (aux.isPresent()) {
            builder = builder.add(new Link(getEndpoint(aux.get(), query, base), "prev"));
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

    private String getEndpoint(Page<Game> page, String query, String base) {
        return URLConstants.getApiBaseUrlBuilder().path(base).toTemplate() + "?offset=" + page.getOffSet()
                + "&limit=" + page.getLimit() + ((query != null && !query.isEmpty()) ? "&" : "" ) + query;
    }
}
