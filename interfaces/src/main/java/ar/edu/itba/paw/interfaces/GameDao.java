package ar.edu.itba.paw.interfaces;

import ar.edu.itba.paw.models.Game;
import ar.edu.itba.paw.models.Team;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * Created by agustin on 21/09/18.
 */
public interface GameDao {
    Optional<Game> findGame(String teamName1, String teamName2,
                            LocalDateTime StartTime, LocalDateTime EndTime);
}
