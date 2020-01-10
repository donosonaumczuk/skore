package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.models.PremiumUser;
import ar.edu.itba.paw.models.User;

import java.util.List;

public class TeamDto {
    private String teamName;
    private List<TeamPlayerDto> players;
    private int playerQuantity;

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
