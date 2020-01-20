package ar.edu.itba.paw.interfaces;

import ar.edu.itba.paw.models.Game;
import ar.edu.itba.paw.models.GameSort;
import ar.edu.itba.paw.models.Page;
import ar.edu.itba.paw.models.PremiumUser;
import org.json.JSONArray;
import org.springframework.transaction.annotation.Transactional;

import java.beans.Transient;
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
    public Page<Game> findGamesPage(final String minStartTime, final String maxStartTime,
                                    final String minFinishTime, final String maxFinishTime,
                                    final List<String> types, final List<String> sportNames,
                                    final Integer minQuantity, final Integer maxQuantity,
                                    final List<String> countries, final List<String> states,
                                    final List<String> cities, final Integer minFreePlaces,
                                    final Integer maxFreePlaces, final List<String> usernamesPlayersInclude,
                                    final List<String> usernamesPlayersNotInclude,
                                    final List<String> usernamesCreatorsInclude,
                                    final List<String> usernamesCreatorsNotInclude, final Integer limit,
                                    final Integer offset, final GameSort sort);

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

    @Transactional
    public Game findByKeyFromURL(final String matchURLKey);

    public String urlDateToKeyDate(String date);

    @Transactional
    public Game updateResultOfGame(final String teamName1, final String starTime, final String finishTime,
                                   final int scoreTeam1, final int scoreTeam2);

    @Transactional
    public List<List<Game>> getGamesThatPlay(final long userId);
}
