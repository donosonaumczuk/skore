package ar.edu.itba.paw.webapp.form;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class JoinMatchForm {

    @NotEmpty
    @Email
    private String email;

    @Size(min = 2, max = 100)
    @Pattern(regexp ="[a-zA-Z]+")
    private String firstName;

    @Size(min = 2, max = 100)
    @Pattern(regexp ="[a-zA-Z]+")
    private String lastName;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}
