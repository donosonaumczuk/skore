package ar.edu.itba.paw.services;

import ar.edu.itba.paw.exceptions.GameAlreadyExist;
import ar.edu.itba.paw.exceptions.GameHasNotBeenPlayException;
import ar.edu.itba.paw.exceptions.GameNotFoundException;
import ar.edu.itba.paw.exceptions.InvalidGameKeyException;
import ar.edu.itba.paw.exceptions.TeamFullException;
import ar.edu.itba.paw.exceptions.UnauthorizedExecption;
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
import ar.edu.itba.paw.models.Team;
import ar.edu.itba.paw.models.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

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

    private static final String GROUP       = "Group";
    private static final String INDIVIDUAL  = "Individual";
    private static final String COMPETITIVE = "Competitive";
    private static final String FRIENDLY    = "Friendly";

    public GameServiceImpl() {

    }

    @Override
    public Game create(final String teamName1, final String teamName2, final LocalDateTime startTime,
                                 final long durationInMinutes, final boolean isCompetitive, final boolean isIndividual,
                                 final String country, final String state, final String city,
                                 final String street, final String tornamentName, final String description,
                                 final String title) {
        //TODO: validate that teams exist and maybe that logged user has permissions
        String type = (isIndividual ? INDIVIDUAL : GROUP) + '-' + (isCompetitive ? COMPETITIVE : FRIENDLY);

        return gameDao.create(teamName1, teamName2, startTime.toString(),
                startTime.plusMinutes(durationInMinutes).toString(), type, null,
                country, state, city, street, tornamentName, description, title)
                .orElseThrow(() -> {
                    GameKey gameKey = new GameKey(startTime, teamName1, startTime.plusMinutes(durationInMinutes));
                    LOGGER.trace("Creation fails, match '{}' already exist", gameKey.toString());
                    return new GameAlreadyExist("Creation fails, match '" + gameKey.toString() + "' already exist");
                });
    }

    @Override
    public Game createNoTeamGame(final LocalDateTime startTime, final long durationInMinutes,
                                           final boolean isCompetitive, final String country,
                                           final String state, final String city,
                                           final String street, final String tornamentName,
                                           final String description, final String creatorName,
                                           final long creatorId, final String sportName, final String title) {
        Team team1 = teamService.createTempTeam1(creatorName, creatorId, sportName);
        Team team2 = teamService.createTempTeam2(creatorName, creatorId, sportName);

        String type = INDIVIDUAL + '-' + (isCompetitive ? COMPETITIVE : FRIENDLY);

        Game game = gameDao.create(team1.getName(), team2.getName(), startTime.toString(),
                startTime.plusMinutes(durationInMinutes).toString(), type, null,
                country, state, city, street, tornamentName, description, title).orElseThrow(() -> {
                    GameKey gameKey = new GameKey(startTime, team1.getName(), startTime.plusMinutes(durationInMinutes));
                    LOGGER.trace("Creation fails, match '{}' already exist", gameKey.toString());
                    return new GameAlreadyExist("Creation fails, match '" + gameKey.toString() + "' already exist");
                });
        return insertUserInGameTeam(game, creatorId, true);
    }

    @Override
    public Game insertUserInGame(final String key, final long userId) {
        Game game;
        try {
            game = insertUserInGameTeam(findByKey(key), userId, true);
        }
        catch (TeamFullException e) {
            game = insertUserInGameTeam(findByKey(key), userId, false);
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
                                 final Long minutesOfDuration, final String type, final String result,
                                 final String country, final String state, final String city,
                                 final String street, final String tornamentName, final String description,
                                 final String title, final String key) {
        GameKey gameKey = getGameKey(key);
        Game gameOptional = findByKey(key), game;
        if ((teamName1 != null || teamName2 != null) && gameOptional.getGroupType().equals(INDIVIDUAL)) {
            throw new IllegalArgumentException("Cannot modify teams in a individual match");
        }
        game = gameDao.modify(teamName1, teamName2, startTime, (minutesOfDuration != null) ?
                        gameOptional.getStartTime().plusMinutes(minutesOfDuration).toString() : null, type,
                result, country, state, city, street, tornamentName, description, title, gameKey.getTeamName1(),
                gameKey.getStartTime(), gameKey.getFinishTime()).orElseThrow(() -> {
                    LOGGER.trace("Modify fails, match '{}' not found", key);
                    return new GameNotFoundException("Modify fails, match '" + key + "' not found");
                });

        return game;
    }

    @Override
    public boolean remove(final String key) {
        GameKey gameKey = getGameKey(key);
        PremiumUser premiumUser = sessionService.getLoggedUser().get();//TODO maybe check
        if (!findByKey(key).getPrimaryKey().getTeam1().getLeader().equals(premiumUser)) {
            LOGGER.trace("User '{}' is not creator of '{}' match", premiumUser.getUserName(), key);
            throw new UnauthorizedExecption("User '" + premiumUser.getUserName() +
                    "' is not creator of '" + key + "' match");//TODO maybe another exception
        }
        return gameDao.remove(gameKey.getTeamName1(), gameKey.getStartTime(), gameKey.getFinishTime());
    }

    @Override
    public Game findByKey(final String key) {
        GameKey gameKey = getGameKey(key);
        return gameDao.findByKey(gameKey.getTeamName1(), gameKey.getStartTime(), gameKey.getFinishTime())
                .orElseThrow(() -> {
                    LOGGER.trace("Find by key fails, match '{}' not found", key);
                    return new GameNotFoundException("Find by key fails, match '" + key + "' not found");
                });
    }

    @Override
    public Game updateResultOfGame(final String key, final int scoreTeam1, final int scoreTeam2) {
       Game game = findByKey(key);
        if (game.getFinishTime().compareTo(LocalDateTime.now()) > 0) {
            throw new GameHasNotBeenPlayException("The game has not been play");
        }
        game.setResult(scoreTeam1 + "-" + scoreTeam2);

        return game;
    }

    @Override
    public List<List<Game>> getGamesThatPlay(final long userId) {
        List<List<Game>> listsOfGames = new LinkedList<>();
        listsOfGames.add(gameDao.gamesThatAUserPlayInTeam1(userId));
        listsOfGames.add(gameDao.gamesThatAUserPlayInTeam2(userId));
        return listsOfGames;
    }

    private Game insertUserInGameTeam(final Game game, final long userId, final boolean toTeam1) {
        if (!toTeam1) {
            game.setTeam2(teamService.addPlayer(game.team2Name(), userId));
        } else {
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
