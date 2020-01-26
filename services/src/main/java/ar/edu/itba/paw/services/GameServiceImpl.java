package ar.edu.itba.paw.services;

import ar.edu.itba.paw.exceptions.GameHasNotBeenPlayException;
import ar.edu.itba.paw.exceptions.GameNotFoundException;
import ar.edu.itba.paw.exceptions.TeamFullException;
import ar.edu.itba.paw.interfaces.GameDao;
import ar.edu.itba.paw.interfaces.GameService;
import ar.edu.itba.paw.interfaces.TeamService;
import ar.edu.itba.paw.models.Game;
import ar.edu.itba.paw.models.GameKey;
import ar.edu.itba.paw.models.GameSort;
import ar.edu.itba.paw.models.Page;
import ar.edu.itba.paw.models.Team;
import ar.edu.itba.paw.models.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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

    private static final String GROUP       = "Group";
    private static final String INDIVIDUAL  = "Individual";
    private static final String COMPETITIVE = "Competitive";
    private static final String FRIENDLY    = "Friendly";

    public GameServiceImpl() {

    }

    @Override
    public Optional<Game> create(final String teamName1, final String teamName2, final LocalDateTime startTime,
                                 final long durationInMinutes, final boolean isCompetitive, final boolean isIndividual,
                                 final String country, final String state, final String city,
                                 final String street, final String tornamentName, final String description,
                                 final String title) {
        String type = (isIndividual ? INDIVIDUAL : GROUP) + '-' + (isCompetitive ? COMPETITIVE : FRIENDLY);

        return gameDao.create(teamName1, teamName2, startTime.toString(),
                startTime.plusMinutes(durationInMinutes).toString(), type, null,
                country, state, city, street, tornamentName, description, title);
    }

    @Override
    public Optional<Game> createNoTeamGame(final LocalDateTime startTime, final long durationInMinutes,
                                           final boolean isCompetitive, final String country,
                                           final String state, final String city,
                                           final String street, final String tornamentName,
                                           final String description, final String creatorName,
                                           final long creatorId, final String sportName, final String title) {
        Team team1 = teamService.createTempTeam1(creatorName, creatorId, sportName);
        Team team2 = teamService.createTempTeam2(creatorName, creatorId, sportName);

        Optional<Game> game = create(team1.getName(), team2.getName(), startTime, durationInMinutes, isCompetitive,
                true, country, state, city, street, tornamentName, description, title);
        return insertUserInGameTeam(game.orElse(null), creatorId, true);
    }

    @Override
    public Optional<Game> insertUserInGame(final String key, final long userId) {
        Optional<Game> game;
        try {
            game = insertUserInGameTeam(findByKey(key).orElse(null), userId, true);
        }
        catch (TeamFullException e) {
            game = insertUserInGameTeam(findByKey(key).orElse(null), userId, false);
        }
        return game;
    }

    @Override
    public Optional<Game> deleteUserInGame(final String key, final long userId) {
        Optional<Game> game = findByKey(key);
        if (game.isPresent()) {
            for (User user : game.get().getTeam1().getPlayers()) {
                if (user.getUserId() == userId) {
                    LOGGER.trace("Found user: {} in team1", userId);
                    teamService.removePlayer(game.get().team1Name(), userId);
                    return findByKey(key);
                }
            }
            for (User user : game.get().getTeam2().getPlayers()) {
                if (user.getUserId() == userId) {
                    LOGGER.trace("Found user: {} in team2", userId);
                    teamService.removePlayer(game.get().team2Name(), userId);
                    return findByKey(key);
                }
            }
            LOGGER.trace("Not found user: {} in game",userId);
        }
        else {
            LOGGER.trace("Game '{}' does not exist", key);
        }
        return game;
    }

    @Override
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
                                    final Integer offset, final GameSort sort, final Boolean onlyWithResults) {
        List<Game> games = gameDao.findGames(getLocalDateTimeFromString(minStartTime),
                getLocalDateTimeFromString(maxStartTime), getLocalDateTimeFromString(minFinishTime),
                getLocalDateTimeFromString(maxFinishTime), types, sportNames, minQuantity, maxQuantity, countries,
                states, cities, minFreePlaces, maxFreePlaces, usernamesPlayersInclude, usernamesPlayersNotInclude,
                usernamesCreatorsInclude, usernamesCreatorsNotInclude, sort, onlyWithResults);

        return new Page<>(games, offset, limit);
    }

    private LocalDateTime getLocalDateTimeFromString(String string) {
        if(string == null || !string.matches("\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}")) {
            return null;
        }
        return LocalDateTime.parse(string);
    }

    @Override
    public Game modify(final String teamName1, final String teamName2, final String startTime,
                       final String finishTime, final String type, final String result,
                       final String country, final String state, final String city,
                       final String street, final String tornamentName, final String description,
                       final String key) {
        GameKey gameKey = new GameKey(key);
        Optional<Game> game = gameDao.modify(teamName1, teamName2, startTime, finishTime, type, result,
                country, state, city, street, tornamentName, description, gameKey.getTeamName1(),
                gameKey.getStartTime(), gameKey.getFinishTime());
        return game.orElseThrow(() -> new GameNotFoundException("There is not a game of " + teamName1
                + " starting at " + startTime + "and finishing at " + finishTime));
    }

    @Override
    public boolean remove(final String key) {
        GameKey gameKey = new GameKey(key);
        return gameDao.remove(gameKey.getTeamName1(), gameKey.getStartTime(), gameKey.getFinishTime());
    }

    @Override
    public Optional<Game> findByKey(final String key) {
        GameKey gameKey = new GameKey(key);
        return gameDao.findByKey(gameKey.getTeamName1(), gameKey.getStartTime(), gameKey.getFinishTime());
    }

    @Override
    public Optional<Game> updateResultOfGame(final String key, final int scoreTeam1, final int scoreTeam2) {
        GameKey gameKey = new GameKey(key);
        Optional<Game> game = gameDao.findByKey(gameKey.getTeamName1(), gameKey.getStartTime(), gameKey.getFinishTime());
        if (game.isPresent()) {
            if (game.get().getFinishTime().compareTo(LocalDateTime.now()) > 0) {
                throw new GameHasNotBeenPlayException("The game has not been play");
            }
            game.get().setResult(scoreTeam1 + "-" + scoreTeam2);
        }
        return game;
    }

    @Override
    public List<List<Game>> getGamesThatPlay(final long userId) {
        List<List<Game>> listsOfGames = new LinkedList<>();
        listsOfGames.add(gameDao.gamesThatAUserPlayInTeam1(userId));
        listsOfGames.add(gameDao.gamesThatAUserPlayInTeam2(userId));
        return listsOfGames;
    }

    private Optional<Game> insertUserInGameTeam(final Game game, final long userId, final boolean toTeam1) {
        Optional<Game> gameAns = Optional.ofNullable(game);
        if (game != null) {
            if (!toTeam1) {
                teamService.addPlayer(game.team2Name(), userId);
            } else {
                teamService.addPlayer(game.team1Name(), userId);
            }
            gameAns = gameDao.findByKey(game.team1Name(), game.getStartTime().toString(), game.getFinishTime().toString()); //TODO check
        }
        return gameAns;
    }
}
