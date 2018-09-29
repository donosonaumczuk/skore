package ar.edu.itba.paw.interfaces;

import ar.edu.itba.paw.models.Team;
import org.springframework.transaction.annotation.Transactional;

public interface TeamService {
    @Transactional
    public Team findByTeamName(final String teamName);

    @Transactional
    public Team create(final String leaderName, final long leaderId,
                       final String acronym, final String teamName,
                       final boolean isTemp, final String sportName);

    @Transactional
    public boolean remove(final String teamName);

    @Transactional
    public Team addPlayer(final String teamName, final long userId);

    @Transactional
    public Team removePlayer(final String teamName, final long userId);

    @Transactional
    public Team updateTeamInfo(final String newTeamName, final String newAcronym,
                               final String newLeaderName, final String newSportName,
                               final String oldTeamName);
}
