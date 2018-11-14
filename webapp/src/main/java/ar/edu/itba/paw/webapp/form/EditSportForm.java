package ar.edu.itba.paw.webapp.form;

import ar.edu.itba.paw.webapp.form.Validators.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class EditSportForm {

    private String sportName;

    @Pattern(regexp ="([a-zA-Z0-9 ]+)")
    private String displayName;

    @ValidSportImageSize
    @ValidSportImageType
    @ValidSportImageNotNull
    private MultipartFile image;

    public String getSportName() {
        return sportName;
    }

    public void setSportName(String sportName) {
        this.sportName = sportName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public MultipartFile getImage() {
        return image;
    }

    public void setImage(MultipartFile image) {
        this.image = image;
    }
}
