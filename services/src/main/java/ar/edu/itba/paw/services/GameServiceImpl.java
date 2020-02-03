package ar.edu.itba.paw.services;

import ar.edu.itba.paw.exceptions.AlreadyJoinedToMatchException;
import ar.edu.itba.paw.exceptions.ForbiddenException;
import ar.edu.itba.paw.exceptions.alreadyexists.GameAlreadyExistException;
import ar.edu.itba.paw.exceptions.GameHasNotBeenPlayException;
import ar.edu.itba.paw.exceptions.notfound.GameNotFoundException;
import ar.edu.itba.paw.exceptions.InvalidGameKeyException;
import ar.edu.itba.paw.exceptions.TeamFullException;
import ar.edu.itba.paw.exceptions.UnauthorizedException;
import ar.edu.itba.paw.interfaces.GameDao;
import ar.edu.itba.paw.interfaces.GameService;
import ar.edu.itba.paw.interfaces.PremiumUserService;
import ar.edu.itba.paw.interfaces.SessionService;
import ar.edu.itba.paw.interfaces.TeamService;
import ar.edu.itba.paw.interfaces.UserService;
import ar.edu.itba.paw.models.Game;
import ar.edu.itba.paw.models.GameKey;
import ar.edu.itba.paw.models.GameSort;
import ar.edu.itba.paw.models.Page;
import ar.edu.itba.paw.models.PremiumUser;
import ar.edu.itba.paw.models.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import static ar.edu.itba.paw.models.GameType.INDIVIDUAL;
import static ar.edu.itba.paw.models.GameType.GROUP;
import static ar.edu.itba.paw.models.GameType.COMPETITIVE;
import static ar.edu.itba.paw.models.GameType.FRIENDLY;


@Service
public class GameServiceImpl implements GameService {

    private static final Logger LOGGER = LoggerFactory.getLogger(GameServiceImpl.class);

    @Autowired
    private GameDao gameDao;

    @Autowired
    private TeamService teamService;

    @Autowired
    private UserService userService;

    @Autowired
    private PremiumUserService premiumUserService;

    @Autowired
    private SessionService sessionService;

    public GameServiceImpl() {

    }

    @Override
    public Game create(final String teamName1, final String teamName2, final LocalDateTime startTime,
                       final long durationInMinutes, final boolean isCompetitive, final boolean isIndividual,
                       final String country, final String state, final String city,
                       final String street, final String tornamentName, final String description,
                       final String title, final String sportName) {
        PremiumUser logged = sessionService.getLoggedUser()
                .orElseThrow(() -> new UnauthorizedException("Must be logged to create match"));

        GameKey gameKey = new GameKey(startTime, teamName1, startTime.plusMinutes(durationInMinutes));
        String type = (isIndividual ? INDIVIDUAL.toString() : GROUP.toString()) + '-' +
                (isCompetitive ? COMPETITIVE.toString() : FRIENDLY.toString());
        String newTeamName1 = teamName1, newTeamName2 = teamName2;

        if (isIndividual) {
            newTeamName1 = teamService.createTempTeam1(logged.getUserName(), logged.getUser().getUserId(), sportName)
                    .getName();
            newTeamName2 = teamService.createTempTeam2(logged.getUserName(), logged.getUser().getUserId(), sportName)
                    .getName();
            if (teamName1 != null || teamName2 != null) {
                LOGGER.trace("Creation fails, match '{}' cannot be individual and add teams to match", gameKey.toString());
                throw new IllegalArgumentException("Creation fails, match '" + gameKey.toString() + "' cannot be " +
                        "individual and add teams to match");
            }
        }

        Game newGame = gameDao.create(newTeamName1, newTeamName2, startTime,
                startTime.plusMinutes(durationInMinutes), type, null,
                country, state, city, street, tornamentName, description, title)
                .orElseThrow(() -> {
                    LOGGER.trace("Creation fails, match '{}' already exist", gameKey.toString());
                    return GameAlreadyExistException.ofKey(gameKey);
                });

        if (isIndividual) {
            newGame = insertUserInGameTeam(newGame, logged.getUser().getUserId(), true);
        }
        return newGame;
    }

