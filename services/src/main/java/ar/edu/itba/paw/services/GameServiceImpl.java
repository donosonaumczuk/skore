package ar.edu.itba.paw.services;

import ar.edu.itba.paw.exceptions.GameHasNotBeenPlayException;
import ar.edu.itba.paw.exceptions.GameNotFoundException;
import ar.edu.itba.paw.exceptions.TeamFullException;
import ar.edu.itba.paw.interfaces.GameDao;
import ar.edu.itba.paw.interfaces.GameService;
import ar.edu.itba.paw.interfaces.TeamService;
import ar.edu.itba.paw.models.Game;
import ar.edu.itba.paw.models.PremiumUser;
import ar.edu.itba.paw.models.Team;
import ar.edu.itba.paw.models.User;
import org.json.JSONArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Service
public class GameServiceImpl implements GameService {

    private static final Logger LOGGER = LoggerFactory.getLogger(GameServiceImpl.class);

    @Autowired
    private GameDao gameDao;

    @Autowired
    private TeamService teamService;

    public GameServiceImpl() {

    }

    private static String getFinishTime(final String startTime, String duration) {
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
        LocalTime durationTime = LocalTime.parse(duration, timeFormatter);
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime startDateTime = LocalDateTime.parse(startTime, dateTimeFormatter);
        String finishTime = startDateTime.plusHours(durationTime.getHour()).plusMinutes(durationTime.getMinute())
                .format(dateTimeFormatter);
        return finishTime;
    }

    private static String formatDate(String date) {
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm");
        LocalDateTime localDateTime = LocalDateTime.parse(date, timeFormatter);
        timeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        String formattedDate = localDateTime.format(timeFormatter);
        return formattedDate;
    }

    @Override
    public Game create(final String teamName1, final String teamName2, final String startTime,
                       final String duration, final String type, final String result,
                       final String country, final String state, final String city,
                       final String street, final String tornamentName, final String description,
                       final String title) {
        final String newStartTime = formatDate(startTime);
        String finishTime = getFinishTime(newStartTime, duration);

        Optional<Game> game = gameDao.create(teamName1, teamName2, newStartTime + ":00", finishTime + ":00", type, result,
                country, state, city, street, tornamentName, description, title);

        return game.orElseThrow(() -> new GameNotFoundException("There is not a game of " + teamName1 + " vs " + teamName2
                + " starting at " + newStartTime + "and finishing at " + finishTime));
    }

    @Override
    public Game createNoTeamGame(final String startTime, final String duration,
                                 final String type, final String country,
                                 final String state, final String city,
                                 final String street, final String tornamentName,
                                 final String description, final String creatorName,
                                 final long creatorId, final String sportName, final String title) {
        Team team1 = teamService.createTempTeam1(creatorName, creatorId, sportName);
        Team team2 = teamService.createTempTeam2(creatorName, creatorId, sportName);

        Game game = create(team1.getName(), team2.getName(), startTime, duration, type, null,
                      country, state, city, street, tornamentName, description, title);
        final String newStartTime = formatDate(startTime);
        String finishTime = getFinishTime(newStartTime, duration);
        insertUserInGame(game.getTeam1().getName(), newStartTime + ":00", finishTime + ":00", creatorId);
        return game;
    }

    @Override
    public Game insertUserInGame(final String teamName1, final String startTime,
                                 final String finishTime, final long userId) {
        Game game;
        try {
            game = insertUserInGameTeam(teamName1, startTime, finishTime, userId, true);
        }
        catch (TeamFullException e) {
            game = insertUserInGameTeam(teamName1, startTime, finishTime, userId, false);
        }
        return game;
    }

    private Game insertUserInGameTeam(final String teamName1, final String startTime,
                                      final String finishTime, final long userId,
                                      final boolean toTeam1) {
        Game game = findByKey(teamName1, startTime, finishTime);
        Game gameAns;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        if(!toTeam1) {
            teamService.addPlayer(game.team2Name(), userId);
            gameAns = findByKey(game.team1Name(), formatter.format(game.getStartTime()),
                        formatter.format(game.getFinishTime()));
        }
        else {
            teamService.addPlayer(game.team1Name(), userId);
            gameAns = findByKey(game.team1Name(), formatter.format(game.getStartTime()),
                        formatter.format(game.getFinishTime()));
        }
        return gameAns;
    }

