package ar.edu.itba.paw.webapp.form;

import javax.validation.constraints.Pattern;

public class SubmitResultForm {

    @Pattern(regexp="[0-9]|[1-9][0-9]+")
    int team1Points;

    @Pattern(regexp="[0-9]|[1-9][0-9]+")
    int team2Points;

    public int getTeam1Points() {
        return team1Points;
    }

    public void setTeam1Points(int team1Points) {
        this.team1Points = team1Points;
    }

    public int getTeam2Points() {
        return team2Points;
    }

    public void setTeam2Points(int team2Points) {
        this.team2Points = team2Points;
    }
}
