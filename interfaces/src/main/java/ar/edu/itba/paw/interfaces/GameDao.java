package ar.edu.itba.paw.interfaces;

import ar.edu.itba.paw.models.Game;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface GameDao {
    Optional<Game> findByKey(final String teamName1, final String teamName2,
                            final LocalDateTime StartTime, final LocalDateTime EndTime);

    List<Game> findGames(final String minStartTime, final String maxStartTime,
                         final String minFinishTime, final String maxFinishTime,
                         final List<String> types, final List<String> sportNames,
                         final Integer minQuantity, final Integer maxQuantity,
                         final List<String> countries, final List<String> states,
                         final List<String> cities, final Integer minFreePlaces,
                         final Integer maxFreePlaces);
}
