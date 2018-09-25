package ar.edu.itba.paw.interfaces;

import ar.edu.itba.paw.models.Game;

import java.util.List;
import java.util.Optional;

public interface GameDao {
    Optional<Game> create(String teamName1, String teamName2, String startTime,
                          String finishTime, String type, String result, String country,
                          String state, String city, String street, String tornamentName);

    Optional<Game> findByKey(final String teamName1, final String teamName2,
                            final String StartTime, final String EndTime);

    List<Game> findGames(final String minStartTime, final String maxStartTime,
                         final String minFinishTime, final String maxFinishTime,
                         final List<String> types, final List<String> sportNames,
                         final Integer minQuantity, final Integer maxQuantity,
                         final List<String> countries, final List<String> states,
                         final List<String> cities, final Integer minFreePlaces,
                         final Integer maxFreePlaces);

    Optional<Game> modify(String teamName1, String teamName2, String startTime,
                          String finishTime, String type, String result, String country,
                          String state, String city, String street, String tornamentName,
                          String teamName1Old, String teamName2Old, String startTimeOld,
                          String finishTimeOld);
}
