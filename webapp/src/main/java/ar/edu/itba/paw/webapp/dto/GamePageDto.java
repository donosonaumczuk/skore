package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.models.Page;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.hateoas.Link;

import javax.ws.rs.core.UriInfo;
import java.util.List;

public class GamePageDto {

    private final List<GameDto> games;
    private final List<Link> links;

    public GamePageDto(Page<GameDto> page, UriInfo uriInfo) {
        this.games = page.getPageData();
        this.links = DtoHelper.getHateoasForPageLinks(page, DtoHelper.getQuery(uriInfo.getQueryParameters(false)),
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
