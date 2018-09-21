package ar.edu.itba.paw.models;

import java.time.LocalDateTime;

public class Game {
    private Team team1;
    private Team team2;
    private Place place;
    private LocalDateTime startTime;
    private LocalDateTime finishTime;
    private String result;
    private boolean isCompetitive;

    public Game(Team team1, Team team2, Place place, LocalDateTime startTime,
                LocalDateTime finishTime, boolean isCompetitive) {
        this.team1 = team1;
        this.team2 = team2;
        this.place = place;
        this.startTime = startTime;
        this.finishTime = finishTime;
        this.isCompetitive = isCompetitive;
        this.result = null;
    }

    public Team getTeam1() {
        return team1;
    }

    public Team getTeam2() {
        return team2;
    }

    public Place getPlace() {
        return place;
    }

    public void setPlace(final Place place) {
        this.place = place;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(final LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(final LocalDateTime finishTime) {
        this.finishTime = finishTime;
    }

    public void setResult(final String result) {
        this.result = result;
    }

    public String getResult() {
        return result;
    }

    public boolean isCompetitive() {
        return isCompetitive;
    }

    @Override
    public boolean equals(Object object) {
        if(object == null || !object.getClass().equals(getClass())) {
            return false;
        }

        Game aGame = ((Game) object);
        return getTeam1().equals(aGame.getTeam1())
                && getTeam2().equals(aGame.getTeam2())
                && getStartTime().equals(aGame.getStartTime())
                && getFinishTime().equals(aGame.getFinishTime());
    }
}
