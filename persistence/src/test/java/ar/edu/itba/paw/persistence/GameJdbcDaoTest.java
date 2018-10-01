package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.Game;
import ar.edu.itba.paw.models.PremiumUser;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.jdbc.JdbcTestUtils;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
@Sql("classpath:schema.sql")
public class GameJdbcDaoTest {
    private static final String FIRSTNAME               = "first_name";
    private static final String LASTNAME                = "last_name";
    private static final String EMAIL                   = "email";
    private static final long   USER_1_ID               = 1;
    private static final long   USER_2_ID               = 2;
    private static final long   USER_3_ID               = 3;
    private static final String SPORTNAME               = "sportname";
    private static final int    QUANTITY                = 10;
    private static final String ACRONYM                 = "acronym";
    private static final long   ISTEMP                  = 0;
    private static final String TEAMNAME_1              = "1";
    private static final String TEAMNAME_2              = "2";
    private static final String TEAMNAME_3              = "3";
    private static final String STARTIME_1              = "2018-12-12 0:00:00";
    private static final String FINISHTIME_1            = "2018-12-12 01:00:00";
    private static final String STARTIME_2              = "2018-12-13 0:00:00";
    private static final String FINISHTIME_2            = "2018-12-13 01:00:00";
    private static final String STARTIME_3              = "2018-12-14 0:00:00";
    private static final String FINISHTIME_3            = "2018-12-14 01:00:00";
    private static final String STARTIME_LOCALTIME_1    = "2018-12-12T00:00";
    private static final String FINISHTIME_LOCALTIME_1  = "2018-12-12T01:00";
    private static final String STARTIME_LOCALTIME_2    = "2018-12-13T00:00";
    private static final String FINISHTIME_LOCALTIME_2  = "2018-12-13T01:00";
    private static final String STARTIME_LOCALTIME_3    = "2018-12-14T00:00";
    private static final String FINISHTIME_LOCALTIME_3  = "2018-12-14T01:00";
    private static final String TYPE                    = "competitive";
    private static final String RESULT                  = "3-0";
    private static final String COUNTRY_1               = "country1";
    private static final String COUNTRY_2               = "country2";
    private static final String COUNTRY_3               = "country3";
    private static final String STATE                   = "state";
    private static final String CITY                    = "city";
    private static final String STREET                  = "street";
    private static final int    QUERY_SIZE_1            = 2;
    private static final int    QUERY_SIZE_2            = 3;
    private static final String PASSWORD                = "password";
    private static final String CELLPHONE               = "cellphone";
    private static final String BIRTHDAY                = "1994-12-26";
    private static final int    REPUTATION              = 10;


    @Autowired
    private DataSource dataSource;

    @Autowired
    private GameJdbcDao gameJdbcDao;

    private JdbcTemplate jdbcTemplate;

    @Before
    public void setUp() {
        jdbcTemplate = new JdbcTemplate(dataSource);
        JdbcTestUtils.deleteFromTables(jdbcTemplate, "games", "isPartOf", "teams",
                "accounts", "users", "sports");
    }

    private void insertAccount(final String firstName, final String lastName, final String email,
                               final long userId, final String userName, final String password,
                               final String country, final String state, final String city,
                               final String street, final int reputation, final String cellphone,
                               final String birthday) {
        jdbcTemplate.execute("INSERT INTO users (firstname, lastname, email, userid)" +
                " VALUES ('" + firstName + "' , '" + lastName + "', '" + email + "', " + userId + ");");
        jdbcTemplate.execute("INSERT INTO accounts (username, cellphone, birthday," +
                " country, state, city, street, reputation, password, userId, email)" +
                " VALUES ('" + userName + "' , '" + cellphone + "', '" + birthday + "', '" +
                country + "', '" + state + "', '" + city + "', '" + street + "', " + reputation +
                ", '" + password +"', " + userId +",'"+email+"');");
    }

