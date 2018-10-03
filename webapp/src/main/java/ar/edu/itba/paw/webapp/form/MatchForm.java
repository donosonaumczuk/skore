package ar.edu.itba.paw.webapp.form;

import ar.edu.itba.paw.webapp.form.Validators.*;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@FutureTime
//@TeamIdIfNotIndividual
public class MatchForm {
    @Size(min = 4, max = 100)
    @Pattern(regexp = "[a-zA-Z0-9 ]+")
    private String matchName;

    @NotEmpty
    private String sportName;

    @Size(min = 0, max = 140)
    private String description;

    @Pattern(regexp = "[0-9][0-9]/[0-9][0-9]/[0-9][0-9][0-9][0-9]")
    @FutureDate(message = "{FutureDate.createMatchForm.date}")
    public String date;

    @Pattern(regexp = "[0-9][0-9]:[0-9][0-9]")
    private String startTime;

    @Pattern(regexp = "[0-9][0-9]:[0-9][0-9]")
    @ValidDuration
    private String duration;

    @NotEmpty
    private String country;

    @NotEmpty
    private String state;

    private String city;

    private String street;

    private String streetNumber;
    @NotEmpty
    private String competitivity;

//    @NotEmpty
//    private String mode;

    //private String teamId;

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

    public void setDate(final String date) {
        this.date = date;
    }

    public String getDate() {
        return date;
    }

    public void setStartTime(final String startTime) {
        this.startTime = startTime;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setDuration(final String duration) {
        this.duration = duration;
    }

    public String getDuration() {
        return duration;
    }

    public void setCountry(final String country) {
        this.country = country;
    }

    public String getCountry() {
        return country;
    }

    public void setState(final String state) {
        this.state = state;
    }

    public String getState() {
        return state;
    }

    public void setCity(final String city) {
        this.city = city;
    }

    public String getCity() {
        return city;
    }

    public void setStreet(final String street) {
        this.street = street;
    }

    public String getStreet() {
        return street;
    }

    public void setStreetNumber(final String streetNumber) {
        this.streetNumber = streetNumber;
    }

    public String getStreetNumber() {
        return streetNumber;
    }

    public String getCompetitivity() {
        return competitivity;
    }

    public void setCompetitivity(String competitivity) {
        this.competitivity = competitivity;
    }

//    public String getMode() {
//        return mode;
//    }
//
//    public void setMode(String mode) {
//        this.mode = mode;
//    }

//    public String getTeamId() {
//        return teamId;
//    }
//
//    public void setTeamId(String teamId) {
//        this.teamId = teamId;
//    }
}
