package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.models.PremiumUser;
import ar.edu.itba.paw.models.Team;
import ar.edu.itba.paw.models.User;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class TeamDto {

    private String teamName;
    private List<TeamPlayerDto> players;
    private Integer playerQuantity;
//    private final List<Link> links; TODO if we decide to show teams should have one

    private TeamDto() {
        /* Required by JSON object mapper */
    }

    private TeamDto(List<TeamPlayerDto> players, String teamName) {
        this.teamName = teamName;
        this.players = players;
        playerQuantity = players.size();
    }

    public static TeamDto from(List<TeamPlayerDto> players) {
        return new TeamDto(players, null);
    }

    public static TeamDto from(Map<User, PremiumUser> userMap, Team team) {
        Set<User> teamusers = team.getPlayers();
        List<TeamPlayerDto> teamPlayers = teamusers.stream().map(user -> {
            PremiumUser premiumUser = userMap.get(user);
            if(premiumUser != null) {
                return TeamPlayerDto.from(premiumUser);
            }
            else {
                return TeamPlayerDto.from(user);
            }
        }).collect(Collectors.toList());
        return TeamDto.from(teamPlayers, team.getName());//TODO add a check to see if name is created by user or autoasigned
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

    public Integer getPlayerQuantity() {
        return playerQuantity;
    }
}
