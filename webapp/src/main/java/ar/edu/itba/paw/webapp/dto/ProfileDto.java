package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.models.PremiumUser;
import ar.edu.itba.paw.webapp.controller.UserController;
import org.springframework.hateoas.Link;

import java.time.LocalDate;
import java.time.Period;
import java.util.LinkedList;
import java.util.List;

public class ProfileDto {

    private final String username;
    private final String firstName;
    private final String lastName;
    private final double winRate;
    private final int age;
    private final String location;
    private final List<Link> links;

    public ProfileDto(PremiumUser premiumUser) {
        this.username = premiumUser.getUserName();
        this.firstName = premiumUser.getUser().getFirstName();
        this.lastName = premiumUser.getUser().getLastName();
        this.winRate = premiumUser.getWinRate();
        this.age = Period.between(LocalDate.now(), premiumUser.getBirthday()).getYears(); //TODO: maybe the GET request of the profile can pass the UTC offset of the Locale?
        this.location = premiumUser.getHome().toString();
        this.links = getHateoasLinks(premiumUser);
    }

    private List<Link> getHateoasLinks(PremiumUser premiumUser) {
        final List<Link> hateoasLinks = new LinkedList<>(); //TODO: Immutable
        hateoasLinks.add(new Link(UserController.getProfileEndpoint(premiumUser.getUserName()), "self"));
//        hateoasLinks.add(new Link("/user/" + username + "/image", "image"));
//        hateoasLinks.add(new Link("/user/" + username + "/matches", "matches"));
//        hateoasLinks.add(new Link("/user/" + username + "/friends", "friends"));
//        hateoasLinks.add(new Link("/user/" + username + "/sports", "liked sports"));
        return hateoasLinks;
    }

    public String getUsername() {
        return username;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public double getWinRate() {
        return winRate;
    }

    public int getAge() {
        return age;
    }

    public String getLocation() {
        return location;
    }

    public List<Link> getLinks() {
        return links;
    }
}
