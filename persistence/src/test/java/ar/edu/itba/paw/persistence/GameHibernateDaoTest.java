package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.GameDao;
import ar.edu.itba.paw.models.*;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
@Sql("classpath:schema.sql")
@Transactional
public class GameHibernateDaoTest {

    @Autowired
    private GameDao gameDao;

    @PersistenceContext
    private EntityManager em;

    private final List<Game> games;

    private final Game game1;

    private final Game game2;

    private final Game game3;

    private final Game gameNotInserted;

    private final PremiumUser account1;

    private final PremiumUser account2;

    private final Sport sport;

    public GameHibernateDaoTest() {
        sport = new Sport("baloncesto", 5, "Baloncesto", null);
        Sport sportAux = new Sport("football", 11, "Football", null);
        account1 = new PremiumUser("Agustin", "Dammiano", "dammiano98@itba.edu.ar",
                             "dammiano98", "92262123", LocalDate.parse("1998-06-05"),
                            null, 50,"321dammiano_aguistin123", "admin", null);
        account2 = new PremiumUser("Agustin", "Izaguirre", "elIza@itba.edu.ar",
                             "Docker", "92358122", LocalDate.parse("1990-10-04"),
                            null, 50,"321dammiano_aguistin123", "admin", null);
        Team team1          = new Team(account2, "C.A.R.", "Club Atletico River", false, sport,
                            null);
        team1.addPlayer(account2.getUser());
        Team team2          = new Team(account2, "C.A.B.", "Club Atletico Boca", false, sport,
                            null);
        team2.addPlayer(account2.getUser());
        game1                = new Game(team1, team2, new Place("Argentina", "Buenos Aires",
                            "Ciudad autonoma de Buenos Aires", "Larea 1058"), LocalDateTime.parse("2018-11-11T17:00:00"),
                            LocalDateTime.parse("2018-11-11T19:00:00"), "competitive", null,
                            "Alan prefiere verlo y recursar", "La final de la libertadores",
                            null);
        game1.setResult("5 - 1");
        Team team3          = new Team(account1, "C.A.I.", "Club Atletico Independiente", false,
                            sport, null);
        team3.addPlayer(account1.getUser());
        gameNotInserted     = new Game(team1, team3,  new Place("Argentina", "Buenos Aires",
                            "Ciudad autonoma de Buenos Aires", "Larea 1058"), LocalDateTime.parse("2018-12-11T17:00:00"),
                            LocalDateTime.parse("2018-12-11T19:00:00"), "competitive", null,
                            "Buen juego", "Partido inolvidable", null);
        game2               = new Game(team3, team2,  new Place("Chile", "Santiago de Chile",
                            "Ciudad autonoma de Buenos Aires", "Larea 1058"), LocalDateTime.parse("2019-12-11T17:00:00"),
                            LocalDateTime.parse("2019-12-11T19:00:00"), "competitive", null,
                            null, "Interesante", null);
        Team team4          = new Team(account1, "T4", "Team 4", false, sportAux, null);
        Team team5          = new Team(account1, "T5", "Team 5", false, sportAux, null);
        game3               = new Game(team4, team5,  new Place("Bolivia", "Algo",
                            "Ciudad", "Calle 1096"), LocalDateTime.parse("2609-12-11T17:00:00"),
                            LocalDateTime.parse("2609-12-11T19:00:00"), "competitive", null,
                            null, "Titulo", null);
        games = new LinkedList<>();
        games.add(game1);
        games.add(game2);
        games.add(game3);
    }

    @Before
    public void initializeDatabase() {
        for (Game g:games) {
            em.persist(g.getTeam1().getSport());
            em.persist(g.getTeam1().getLeader().getUser());
            em.persist(g.getTeam1().getLeader());
            for (User p: g.getTeam1().getPlayers()) {
                em.persist(p);
            }
            em.persist(g.getTeam1());

            if(g.getTeam2() != null) {
                em.persist(g.getTeam2().getSport());
                em.persist(g.getTeam2().getLeader().getUser());
                em.persist(g.getTeam2().getLeader());
                for (User p : g.getTeam2().getPlayers()) {
                    em.persist(p);
                }
                em.persist(g.getTeam2());
            }
            em.persist(g);
        }
        em.flush();
    }

    @After
    public void removeAllData() {
        em.createNativeQuery("delete from games");
        em.createNativeQuery("delete from teams");
        em.createNativeQuery("delete from sports");
        em.createNativeQuery("delete from accounts");
        em.createNativeQuery("delete from users");
    }

