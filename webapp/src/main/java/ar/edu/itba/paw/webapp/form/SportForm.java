package ar.edu.itba.paw.webapp.form;

import ar.edu.itba.paw.webapp.form.Validators.UniqueSportName;
import ar.edu.itba.paw.webapp.form.Validators.ValidSportImageNotNull;
import ar.edu.itba.paw.webapp.form.Validators.ValidSportImageSize;
import ar.edu.itba.paw.webapp.form.Validators.ValidSportImageType;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class SportForm {

    @UniqueSportName
    @Pattern(regexp ="([a-zA-Z0-9]+)")
    private String sportName;

    @Pattern(regexp ="([a-zA-Z0-9 ]+)")
    private String displayName;

    @Size(min = 1)
    @Pattern(regexp ="([0-9 ]|[1-9]+)")
    private String playerQuantity;

    @ValidSportImageSize
    @ValidSportImageType
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

    public String getPlayerQuantity() {
        return playerQuantity;
    }

    public void setPlayerQuantity(String playerQuantity) {
        this.playerQuantity = playerQuantity;
    }

    public MultipartFile getImage() {
        return image;
    }

    public void setImage(MultipartFile image) {
        this.image = image;
    }
}
