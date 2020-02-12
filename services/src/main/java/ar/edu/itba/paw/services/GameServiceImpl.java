package ar.edu.itba.paw.services;

import ar.edu.itba.paw.exceptions.LackOfPermissionsException;
import ar.edu.itba.paw.exceptions.alreadyexists.GameAlreadyExistException;
import ar.edu.itba.paw.exceptions.invalidstate.GameInvalidStateException;
import ar.edu.itba.paw.exceptions.notfound.GameNotFoundException;
import ar.edu.itba.paw.exceptions.MalformedGameKeyException;
import ar.edu.itba.paw.exceptions.UnauthorizedException;
import ar.edu.itba.paw.exceptions.notfound.PlayerNotFoundException;
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
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;

import static ar.edu.itba.paw.models.GameType.INDIVIDUAL;
import static ar.edu.itba.paw.models.GameType.GROUP;
import static ar.edu.itba.paw.models.GameType.COMPETITIVE;
import static ar.edu.itba.paw.models.GameType.FRIENDLY;


@Service
public class GameServiceImpl implements GameService {

    private static final Logger LOGGER = LoggerFactory.getLogger(GameServiceImpl.class);
    private static final int NUMBER_OF_TEAMS = 2;

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

    @Transactional
    @Override
    public Game create(final String teamName1, final String teamName2, final LocalDateTime startTime,
                       final long durationInMinutes, final boolean isCompetitive, final boolean isIndividual,
                       final String country, final String state, final String city,
                       final String street, final String tornamentName, final String description,
                       final String title, final String sportName) {
        if (startTime.isBefore(LocalDateTime.now())) {
            LOGGER.trace("StartTime must happen in the future");
            throw new IllegalArgumentException("Birthday must happen in the past");
        }
        PremiumUser logged = sessionService.getLoggedUser()
                .orElseThrow(() -> new UnauthorizedException("Must be logged to create match"));

        GameKey gameKey = new GameKey(startTime, teamName1, startTime.plusMinutes(durationInMinutes));
        String type = (isIndividual ? INDIVIDUAL.toString() : GROUP.toString()) + '-' +
                (isCompetitive ? COMPETITIVE.toString() : FRIENDLY.toString());
        String newTeamName1 = teamName1, newTeamName2 = teamName2;

        if (isIndividual) {
            if (teamName1 != null || teamName2 != null) {
                LOGGER.trace("Creation fails, match '{}' cannot be individual and add teams to match", gameKey.toString());
                throw new IllegalArgumentException("Creation fails, match '" + gameKey.toString() + "' cannot be " +
                        "individual and add teams to match"); //TODO: map!!!!!
            }
            newTeamName1 = teamService.createTempTeam1(logged.getUserName(), logged.getUser().getUserId(), sportName)
                    .getName();
            newTeamName2 = teamService.createTempTeam2(logged.getUserName(), logged.getUser().getUserId(), sportName)
                    .getName();
        }

        //TODO: check a game with key is no already added

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

    @Transactional
    @Override
    public Game insertPlayerInGame(final String key, final long userId, final String code, final Locale locale) {
        if (code != null) {
            User user = userService.getUserFromData(code, key);
            if (user.getUserId() != userId) {
                LOGGER.trace("Insert player from game fails, code '{}' is invalid for player '{}'", code, userId);
                throw new LackOfPermissionsException("Insert player from game fails, code '" + code +
                        "' is invalid for player '" + userId + "'");
            }
            return insertTemporalUserInGame(key, user, locale);
        }
        PremiumUser loggedUser = sessionService.getLoggedUser().orElseThrow(() -> {
            LOGGER.trace("Insert player from game fails, must be logged or have a code");
            return new UnauthorizedException("Insert player from game fails, must be logged or have a code");
        });
        if (loggedUser.getUser().getUserId() != userId) {
            LOGGER.trace("User '{}' is not user '{}'", loggedUser.getUserName(), userId);
            throw new LackOfPermissionsException("User '" + loggedUser.getUserName() +
                    "' is not user '" + userId + "'");
        }
        return insertUserInGame(key, loggedUser.getUser().getUserId());
    }

    @Transactional
    @Override
    public void deleteUserInGameWithCode(final String key, final long userId, final String code) {
        Game game = findByKey(key).orElseThrow(() -> {
            LOGGER.trace("Delete player from game failed, game '{}' not found", key);
            return GameNotFoundException.ofKey(key);
        });

        if (game.getStartTime().isBefore(LocalDateTime.now())) {
            LOGGER.trace("Delete player from game failed, game '{}' has already started", key);
            throw GameInvalidStateException.ofGameAlreadyStarted(key);
        }

        if (code != null) {
            User user = userService.getUserFromData(code, key);
            if (user.getUserId() != userId) {
                LOGGER.trace("Delete player from game fails, code '{}' is invalid for player '{}'", code, userId);
                throw new LackOfPermissionsException("Delete player from game fails, code '" + code +
                        "' is invalid for player '" + userId + "'");
            }
            deleteUserInGame(game, userId);
        }
        else {
            PremiumUser loggedUser = sessionService.getLoggedUser().orElseThrow(() -> {
                LOGGER.trace("Delete player from game fails, must be logged");
                return new UnauthorizedException("Delete player from game fails, must be logged");
            });
            if (!game.getTeam1().getLeader().equals(loggedUser) &&
                    loggedUser.getUser().getUserId() != userId) {
                LOGGER.trace("Delete player from game fails, must be leader of match '{}' or player '{}'", key, userId);
                throw new LackOfPermissionsException("Delete player from game fails, must be leader of match '" + key +
                        "' or player '" + userId + "'");
            }
            if (userId == game.getTeam1().getLeader().getUser().getUserId()) {
                LOGGER.trace("Delete player from game fails, can not delete leader '{}' from match '{}'", userId, key);
                throw GameInvalidStateException.ofGameWithOutLeader(key);
            }
            deleteUserInGame(game, userId);
        }
    }

    private void deleteUserInGame(final Game game, final long userIdReceive) {
        for (User user : game.getTeam1().getPlayers()) {
            if (user.getUserId() == userIdReceive) {
                LOGGER.trace("Found user: {} in team1", userIdReceive);
                game.getPrimaryKey().setTeam1(teamService.removePlayer(game.team1Name(), userIdReceive));
                if (!premiumUserService.findById(userIdReceive).isPresent()) {
                    userService.remove(userIdReceive);
                }
                return;
            }
        }
        for (User user : game.getTeam2().getPlayers()) {
            if (user.getUserId() == userIdReceive) {
                LOGGER.trace("Found user: {} in team2", userIdReceive);
                game.setTeam2(teamService.removePlayer(game.team2Name(), userIdReceive));
                if (!premiumUserService.findById(userIdReceive).isPresent()) {
                    userService.remove(userIdReceive);
                }
                return;
            }
        }
        LOGGER.trace("Not found user: '{}' in match '{}'", userIdReceive, game.getKey());
        throw PlayerNotFoundException.ofId(userIdReceive);
    }

    @Transactional
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

    @Transactional
    @Override
    public Game modify(final String teamName1, final String teamName2, final LocalDateTime startTime,
                       final Long minutesOfDuration, final String type, final String result,
                       final String country, final String state, final String city,
                       final String street, final String tornamentName, final String description,
                       final String title, final String key) {
        if (startTime.isBefore(LocalDateTime.now())) {
            LOGGER.trace("StartTime must happen in the future");
            throw new IllegalArgumentException("Birthday must happen in the past");
        }
        GameKey gameKey = getGameKey(key);
        Game gameOld = findByKey(key).orElseThrow(() -> {
            LOGGER.trace("Update game failed, game '{}' not found", key);
            return GameNotFoundException.ofKey(key);
        });
        PremiumUser loggedUser = sessionService.getLoggedUser()
                .orElseThrow(() -> new UnauthorizedException("Must be logged to update match"));

        if (!gameOld.getTeam1().getLeader().equals(loggedUser)) {
            LOGGER.trace("User '{}' is not creator of '{}' match", loggedUser.getUserName(), key);
            throw new LackOfPermissionsException("User '" + loggedUser.getUserName() +
                    "' is not creator of '" + key + "' match");
        }
        if ((teamName1 != null || teamName2 != null) && gameOld.getGroupType().equals(INDIVIDUAL.toString())) {
            throw new IllegalArgumentException("Cannot modify teams in a individual match"); //TODO: map!!
        }

        //TODO: check a game with key is no already added

        return gameDao.modify(
                teamName1, teamName2, startTime,
                (minutesOfDuration != null) ? gameOld.getStartTime().plusMinutes(minutesOfDuration) : null,
                type, result, country, state, city, street, tornamentName, description, title, gameKey.getTeamName1(),
                gameKey.getStartTime(), gameKey.getFinishTime()
        ).orElseThrow(() -> {
            LOGGER.trace("Update game failed, game '{}' not found", key);
            return GameNotFoundException.ofKey(key);
        });
    }

    @Transactional
    @Override
    public void remove(final String key) {
        Game game = findByKey(key).orElseThrow(() -> {
            LOGGER.trace("Delete game failed, game '{}' not found", key);
            return GameNotFoundException.ofKey(key);
        });
        GameKey gameKey = getGameKey(key);
        PremiumUser loggedUser = sessionService.getLoggedUser()
                .orElseThrow(() -> new UnauthorizedException("Must be logged to delete match"));
        if (!game.getPrimaryKey().getTeam1().getLeader().equals(loggedUser)) {
            LOGGER.trace("User '{}' is not creator of '{}' match", loggedUser.getUserName(), key);
            throw new LackOfPermissionsException("User '" + loggedUser.getUserName() +
                    "' is not creator of '" + key + "' match");
        }
        if (game.getStartTime().isBefore(LocalDateTime.now())
                && (game.getTeam1().getPlayers().size() + game.getTeam1().getPlayers().size() ==
                game.getTeam1().getSport().getQuantity() * NUMBER_OF_TEAMS)) {
            LOGGER.trace("Delete game failed, game '{}' has already started", key);
            throw GameInvalidStateException.ofGameAlreadyStarted(key);
        }
        if (game.getTeam1().getPlayers().size() + game.getTeam1().getPlayers().size() ==
                game.getTeam1().getSport().getQuantity() * NUMBER_OF_TEAMS) {
            LOGGER.trace("Delete game failed, game '{}' is full", key);
            throw GameInvalidStateException.ofGameFull(key);
        }
        if (gameDao.remove(gameKey.getTeamName1(), gameKey.getStartTime(), gameKey.getFinishTime())) {
            LOGGER.trace("{} removed", key);
        } else {
            LOGGER.error("{} wasn't removed", key);
            throw GameNotFoundException.ofKey(key);
        }
    }

    @Transactional
    @Override
    public Optional<Game> findByKey(final String key) {
        GameKey gameKey = getGameKey(key);
        return gameDao.findByKey(gameKey.getTeamName1(), gameKey.getStartTime(), gameKey.getFinishTime());
    }

    @Transactional
    @Override
    public Game updateResultOfGame(final String key, final int scoreTeam1, final int scoreTeam2) {
        Game game = findByKey(key).orElseThrow(() -> {
            LOGGER.trace("Update game failed, game '{}' not found", key);
            return GameNotFoundException.ofKey(key);
        });
        PremiumUser premiumUser = sessionService.getLoggedUser()
                .orElseThrow(() -> new UnauthorizedException("Must be logged to update match result"));
        if (game.getTeam1().getPlayers().size() + game.getTeam2().getPlayers().size() <
                game.getTeam1().getSport().getQuantity() * NUMBER_OF_TEAMS) {
            LOGGER.trace("Update game failed, game '{}' is not full", key);
            throw GameInvalidStateException.ofGameNotFull(key);
        }
        if (!game.getTeam1().getLeader().equals(premiumUser)) {
            LOGGER.trace("Update game failed, user '{}' is not creator of '{}' match", premiumUser.getUserName(), key);
            throw new LackOfPermissionsException("User '" + premiumUser.getUserName() +
                    "' must be the creator of '" + key + "' match");
        }
        if (game.getFinishTime().isAfter(LocalDateTime.now())) {
            throw GameInvalidStateException.ofGameNotPlayedYet(key);
        }
        game.setResult(scoreTeam1 + "-" + scoreTeam2);

        return game;
    }

    @Transactional
    @Override
    public void createRequestToJoin(final String key, final String firstName, final String lastName,
                                    final String email, final Locale locale) {
        Game game = findByKey(key).orElseThrow(() -> {
            LOGGER.trace("Join request creation failed, game '{}' not found", key);
            return GameNotFoundException.ofKey(key);
        });
        User newUser = userService.create(firstName, lastName, email);
        userService.sendConfirmMatchAssistance(newUser, game, key, locale);
    }

    @Transactional
    @Override
    public List<List<Game>> getGamesThatPlay(final long userId) {
        List<List<Game>> listsOfGames = new LinkedList<>();
        listsOfGames.add(gameDao.gamesThatAUserPlayInTeam1(userId));
        listsOfGames.add(gameDao.gamesThatAUserPlayInTeam2(userId));
        return listsOfGames;
    }

    private Game insertUserInGame(String key, long userId) {
        if (getGameKey(key).getStartTime().isBefore(LocalDateTime.now())) {
            LOGGER.trace("Insert player in game failed, game '{}' has already started", key);
            throw GameInvalidStateException.ofGameAlreadyStarted(key);
        }

        Game game = findByKey(key).orElseThrow(() -> {
            LOGGER.trace("Insert user in game failed, game '{}' not found", key);
            return GameNotFoundException.ofKey(key);
        });


        if (game.getTeam1().getPlayers().size() < game.getTeam1().getSport().getQuantity()) {
            game = insertUserInGameTeam(game, userId, true);
        }
        else if (game.getTeam2().getPlayers().size() < game.getTeam2().getSport().getQuantity()) {
            game = insertUserInGameTeam(game, userId, false);
        }
        else {
            throw GameInvalidStateException.ofGameFull(key);
        }
        return game;
    }

    private Game insertUserInGameTeam(final Game game, final long userId, final boolean toTeam1) {
        if (!toTeam1) {
            if(!game.getTeam1().getPlayers().stream()
                    .filter((u) -> u.getUserId() == userId).collect(Collectors.toList()).isEmpty()) {
                LOGGER.trace("Inset user to game failed, user already joined to match");
                throw GameInvalidStateException.ofGameAlreadyJoined(game.getKey(), userId);
            }
            game.setTeam2(teamService.addPlayer(game.team2Name(), userId));
        } else {
            if(game.getTeam2() != null && !game.getTeam2().getPlayers().stream()
                    .filter((u) -> u.getUserId() == userId).collect(Collectors.toList()).isEmpty()) {
                LOGGER.trace("Inset user to game failed, user already joined to match");
                throw GameInvalidStateException.ofGameAlreadyJoined(game.getKey(), userId);
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
            throw new MalformedGameKeyException("Invalid key");
        }
    }

    private Game insertTemporalUserInGame(final String key, final User user, final Locale locale) {
        Game game;
        try {
            game = insertUserInGame(key, user.getUserId());
            userService.sendCancelOptionMatch(user, game, key, locale);
        }
        catch (Exception e) {
            userService.remove(user.getUserId());
            throw e;
        }
        return game;
    }
}
