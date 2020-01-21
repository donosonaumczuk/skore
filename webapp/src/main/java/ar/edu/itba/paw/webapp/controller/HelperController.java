package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.models.PremiumUser;
import ar.edu.itba.paw.models.Team;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.webapp.dto.TeamDto;
import ar.edu.itba.paw.webapp.dto.TeamPlayerDto;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class HelperController {

    public static TeamDto mapTeamToDto(Team team) {
        Map<User, PremiumUser> userMap = team.getAccountsPlayers();
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
}
