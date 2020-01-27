package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.models.PremiumUser;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.webapp.controller.UserController;
import com.google.common.collect.ImmutableList;
import org.springframework.hateoas.Link;

import java.util.List;

public class TeamPlayerDto {

    private String username;
    private String email;
    private List<Link> links;

    private TeamPlayerDto() {
        /* Required by JSON object mapper */
    }

    private TeamPlayerDto(String username, String email) {
        this.username = username;
        this.email = email;
        this.links = username != null ? getHateoasLinks(username) : null;
    }

    public static TeamPlayerDto from(User user) {
        return new TeamPlayerDto(null, user.getEmail());
    }

    public static TeamPlayerDto from(PremiumUser premiumUser) {
        return new TeamPlayerDto(premiumUser.getUserName(), premiumUser.getUser().getEmail());
    }

    private List<Link> getHateoasLinks(String username) {
        return ImmutableList.of( new Link(UserController.getUserProfileEndpoint(username), "player"));
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public List<Link> getLinks() {
        return links;
    }
}
