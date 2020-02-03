package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.models.PremiumUser;
import ar.edu.itba.paw.webapp.controller.UserController;
import com.google.common.collect.ImmutableList;
import org.springframework.hateoas.Link;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

public class UserDto {

    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private String birthday;
    private String cellphone;
    private PlaceDto home;
    private String password;
    private String image;
    private int reputation;
    private Boolean isVerified;
    private List<Link> links;

    private UserDto() {
        /* Required by JSON object mapper */
    }

    private UserDto(PremiumUser premiumUser) {
        this.username = premiumUser.getUserName();
        this.email      = premiumUser.getEmail();
        this.firstName  = premiumUser.getUser().getFirstName();
        this.lastName   = premiumUser.getUser().getLastName();
        this.birthday   = premiumUser.getBirthday().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        this.cellphone  = premiumUser.getCellphone();
        this.home       = PlaceDto.from(premiumUser.getHome());
        this.password   = null;
        this.image      = null;
        this.reputation = premiumUser.getReputation();
        this.isVerified = premiumUser.getEnabled();
        this.links      = getHateoasLinks(premiumUser);
    }

    private List<Link> getHateoasLinks(PremiumUser premiumUser) {
        return ImmutableList.of(
                new Link(UserController.getUserEndpoint(premiumUser.getUserName()), Link.REL_SELF),
                new Link(UserController.getUserImageEndpoint(premiumUser.getUserName()), "image"),
                new Link(UserController.getUserProfileEndpoint(premiumUser.getUserName()), "profile")
        );
    }

    public static UserDto from(PremiumUser premiumUser) {
        return new UserDto(premiumUser);
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getBirthday() {
        return birthday;
    }

    public String getCellphone() {
        return cellphone;
    }

    public Optional<PlaceDto> getHome() {
        return Optional.ofNullable(home);
    }

    public String getPassword() {
        return password;
    }

    public String getImage() {
        return image;
    }

    public int getReputation() {
        return reputation;
    }

    public List<Link> getLinks() {
        return links;
    }

    public Boolean isVerified() {
        return isVerified;
    }
}