    @Override
    public Game insertPremiumUserInGame(final String key, final String username) {
        PremiumUser loggedUser = sessionService.getLoggedUser().get();//TODO: check
        if (!loggedUser.getUserName().equals(username)) {
            LOGGER.trace("User '{}' is not user '{}'", loggedUser.getUserName(), username);
            throw new ForbiddenException("User '" + loggedUser.getUserName() +
                    "' is not user '" + username + "'");
        }
        return insertUserInGame(key, loggedUser.getUser().getUserId());
    }

    @Override
    public Game insertTemporalUserInGame(final String key, final String code) {
        Game game;
        User user = userService.getUserFromData(code, key);
        try {
            game = insertUserInGame(key, user.getUserId());
            userService.sendCancelOptionMatch(user, game, key);
        }
        catch (Exception e) {
            userService.remove(user.getUserId());
            throw e;
        }
        return game;
    }

    @Override
    public boolean deleteUserInGame(final String key, final long userId) {
        Game game = findByKey(key);
        for (User user : game.getTeam1().getPlayers()) {
            if (user.getUserId() == userId) {
                LOGGER.trace("Found user: {} in team1", userId);
                game.getPrimaryKey().setTeam1(teamService.removePlayer(game.team1Name(), userId));
                if (!premiumUserService.findById(userId).isPresent()) {
                    userService.remove(userId);
                }
                return true;
            }
        }
        for (User user : game.getTeam2().getPlayers()) {
            if (user.getUserId() == userId) {
                LOGGER.trace("Found user: {} in team2", userId);
                game.setTeam2(teamService.removePlayer(game.team2Name(), userId));
                if (!premiumUserService.findById(userId).isPresent()) {
                    userService.remove(userId);
                }
                return true;
            }
        }
        LOGGER.trace("Not found user: '{}' in match '{}'", userId, key);
        return false;
    }

    @Override
    public Page<Game> findGamesPage(final LocalDateTime minStartTime, final LocalDateTime maxStartTime,
                                    final LocalDateTime minFinishTime, final LocalDateTime maxFinishTime,
                                    final List<String> types, final List<String> sportNames,
                                    final Integer minQuantity, final Integer maxQuantity,
                                    final List<String> countries, final List<String> states,
                                    final List<String> cities, final Integer minFreePlaces,
                                    final Integer maxFreePlaces, final List<String> usernamesPlayersInclude,
                                    final List<String> usernamesPlayersNotInclude,
                                    final List<String> usernamesCreatorsInclude,
                                    final List<String> usernamesCreatorsNotInclude, final Integer limit,
                                    final Integer offset, final GameSort sort, final Boolean onlyWithResults) {
        List<Game> games = gameDao.findGames(minStartTime, maxStartTime, minFinishTime, maxFinishTime, types,
                sportNames, minQuantity, maxQuantity, countries, states, cities, minFreePlaces, maxFreePlaces,
                usernamesPlayersInclude, usernamesPlayersNotInclude, usernamesCreatorsInclude,
                usernamesCreatorsNotInclude, sort, onlyWithResults);

        return new Page<>(games, offset, limit);
    }

    @Override
    public Game modify(final String teamName1, final String teamName2, final LocalDateTime startTime,
                       final Long minutesOfDuration, final String type, final String result,
                       final String country, final String state, final String city,
                       final String street, final String tornamentName, final String description,
                       final String title, final String key) {
        GameKey gameKey = getGameKey(key);
        Game gameOld = findByKey(key), game;
        PremiumUser loggedUser = sessionService.getLoggedUser().get();//TODO check
        if (!gameOld.getTeam1().getLeader().equals(loggedUser)) {
            LOGGER.trace("User '{}' is not creator of '{}' match", loggedUser.getUserName(), key);
            throw new ForbiddenException("User '" + loggedUser.getUserName() +
                    "' is not creator of '" + key + "' match");
        }
        if ((teamName1 != null || teamName2 != null) && gameOld.getGroupType().equals(INDIVIDUAL.toString())) {
            throw new IllegalArgumentException("Cannot modify teams in a individual match");
        }
        game = gameDao.modify(teamName1, teamName2, startTime, (minutesOfDuration != null) ?
                        gameOld.getStartTime().plusMinutes(minutesOfDuration) : null, type,
                result, country, state, city, street, tornamentName, description, title, gameKey.getTeamName1(),
                gameKey.getStartTime(), gameKey.getFinishTime()).orElseThrow(() -> {
                    LOGGER.trace("Modify fails, match '{}' not found", key);
                    return GameNotFoundException.ofKey(key);
                });

        return game;
    }

