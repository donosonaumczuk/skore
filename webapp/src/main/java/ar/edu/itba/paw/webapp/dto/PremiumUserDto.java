package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.models.PremiumUser;
import ar.edu.itba.paw.models.User;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;


public class PremiumUserDto {

    private final String username;
    private final String email;
    private final String cellphone;
    private final LocalDate birthday;
    private final int reputation;
    private final boolean enabled;
    private final User user; //TODO: This must be an UserDto
    @JsonProperty("win_rate") //TODO: This was for testing, we must agree a convention!
    private final double winRate;

    public PremiumUserDto(PremiumUser premiumUser) {
        this.username = premiumUser.getUserName();
        this.email = premiumUser.getEmail();
        this.cellphone = premiumUser.getCellphone();
        this.birthday = premiumUser.getBirthday();
        this.reputation = premiumUser.getReputation();
        this.enabled = premiumUser.getEnabled();
        this.user = premiumUser.getUser();
        this.winRate = premiumUser.getWinRate();
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public int getReputation() {
        return reputation;
    }

    public String getCellphone() {
        return cellphone;
    }

    public LocalDate getBirthday() {
        return birthday;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public User getUser() {
        return user;
    }

    public double getWinRate() {
        return winRate;
    }
}