    private void insertSport(final String sportName, final long quantity) {
        jdbcTemplate.execute("INSERT INTO sports (sportName, playerQuantity)" +
                " VALUES ('" + sportName + "', " + quantity + ");");
    }

    private void insertTeam(final String teamName, final String acronym, final String leaderName,
                            final long isTemp, final String sportName, final long leaderId) {
        jdbcTemplate.execute("INSERT INTO teams (teamName, acronym, leaderName, isTemp, sportName)" +
                " VALUES ('" + teamName + "' , '" + acronym + "', '" + leaderName + "', " + isTemp +
                ",'" + sportName + "');");
        jdbcTemplate.execute("INSERT INTO isPartOf (userId, teamName) VALUES ('" + teamName +
                "'," + leaderId + ");");
    }

    private void insertGame(final String teamName1, final String teamName2, final String startTime,
                            final String finishTime, final String type, final String result,
                            final String country, final String state, final String city,
                            final String street) {
        jdbcTemplate.execute("INSERT INTO games (teamName1, teamName2, startTime, finishTime," +
                " type, result, country, state, city, street)" +
                " VALUES('" + teamName1 + "','" + teamName2 + "','" + startTime + "','" +
                finishTime + "','" + type + "','" + result + "','" + country + "','" + state +
                "','" + city + "','" + street + "');");
    }

    @Test
    public void createTest() {
        insertAccount(FIRSTNAME+USER_1_ID,LASTNAME+USER_1_ID, EMAIL+USER_1_ID,
                USER_1_ID, FIRSTNAME+USER_1_ID, PASSWORD, COUNTRY_1, STATE, CITY, STREET,
                REPUTATION, CELLPHONE, BIRTHDAY);
        insertAccount(FIRSTNAME+USER_2_ID,LASTNAME+USER_2_ID, EMAIL+USER_2_ID,
                USER_2_ID, FIRSTNAME+USER_2_ID, PASSWORD, COUNTRY_2, STATE, CITY, STREET,
                REPUTATION, CELLPHONE, BIRTHDAY);
        insertSport(SPORTNAME,QUANTITY);
        insertTeam(TEAMNAME_1,ACRONYM+TEAMNAME_1,FIRSTNAME+USER_1_ID,ISTEMP,
                SPORTNAME,USER_1_ID);
        insertTeam(TEAMNAME_2,ACRONYM+TEAMNAME_2,FIRSTNAME+USER_2_ID,ISTEMP,
                SPORTNAME,USER_2_ID);

        final Optional<Game> gameOpt = gameJdbcDao.create(TEAMNAME_1,TEAMNAME_2,STARTIME_1,FINISHTIME_1,
                TYPE,RESULT,COUNTRY_1,STATE,CITY,STREET,null, null);

        Assert.assertEquals(true, gameOpt.isPresent());
        Game game = gameOpt.get();
        Assert.assertEquals(TEAMNAME_1, game.getTeam1().getName());
        Assert.assertEquals(TEAMNAME_2, game.getTeam2().getName());
        Assert.assertEquals(STARTIME_LOCALTIME_1, game.getStartTime().toString());
        Assert.assertEquals(FINISHTIME_LOCALTIME_1, game.getFinishTime().toString());
        Assert.assertEquals(TYPE, game.getType());
        Assert.assertEquals(RESULT, game.getResult());
        Assert.assertEquals(COUNTRY_1, game.getPlace().getCountry());
        Assert.assertEquals(STATE, game.getPlace().getState());
        Assert.assertEquals(CITY, game.getPlace().getCity());
        Assert.assertEquals(STREET, game.getPlace().getStreet());

        Assert.assertEquals(1, JdbcTestUtils.countRowsInTable(jdbcTemplate, "games"));
    }

