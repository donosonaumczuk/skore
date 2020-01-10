package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.models.PremiumUser;
import ar.edu.itba.paw.models.User;
import org.springframework.hateoas.Link;

import java.util.List;

public class TeamDto {
    private final String teamName;
    private final List<TeamPlayerDto> players;
    private final int playerQuantity;
//    private final List<Link> links; TODO if we decide to show teams should have one

    private TeamDto(List<TeamPlayerDto> players, String teamName) {
        this.teamName = teamName;
        this.players = players;
        playerQuantity = players.size();
    }

    public static TeamDto from(List<TeamPlayerDto> players) {
        return new TeamDto(players, null);
    }

    public static TeamDto from(List<TeamPlayerDto> players, String teamName) {
        return new TeamDto(players, teamName);
    }

    public String getTeamName() {
        return teamName;
    }

    public List<TeamPlayerDto> getPlayers() {
        return players;
    }

    public int getPlayerQuantity() {
        return playerQuantity;
    }
}
