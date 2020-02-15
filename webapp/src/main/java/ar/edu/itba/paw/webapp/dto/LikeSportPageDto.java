package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.models.Page;
import ar.edu.itba.paw.webapp.utils.HateoasUtils;
import org.springframework.hateoas.Link;

import javax.ws.rs.core.UriInfo;
import java.util.List;

public class LikeSportPageDto {

    private final List<LikeSportDto> likedUsers;
    private final List<Link> links;

    private LikeSportPageDto(Page<LikeSportDto> page, UriInfo uriInfo) {
        this.likedUsers = page.getData();
        this.links = HateoasUtils.getHateoasForPageLinks(page, HateoasUtils.getQuery(uriInfo.getQueryParameters(false)),
                uriInfo.getPath());
    }

    public static LikeSportPageDto from(Page<LikeSportDto> allLikedSportsPage, UriInfo uriInfo) {
        return new LikeSportPageDto(allLikedSportsPage, uriInfo);
    }

    public List<LikeSportDto> getLikedUsers() {
        return likedUsers;
    }

    public List<Link> getLinks() {
        return links;
    }
}
