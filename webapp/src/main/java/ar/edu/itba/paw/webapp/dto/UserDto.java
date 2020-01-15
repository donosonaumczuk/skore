package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.models.PremiumUser;
import ar.edu.itba.paw.webapp.controller.UserController;
import com.google.common.collect.ImmutableList;
import org.springframework.hateoas.Link;

import java.time.format.DateTimeFormatter;
import java.util.List;

public class UserDto {

    private String userName;
    private String email;
    private String firstName;
    private String lastName;
    private String birthDay;
    private String cellphone;
    private PlaceDto home;
    private String password;
    private String image;
    private int reputation;
    private boolean isVerify;
    private List<Link> links;

    public UserDto() {

    }

    public UserDto(PremiumUser premiumUser) {
        this.userName   = premiumUser.getUserName();
        this.email      = premiumUser.getEmail();
        this.firstName  = premiumUser.getUser().getFirstName();
        this.lastName   = premiumUser.getUser().getFirstName();
        this.birthDay   = premiumUser.getBirthday().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        this.cellphone  = premiumUser.getCellphone();
        this.home       = new PlaceDto(premiumUser.getHome());
        this.password   = null;
        this.image      = null;
        this.reputation = premiumUser.getReputation();
        this.isVerify   = premiumUser.getEnabled();
        this.links      = getHateoasLinks(premiumUser);
    }

    private List<Link> getHateoasLinks(PremiumUser premiumUser) {
        return ImmutableList.of(
                new Link(UserController.getUserEndpoint(premiumUser.getUserName()), Link.REL_SELF),
                new Link(UserController.getUserImageEndpoint(premiumUser.getUserName()), "image")
        );
    }

    public static UserDto from(PremiumUser premiumUser) {
        return new UserDto(premiumUser);
    }

    public String getUserName() {
        return userName;
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

    public String getBirthDay() {
        return birthDay;
    }

    public String getCellphone() {
        return cellphone;
    }

    public PlaceDto getHome() {
        return home;
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

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setBirthDay(String birthDay) {
        this.birthDay = birthDay;
    }

    public void setCellphone(String cellphone) {
        this.cellphone = cellphone;
    }

    public void setHome(PlaceDto home) {
        this.home = home;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setReputation(int reputation) {
        this.reputation = reputation;
    }
}
