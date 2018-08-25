package ar.edu.itba.paw.models;

public class User {

    private String id;
    private String name;
    private String password;
    public User() {
        name = "Agustin";
        id = "57774";
        password = "password";
    }
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

}