    @Override
    public Game deleteUserInGame(final String teamName1, final String startTime,
                                 final String finishTime, final long userId) {
        Game game = findByKey(teamName1, startTime, finishTime);
        for (User user:game.getTeam1().getPlayers()) {
            if(user.getUserId() == userId) {
                LOGGER.trace("Found user: {} in team1",userId);
                teamService.removePlayer(game.team1Name(), userId);
                return findByKey(teamName1, startTime, finishTime);
            }
        }
        for (User user:game.getTeam2().getPlayers()) {
            if(user.getUserId() == userId) {
                LOGGER.trace("Found user: {} in team2",userId);
                teamService.removePlayer(game.team2Name(), userId);
                return findByKey(teamName1, startTime, finishTime);
            }
        }
        LOGGER.trace("Not found user: {} in game",userId);
        return game;
    }

    @Override
    public Game findByKey(String teamName1, String startTime, String finishTime) {
        Optional<Game> game = gameDao.findByKey(teamName1, startTime, finishTime);

        return game.orElseThrow(() -> new GameNotFoundException("There is not a game of " + teamName1
                + " starting at " + startTime + "and finishing at " + finishTime));
    }

    @Override
    public List<Game> findGamesPage(final String minStartTime, final String maxStartTime,
                                    final String minFinishTime, final String maxFinishTime,
                                    final JSONArray types, final JSONArray sportNames,
                                    final Integer minQuantity, final Integer maxQuantity,
                                    final JSONArray countries, final JSONArray states,
                                    final JSONArray cities, final Integer minFreePlaces,
                                    final Integer maxFreePlaces, final int pageNumber) {
        List<Game> games =
                gameDao.findGames(minStartTime, maxStartTime, minFinishTime, maxFinishTime,
                    jsonArrayToList(types), jsonArrayToList(sportNames), minQuantity,  maxQuantity,
                    jsonArrayToList(countries), jsonArrayToList(states), jsonArrayToList(cities),
                    minFreePlaces, maxFreePlaces, null, false,
                    false);

        int start = ((pageNumber-1)*10 < games.size())?(pageNumber-1)*10:games.size();
        int end = (pageNumber*10 < games.size())?pageNumber*10:games.size();
        return games.subList(start, end);
    }

    @Override
    public List<Game> findGamesPageThatIsAPartOf(final String minStartTime, final String maxStartTime,
                                                 final String minFinishTime, final String maxFinishTime,
                                                 final JSONArray types, final JSONArray sportNames,
                                                 final Integer minQuantity, final Integer maxQuantity,
                                                 final JSONArray countries, final JSONArray states,
                                                 final JSONArray cities, final Integer minFreePlaces,
                                                 final Integer maxFreePlaces, final int pageNumber,
                                                 final PremiumUser user) {
        List<Game> games =
                gameDao.findGames(minStartTime, maxStartTime, minFinishTime, maxFinishTime,
                        jsonArrayToList(types), jsonArrayToList(sportNames), minQuantity,  maxQuantity,
                        jsonArrayToList(countries), jsonArrayToList(states), jsonArrayToList(cities),
                        minFreePlaces, maxFreePlaces, user, true, false);

        int start = ((pageNumber-1)*10 < games.size())?(pageNumber-1)*10:games.size();
        int end = (pageNumber*10 < games.size())?pageNumber*10:games.size();
        return games.subList(start, end);
    }

    @Override
    public List<Game> findGamesPageThatIsNotAPartOf(final String minStartTime, final String maxStartTime,
                                                    final String minFinishTime, final String maxFinishTime,
                                                    final JSONArray types, final JSONArray sportNames,
                                                    final Integer minQuantity, final Integer maxQuantity,
                                                    final JSONArray countries, final JSONArray states,
                                                    final JSONArray cities, final Integer minFreePlaces,
                                                    final Integer maxFreePlaces, final int pageNumber,
                                                    final PremiumUser user) {
        List<Game> games =
                gameDao.findGames(minStartTime, maxStartTime, minFinishTime, maxFinishTime,
                        jsonArrayToList(types), jsonArrayToList(sportNames), minQuantity,  maxQuantity,
                        jsonArrayToList(countries), jsonArrayToList(states), jsonArrayToList(cities),
                        minFreePlaces, maxFreePlaces, user, false, false);

        int start = ((pageNumber-1)*10 < games.size())?(pageNumber-1)*10:games.size();
        int end = (pageNumber*10 < games.size())?pageNumber*10:games.size();
        return games.subList(start, end);
    }

