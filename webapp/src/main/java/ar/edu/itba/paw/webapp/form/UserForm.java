package ar.edu.itba.paw.webapp.form;

import ar.edu.itba.paw.webapp.form.Validators.*;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@PasswordMatches
public class UserForm {
    @Size(min = 2, max = 100)
    @Pattern(regexp ="[a-zA-Z]+")
    private String firstName;

    @Size(min = 2, max = 100)
    @Pattern(regexp ="[a-zA-Z ]+")
    private String lastName;

    @NotEmpty
    @Email
    @NonExistantEmail
    private String email;

    @Pattern(regexp ="([0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9])|( *)")
    private String cellphone;

    private String country;

    private String state;

    private String city;

    private String street;

    @ValidImageSize
    @ValidImageType
    private MultipartFile image;

    @Pattern(regexp ="[0-9][0-9]/[0-9][0-9]/[0-9][0-9][0-9][0-9]")
    @PastDate
    //(message = "{PastDate.registerForm.date}")
    private String birthday;

    @Size(min = 4, max = 100)
    @Pattern(regexp = "[a-zA-Z0-9_]+")
    @NonExistantUser
    //(message = "{NonExistantUser.registerForm.username}")
    private String username;

    @Size(min = 5, max = 100)
    @Pattern(regexp = "[a-zA-Z0-9]+")
    private String password;

    @Size(min = 5, max = 100)
    @Pattern(regexp = "[a-zA-Z0-9]+")
    private String repeatPassword;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(final String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(final String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(final String email) {
        this.email = email;
    }

    public String getCellphone() {
        return cellphone;
    }

    public void setCellphone(final String cellphone) {
        this.cellphone = cellphone;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(final String country) {
        this.country = country;
    }

    public String getState() {
        return state;
    }

    public void setState(final String state) {
        this.state = state;
    }

    public String getCity() {
        return city;
    }

    public void setCity(final String city) {
        this.city = city;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(final String street) {
        this.street = street;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(final String birthday) {
        this.birthday = birthday;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(final String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(final String password) {
        this.password = password;
    }

    public String getRepeatPassword() {
        return repeatPassword;
    }

    public void setRepeatPassword(final String repeatPassword) {
        this.repeatPassword = repeatPassword;
    }

    public MultipartFile getImage() {
        return image;
    }

    public void setImage(MultipartFile image) {
        this.image = image;
    }
}
