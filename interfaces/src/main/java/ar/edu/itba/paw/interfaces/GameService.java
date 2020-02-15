package ar.edu.itba.paw.interfaces;

import ar.edu.itba.paw.models.Game;
import ar.edu.itba.paw.models.GameSort;
import ar.edu.itba.paw.models.Page;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

public interface GameService {

    Game create(final String teamName1, final String teamName2, final LocalDateTime startTime,
                final long durationInMinutes, final boolean isCompetitive, final boolean isIndividual,
                final String country, final String state, final String city,
                final String street, final String tornamentName, final String description,
                final String title, final String sportName);

    Page<Game> findGamesPage(final LocalDateTime minStartTime, final LocalDateTime maxStartTime,
                             final LocalDateTime minFinishTime, final LocalDateTime maxFinishTime,
                             final List<String> types, final List<String> sportNames,
                             final Integer minQuantity, final Integer maxQuantity,
                             final List<String> countries, final List<String> states,
                             final List<String> cities, final Integer minFreePlaces,
                             final Integer maxFreePlaces, final List<String> usernamesPlayersInclude,
                             final List<String> usernamesPlayersNotInclude,
                             final List<String> usernamesCreatorsInclude,
                             final List<String> usernamesCreatorsNotInclude, final Integer limit,
                             final Integer offset, final GameSort sort, final Boolean onlyWithResults,
                             final boolean onlyWithLikedUsers, final boolean onlyWithLikedSport);

    Game modify(final String teamName1, final String teamName2, final LocalDateTime startTime,
                final Long minutesOfDuration, final String type, final String result,
                final String country, final String state, final String city,
                final String street, final String tornamentName, final String description,
                final String title, final String key);

    void remove(final String key);

    Optional<Game> findByKey(final String key);

    Game insertPlayerInGame(final String key, final long userId, final String code, final Locale locale);

    void deleteUserInGameWithCode(final String key, final long userId, final String code);

    Game updateResultOfGame(final String key, final int scoreTeam1, final int scoreTeam2);

    List<List<Game>> getGamesThatPlay(final long userId);

    void createRequestToJoin(final String key, final String firstName, final String lastName, final String email,
                             final Locale locale);
}
