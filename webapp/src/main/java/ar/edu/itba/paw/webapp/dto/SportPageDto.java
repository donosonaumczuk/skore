package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.models.Page;
import org.springframework.hateoas.Link;

import javax.ws.rs.core.UriInfo;
import java.util.List;

public class SportPageDto {

    private final List<SportDto> sports;
    private final List<Link> links;

    public SportPageDto(Page<SportDto> page, UriInfo uriInfo) {
        this.sports = page.getPageData();
        this.links = DtoHelper.getHateoasForPageLinks(page, DtoHelper.getQuery(uriInfo.getQueryParameters(false)),
                uriInfo.getPath());
    }

    public static SportPageDto from(Page<SportDto> allSportsPage, UriInfo uriInfo) {
        return new SportPageDto(allSportsPage, uriInfo);
    }

    public List<SportDto> getSports() {
        return sports;
    }

    public List<Link> getLinks() {
        return links;
    }
}
