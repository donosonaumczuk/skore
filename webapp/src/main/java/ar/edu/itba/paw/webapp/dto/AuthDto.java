package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.models.PremiumUser;
import ar.edu.itba.paw.models.Role;

public class AuthDto {

    private String username;
    private long userId;
    private Boolean isAdmin;

    private AuthDto() {
        /* Required by JSON object mapper */
    }

    private AuthDto(String username, long userId, Boolean isAdmin) {
        this.username = username;
        this.userId = userId;
        this.isAdmin = isAdmin;
    }

    public static AuthDto from(String username, long userId, boolean isAdmin) {
        return new AuthDto(username, userId, isAdmin);
    }

    public static AuthDto from(PremiumUser premiumUser) {
        Role adminRole = new Role("ROLE_ADMIN", 1);//TODO in premiumUser add a method isAdmin
        return new AuthDto(premiumUser.getUserName(), premiumUser.getUser().getUserId(),
                premiumUser.getRoles().contains(adminRole));
    }

    public String getUsername() {
        return username;
    }

    public Boolean getAdmin() {
        return isAdmin;
    }

    public long getUserId() {
        return userId;
    }
}
