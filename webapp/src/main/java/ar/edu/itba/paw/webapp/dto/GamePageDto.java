package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.models.Page;
import ar.edu.itba.paw.webapp.utils.HateoasUtils;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.hateoas.Link;

import javax.ws.rs.core.UriInfo;
import java.util.List;

public class GamePageDto {

    private final List<GameDto> games;
    private final List<Link> links;

    private GamePageDto(Page<GameDto> page, UriInfo uriInfo) {
        this.games = page.getData();
        this.links = HateoasUtils.getHateoasForPageLinks(page, HateoasUtils.getQuery(uriInfo.getQueryParameters(false)),
                uriInfo.getPath());
    }

    public static GamePageDto from(Page<GameDto> page, UriInfo uriInfo) {
        return new GamePageDto(page, uriInfo);
    }

    @JsonProperty("matches")
    public List<GameDto> getGames() {
        return games;
    }

    public List<Link> getLinks() {
        return links;
    }
}
