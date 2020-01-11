package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.models.Game;
import ar.edu.itba.paw.webapp.controller.GameController;
import ar.edu.itba.paw.webapp.controller.UserController;
import com.google.common.collect.ImmutableList;
import org.springframework.hateoas.Link;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

public class GameDto {

    private final String title;
    private final String description;
    private final String creator;
    private final boolean isCompetitive;
    private final String sport;
    private final LocalDate date;
    private final LocalTime time;
    private final int totalPlayers;
    private final int currentplayers;
    private final boolean hasStarted;
    private final boolean hasFinished;
    private final String results;
    private final TeamDto team1;
    private final TeamDto team2;
    private final List<Link> links;
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
        this.links = getHateoasLinks(game, creator);
    }

    public static GameDto from(Game game, TeamDto team1, TeamDto team2) {
        return new GameDto(game, team1, team2);
    }

    private List<Link> getHateoasLinks(Game game, String creator) {
        String gameId = game.getTeam1().getName() + game.getStartTime().toString() + game.getFinishTime();
        //TODO ver de mejorar el id, ese es el id de nuestra base, pero es re choto un endpoint asi
        return ImmutableList.of(
                new Link(GameController.getGameEndpoint(gameId), Link.REL_SELF),
                new Link(UserController.getProfileEndpoint(creator), "creator"));
                //TODO add team 1 and team 2 on future
                //TODO add sport in future
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

    public List<Link> getLinks() {
        return links;
    }
}