    @Test
    public void findGamesTestFilterMultipleCountries() {
        insertAccount(FIRSTNAME+USER_1_ID,LASTNAME+USER_1_ID, EMAIL+USER_1_ID,
                USER_1_ID, FIRSTNAME+USER_1_ID, PASSWORD, COUNTRY_1, STATE, CITY, STREET,
                REPUTATION, CELLPHONE, BIRTHDAY);
        insertAccount(FIRSTNAME+USER_2_ID,LASTNAME+USER_2_ID, EMAIL+USER_2_ID,
                USER_2_ID, FIRSTNAME+USER_2_ID, PASSWORD, COUNTRY_2, STATE, CITY, STREET,
                REPUTATION, CELLPHONE, BIRTHDAY);
        insertSport(SPORTNAME,QUANTITY);
        insertTeam(TEAMNAME_1,ACRONYM+TEAMNAME_1,FIRSTNAME+USER_1_ID,ISTEMP,
                SPORTNAME,USER_1_ID);
        insertTeam(TEAMNAME_2,ACRONYM+TEAMNAME_2,FIRSTNAME+USER_2_ID,ISTEMP,
                SPORTNAME,USER_2_ID);
        insertGame(TEAMNAME_1,TEAMNAME_2,STARTIME_1,FINISHTIME_1,TYPE,RESULT, COUNTRY_1,STATE,CITY,
                STREET);
        insertGame(TEAMNAME_1,TEAMNAME_2,STARTIME_2,FINISHTIME_2,TYPE,RESULT, COUNTRY_2,STATE,CITY,
                STREET);
        insertGame(TEAMNAME_1,TEAMNAME_2,STARTIME_3,FINISHTIME_3,TYPE,RESULT, COUNTRY_3,STATE,CITY,
                STREET);

        List<String> type = new ArrayList<>();
        type.add(TYPE);
        List<String> sportnames = new ArrayList<>();
        sportnames.add(SPORTNAME);
        List<String> countries = new ArrayList<>();
        countries.add(COUNTRY_1);
        countries.add(COUNTRY_3);
        List<String> states = new ArrayList<>();
        states.add(STATE);
        List<String> cities = new ArrayList<>();
        cities.add(CITY);

        final List<Game> games = gameJdbcDao.findGames(null, null,
                null, null, type, sportnames, QUANTITY-1,
                QUANTITY+1, countries, states, cities, QUANTITY-2,
                2*QUANTITY, null, false, false);

        Assert.assertEquals(QUERY_SIZE_1,games.size());
        Assert.assertEquals(TEAMNAME_1,games.get(0).getTeam1().getName());
        Assert.assertEquals(TEAMNAME_2,games.get(0).getTeam2().getName());
        Assert.assertEquals(STARTIME_LOCALTIME_1,games.get(0).getStartTime().toString());
        Assert.assertEquals(FINISHTIME_LOCALTIME_1,games.get(0).getFinishTime().toString());
        Assert.assertEquals(TEAMNAME_1,games.get(1).getTeam1().getName());
        Assert.assertEquals(TEAMNAME_2,games.get(1).getTeam2().getName());
        Assert.assertEquals(STARTIME_LOCALTIME_3,games.get(1).getStartTime().toString());
        Assert.assertEquals(FINISHTIME_LOCALTIME_3,games.get(1).getFinishTime().toString());
    }

