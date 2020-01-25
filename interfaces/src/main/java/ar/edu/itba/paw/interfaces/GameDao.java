package ar.edu.itba.paw.interfaces;

import ar.edu.itba.paw.models.Game;
import ar.edu.itba.paw.models.GameSort;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface GameDao {
    public Optional<Game> create(final String teamName1, final String teamName2, final String startTime,
                                 final String finishTime, final String type, final String result,
                                 final String country, final String state, final String city,
                                 final String street, final String tornamentName, final String description,
                                 final String title);

    public Optional<Game> findByKey(String teamName1, String startTime, String finishTime);

    public List<Game> findGames(final LocalDateTime minStartTime, final LocalDateTime maxStartTime,
                                final LocalDateTime minFinishTime, final LocalDateTime maxFinishTime,
                                final List<String> types, final List<String> sportNames,
                                final Integer minQuantity, final Integer maxQuantity,
                                final List<String> countries, final List<String> states,
                                final List<String> cities, final Integer minFreePlaces,
                                final Integer maxFreePlaces, final List<String> usernamesPlayersInclude,
                                final List<String> usernamesPlayersNotInclude,
                                final List<String> usernamesCreatorsInclude,
                                final List<String> usernamesCreatorsNotInclude, final GameSort sort,
                                final Boolean onlyWithResults);

    public List<Game> gamesThatAUserPlayInTeam1(final long userId);

    public List<Game> gamesThatAUserPlayInTeam2(final long userId);

    public Optional<Game> modify(final String teamName1, final String teamName2, final String startTime,
                                 final String finishTime, final String type, final String result,
                                 final String country, final String state, final String city,
                                 final String street, final String tornamentName, final String description,
                                 final String teamName1Old, final String startTimeOld, final String finishTimeOld);

    public boolean remove(final String teamName1, final String startTime, final String finishTime);
}
