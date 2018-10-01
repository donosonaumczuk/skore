package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.GameDao;
import ar.edu.itba.paw.interfaces.TeamDao;
import ar.edu.itba.paw.models.*;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Repository
public class GameJdbcDao implements GameDao {

    private static final Logger LOGGER = LoggerFactory.getLogger(GameJdbcDao.class);

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;

    private final TeamDao teamDao;

    private final static RowMapper<Game> ROW_MAPPER = (resultSet, rowNum) ->
            new Game(new Team(resultSet.getString("teamName1"), new Sport(
                        resultSet.getString("sportName"), resultSet.getInt("playerQuantity"))),
                    (resultSet.getString("teamName2") == null)?null:
                            new Team(resultSet.getString("teamName2"), new Sport(
                                resultSet.getString("sportName"), resultSet.getInt("playerQuantity"))),
                    new Place(resultSet.getString("country"),resultSet.getString("state"),
                            resultSet.getString("city"), resultSet.getString("street")),
                    resultSet.getTimestamp("startTime").toLocalDateTime(),
                    resultSet.getTimestamp("finishTime").toLocalDateTime(),
                    resultSet.getString("type"),
                    resultSet.getInt("OccupiedQuantity"),
                    resultSet.getString("result"),
                    resultSet.getString("description"));

    @Autowired
    public GameJdbcDao(final DataSource dataSource, final TeamDao teamDao) {
        jdbcTemplate = new JdbcTemplate(dataSource);
        jdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("games");
        this.teamDao = teamDao;
    }


    @Override
    public Optional<Game> create(final String teamName1, final String teamName2, final String startTime,
                                 final String finishTime, final String type, final String result,
                                 final String country, final String state, final String city,
                                 final String street, final String tornamentName, final String description) {
        LOGGER.trace("Try to create game: {} vs {} |starting at {} |finishing at {}",
                teamName1, teamName2, startTime,finishTime);
        final Map<String, Object> args =  new HashMap<>();

        args.put("teamName1", teamName1);
        args.put("teamName2", teamName2);
        args.put("startTime", startTime);
        args.put("finishTime", finishTime);
        args.put("type", type);
        args.put("result", result);
        args.put("country", country);
        args.put("state", state);
        args.put("city", city);
        args.put("street", street);
        args.put("tornamentName", tornamentName);
        args.put("description", description);

        jdbcInsert.execute(args);
        LOGGER.trace("Successfully create game: {} vs {} |starting at {} |finishing at {}",
                teamName1, teamName2, startTime,finishTime);
        return findByKey(teamName1, startTime, finishTime);
    }

    @Override
    public Optional<Game> findByKey(String teamName1, String startTime, String finishTime) {
        LOGGER.trace("Try to find game: {} |starting at {} |finishing at {}", teamName1, startTime, finishTime);

        final String getAGame =
                "SELECT teamName1, teamName2, startTime, finishTime, sports.sportName as  sportName, " +
                    "playerQuantity, country, state, city, street, type, result, description, " +
                    "(count(team1.userId)+count(team2.userId)) as OccupiedQuantity " +
                "FROM " +
                    "games LEFT OUTER JOIN (teams NATURAL JOIN isPartOf) as team2 " +
                    "ON teamName2 = team2.teamName," +
                    "(teams NATURAL JOIN isPartOf) as team1, sports " +
                "WHERE teamName1 = team1.teamName AND teamName1 = ? AND startTime = TO_TIMESTAMP(?,'YYYY-MM-DD HH24:MI:SS') AND " +
                    "finishTime = TO_TIMESTAMP(?,'YYYY-MM-DD HH24:MI:SS') AND team1.sportName = sports.sportName" +
                " GROUP BY startTime, finishTime, teamName1, teamName2, sports.sportName, sports.playerQuantity;";
        final List<Game> list = jdbcTemplate.query(getAGame, ROW_MAPPER, teamName1, startTime,
                finishTime);

        final Optional<Game> gameOpt = list.stream().findFirst();
        if(gameOpt.isPresent()) {
            final Game game = gameOpt.get();
            Team team = teamDao.findByTeamName(game.team1Name()).get();
            game.setTeam1(team);
            team = (game.getTeam2() == null)?null:teamDao.findByTeamName(game.team2Name()).get();
            game.setTeam2(team);
        }

        LOGGER.trace("Returning what was find");
        return gameOpt;
    }

