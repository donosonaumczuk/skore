package ar.edu.itba.paw.webapp.dto;

public class LoginDto {

    private String username;
    private String password;

    private LoginDto() {
        /* Required by JSON object mapper */
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
