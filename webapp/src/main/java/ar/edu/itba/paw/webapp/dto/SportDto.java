package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.models.Sport;
import ar.edu.itba.paw.webapp.controller.SportController;
import com.google.common.collect.ImmutableList;
import org.springframework.hateoas.Link;

import java.util.List;

public class SportDto {

    private String sportName;
    private Integer playerQuantity;
    private String displayName;
    private String imageSport;
    private List<Link> links;

    private SportDto() {
        /* Required by JSON object mapper */
    }

    private SportDto(Sport sport) {
        this.sportName = sport.getName();
        this.playerQuantity = sport.getQuantity();
        this.displayName = sport.getDisplayName();
        this.links = getHateoasLinks(sport);
    }

    public static SportDto from(Sport sport) {
        return new SportDto(sport);
    }

    private List<Link> getHateoasLinks(Sport sport) {
        return ImmutableList.of(
                new Link(SportController.getSportEndpoint(sport.getName()), Link.REL_SELF),
                new Link(SportController.getSportImageEndpoint(sport.getName()), "image")
        );
    }

    public String getSportName() {
        return sportName;
    }

    public Integer getPlayerQuantity() {
        return playerQuantity;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getImageSport() {
        return imageSport;
    }

    public List<Link> getLinks() {
        return links;
    }
}