    @Test
    public void createTest() {

        final Optional<Game> gameOpt = gameDao.create(gameNotInserted.getTeam1().getName(),
                gameNotInserted.getTeam2().getName(), gameNotInserted.getStartTime(),
                gameNotInserted.getFinishTime(), gameNotInserted.getType(),
                gameNotInserted.getResult(), gameNotInserted.getPlace().getCountry(),
                gameNotInserted.getPlace().getState(), gameNotInserted.getPlace().getCity(),
                gameNotInserted.getPlace().getStreet(), gameNotInserted.getTornament(),
                gameNotInserted.getDescription(), gameNotInserted.getTitle());

        Assert.assertEquals(true, gameOpt.isPresent());
        Game gameRetuned = gameOpt.get();
        Assert.assertEquals(gameNotInserted, gameRetuned);
        Assert.assertEquals(gameNotInserted, em.find(Game.class, new GamePK(gameNotInserted.getTeam1(),
                gameNotInserted.getStartTime(), gameNotInserted.getFinishTime())));
    }

    @Test
    public void findGamesTestFilterTime() {

        final List<Game> games = gameDao.findGames(LocalDateTime.parse("2018-11-11T12:00:00"),
                LocalDateTime.parse("2018-12-11T18:00:00"), LocalDateTime.parse("2018-11-11T12:00:00"),
                LocalDateTime.parse("2018-12-11T18:00:00"), null, null, 0,
                null, null, null, null, null,
                null, null, null,
                null, null, null, null,
                null, false, false);

        Assert.assertEquals(1,games.size());
        Assert.assertEquals(game1, games.get(0));
    }

    @Test
    public void findGamesTestFilterMultipleCountries() {

        List<String> type = new ArrayList<>();
        type.add(game1.getType());
        List<String> sportnames = new ArrayList<>();
        sportnames.add(game1.getTeam1().getSport().getName());
        List<String> countries = new ArrayList<>();
        countries.add(game1.getPlace().getCountry());
        countries.add(gameNotInserted.getPlace().getCountry());
        List<String> states = new ArrayList<>();
        states.add(game1.getPlace().getState());
        List<String> cities = new ArrayList<>();
        cities.add(game1.getPlace().getCity());

        final List<Game> games = gameDao.findGames(null, null,
                null, null, type, sportnames, 0,
                10, countries, states, cities, 0,
                10, null, null,
                null, null, null, null,
                null, false, false);

        Assert.assertEquals(1,games.size());
        Assert.assertEquals(game1, games.get(0));
    }

    @Test
    public void findGamesTestNoFilter() {

        final List<Game> games = gameDao.findGames(null, null,
                null, null, null, null, null,
                null, null, null, null, null,
                null, null, null,
                null, null, null, null,
                null, false, false);

        Assert.assertEquals(3,games.size());
        Assert.assertEquals(game2,games.get(0));
        Assert.assertEquals(game1,games.get(1));
        Assert.assertEquals(game3,games.get(2));
    }

    @Test
    public void findGamesTestSort() {

        final List<Game> games = gameDao.findGames(null, null,
                null, null, null, null, null,
                null, null, null, null, null,
                null, null, null,
                null, null, new GameSort("country asc,state asc"),
                null, null, false, false);


        Assert.assertEquals(3,games.size());
        Assert.assertEquals(game1,games.get(0));
        Assert.assertEquals(game3,games.get(1));
        Assert.assertEquals(game2,games.get(2));
    }

    @Test
    public void findGamesTestGamesAUserIsNotPartOf() {

        List<String> usernamesNotInclude = new ArrayList<>();
        usernamesNotInclude.add(account1.getUserName());
        final List<Game> games = gameDao.findGames(null, null,
                null, null, null, null, null,
                null, null, null, null, null,
                null, null, usernamesNotInclude,
                null, null, null, null,
                null, false, false);

        Assert.assertEquals(2,games.size());
        Assert.assertEquals(game1, games.get(0));
        Assert.assertEquals(game3,games.get(1));
    }

    @Test
    public void findGamesTestGamesAUserIsPartOfButIsNotCreator() {
        em.persist(gameNotInserted);

        List<String> usernames = new ArrayList<>();
        usernames.add(account1.getUserName());
        final List<Game> games = gameDao.findGames(null, null,
                null, null, null, null, null,
                null, null, null, null, null,
                null, usernames, null,
                null, usernames, null, null,
                null, false, false);

        Assert.assertEquals(1,games.size());
        Assert.assertEquals(gameNotInserted, games.get(0));
    }

