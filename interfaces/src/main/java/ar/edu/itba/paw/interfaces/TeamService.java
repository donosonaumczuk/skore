package ar.edu.itba.paw.interfaces;

import ar.edu.itba.paw.models.PremiumUser;
import ar.edu.itba.paw.models.Team;
import ar.edu.itba.paw.models.User;

import java.util.Map;
import java.util.Optional;

public interface TeamService {

    Optional<Team> findByTeamName(final String teamName);

    Team create(final String leaderName, final long leaderId,
                final String acronym, final String teamName,
                final boolean isTemp, final String sportName);

    Team createTempTeam1(final String leaderName, final long leaderId, final String sportName);

    Team createTempTeam2(final String leaderName, final long leaderId, final String sportName);

    boolean remove(final String teamName);


    Team addPlayer(final String teamName, final long userId);

    Team removePlayer(final String teamName, final long userId);

    Team updateTeamInfo(final String newTeamName, final String newAcronym,
                        final String newLeaderName, final String newSportName,
                        final String oldTeamName);

    Map<User, PremiumUser> getAccountsMap(Team team);
}
