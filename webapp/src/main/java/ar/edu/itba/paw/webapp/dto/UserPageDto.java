package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.models.Page;
import ar.edu.itba.paw.webapp.utils.HateoasUtils;
import org.springframework.hateoas.Link;

import javax.ws.rs.core.UriInfo;
import java.util.List;

public class UserPageDto {

    private final List<UserDto> users;
    private final List<Link> links;

    private UserPageDto(Page<UserDto> page, UriInfo uriInfo) {
        this.users = page.getData();
        this.links = HateoasUtils.getHateoasForPageLinks(page, HateoasUtils.getQuery(uriInfo.getQueryParameters(false)),
                uriInfo.getPath());
    }

    public static UserPageDto from(Page<UserDto> userPage, UriInfo uriInfo) {
        return new UserPageDto(userPage, uriInfo);
    }


    public List<UserDto> getUsers() {
        return users;
    }

    public List<Link> getLinks() {
        return links;
    }
}
