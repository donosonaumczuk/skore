package ar.edu.itba.paw.models;


import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "users")
public class User {

    @Id
//    @SequenceGenerator(name = "users_userid", sequenceName = "users_userid_seq", allocationSize = 1)
//    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "users_userid")//TODO: los test no pasan con esto no se si andara en POSTGRESQL
    @Column(name = "userid")
    private long userId;

    @Column(name = "firstname", length = 100)
    private String firstName;

    @Column(name = "lastname", length = 100)
    private String lastName;

    @Column(name = "email", length = 100)
    private String email;

    public User() {

    }

    public User(String firstName, String lastName, String email, long userId) {
        this.firstName  = firstName;
        this.lastName   = lastName;
        this.email      = email;
        this.userId     = userId;
    }

    public User(String firstName, String lastName, String email) {
        this.firstName  = firstName;
        this.lastName   = lastName;
        this.email      = email;
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
