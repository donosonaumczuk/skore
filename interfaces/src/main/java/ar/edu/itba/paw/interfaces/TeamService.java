package ar.edu.itba.paw.interfaces;

import ar.edu.itba.paw.models.PremiumUser;
import ar.edu.itba.paw.models.Team;
import ar.edu.itba.paw.models.User;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

public interface TeamService {
    @Transactional
    public Team findByTeamName(final String teamName);

    @Transactional
    public Team create(final String leaderName, final long leaderId,
                       final String acronym, final String teamName,
                       final boolean isTemp, final String sportName);

    Team createTempTeam1(final String leaderName, final long leaderId, final String sportName);

    Team createTempTeam2(final String leaderName, final long leaderId, final String sportName);

    @Transactional
    public boolean remove(final String teamName);


    public Team addPlayer(final String teamName, final long userId);

    @Transactional
    public Team removePlayer(final String teamName, final long userId);

    @Transactional
    public Team updateTeamInfo(final String newTeamName, final String newAcronym,
                               final String newLeaderName, final String newSportName,
                               final String oldTeamName);

    @Transactional
    public Map<User, PremiumUser> getAccountsMap(Team team);
}