    @Test
    public void findGamesTestGamesAUserCreate() {
        em.persist(gameNotInserted);

        List<String> usernames = new ArrayList<>();
        usernames.add(account1.getUserName());
        final List<Game> games = gameDao.findGames(null, null,
                null, null, null, null, null,
                null, null, null, null, null,
                null, null, null,
                usernames, null, null, null,
                null, false, false);

        Assert.assertEquals(2,games.size());
        Assert.assertEquals(game2, games.get(0));
        Assert.assertEquals(game3,games.get(1));
    }

    @Test
    public void findGamesTestIsApartOf() {
        em.persist(gameNotInserted);

        List<String> usernamesInclude = new ArrayList<>();
        usernamesInclude.add(account1.getUserName());
        final List<Game> games = gameDao.findGames(null, null,
                null, null, null, null, null,
                null, null, null, null, null,
                null, usernamesInclude, null,
                null, null, null, null,
                null, false, false);

        Assert.assertEquals(2,games.size());
        Assert.assertEquals(game2, games.get(0));
        Assert.assertEquals(gameNotInserted, games.get(1));
    }

    @Test
    public void findGamesTestOnlyFriends() {
        account2.getFriends().add(account1);
        em.persist(account2);
        em.persist(gameNotInserted);

        final List<Game> games = gameDao.findGames(null, null,
                null, null, null, null, null,
                null, null, null, null, null,
                null, null, null,
                null, null, null, null,
                account2.getUserName(), true, false);

        Assert.assertEquals(2,games.size());
        Assert.assertEquals(game2, games.get(0));
        Assert.assertEquals(gameNotInserted, games.get(1));
    }

    @Test
    public void findGamesTestOnlyLikedSport() {
        account2.getLikes().add(sport);
        em.persist(account2);
        em.persist(gameNotInserted);

        final List<Game> games = gameDao.findGames(null, null,
                null, null, null, null, null,
                null, null, null, null, null,
                null, null, null,
                null, null, null, null,
                account2.getUserName(), false, true);

        Assert.assertEquals(3,games.size());
        Assert.assertEquals(game2, games.get(0));
        Assert.assertEquals(game1, games.get(1));
        Assert.assertEquals(gameNotInserted, games.get(2));
    }

    @Test
    public void findGamesTestHasResult() {

        final List<Game> games = gameDao.findGames(null, null,
                null, null, null, null, null,
                null, null, null, null, null,
                null, null, null,
                null, null, null, true,
                null, false, false);

        Assert.assertEquals(1,games.size());
        Assert.assertEquals(game1, games.get(0));
    }

    @Test
    public void findGamesTestHasNotResult() {
        final List<Game> games = gameDao.findGames(null, null,
                null, null, null, null, null,
                null, null, null, null, null,
                null, null, null,
                null, null, null, false,
                null, false, false);

        Assert.assertEquals(2,games.size());
        Assert.assertEquals(game2, games.get(0));
        Assert.assertEquals(game3,games.get(1));
    }

    @Test
    public void modifyTest() {

        final Game gameReturned = gameDao.modify(gameNotInserted.getTeam1().getName(), gameNotInserted.getTeam2().getName(),
                gameNotInserted.getStartTime(), gameNotInserted.getFinishTime(), gameNotInserted.getType(),
                gameNotInserted.getResult(), gameNotInserted.getPlace().getCountry(), gameNotInserted.getPlace().getState(),
                gameNotInserted.getPlace().getCity(), gameNotInserted.getPlace().getStreet(),
                gameNotInserted.getTornament(), gameNotInserted.getDescription(), gameNotInserted.getTitle(),
                game1.getTeam1().getName(), game1.getStartTime(), game1.getFinishTime()).get();

        Assert.assertEquals(game1, gameReturned);
    }

    @Test
    public void deleteTest() {

        boolean ans = gameDao.remove(game1.getTeam1().getName(), game1.getStartTime(), game1.getFinishTime());

        Assert.assertEquals(true, ans);
        Assert.assertNull(em.find(Game.class, new GamePK(game1.getTeam1(), game1.getStartTime(),
                game1.getFinishTime())));
    }

    @Test
    public void getGamesThatAUserPlayInTeam1() {

        List<Game> games = gameDao.gamesThatAUserPlayInTeam1(account1.getUser().getUserId());

        Assert.assertEquals(0, games.size());
    }

    @Test
    public void getGamesThatAUserPlayInTeam2() {

        List<Game> games = gameDao.gamesThatAUserPlayInTeam2(account1.getUser().getUserId());

        Assert.assertEquals(0, games.size());
    }
}
