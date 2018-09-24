package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.GameDao;
import ar.edu.itba.paw.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.time.LocalDateTime;
import java.util.*;

@Repository
public class GameJdbcDao implements GameDao {
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;

    private final static RowMapper<Game> ROW_MAPPER = (resultSet, rowNum) ->
            new Game(new Team(resultSet.getString("teamName1"), new Sport(
                        resultSet.getString("sportName"), resultSet.getInt("playerQuantity"))),
                    new Team(resultSet.getString("teamName2"), new Sport(
                            resultSet.getString("sportName"), resultSet.getInt("playerQuantity"))),
                    new Place(resultSet.getString("country"),resultSet.getString("state"),
                            resultSet.getString("city"), resultSet.getString("street")),
                    resultSet.getTimestamp("startTime").toLocalDateTime(),
                    resultSet.getTimestamp("finishTime").toLocalDateTime(),
                    resultSet.getString("type"),
                    resultSet.getInt("OccupiedQuantity"),
                    resultSet.getString("result"));

    @Autowired
    public GameJdbcDao(final DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
        jdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("games");
    }

    public Optional<Game> create(String teamName1, String teamName2, String startTime,
                                 String finishTime, String type, String result, String country,
                                 String state, String city, String street, String tornamentName) {
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


        jdbcInsert.execute(args);
        return findByKey(teamName1, teamName2, startTime, finishTime);
    }

    @Override
    public Optional<Game> findByKey(String teamName1, String teamName2,
                                   String startTime, String finishTime) {
        final String getAGame =
                "SELECT teamName1, teamName2, startTime, finishTime, sportName, " +
                    "playerQuantity, country, state, city, street, type, result, " +
                    "(count(team1.userId)+count(team2.userId)) as OccupiedQuantity " +
                "FROM games, (teams NATURAL JOIN isPartOf) as team1, " +
                    "(teams NATURAL JOIN isPartOf) as team2, sports " +
                "WHERE teamName1 = team1.teamName AND teamName2 = team2.teamName AND " +
                    "teamName1 = ? AND teamName2 = ? AND startTime = ? AND finishTime = ?" +
                    " AND team1.sportName = sports.sportName" +
                " GROUP BY startTime, finishTime, teamName1, teamName2, sportName, playerQuantity;";
        final List<Game> list = jdbcTemplate.query(getAGame, ROW_MAPPER, teamName1,
                teamName2, startTime, finishTime);

        return list.stream().findFirst();
    }

    public List<Game> findGames(final String minStartTime, final String maxStartTime,
                                final String minFinishTime, final String maxFinishTime,
                                final List<String> types, final List<String> sportNames,
                                final Integer minQuantity, final Integer maxQuantity,
                                final List<String> countries, final List<String> states,
                                final List<String> cities, final Integer minFreePlaces,
                                final Integer maxFreePlaces) {
        String getGamesQuery =
                "SELECT teamName1, teamName2, startTime, finishTime, sportName, " +
                        "playerQuantity, country, state, city, street, type, result, " +
                        "(count(team1.userId)+count(team2.userId)) as OccupiedQuantity " +
                "FROM games, (teams NATURAL JOIN isPartOf) as team1, " +
                        "(teams NATURAL JOIN isPartOf) as team2, sports" +
                " WHERE teamName1 = team1.teamName AND teamName2 = team2.teamName AND " +
                        "team1.sportName = sports.sportName";
        String groupBy = " GROUP BY startTime, finishTime, teamName1, " +
                "teamName2, sportName, playerQuantity";

        final List<Object> filters = new ArrayList<>();
        final Filters gameFilters = new Filters();

        gameFilters.addMinFilter("games.startTime", minStartTime);
        gameFilters.addMinFilter("games.finishTime", minFinishTime);
        gameFilters.addMinFilter("sports.playerQuantity", minQuantity);

        gameFilters.addMaxFilter("games.startTime", maxStartTime);
        gameFilters.addMaxFilter("games.finishTime", maxFinishTime);
        gameFilters.addMaxFilter("sports.playerQuantity", maxQuantity);

        gameFilters.addSameFilter("games.type", types);
        gameFilters.addSameFilter("sports.sportName", sportNames);
        gameFilters.addSameFilter("games.country", countries);
        gameFilters.addSameFilter("games.state", states);
        gameFilters.addSameFilter("games.city", cities);

        gameFilters.addMinHavingFilter("2*sports.playerQuantity-count(userId)",
                minFreePlaces);
        gameFilters.addMaxHavingFilter("2*sports.playerQuantity-count(userId)",
                maxFreePlaces);

        String whereQuery = gameFilters.generateQueryWhere(filters);
        whereQuery = (whereQuery.equals(""))? whereQuery : " AND " + whereQuery;
        getGamesQuery = getGamesQuery + whereQuery + groupBy +
                gameFilters.generateQueryHaving(filters) + ";";

        return jdbcTemplate.query(getGamesQuery, filters.toArray(), ROW_MAPPER);
    }
}
