package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.models.Sport;
import ar.edu.itba.paw.webapp.controller.SportController;
import ar.edu.itba.paw.webapp.controller.UserController;
import com.google.common.collect.ImmutableList;
import org.springframework.hateoas.Link;

import java.util.List;

public class LikeSportDto {

    private String sportName;
    private List<Link> links;

    private LikeSportDto() {
        /* Required by JSON object mapper */
    }

    private LikeSportDto(Sport likedSport, String username) {
        this.sportName = likedSport.getName();
        this.links = getHateoasLinks(likedSport, username);
    }

    public static LikeSportDto from(Sport likedSport, String username) {
        return new LikeSportDto(likedSport, username);
    }

    private List<Link> getHateoasLinks(Sport likedSport, String username) {
        return ImmutableList.of(
                new Link(UserController.getLikedSportEndpoint(username, likedSport.getName()), Link.REL_SELF),
                new Link(SportController.getSportEndpoint(likedSport.getName()), "sport"),
                new Link(SportController.getSportImageEndpoint(likedSport.getName()), "image")
        );
    }

    public String getSportName() {
        return sportName;
    }

    public List<Link> getLinks() {
        return links;
    }
}
