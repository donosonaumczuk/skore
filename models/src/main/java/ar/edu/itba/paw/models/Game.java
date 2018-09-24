package ar.edu.itba.paw.models;

import java.time.LocalDateTime;

public class Game {
    private Team team1;
    private Team team2;
    private Place place;
    private LocalDateTime startTime;
    private LocalDateTime finishTime;
    private String result;
    private String type;
    private int quantityOccupiedPlaces;

    public Game(Team team1, Team team2, Place place, LocalDateTime startTime,
                LocalDateTime finishTime, String type, int quantityOccupiedPlaces,
                String result) {
        this.team1                  = team1;
        this.team2                  = team2;
        this.place                  = place;
        this.startTime              = startTime;
        this.finishTime             = finishTime;
        this.type                   = type;
        this.result                 = result;
        this.quantityOccupiedPlaces = quantityOccupiedPlaces;
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

    public String getType() {
        return type;
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

    public int getQuantityOccupiedPlaces() {
        return quantityOccupiedPlaces;
    }
}
