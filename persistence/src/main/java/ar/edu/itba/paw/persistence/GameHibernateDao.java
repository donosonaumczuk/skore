package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.exceptions.TeamNotFoundException;
import ar.edu.itba.paw.interfaces.GameDao;
import ar.edu.itba.paw.interfaces.TeamDao;
import ar.edu.itba.paw.models.*;
import org.checkerframework.javacutil.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Repository
public class GameHibernateDao implements GameDao {

    private static final Logger LOGGER = LoggerFactory.getLogger(GameHibernateDao.class);

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private TeamDao teamDao;

    private static final String QUERY_START =
            "SELECT games " +
            "FROM Game as games, Team t " +
            "WHERE teamName1 = t.teamName";
    private static final String GREATER_THAN           = ">";
    private static final String LESS_THAN              = "<";
    private static final String START_TIME_MAX         = "startTimeMax";
    private static final String FINISH_TIME_MIN        = "finishTimeMin";
    private static final String FINISH_TIME_MAX        = "finishTimeMax";
    private static final String QUERY_START_TIME_NAME  = "games.primaryKey.startTime";
    private static final String QUERY_FINISH_TIME_NAME = "games.primaryKey.finishTime";
    private static final String START_TIME_MIN         = "startTimeMin";
    private static final String SPORT_NAME             = "sportName";
    private static final String TYPE                   = "type";
    private static final String QUERY_SPORT_NAME       = "t.sport.displayName";
    private static final String COUNTRY                = "country";
    private static final String STATE                  = "state";
    private static final String CITY                   = "city";
    private static final String QUERY_USER_NAME        = "games.primaryKey.team1.leader.userName";
    private static final String USERNAME_PI            = "usernamePI";
    private static final String USERNAME_PNI           = "usernamePNI";
    private static final String USERNAME_CI            = "usernameCI";
    private static final String USERNAME_CNI           = "usernameCNI";
    private static final String QUERY_QUANTITY         = "games.primaryKey.team1.sport.playerQuantity";
    private static final String MIN_QUANTITY           = "minQuantity";
    private static final String MAX_QUANTITY           = "maxQuantity";
    private static final String QUERY_FREE_QUANTITY    = "(2 * games.primaryKey.team1.sport.playerQuantity - " +
                                "((CASE WHEN teamName2 IS NULL THEN 0 ELSE (SELECT teams.players.size FROM Team as " +
                                "teams WHERE teams.teamName = teamName2) END) + " +
                                "games.primaryKey.team1.players.size))";
    private static final String MIN_FREE_PLACES        = "minFreePlaces";
    private static final String MAX_FREE_PLACES        = "maxFreePlaces";

