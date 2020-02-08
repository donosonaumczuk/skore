package ar.edu.itba.paw.interfaces;

import ar.edu.itba.paw.models.Team;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

public interface TeamDao {

    Optional<Team> findByTeamName(final String teamName);

    Optional<Team> create(final String leaderName, final long leaderId,
                          final String acronym, final String teamName,
                          final boolean isTemp, final String sportName,
                          final MultipartFile file) throws IOException;

    boolean remove(final String teamName);

    Optional<Team> addPlayer(final String teamName, final long userId);

    Optional<Team> removePlayer(final String teamName, final long userId);

    Optional<Team> updateTeamInfo(final String newTeamName, final String newAcronym,
                                  final String newLeaderName, final String newSportName,
                                  final String oldTeamName);
}
