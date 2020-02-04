package ar.edu.itba.paw.webapp.dto;

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