    @Override
    public boolean remove(final String key) {
        GameKey gameKey = getGameKey(key);
        PremiumUser loggedUser = sessionService.getLoggedUser().get();//TODO maybe check
        if (!findByKey(key).getPrimaryKey().getTeam1().getLeader().equals(loggedUser)) {
            LOGGER.trace("User '{}' is not creator of '{}' match", loggedUser.getUserName(), key);
            throw new ForbiddenException("User '" + loggedUser.getUserName() +
                    "' is not creator of '" + key + "' match");
        }
        return gameDao.remove(gameKey.getTeamName1(), gameKey.getStartTime(), gameKey.getFinishTime());
    }

    @Override
    public Game findByKey(final String key) {
        GameKey gameKey = getGameKey(key);
        return gameDao.findByKey(gameKey.getTeamName1(), gameKey.getStartTime(), gameKey.getFinishTime())
                .orElseThrow(() -> {
                    LOGGER.trace("Find by key fails, match '{}' not found", key);
                    return GameNotFoundException.ofKey(key);
                });
    }

    @Override
    public Game updateResultOfGame(final String key, final int scoreTeam1, final int scoreTeam2) {
        Game game = findByKey(key);
        PremiumUser premiumUser = sessionService.getLoggedUser().get();//TODO check
        if (!game.getTeam1().getLeader().equals(premiumUser)) {
            LOGGER.trace("User '{}' is not creator of '{}' match", premiumUser.getUserName(), key);
            throw new ForbiddenException("User '" + premiumUser.getUserName() +
                    "' is not creator of '" + key + "' match");
        }
        if (game.getFinishTime().compareTo(LocalDateTime.now()) > 0) {
            throw new GameHasNotBeenPlayException("The match has not been play");
        }
        game.setResult(scoreTeam1 + "-" + scoreTeam2);

        return game;
    }

    public void createRequestToJoin(final String key, final String firstName, final String lastName,
                                    final String email) {
        User newUser = userService.create(firstName, lastName, email);
        userService.sendConfirmMatchAssistance(newUser, findByKey(key), key);
    }

    @Override
    public List<List<Game>> getGamesThatPlay(final long userId) {
        List<List<Game>> listsOfGames = new LinkedList<>();
        listsOfGames.add(gameDao.gamesThatAUserPlayInTeam1(userId));
        listsOfGames.add(gameDao.gamesThatAUserPlayInTeam2(userId));
        return listsOfGames;
    }

    private Game insertUserInGame(String key, long userId) {
        Game game = findByKey(key);
        try {
            game = insertUserInGameTeam(game, userId, true);
        }
        catch (TeamFullException e) {
            game = insertUserInGameTeam(game, userId, false);
        }
        return game;
    }

    private Game insertUserInGameTeam(final Game game, final long userId, final boolean toTeam1) {
        if (!toTeam1) {
            if(!game.getTeam1().getPlayers().stream()
                    .filter((u) -> u.getUserId() == userId).collect(Collectors.toList()).isEmpty()) {
                LOGGER.trace("User already joined to match");
                throw new AlreadyJoinedToMatchException("User already joined to match");
            }
            game.setTeam2(teamService.addPlayer(game.team2Name(), userId));
        } else {
            if(game.getTeam2() != null && !game.getTeam2().getPlayers().stream()
                    .filter((u) -> u.getUserId() == userId).collect(Collectors.toList()).isEmpty()) {
                LOGGER.trace("User already joined to match");
                throw new AlreadyJoinedToMatchException("User already joined to match");
            }
            game.getPrimaryKey().setTeam1(teamService.addPlayer(game.team1Name(), userId));
        }
        return game;
    }

    private GameKey getGameKey(final String key) {
        try {
            return new GameKey(key);
        }
        catch (Exception e) {
            throw new InvalidGameKeyException("Invalid key");
        }
    }
}