    @Override
    public Optional<Game> create(final String teamName1, final String teamName2, final String startTime,
                                 final String finishTime, final String type, final String result,
                                 final String country, final String state, final String city,
                                 final String street, final String tornamentName, final String description,
                                 final String title) {
        LOGGER.trace("Try to create game: {} vs {} |starting at {} |finishing at {}",
                teamName1, teamName2, startTime,finishTime);
        Team team1 = teamDao.findByTeamName(teamName1)
                .orElseThrow(() -> new TeamNotFoundException("Team does not exist"));
        Team team2;
        if(teamName2 != null) {
            team2 = teamDao.findByTeamName(teamName2)
                    .orElseThrow(() -> new TeamNotFoundException("Team does not exist"));
        }
        else {
            team2 = null;
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        Game game = new Game(team1, team2, new Place(country, state, city, street),
                LocalDateTime.parse(startTime, formatter), LocalDateTime.parse(finishTime, formatter),
                type, result, description, title, tornamentName);
        em.persist(game);
        LOGGER.trace("Successfully create game: {} vs {} |starting at {} |finishing at {}",
                teamName1, teamName2, startTime,finishTime);
        return Optional.of(game);
    }

    @Override
    public Optional<Game> findByKey(String teamName1, String startTime, String finishTime) {
        LOGGER.trace("Try to find game: {} |starting at {} |finishing at {}", teamName1, startTime, finishTime);
        Team team1 = teamDao.findByTeamName(teamName1)
                .orElseThrow(() -> new TeamNotFoundException("Team1 does not exist"));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        Game game = em.find(Game.class, new GamePK(team1, LocalDateTime.parse(startTime, formatter),
                LocalDateTime.parse(finishTime, formatter)));
        LOGGER.trace("Returning what was find");
        return (game == null)?Optional.empty():Optional.of(game);
    }

    @Override
    public List<Game> findGames(final LocalDateTime minStartTime, final LocalDateTime maxStartTime,
                                final LocalDateTime minFinishTime, final LocalDateTime maxFinishTime,
                                final List<String> types, final List<String> sportNames,
                                final Integer minQuantity, final Integer maxQuantity,
                                final List<String> countries, final List<String> states,
                                final List<String> cities, final Integer minFreePlaces,
                                final Integer maxFreePlaces, final List<String> usernamesPlayersInclude,
                                final List<String> usernamesPlayersNotInclude,
                                final List<String> usernamesCreatorsInclude,
                                final List<String> usernamesCreatorsNotInclude, GameSort sort) {
        Filters  filter = new Filters(QUERY_START);
        filter.addFilter(QUERY_START_TIME_NAME, GREATER_THAN, START_TIME_MIN, minStartTime);
        filter.addFilter(QUERY_START_TIME_NAME, LESS_THAN, START_TIME_MAX, maxStartTime);
        filter.addFilter(QUERY_FINISH_TIME_NAME, GREATER_THAN, FINISH_TIME_MIN, minFinishTime);
        filter.addFilter(QUERY_FINISH_TIME_NAME, LESS_THAN, FINISH_TIME_MAX, maxFinishTime);

        filter.addListFilters(true, false, TYPE, TYPE, types);
        filter.addFilter(QUERY_QUANTITY, GREATER_THAN, MIN_QUANTITY, minQuantity);
        filter.addFilter(QUERY_QUANTITY, LESS_THAN, MAX_QUANTITY, maxQuantity);

        filter.addListFilters(false, false, QUERY_SPORT_NAME, SPORT_NAME, sportNames);
        filter.addListFilters(false, false, COUNTRY, COUNTRY, countries);
        filter.addListFilters(false, false, STATE, STATE, states);
        filter.addListFilters(false, false, CITY, CITY, cities);

        filter.addFilter(QUERY_FREE_QUANTITY, GREATER_THAN, MIN_FREE_PLACES, minFreePlaces);
        filter.addFilter(QUERY_FREE_QUANTITY, LESS_THAN, MAX_FREE_PLACES, maxFreePlaces);

        filter.addListFilters(true, USERNAME_PI, usernamesPlayersInclude);
        filter.addListFilters(false, USERNAME_PNI, usernamesPlayersNotInclude);

        filter.addListFilters(true, false, QUERY_USER_NAME, USERNAME_CI, usernamesCreatorsInclude);
        filter.addListFilters(true, true, QUERY_USER_NAME, USERNAME_CNI, usernamesCreatorsNotInclude);

        filter.addFilterOnlyFinished();

        final TypedQuery<Game> query = em.createQuery(filter.toString() + gameSortToQuery(sort), Game.class);
        List<String> valueName = filter.getValueNames();
        List<Object> values    = filter.getValues();

        for(int i = 0; i < valueName.size(); i++) {
            query.setParameter(valueName.get(i), values.get(i));
        }

        return query.getResultList();
    }

    private String gameSortToQuery(GameSort gameSort) {
        if(gameSort == null || gameSort.getSortCategories().size() == 0) {
            return "";
        }

        boolean isFirst = true;
        StringBuilder stringBuilder = new StringBuilder(" ORDER BY");
        for (Pair<String, SortType> sortValue: gameSort.getSortCategories()) {
            if(isFirst) {
                isFirst = false;
            }
            else {
                stringBuilder.append(',');
            }
            stringBuilder.append(' ').append(sortValue.first).append(" ").append(sortValue.second.toString());
        }
        return stringBuilder.toString();
    }

    @Override
    public List<Game> gamesThatAUserPlayInTeam1(final long userId) {
        String queryString =
                "SELECT games " +
                "FROM Game as games " +
                "WHERE :userId IN " +
                            "(SELECT p.userId " +
                            "FROM Game g, Team t JOIN t.players p " +
                            "WHERE games = g AND g.primaryKey.team1.teamName = t.teamName " +
                                "AND g.result IS NOT NULL)";
        final TypedQuery<Game> query = em.createQuery(queryString, Game.class);
        query.setParameter("userId", userId);
        return query.getResultList();
    }

    @Override
    public List<Game> gamesThatAUserPlayInTeam2(final long userId) {
        String queryString =
                "SELECT games " +
                "FROM Game as games " +
                "WHERE :userId IN " +
                            "(SELECT p.userId " +
                            "FROM Game g, Team t JOIN t.players p " +
                            "WHERE games = g AND g.team2.teamName = t.teamName " +
                                "AND g.result IS NOT NULL)";
        final TypedQuery<Game> query = em.createQuery(queryString, Game.class);
        query.setParameter("userId", userId);
        return query.getResultList();
    }

    @Override
    public Optional<Game> modify(final String teamName1, final String teamName2, final String startTime,
                                 final String finishTime, final String type, final String result,
                                 final String country, final String state, final String city,
                                 final String street, final String tornamentName, final String description,
                                 final String teamName1Old, final String startTimeOld, final String finishTimeOld) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        Team team1Old = teamDao.findByTeamName(teamName1Old)
                .orElseThrow(() -> new TeamNotFoundException("Team1 does not exist"));
        Game game = em.find(Game.class, new GamePK(team1Old, LocalDateTime.parse(startTimeOld, formatter),
                LocalDateTime.parse(finishTimeOld, formatter)));
        LOGGER.trace("Try to modify game: {} |starting at {} |finishing at {}", teamName1Old,
                startTimeOld, finishTimeOld);
        if(game == null) {
            LOGGER.trace("Fail to modify game: {} |starting at {} |finishing at {}", teamName1Old,
                    startTimeOld, finishTimeOld);
            return Optional.empty();
        }
        Team team = teamDao.findByTeamName(teamName1)
                .orElseThrow(() -> new TeamNotFoundException("Team1 does not exist"));
        game.setTeam1(team);
        team = teamDao.findByTeamName(teamName1)
                .orElseThrow(() -> new TeamNotFoundException("Team2 does not exist"));
        game.setTeam2(team);
        game.setStartTime(LocalDateTime.parse(startTime, formatter));
        game.setFinishTime(LocalDateTime.parse(finishTime, formatter));
        game.setType(type);
        game.setResult(result);
        game.setPlace(new Place(country, state, city, street));
        game.setTornament(tornamentName);
        game.setDescription(description);

        em.merge(game);
        LOGGER.trace("Successfully modify game: {} |starting at {} |finishing at {}", teamName1Old,
                startTimeOld, finishTimeOld);
        return Optional.of(game);
    }

    @Override
    public boolean remove(final String teamName1, final String startTime, final String finishTime) {
        LOGGER.trace("Try to delete game: {}|{}|{}", teamName1, startTime, finishTime);
        Optional<Game> game = findByKey(teamName1, startTime, finishTime);
        boolean ans = false;
        if(game.isPresent()) {
            em.remove(game.get());
            ans = true;
        }
        return ans;
    }
}
