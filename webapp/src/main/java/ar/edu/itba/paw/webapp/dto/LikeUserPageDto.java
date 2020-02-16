package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.models.Page;
import ar.edu.itba.paw.webapp.utils.HateoasUtils;
import org.springframework.hateoas.Link;

import javax.ws.rs.core.UriInfo;
import java.util.List;

public class LikeUserPageDto {

    private final List<LikeUserDto> likedUsers;
    private final List<Link> links;

    private LikeUserPageDto(Page<LikeUserDto> page, UriInfo uriInfo) {
        this.likedUsers = page.getData();
        this.links = HateoasUtils.getHateoasForPageLinks(page, HateoasUtils.getQuery(uriInfo.getQueryParameters(false)),
                uriInfo.getPath());
    }

    public static LikeUserPageDto from(Page<LikeUserDto> allLikedUsersPage, UriInfo uriInfo) {
        return new LikeUserPageDto(allLikedUsersPage, uriInfo);
    }

    public List<LikeUserDto> getLikedUsers() {
        return likedUsers;
    }

    public List<Link> getLinks() {
        return links;
    }
}
