package ar.edu.itba.paw.interfaces;

import ar.edu.itba.paw.models.Game;

import java.util.List;
import java.util.Optional;

public interface GameService {
    Game create(final String teamName1, final String teamName2, final String startTime,
                final String finishTime, final String type, final String result,
                final String country, final String state, final String city,
                final String street, final String tornamentName, final String description);

    Game findByKey(String teamName1, String startTime, String finishTime);

    List<Game> findGames(final String minStartTime, final String maxStartTime,
                         final String minFinishTime, final String maxFinishTime,
                         final List<String> types, final List<String> sportNames,
                         final Integer minQuantity, final Integer maxQuantity,
                         final List<String> countries, final List<String> states,
                         final List<String> cities, final Integer minFreePlaces,
                         final Integer maxFreePlaces);

    Game modify(final String teamName1, final String teamName2, final String startTime,
                final String finishTime, final String type, final String result,
                final String country, final String state, final String city,
                final String street, final String tornamentName, final String description,
                final String teamName1Old, final String teamName2Old,
                final String startTimeOld, final String finishTimeOld);

}
