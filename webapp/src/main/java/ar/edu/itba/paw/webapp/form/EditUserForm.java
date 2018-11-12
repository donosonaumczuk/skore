package ar.edu.itba.paw.webapp.form;

import ar.edu.itba.paw.webapp.form.Validators.*;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class EditUserForm {
    @Size(min = 2, max = 100)
    @Pattern(regexp ="[a-zA-ZÁÉÍÓÚáéíñóöúü ]+")
    private String firstName;

    @Size(min = 2, max = 100)
    @Pattern(regexp ="[a-zA-ZÁÉÍÓÚáéíñóöúü ]+")
    private String lastName;

    @Pattern(regexp ="([0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9])|( *)")
    private String cellphone;

    @ValidImageSize
    @ValidImageType
    private MultipartFile image;

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

    public String getCellphone() {
        return cellphone;
    }

    public void setCellphone(final String cellphone) {
        this.cellphone = cellphone;
    }

    public MultipartFile getImage() {
        return image;
    }

    public void setImage(MultipartFile image) {
        this.image = image;
    }
}
