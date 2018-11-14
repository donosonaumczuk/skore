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
    public List<Game> findGames(final String minStartTime, final String maxStartTime,
                                final String minFinishTime, final String maxFinishTime,
                                final List<String> types, final List<String> sportNames,
                                final Integer minQuantity, final Integer maxQuantity,
                                final List<String> countries, final List<String> states,
                                final List<String> cities, final Integer minFreePlaces,
                                final Integer maxFreePlaces, final PremiumUser loggedUser,
                                final boolean listOfGamesThatIsPartOf, final boolean wantCreated) {
        Filters  filter = new Filters("SELECT games FROM Game as games, " +
                "Team t WHERE teamName1 = t.teamName");
        filter.addFilter("games.primaryKey.startTime", ">",
                "startTimeMin", minStartTime);
        filter.addFilter("games.primaryKey.startTime", "<",
                "startTimeMax", maxStartTime);
        filter.addFilter("games.primaryKey.finishTime", ">",
                "finishTimeMin", minFinishTime);
        filter.addFilter("games.primaryKey.finishTime", "<",
                "finishTimeMax", maxFinishTime);
//TODO        final List<String> types,
//TODO        final Integer minQuantity,
//TODO        final Integer maxQuantity,
        filter.addListFilters("t.sport.sportName", "like",
                "sportName", sportNames);
        filter.addListFilters("lower(country)", "like",
                "country", countries);
        filter.addListFilters("lower(state)", "like",
                "state", states);
        filter.addListFilters("lower(city)", "like",
                "city", cities);
//TODO        final Integer minFreePlaces,
//TODO        final Integer maxFreePlaces,
//TODO        final PremiumUser loggedUser,
//TODO        final boolean listOfGamesThatIsPartOf,
//TODO        final boolean wantCreated

        String queryString = filter.toString();
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
            String nestedQuery1 =
                    "SELECT p.userId " +
                            "FROM Game g, Team t JOIN t.players p " +
                            "WHERE games = g AND g.primaryKey.team1.teamName = t.teamName";
            String nestedQuery2 =
                    "SELECT p.userId " +
                            "FROM Game g, Team t JOIN t.players p " +
                            "WHERE games = g AND g.team2.teamName = t.teamName";
            queryString = queryString + " AND" +
                    " (:userId1 " + start + " (" + nestedQuery1 + ") " +
                            logicalOperator + " :userId2 " + start + " (" + nestedQuery2 + "))";

            if (wantCreated) {
                queryString = queryString + " AND (games.primaryKey.team1.leader.userName = :user)";
            } else {
                queryString = queryString + " AND (games.primaryKey.team1.leader.userName != :user)";
            }
        }
        queryString = queryString + " AND (games.result IS NULL)";

        final TypedQuery<Game> query = em.createQuery(queryString, Game.class);
        List<String> valueName = filter.getValueNames();
        List<Object> values    = filter.getValues();

        for(int i = 0; i < valueName.size(); i++) {
            query.setParameter(valueName.get(i), values.get(i));
        }
        if(loggedUser != null) {
            query.setParameter("userId1", loggedUser.getUser().getUserId());
            query.setParameter("userId2", loggedUser.getUser().getUserId());
            query.setParameter("user", loggedUser.getUserName());
        }
        return query.getResultList();
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
