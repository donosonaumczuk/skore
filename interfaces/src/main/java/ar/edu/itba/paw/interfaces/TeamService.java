package ar.edu.itba.paw.interfaces;

import ar.edu.itba.paw.models.Team;

public interface TeamService {
    public Team findByTeamName(final String teamName);

    public Team create(final String leaderName, final long leaderId,
                       final String acronym, final String teamName,
                       final boolean isTemp, final String sportName);

    public boolean remove(final String teamName);

    public Team addPlayer(final String teamName, final long userId);

    public Team removePlayer(final String teamName, final long userId);

    public Team updateTeamInfo(final String newTeamName, final String newAcronym,
                               final String newLeaderName, final String newSportName,
                               final String oldTeamName);
}
