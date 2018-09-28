package ar.edu.itba.paw.webapp.form;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class MatchForm {
    @Size(min = 4, max = 100)
    @Pattern(regexp = "[a-zA-Z0-9]+")
    private String matchName;

    private String sportName;

    //@Size(min = 0, max = 140)
    @Size(min = 0, max = 10)
    private String description;

    @Pattern(regexp = "[0-9][0-9]/0-9][0-9]/0-9][0-9]0-9][0-9]")
    private String date;

    //private String country;
//    private String state;
//    private String city;
//    private String street;
//    private String tornamentName;


    public String getMatchName() {
        return matchName;
    }

    public void setMatchName(final String matchName) {
        this.matchName = matchName;
    }

    public String getSportName() {
        return sportName;
    }

    public void setSportName(final String sportName) {
        this.sportName = sportName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }
    public String getDate() {
        return date;
    }

    public void setDate(final String date) {
        this.date = date;
    }
}
