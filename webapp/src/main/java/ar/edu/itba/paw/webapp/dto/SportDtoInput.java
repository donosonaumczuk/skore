package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.models.Sport;
import org.springframework.web.multipart.MultipartFile;

public class SportDtoInput {

    private String sportName;
    private int playerQuantity;
    private String displayName;
    private MultipartFile imageSport;

    public SportDtoInput() {

    }

    public SportDtoInput(Sport sport) {
        this.sportName = sport.getName();
        this.playerQuantity = sport.getQuantity();
        this.displayName = sport.getDisplayName();
    }

    public static SportDtoInput from(Sport sport) {
        return new SportDtoInput(sport);
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

    public void setSportName(String sportName) {
        this.sportName = sportName;
    }

    public void setPlayerQuantity(int playerQuantity) {
        this.playerQuantity = playerQuantity;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public void setImageSport(MultipartFile imageSport) {
        this.imageSport = imageSport;
    }
}
