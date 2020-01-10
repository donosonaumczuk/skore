package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.models.Game;
import ar.edu.itba.paw.models.PremiumUser;
import ar.edu.itba.paw.models.User;

public class TeamPlayerDto {
    private String username;
    private String email;

    private TeamPlayerDto(String username, String email) {
        this.username = username;
        this.email = email;
    }

    public static TeamPlayerDto from(User user) {
        return new TeamPlayerDto(null, user.getEmail());
    }

    public static TeamPlayerDto from(PremiumUser premiumUser) {
        return new TeamPlayerDto(premiumUser.getUserName(), premiumUser.getUser().getEmail());
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }
}
