package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.models.Game;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class GameDto {

    private String title;
    private String description;
    private String creator;
    private boolean isCompetitive;
    private String sport;
    private LocalDate date;
    private LocalTime time;
    private int totalPlayers;
    private int currentplayers;
    private boolean hasStarted;
    private boolean hasFinished;
    private String results;
    // -1 undefined, 0 for tie, 1 for team 1 and 2 for team 2
    private int winnerTeam;
//    private ResultDto results; TODO maybe dto with specific results according to sport

    private GameDto(Game game) {
        title = game.getTitle();
        description = game.getDescription();
        creator = game.getTeam1().getLeader().getUserName();
//        isCompetitive = game.getCompetitiveness(); //TODO check for string to compare
        sport = game.getTeam1().getSport().getName();
//        date = calculateDate(game.getStartTime()); TODO implement
//        time = calculateTime(game.getStartTime()); TODO implement
        totalPlayers = game.getTeam1().getSport().getQuantity();
        currentplayers = game.getTeam1().getPlayers().size() + game.getTeam2().getPlayers().size();
        hasStarted = game.getStartTime().isBefore(LocalDateTime.now());
        hasFinished = game.getFinishTime().isBefore(LocalDateTime.now());
        results = game.getResult();
        if (!hasFinished) {
            winnerTeam = -1;
        }
        else if(game.getSecondScoreFromResult() != game.getSecondScoreFromResult()){
            winnerTeam = game.getFirstScoreFromResult() > game.getSecondScoreFromResult() ? 1 : 2;
        }
        else {
            winnerTeam = 0;
        }
    }

    public static GameDto from(Game game) {
        return new GameDto(game);
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getCreator() {
        return creator;
    }

    public boolean isCompetitive() {
        return isCompetitive;
    }

    public String getSport() {
        return sport;
    }

    public LocalDate getDate() {
        return date;
    }

    public LocalTime getTime() {
        return time;
    }

    public int getTotalPlayers() {
        return totalPlayers;
    }

    public int getCurrentplayers() {
        return currentplayers;
    }

    public boolean isHasStarted() {
        return hasStarted;
    }

    public boolean isHasFinished() {
        return hasFinished;
    }

    public String getResults() {
        return results;
    }

    public int getWinnerTeam() {
        return winnerTeam;
    }
}
