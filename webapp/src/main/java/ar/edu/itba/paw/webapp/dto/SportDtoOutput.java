package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.models.Sport;
import ar.edu.itba.paw.webapp.controller.SportController;
import com.google.common.collect.ImmutableList;
import org.springframework.hateoas.Link;

import java.util.List;

public class SportDtoOutput {

    private String sportName;
    private int playerQuantity;
    private String displayName;
    private final List<Link> links;

    public SportDtoOutput(Sport sport) {
        this.sportName = sport.getName();
        this.playerQuantity = sport.getQuantity();
        this.displayName = sport.getDisplayName();
        this.links = getHateoasLinks(sport);
    }

    public static SportDtoOutput from(Sport sport) {
        return new SportDtoOutput(sport);
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

    public int getPlayerQuantity() {
        return playerQuantity;
    }

    public String getDisplayName() {
        return displayName;
    }

    public List<Link> getLinks() {
        return links;
    }
}