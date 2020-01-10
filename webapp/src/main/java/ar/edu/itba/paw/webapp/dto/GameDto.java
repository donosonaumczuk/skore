package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.models.Game;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

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
    private TeamDto team1;
    private TeamDto team2;
    // -1 undefined, 0 for tie, 1 for team 1 and 2 for team 2
    private int winnerTeam;
//    private ResultDto results; TODO maybe dto with specific results according to sport

    private GameDto(Game game, TeamDto team1, TeamDto team2) {
        title = game.getTitle();
        description = game.getDescription();
        creator = game.getTeam1().getLeader().getUserName();
        isCompetitive = game.getCompetitiveness().equals("Competitive");
        sport = game.getTeam1().getSport().getName();
        LocalDateTime startTime = game.getStartTime();
        date = LocalDate.of(startTime.getYear(), startTime.getMonth(), startTime.getDayOfMonth());
        time = LocalTime.of(startTime.getHour(), startTime.getMinute());
        totalPlayers = game.getTeam1().getSport().getQuantity() * 2;//one for each team
        currentplayers = team1.getPlayerQuantity() + team2.getPlayerQuantity();
        hasStarted = game.getStartTime().isBefore(LocalDateTime.now());
        hasFinished = game.getFinishTime().isBefore(LocalDateTime.now());
        results = game.getResult();
        this.team1 = team1;
        this.team2 = team2;
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

    public static GameDto from(Game game, TeamDto team1, TeamDto team2) {
        return new GameDto(game, team1, team2);
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

    public TeamDto getTeam1() {
        return team1;
    }

    public TeamDto getTeam2() {
        return team2;
    }

    public int getWinnerTeam() {
        return winnerTeam;
    }
}