    @Override
    public List<Game> findGamesPageCreateBy(final String minStartTime, final String maxStartTime,
                                            final String minFinishTime, final String maxFinishTime,
                                            final JSONArray types, final JSONArray sportNames,
                                            final Integer minQuantity, final Integer maxQuantity,
                                            final JSONArray countries, final JSONArray states,
                                            final JSONArray cities, final Integer minFreePlaces,
                                            final Integer maxFreePlaces, final int pageNumber,
                                            final PremiumUser user) {
        List<Game> games =
                gameDao.findGames(minStartTime, maxStartTime, minFinishTime, maxFinishTime,
                        jsonArrayToList(types), jsonArrayToList(sportNames), minQuantity,  maxQuantity,
                        jsonArrayToList(countries), jsonArrayToList(states), jsonArrayToList(cities),
                        minFreePlaces, maxFreePlaces, user, true, true);

        int start = ((pageNumber-1)*10 < games.size())?(pageNumber-1)*10:games.size();
        int end = (pageNumber*10 < games.size())?pageNumber*10:games.size();
        return games.subList(start, end);
    }

    @Override
    public Game modify(final String teamName1, final String teamName2, final String startTime,
                       final String finishTime, final String type, final String result,
                       final String country, final String state, final String city,
                       final String street, final String tornamentName, final String description,
                       final String teamName1Old, final String startTimeOld, final String finishTimeOld) {
        Optional<Game> game = gameDao.modify(teamName1, teamName2, startTime, finishTime, type, result,
                country, state, city, street, tornamentName, description, teamName1Old, startTimeOld,
                finishTimeOld);
        return game.orElseThrow(() -> new GameNotFoundException("There is not a game of " + teamName1
                + " starting at " + startTime + "and finishing at " + finishTime));
    }

    @Override
    public boolean remove(final String teamName1, final String startTime, final String finishTime,
                          final long userId) {
        Game game = findByKey(teamName1, startTime, finishTime);
        if(game.getTeam1().getLeader().getUser().getUserId() != userId) {
            return false;
        }
        return gameDao.remove(teamName1, startTime, finishTime);
    }

    private List<String> jsonArrayToList(JSONArray jsonArray) {
        List<String> list = new ArrayList<String>();
        for (int i=0; i<jsonArray.length(); i++) {
            list.add( jsonArray.getString(i) );
        }
        return list;
    }

    @Override
    public Game findByKeyFromURL(final String matchURLKey) {
        final int URL_DATE_LENGTH = 12;
        final int MIN_TEAMNAME1_LENGTH = 1;
        final int MIN_LENGTH = URL_DATE_LENGTH * 2 + MIN_TEAMNAME1_LENGTH;

        int length = matchURLKey.length();

        if(length < MIN_LENGTH)
            throw new GameNotFoundException("matchURLKey '" + matchURLKey + "' is too short to be formatted to a key");

        String startTime = urlDateToKeyDate(matchURLKey.substring(0, URL_DATE_LENGTH));
        String teamName1 = matchURLKey.substring(URL_DATE_LENGTH, length - URL_DATE_LENGTH);
        String finishTime = urlDateToKeyDate(matchURLKey.substring(length - URL_DATE_LENGTH));

        Game game = findByKey(teamName1, startTime, finishTime);
        teamService.getAccountsList(game.getTeam1());
        teamService.getAccountsList(game.getTeam2());
        return game;
    }

    @Override
    public String urlDateToKeyDate(final String date) {
        StringBuilder formattedDate = new StringBuilder(date);

        formattedDate = formattedDate.insert(4, "-");
        formattedDate = formattedDate.insert(7, "-");
        formattedDate = formattedDate.insert(10, " ");
        formattedDate = formattedDate.insert(13, ":");
        formattedDate = formattedDate.insert(16, ":00");

        return formattedDate.toString();
    }

    @Override
    public Game updateResultOfGame(final String teamName1, final String starTime, final String finishTime,
                                   final int scoreTeam1, final int scoreTeam2) {
        Game game = gameDao.findByKey(teamName1, starTime, finishTime)
                .orElseThrow(() -> new GameNotFoundException("Game does not exist"));
        if(game.getFinishTime().compareTo(LocalDateTime.now()) > 0) {
            throw new GameHasNotBeenPlayException("The game has not been play");
        }
        game.setResult(scoreTeam1+"-"+scoreTeam2);
        return game;
    }

    @Override
    public List<List<Game>> getGamesThatPlay(final long userId) {
        List<List<Game>> listsOfGames = new LinkedList<>();
        listsOfGames.add(gameDao.gamesThatAUserPlayInTeam1(userId));
        listsOfGames.add(gameDao.gamesThatAUserPlayInTeam2(userId));
        return listsOfGames;
    }

}