    @Test
    public void findGamesTestNoFilter() {
        insertAccount(FIRSTNAME+USER_1_ID,LASTNAME+USER_1_ID, EMAIL+USER_1_ID,
                USER_1_ID, FIRSTNAME+USER_1_ID, PASSWORD, COUNTRY_1, STATE, CITY, STREET,
                REPUTATION, CELLPHONE, BIRTHDAY);
        insertAccount(FIRSTNAME+USER_2_ID,LASTNAME+USER_2_ID, EMAIL+USER_2_ID,
                USER_2_ID, FIRSTNAME+USER_2_ID, PASSWORD, COUNTRY_2, STATE, CITY, STREET,
                REPUTATION, CELLPHONE, BIRTHDAY);
        insertSport(SPORTNAME,QUANTITY);
        insertTeam(TEAMNAME_1,ACRONYM+TEAMNAME_1,FIRSTNAME+USER_1_ID,ISTEMP,
                SPORTNAME,USER_1_ID);
        insertTeam(TEAMNAME_2,ACRONYM+TEAMNAME_2,FIRSTNAME+USER_2_ID,ISTEMP,
                SPORTNAME,USER_2_ID);
        insertGame(TEAMNAME_1,TEAMNAME_2,STARTIME_1,FINISHTIME_1,TYPE,RESULT, COUNTRY_1,STATE,CITY,
                STREET);
        insertGame(TEAMNAME_1,TEAMNAME_2,STARTIME_2,FINISHTIME_2,TYPE,RESULT, COUNTRY_2,STATE,CITY,
                STREET);
        insertGame(TEAMNAME_1,TEAMNAME_2,STARTIME_3,FINISHTIME_3,TYPE,RESULT, COUNTRY_3,STATE,CITY,
                STREET);

        final List<Game> games = gameJdbcDao.findGames(null, null,
                null, null, null, null, null,
                null, null, null, null, null,
                null, null, false, false);

        Assert.assertEquals(QUERY_SIZE_2,games.size());
        Assert.assertEquals(TEAMNAME_1,games.get(0).getTeam1().getName());
        Assert.assertEquals(TEAMNAME_2,games.get(0).getTeam2().getName());
        Assert.assertEquals(STARTIME_LOCALTIME_1,games.get(0).getStartTime().toString());
        Assert.assertEquals(FINISHTIME_LOCALTIME_1,games.get(0).getFinishTime().toString());
        Assert.assertEquals(TEAMNAME_1,games.get(1).getTeam1().getName());
        Assert.assertEquals(TEAMNAME_2,games.get(1).getTeam2().getName());
        Assert.assertEquals(STARTIME_LOCALTIME_2,games.get(1).getStartTime().toString());
        Assert.assertEquals(FINISHTIME_LOCALTIME_2,games.get(1).getFinishTime().toString());
        Assert.assertEquals(TEAMNAME_1,games.get(2).getTeam1().getName());
        Assert.assertEquals(TEAMNAME_2,games.get(2).getTeam2().getName());
        Assert.assertEquals(STARTIME_LOCALTIME_3,games.get(2).getStartTime().toString());
        Assert.assertEquals(FINISHTIME_LOCALTIME_3,games.get(2).getFinishTime().toString());
    }

