package ar.edu.itba.paw.interfaces;

import ar.edu.itba.paw.models.Game;
import ar.edu.itba.paw.models.PremiumUser;
import org.json.JSONArray;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface GameService {
    @Transactional
    public Game create(final String teamName1, final String teamName2, final String startTime,
                       final String finishTime, final String type, final String result,
                       final String country, final String state, final String city,
                       final String street, final String tornamentName, final String description,
                       final String title);

    @Transactional
    public Game createNoTeamGame( final String startTime, final String finishTime,
                                  final String type, final String country,
                                  final String state, final String city,
                                  final String street, final String tornamentName,
                                  final String description, final String creatorName,
                                  final long creatorId, final String sportName, final String title);

    @Transactional
    public Game findByKey(String teamName1, String startTime, String finishTime);

    @Transactional
    public List<Game> findGamesPage(final String minStartTime, final String maxStartTime,
                                    final String minFinishTime, final String maxFinishTime,
                                    final JSONArray types, final JSONArray sportNames,
                                    final Integer minQuantity, final Integer maxQuantity,
                                    final JSONArray countries, final JSONArray states,
                                    final JSONArray cities, final Integer minFreePlaces,
                                    final Integer maxFreePlaces, final int pageNumber);

    @Transactional
    public List<Game> findGamesPageThatIsNotAPartOf(final String minStartTime, final String maxStartTime,
                                                    final String minFinishTime, final String maxFinishTime,
                                                    final JSONArray types, final JSONArray sportNames,
                                                    final Integer minQuantity, final Integer maxQuantity,
                                                    final JSONArray countries, final JSONArray states,
                                                    final JSONArray cities, final Integer minFreePlaces,
                                                    final Integer maxFreePlaces, final int pageNumber,
                                                    final PremiumUser user);

    @Transactional
    public List<Game> findGamesPageThatIsAPartOf(final String minStartTime, final String maxStartTime,
                                                 final String minFinishTime, final String maxFinishTime,
                                                 final JSONArray types, final JSONArray sportNames,
                                                 final Integer minQuantity, final Integer maxQuantity,
                                                 final JSONArray countries, final JSONArray states,
                                                 final JSONArray cities, final Integer minFreePlaces,
                                                 final Integer maxFreePlaces, final int pageNumber,
                                                 final PremiumUser user);

    @Transactional
    public List<Game> findGamesPageCreateBy(final String minStartTime, final String maxStartTime,
                                            final String minFinishTime, final String maxFinishTime,
                                            final JSONArray types, final JSONArray sportNames,
                                            final Integer minQuantity, final Integer maxQuantity,
                                            final JSONArray countries, final JSONArray states,
                                            final JSONArray cities, final Integer minFreePlaces,
                                            final Integer maxFreePlaces, final int pageNumber,
                                            final PremiumUser user);

    @Transactional
    public Game modify(final String teamName1, final String teamName2, final String startTime,
                       final String finishTime, final String type, final String result,
                       final String country, final String state, final String city,
                       final String street, final String tornamentName, final String description,
                       final String teamName1Old, final String startTimeOld, final String finishTimeOld);

    @Transactional
    public Game insertUserInGame(final String teamName1, final String startTime,
                                 final String finishTime, final long userId);

    @Transactional
    public Game deleteUserInGame(final String teamName1, final String startTime,
                                 final String finishTime, final long userId);

    @Transactional
    public boolean remove(final String teamName1, final String startTime, final String finishtime,
                          final long userId);

}
