package ar.edu.itba.paw.webapp.dto;

public class AuthDto {
    private final String username;
    private final Boolean isAdmin;

    private AuthDto(String username, Boolean isAdmin) {
        this.username = username;
        this.isAdmin = isAdmin;
    }

    public static AuthDto from(String username, boolean isAdmin) {
        return new AuthDto(username, isAdmin);
    }

    public String getUsername() {
        return username;
    }

    public Boolean getAdmin() {
        return isAdmin;
    }
}