    @Test
    public void findGamesTestGamesAUserIsNotPartOf() {
        insertAccount(FIRSTNAME+USER_1_ID,LASTNAME+USER_1_ID, EMAIL+USER_1_ID,
                USER_1_ID, FIRSTNAME+USER_1_ID, PASSWORD, COUNTRY_1, STATE, CITY, STREET,
                REPUTATION, CELLPHONE, BIRTHDAY);
        insertAccount(FIRSTNAME+USER_2_ID,LASTNAME+USER_2_ID, EMAIL+USER_2_ID,
                USER_2_ID, FIRSTNAME+USER_2_ID, PASSWORD, COUNTRY_2, STATE, CITY, STREET,
                REPUTATION, CELLPHONE, BIRTHDAY);
        insertAccount(FIRSTNAME+USER_3_ID,LASTNAME+USER_3_ID, EMAIL+USER_3_ID,
                USER_3_ID, FIRSTNAME+USER_3_ID, PASSWORD, COUNTRY_2, STATE, CITY, STREET,
                REPUTATION, CELLPHONE, BIRTHDAY);
        insertSport(SPORTNAME,QUANTITY);
        insertTeam(TEAMNAME_1,ACRONYM+TEAMNAME_1,FIRSTNAME+USER_1_ID,ISTEMP,
                SPORTNAME,USER_1_ID);
        insertTeam(TEAMNAME_2,ACRONYM+TEAMNAME_2,FIRSTNAME+USER_2_ID,ISTEMP,
                SPORTNAME,USER_2_ID);
        insertTeam(TEAMNAME_3,ACRONYM+TEAMNAME_3,FIRSTNAME+USER_3_ID,ISTEMP,
                SPORTNAME,USER_3_ID);
        insertGame(TEAMNAME_1,TEAMNAME_2,STARTIME_1,FINISHTIME_1,TYPE,RESULT, COUNTRY_1,STATE,CITY,
                STREET);
        insertGame(TEAMNAME_1,TEAMNAME_2,STARTIME_2,FINISHTIME_2,TYPE,RESULT, COUNTRY_2,STATE,CITY,
                STREET);
        insertGame(TEAMNAME_1,TEAMNAME_2,STARTIME_3,FINISHTIME_3,TYPE,RESULT, COUNTRY_3,STATE,CITY,
                STREET);
        insertGame(TEAMNAME_3,TEAMNAME_2,STARTIME_3,FINISHTIME_3,TYPE,RESULT, COUNTRY_3,STATE,CITY,
                STREET);
        PremiumUser loggedUser = new PremiumUser(FIRSTNAME+USER_3_ID,
                LASTNAME+USER_3_ID, EMAIL+USER_3_ID, USER_3_ID,
                FIRSTNAME+USER_3_ID);

        final List<Game> games = gameJdbcDao.findGames(null, null,
                null, null, null, null, null,
                null, null, null, null, null,
                null, loggedUser, false, false);

        Assert.assertEquals(QUERY_SIZE_2,games.size());
        Assert.assertEquals(TEAMNAME_1,games.get(0).getTeam1().getName());
        Assert.assertEquals(TEAMNAME_2,games.get(0).getTeam2().getName());
        Assert.assertEquals(STARTIME_LOCALTIME_1,games.get(0).getStartTime().toString());
        Assert.assertEquals(FINISHTIME_LOCALTIME_1,games.get(0).getFinishTime().toString());
        Assert.assertEquals(TEAMNAME_1,games.get(1).getTeam1().getName());
        Assert.assertEquals(TEAMNAME_2,games.get(1).getTeam2().getName());
        Assert.assertEquals(STARTIME_LOCALTIME_2,games.get(1).getStartTime().toString());
        Assert.assertEquals(FINISHTIME_LOCALTIME_2,games.get(1).getFinishTime().toString());
        Assert.assertEquals(TEAMNAME_1,games.get(2).getTeam1().getName());
        Assert.assertEquals(TEAMNAME_2,games.get(2).getTeam2().getName());
        Assert.assertEquals(STARTIME_LOCALTIME_3,games.get(2).getStartTime().toString());
        Assert.assertEquals(FINISHTIME_LOCALTIME_3,games.get(2).getFinishTime().toString());
    }

