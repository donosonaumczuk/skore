package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.models.Game;
import ar.edu.itba.paw.models.PremiumUser;
import ar.edu.itba.paw.webapp.controller.GameController;
import ar.edu.itba.paw.webapp.controller.SportController;
import ar.edu.itba.paw.webapp.controller.UserController;
import com.google.common.collect.ImmutableList;
import org.springframework.hateoas.Link;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

public class GameDto {

    private static final int TEAMS_PER_SPORT = 2;

    private String title;
    private String description;
    private String creator;
    private String tornamentNameThatIsFrom;
    private Boolean individual;
    private Boolean competitive;
    private String sport;
    private String sportName;
    private DateDto date;
    private TimeDto time;
    private Long minutesOfDuration;
    private PlaceDto location;
    private Integer totalPlayers;
    private Integer currentPlayers;
    private Boolean hasStarted;
    private Boolean hasFinished;
    private String results;
    private TeamDto team1;
    private String teamName1;
    private TeamDto team2;
    private String teamName2;
    private String key;
    private Boolean isInTeam1;
    private Boolean isInTeam2;
    private List<Link> links;

    private GameDto() {
        /* Required by JSON object mapper */
    }

    private GameDto(Game game, TeamDto team1, TeamDto team2, Boolean isInTeam1, Boolean isInTeam2) {
        this.isInTeam1 = isInTeam1;
        this.isInTeam2 = isInTeam2;
        title = game.getTitle();
        description = game.getDescription();
        creator = game.getTeam1().getLeader().getUserName();
        individual = game.getGroupType().equals("Individual");
        competitive = game.getCompetitiveness().equals("Competitive");
        sportName = game.getTeam1().getSport().getDisplayName();
        sport = game.getTeam1().getSport().getName();
        LocalDateTime startTime = game.getStartTime();
        LocalDateTime finishTime = game.getFinishTime();
        date = DateDto.from(LocalDate.of(startTime.getYear(), startTime.getMonth(), startTime.getDayOfMonth()));
        time = TimeDto.from(LocalTime.of(startTime.getHour(), startTime.getMinute()));
        minutesOfDuration = ChronoUnit.MINUTES.between(startTime, finishTime);
        tornamentNameThatIsFrom = game.getTornament();
        location = PlaceDto.from(game.getPlace());
        totalPlayers = game.getTeam1().getSport().getQuantity() * TEAMS_PER_SPORT;
        currentPlayers = game.getTeam1().getPlayers().size() +
                (game.getTeam2() == null ? 0 : game.getTeam2().getPlayers().size());
        hasStarted = game.getStartTime().isBefore(LocalDateTime.now());
        hasFinished = game.getFinishTime().isBefore(LocalDateTime.now());
        results = game.getResult();
        this.team1 = team1;
        this.team2 = team2;
        key = game.getKey();
        this.links = getHateoasLinks(game, creator);
    }

    public static GameDto from(Game game, TeamDto team1, TeamDto team2) {
        return new GameDto(game, team1, team2, null, null);
    }

    public static GameDto from(Game game, PremiumUser loggedUser) {
        boolean isInTeam1 = false, isInTeam2 = false;
        if (loggedUser != null) {
            if (game.getTeam1().getPlayers().contains(loggedUser.getUser())) {
                isInTeam1 = true;
                isInTeam2 = false;
            } else if (game.getTeam2().getPlayers().contains(loggedUser.getUser())) {
                isInTeam1 = false;
                isInTeam2 = true;
            }
        }
        return new GameDto(game, null, null, isInTeam1, isInTeam2);
    }

    private List<Link> getHateoasLinks(Game game, String creator) {
        String gameId = game.getTeam1().getName() + game.getStartTime().toString() + game.getFinishTime();
        //TODO improve id so that it is more semantic
        return ImmutableList.of(
                new Link(GameController.getGameEndpoint(gameId), Link.REL_SELF),
                new Link(UserController.getUserProfileEndpoint(creator), "creator"),
                new Link(UserController.getUserImageEndpoint(creator), "creatorImage"),
                new Link(SportController.getSportImageEndpoint(sport), "sportImage"));

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

    public Boolean getCompetitive() {
        return competitive;
    }

    public String getSport() {
        return sport;
    }

    public Long getMinutesOfDuration() {
        return minutesOfDuration;
    }

    public String getSportName() {
        return sportName;
    }

    public Optional<DateDto> getDate() {
        return Optional.ofNullable(date);
    }

    public Optional<TimeDto> getTime() {
        return Optional.ofNullable(time);
    }


    public Optional<PlaceDto> getLocation() {
        return Optional.ofNullable(location);
    }

    public Integer getTotalPlayers() {
        return totalPlayers;
    }

    public Integer getCurrentPlayers() {
        return currentPlayers;
    }

    public Boolean isHasStarted() {
        return hasStarted;
    }

    public Boolean isHasFinished() {
        return hasFinished;
    }

    public Boolean isIndividual() {
        return individual;
    }

    public String getResults() {
        return results;
    }

    public Optional<TeamDto> getTeam1() {
        return Optional.ofNullable(team1);
    }

    public Optional<TeamDto> getTeam2() {
        return Optional.ofNullable(team2);
    }

    public Boolean getInTeam1() {
        return isInTeam1;
    }

    public Boolean getInTeam2() {
        return isInTeam2;
    }

    public List<Link> getLinks() {
        return links;
    }

    public String getTornamentName() {
        return tornamentNameThatIsFrom;
    }

    public String getKey() {
        return key;
    }

    public String getTeamName1() {
        return teamName1;
    }

    public String getTeamName2() {
        return teamName2;
    }
}
