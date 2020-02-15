package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.models.PremiumUser;
import ar.edu.itba.paw.webapp.controller.UserController;
import com.google.common.collect.ImmutableList;
import org.springframework.hateoas.Link;

import java.util.List;

public class LikeUserDto {

    private String username;
    private List<Link> links;

    private LikeUserDto() {
        /* Required by JSON object mapper */
    }

    private LikeUserDto(PremiumUser likedPremiumUser, String username) {
        this.username = likedPremiumUser.getUserName();
        this.links = getHateoasLinks(likedPremiumUser, username);
    }

    public static LikeUserDto from(PremiumUser likedPremiumUser, String username) {
        return new LikeUserDto(likedPremiumUser, username);
    }

    private List<Link> getHateoasLinks(PremiumUser premiumUser, String username) {
        return ImmutableList.of(
                new Link(UserController.getLikedUserEndpoint(username, premiumUser.getUserName()), Link.REL_SELF),
                new Link(UserController.getUserProfileEndpoint(premiumUser.getUserName()), "profile"),
                new Link(UserController.getUserImageEndpoint(premiumUser.getUserName()), "image")
        );
    }

    public String getUsername() {
        return username;
    }

    public List<Link> getLinks() {
        return links;
    }
}
