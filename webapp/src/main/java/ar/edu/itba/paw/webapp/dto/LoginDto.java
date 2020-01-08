package ar.edu.itba.paw.webapp.dto;

public class LoginDto {
    private String username;
    private String password;

    public LoginDto() {}

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
