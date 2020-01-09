package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.models.Sport;
import org.springframework.web.multipart.MultipartFile;

public class SportDtoInput {

    private String sportName;
    private int playerQuantity;
    private String displayName;
    private MultipartFile imageSport;

    public SportDtoInput(Sport sport) {

    }

    public String getSportName() {
        return sportName;
    }

    public int getPlayerQuantity() {
        return playerQuantity;
    }

    public String getDisplayName() {
        return displayName;
    }

    public MultipartFile getImageSport() {
        return imageSport;
    }
}
