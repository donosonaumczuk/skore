package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.Exceptions.TeamNotFoundException;
import ar.edu.itba.paw.interfaces.GameDao;
import ar.edu.itba.paw.interfaces.TeamDao;
import ar.edu.itba.paw.models.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public class GameHibernateDao implements GameDao {

    private static final Logger LOGGER = LoggerFactory.getLogger(GameHibernateDao.class);

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private TeamDao teamDao;

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
        Team team2 = teamDao.findByTeamName(teamName2)
                .orElseThrow(() -> new TeamNotFoundException("Team does not exist"));
        Game game = new Game(team1, team2, new Place(country, state, city, street),
                LocalDateTime.parse(startTime), LocalDateTime.parse(finishTime),
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
        Game game = em.find(Game.class, new GamePK(team1, LocalDateTime.parse(startTime),
                LocalDateTime.parse(finishTime)));
        LOGGER.trace("Returning what was find");
        return (game == null)?Optional.empty():Optional.of(game);
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
//        "SELECT teamName1, teamName2, startTime, finishTime, sports.sportName AS sportName, " +
//                "playerQuantity, displayName, country, state, city, street, type, result, description, " +
//                "count(team1.userId) as OccupiedQuantity1, count(team2.userId) as OccupiedQuantity2, " +
//                "title, tornamentName, team1.leaderName as leaderUserName1, team2.leaderName as leaderUserName2 " +
//                "FROM games LEFT OUTER JOIN (teams NATURAL JOIN isPartOf) as team2 " +
//                "ON games.teamName2 = team2.teamName, " +
//                "(teams NATURAL JOIN isPartOf) as team1, sports " +
//                "WHERE teamName1 = team1.teamName AND team1.sportName = sports.sportName";
//        String groupBy = " GROUP BY startTime, finishTime, teamName1, sports.sportName, playerQuantity, " +
//                "sports.displayName,  country, state, city, street, type, result, description, title, tornamentName, " +
//                "team1.teamName, team1.leaderName, teamName2, team2.teamName, team2.leaderName";
//
//        final List<Object> filters = new ArrayList<>();
//        final Filters gameFilters = new Filters();
//
//        gameFilters.addMinFilter("games.startTime", minStartTime);
//        gameFilters.addMinFilter("games.finishTime", minFinishTime);
//        gameFilters.addMinFilter("sports.playerQuantity", minQuantity);
//
//        gameFilters.addMaxFilter("games.startTime", maxStartTime);
//        gameFilters.addMaxFilter("games.finishTime", maxFinishTime);
//        gameFilters.addMaxFilter("sports.playerQuantity", maxQuantity);
//
//        gameFilters.addSameFilter("games.type", types);
//        gameFilters.addSameFilter("sports.sportName", sportNames);
//        gameFilters.addSameFilter("games.country", countries);
//        gameFilters.addSameFilter("games.state", states);
//        gameFilters.addSameFilter("games.city", cities);
//
//        gameFilters.addMinHavingFilter("2*sports.playerQuantity-count(team1.userId)-count(team2.userId)",
//                minFreePlaces);
//        gameFilters.addMaxHavingFilter("2*sports.playerQuantity-count(team1.userId)-count(team2.userId)",
//                maxFreePlaces);
//
//        String whereQuery = "";
//
//        if(loggedUser != null) {
//            String start;
//            String logicalOperator;
//            if (listOfGamesThatIsPartOf) {
//                start = "IN";
//                logicalOperator = "OR";
//            } else {
//                start = "NOT IN";
//                logicalOperator = "AND";
//            }
//            String nestedQuery =
//                    "SELECT teamAux.userId " +
//                            "FROM games as gameAux, (teams NATURAL JOIN isPartOf) AS teamAux " +
//                            "WHERE gameAux.startTime = games.startTime AND " +
//                            "gameAux.finishTime = games.finishTime AND " +
//                            "gameAux.teamName1 = games.teamName1";
//            whereQuery =
//                    "(? " + start + " (" + nestedQuery + " AND gameAux.teamName1 = teamAux.teamName AND " +
//                            "teamAux.teamName = games.teamName1) " +
//                            logicalOperator + " ? " + start + " (" + nestedQuery + " AND gameAux.teamName2 = teamAux.teamName AND " +
//                            "teamAux.teamName = games.teamName2))";
//
//            filters.add(loggedUser.getUserId());
//            filters.add(loggedUser.getUserId());
//
//            whereQuery = (whereQuery.equals("")) ? whereQuery : " AND " + whereQuery;
//
//            if (wantCreated) {
//                whereQuery = whereQuery + " AND (team1.leaderName = ?)";
//            } else {
//                whereQuery = whereQuery + " AND (team1.leaderName != ?)";
//            }
//
//            filters.add(loggedUser.getUserName());
//        }
//
//        String nextWhere = gameFilters.generateQueryWhere(filters);
//        whereQuery = (nextWhere.equals(""))? whereQuery : whereQuery + " AND " + nextWhere;
//
//        getGamesQuery = getGamesQuery + whereQuery + groupBy +
//                gameFilters.generateQueryHaving(filters) + ";";
//
//        LOGGER.trace("Try to find a game with this criteria: {}", getGamesQuery);
//        return jdbcTemplate.query(getGamesQuery, filters.toArray(), ROW_MAPPER);
        return null;
    }

    @Override
    public Optional<Game> modify(final String teamName1, final String teamName2, final String startTime,
                                 final String finishTime, final String type, final String result,
                                 final String country, final String state, final String city,
                                 final String street, final String tornamentName, final String description,
                                 final String teamName1Old, final String startTimeOld, final String finishTimeOld) {
        Team team1Old = teamDao.findByTeamName(teamName1Old)
                .orElseThrow(() -> new TeamNotFoundException("Team1 does not exist"));
        Game game = em.find(Game.class, new GamePK(team1Old, LocalDateTime.parse(startTimeOld),
                LocalDateTime.parse(finishTimeOld)));
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
        game.setStartTime(LocalDateTime.parse(startTime));
        game.setFinishTime(LocalDateTime.parse(finishTime));
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
