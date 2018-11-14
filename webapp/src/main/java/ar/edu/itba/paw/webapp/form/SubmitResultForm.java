package ar.edu.itba.paw.webapp.form;

import javax.validation.constraints.Pattern;

public class SubmitResultForm {
    private String matchKey;

    @Pattern(regexp="[0-9]|[1-9][0-9]+")
    String team1Points;

    @Pattern(regexp="[0-9]|[1-9][0-9]+")
    String team2Points;

    public String getMatchKey() {
        return matchKey;
    }

    public void setMatchKey(String matchKey) {
        this.matchKey = matchKey;
    }

    public String getTeam1Points() {
        return team1Points;
    }

    public void setTeam1Points(String team1Points) {
        this.team1Points = team1Points;
    }

    public String getTeam2Points() {
        return team2Points;
    }

    public void setTeam2Points(String team2Points) {
        this.team2Points = team2Points;
    }
}