    @Override
    public List<Game> findGames(final String minStartTime, final String maxStartTime,
                                final String minFinishTime, final String maxFinishTime,
                                final List<String> types, final List<String> sportNames,
                                final Integer minQuantity, final Integer maxQuantity,
                                final List<String> countries, final List<String> states,
                                final List<String> cities, final Integer minFreePlaces,
                                final Integer maxFreePlaces, final PremiumUser loggedUser,
                                final boolean listOfGamesThatIsPartOf, final boolean wantCreated) {
        String getGamesQuery =
                "SELECT teamName1, teamName2, startTime, finishTime, sports.sportName AS sportName, " +
                        "playerQuantity, country, state, city, street, type, result, description, " +
                        "(count(team1.userId)+count(team2.userId)) as OccupiedQuantity " +
                "FROM games as gamesReal, (teams NATURAL JOIN isPartOf) as team1, " +
                        "(teams NATURAL JOIN isPartOf) as team2, sports " +
                "WHERE teamName1 = team1.teamName AND teamName2 = team2.teamName AND " +
                        "team1.sportName = sports.sportName";
        String groupBy = " GROUP BY startTime, finishTime, teamName1, " +
                "teamName2, sports.sportName, playerQuantity";

        final List<Object> filters = new ArrayList<>();
        final Filters gameFilters = new Filters();

        gameFilters.addMinFilter("gamesReal.startTime", minStartTime);
        gameFilters.addMinFilter("gamesReal.finishTime", minFinishTime);
        gameFilters.addMinFilter("sports.playerQuantity", minQuantity);

        gameFilters.addMaxFilter("gamesReal.startTime", maxStartTime);
        gameFilters.addMaxFilter("gamesReal.finishTime", maxFinishTime);
        gameFilters.addMaxFilter("sports.playerQuantity", maxQuantity);

        gameFilters.addSameFilter("gamesReal.type", types);
        gameFilters.addSameFilter("sports.sportName", sportNames);
        gameFilters.addSameFilter("gamesReal.country", countries);
        gameFilters.addSameFilter("gamesReal.state", states);
        gameFilters.addSameFilter("gamesReal.city", cities);

        gameFilters.addMinHavingFilter("2*sports.playerQuantity-count(team1.userId)-count(team2.userId)",
                minFreePlaces);
        gameFilters.addMaxHavingFilter("2*sports.playerQuantity-count(team1.userId)-count(team2.userId)",
                maxFreePlaces);

        String whereQuery = "";

        if(loggedUser != null) {
            String start;
            String logicalOperator;
            if (listOfGamesThatIsPartOf) {
                start = "IN";
                logicalOperator = "OR";
            } else {
                start = "NOT IN";
                logicalOperator = "AND";
            }
            String nestedQuery =
                    "SELECT teamAux.userId " +
                            "FROM games as gameAux, (teams NATURAL JOIN isPartOf) AS teamAux " +
                            "WHERE gameAux.startTime = gamesReal.startTime AND " +
                            "gameAux.finishTime = gamesReal.finishTime AND " +
                            "gameAux.teamName1 = gamesReal.teamName1";
            whereQuery =
                    "(? " + start + " (" + nestedQuery + " AND gameAux.teamName1 = teamAux.teamName AND " +
                            "teamAux.teamName = gamesReal.teamName1) " +
                            logicalOperator + " ? " + start + " (" + nestedQuery + " AND gameAux.teamName2 = teamAux.teamName AND " +
                            "teamAux.teamName = gamesReal.teamName2))";

            filters.add(loggedUser.getUserId());
            filters.add(loggedUser.getUserId());

            whereQuery = (whereQuery.equals("")) ? whereQuery : " AND " + whereQuery;

            if (wantCreated) {
                whereQuery = whereQuery + " AND (team1.leaderName = ?)";
            } else {
                whereQuery = whereQuery + " AND (team1.leaderName != ?)";
            }

            filters.add(loggedUser.getUserName());
        }

        String nextWhere = gameFilters.generateQueryWhere(filters);
        whereQuery = (nextWhere.equals(""))? whereQuery : whereQuery + " AND " + nextWhere;

        getGamesQuery = getGamesQuery + whereQuery + groupBy +
                gameFilters.generateQueryHaving(filters) + ";";

        LOGGER.trace("Try to find a game with this criteria: {}", getGamesQuery);
        return jdbcTemplate.query(getGamesQuery, filters.toArray(), ROW_MAPPER);
    }

    @Override
    public Optional<Game> modify(final String teamName1, final String teamName2, final String startTime,
                                 final String finishTime, final String type, final String result,
                                 final String country, final String state, final String city,
                                 final String street, final String tornamentName, final String description,
                                 final String teamName1Old, final String teamName2Old,
                                 final String startTimeOld, final String finishTimeOld) {
        String updateSentence = "UPDATE users SET teamName1 = ?, teamName2 = ?, startTime = ?," +
                "finishTime = ?, type = ?, result = ?, country = ?, state = ?, city = ?, street = ?," +
                "tornamentName = ?, descrption = ? WHERE teamName1 = ? AND teamName2 = ? AND " +
                "startTime = ? AND finishTime = ?;";
        LOGGER.trace("Try to modify game: {} |starting at {} |finishing at {}", teamName1Old,
                startTimeOld, finishTimeOld);
        jdbcTemplate.update(updateSentence, teamName1, teamName2, startTime, finishTime, type, result,
                country, state, city, street, tornamentName, description, teamName1Old, teamName2Old,
                startTimeOld, finishTimeOld);
        LOGGER.trace("Successfully modify game: {} |starting at {} |finishing at {}", teamName1Old,
                startTimeOld, finishTimeOld);
        return findByKey(teamName1, startTime, finishTime);
    }
}
