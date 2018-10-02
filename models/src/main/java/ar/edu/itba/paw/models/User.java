package ar.edu.itba.paw.models;


import java.util.Objects;

public class User {

    private long userId;
    private String firstName;
    private String lastName;
    private String email;

//    public User() {
//        firstName = "Agustin";
//        userId = 57774;
//        lastName = "Izaguirre";
//        email = "aizaguirre@itba.edu.ar";
//    }

    public User(String firstName, String lastName, String email, long userId) {
        this.firstName  = firstName;
        this.lastName   = lastName;
        this.email      = email;
        this.userId     = userId;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(final long userId) {
        this.userId = userId;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setFirstName(final String newFirstName) {
        firstName = newFirstName;
    }

    public void setLastName(final String newLastName) {
        lastName = newLastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(final String newEmail) {
        email = newEmail;
    }

    @Override
    public boolean equals(Object aUser) {
        if(aUser == null || !aUser.getClass().equals(getClass())) {
            return false;
        }

        return getUserId() == ((User) aUser).getUserId();
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, email);
    }
}
