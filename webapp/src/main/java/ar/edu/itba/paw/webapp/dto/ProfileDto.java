package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.models.PremiumUser;
import ar.edu.itba.paw.webapp.controller.UserController;
import com.google.common.collect.ImmutableList;
import org.springframework.hateoas.Link;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;

public class ProfileDto {

    private final String username;
    private final String firstName;
    private final String lastName;
    private final double winRate;
    private final int age;
    private final String location;
    private final List<Link> links;

    private ProfileDto(PremiumUser premiumUser) {
        this.username = premiumUser.getUserName();
        this.firstName = premiumUser.getUser().getFirstName();
        this.lastName = premiumUser.getUser().getLastName();
        this.winRate = premiumUser.getWinRate();
        this.age = Period.between(premiumUser.getBirthday(), LocalDate.now()).getYears(); //TODO: maybe the GET request of the profile can pass the UTC offset of the Locale?
        this.location = premiumUser.getHome().toString();
        this.links = getHateoasLinks(premiumUser);
    }

    public static ProfileDto from(PremiumUser premiumUser) {
        return new ProfileDto(premiumUser);
    }

    private List<Link> getHateoasLinks(PremiumUser premiumUser) {
        return ImmutableList.of(
                new Link(UserController.getUserProfileEndpoint(premiumUser.getUserName()), Link.REL_SELF),
                new Link(UserController.getUserGamesEndpoint(premiumUser.getUserName()), "matches"),
                new Link(UserController.getUserSportsEndpoint(premiumUser.getUserName()), "sports"),
                new Link(UserController.getUserImageEndpoint(premiumUser.getUserName()), "image")
        );
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