    @Test
    public void findGamesTestGamesAUserIsPartOf() {
        insertAccount(FIRSTNAME+USER_1_ID,LASTNAME+USER_1_ID, EMAIL+USER_1_ID,
                USER_1_ID, FIRSTNAME+USER_1_ID, PASSWORD, COUNTRY_1, STATE, CITY, STREET,
                REPUTATION, CELLPHONE, BIRTHDAY);
        insertAccount(FIRSTNAME+USER_2_ID,LASTNAME+USER_2_ID, EMAIL+USER_2_ID,
                USER_2_ID, FIRSTNAME+USER_2_ID, PASSWORD, COUNTRY_2, STATE, CITY, STREET,
                REPUTATION, CELLPHONE, BIRTHDAY);
        insertAccount(FIRSTNAME+USER_3_ID,LASTNAME+USER_3_ID, EMAIL+USER_3_ID,
                USER_3_ID, FIRSTNAME+USER_3_ID, PASSWORD, COUNTRY_2, STATE, CITY, STREET,
                REPUTATION, CELLPHONE, BIRTHDAY);
        insertSport(SPORTNAME,QUANTITY);
        insertTeam(TEAMNAME_1,ACRONYM+TEAMNAME_1,FIRSTNAME+USER_1_ID,ISTEMP,
                SPORTNAME,USER_1_ID);
        insertTeam(TEAMNAME_2,ACRONYM+TEAMNAME_2,FIRSTNAME+USER_2_ID,ISTEMP,
                SPORTNAME,USER_2_ID);
        insertTeam(TEAMNAME_3,ACRONYM+TEAMNAME_3,FIRSTNAME+USER_3_ID,ISTEMP,
                SPORTNAME,USER_3_ID);
        insertGame(TEAMNAME_1,TEAMNAME_2,STARTIME_1,FINISHTIME_1,TYPE,RESULT, COUNTRY_1,STATE,CITY,
                STREET);
        insertGame(TEAMNAME_1,TEAMNAME_2,STARTIME_2,FINISHTIME_2,TYPE,RESULT, COUNTRY_2,STATE,CITY,
                STREET);
        insertGame(TEAMNAME_1,TEAMNAME_2,STARTIME_3,FINISHTIME_3,TYPE,RESULT, COUNTRY_3,STATE,CITY,
                STREET);
        insertGame(TEAMNAME_3,TEAMNAME_2,STARTIME_3,FINISHTIME_3,TYPE,RESULT, COUNTRY_3,STATE,CITY,
                STREET);
        PremiumUser loggedUser = new PremiumUser(FIRSTNAME+USER_3_ID,
                LASTNAME+USER_3_ID, EMAIL+USER_3_ID, USER_3_ID,
                FIRSTNAME+USER_3_ID);

        final List<Game> games = gameJdbcDao.findGames(null, null,
                null, null, null, null, null,
                null, null, null, null, null,
                null, loggedUser, true, true);

        Assert.assertEquals(1,games.size());
        Assert.assertEquals(TEAMNAME_3,games.get(0).getTeam1().getName());
        Assert.assertEquals(TEAMNAME_2,games.get(0).getTeam2().getName());
        Assert.assertEquals(STARTIME_LOCALTIME_3,games.get(0).getStartTime().toString());
        Assert.assertEquals(FINISHTIME_LOCALTIME_3,games.get(0).getFinishTime().toString());
    }

    public void modifyTest() {
        insertAccount(FIRSTNAME+USER_1_ID,LASTNAME+USER_1_ID, EMAIL+USER_1_ID,
                USER_1_ID, FIRSTNAME+USER_1_ID, PASSWORD, COUNTRY_1, STATE, CITY, STREET,
                REPUTATION, CELLPHONE, BIRTHDAY);
        insertAccount(FIRSTNAME+USER_2_ID,LASTNAME+USER_2_ID, EMAIL+USER_2_ID,
                USER_2_ID, FIRSTNAME+USER_2_ID, PASSWORD, COUNTRY_2, STATE, CITY, STREET,
                REPUTATION, CELLPHONE, BIRTHDAY);
        insertSport(SPORTNAME,QUANTITY);
        insertTeam(TEAMNAME_1,ACRONYM+TEAMNAME_1,FIRSTNAME+USER_1_ID,ISTEMP,
                SPORTNAME,USER_1_ID);
        insertTeam(TEAMNAME_2,ACRONYM+TEAMNAME_2,FIRSTNAME+USER_2_ID,ISTEMP,
                SPORTNAME,USER_2_ID);
        insertGame(TEAMNAME_2,TEAMNAME_1,STARTIME_2,FINISHTIME_2,TYPE,RESULT, COUNTRY_1,STATE,CITY,
                STREET);

        final Game game = gameJdbcDao.modify(TEAMNAME_1,TEAMNAME_2,STARTIME_1,FINISHTIME_1,
                TYPE,RESULT, COUNTRY_1,STATE,CITY, STREET, null, null,
                TEAMNAME_2,TEAMNAME_1,STARTIME_2,FINISHTIME_2).get();

        Assert.assertEquals(TEAMNAME_1, game.getTeam1().getName());
        Assert.assertEquals(TEAMNAME_2, game.getTeam2().getName());
        Assert.assertEquals(STARTIME_LOCALTIME_1, game.getStartTime().toString());
        Assert.assertEquals(FINISHTIME_LOCALTIME_1, game.getFinishTime().toString());
    }
}
