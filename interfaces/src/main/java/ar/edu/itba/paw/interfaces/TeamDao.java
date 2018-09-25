package ar.edu.itba.paw.interfaces;

import ar.edu.itba.paw.models.Team;

import java.util.Optional;

public interface TeamDao {

    public Optional<Team> findByName(final String teamName);

    public Optional<Team> create(final String leaderName, final String acronym,
                                  final String teamName,final boolean isTemp,
                                  final String sportName);

    public boolean remove(final String teamName);

    public Optional<Team> addPlayer(final String teamName, final long userId);

    public Optional<Team> removePlayer(final String teamName, final long userId);

}
