package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.Game;
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

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
@Sql("classpath:schema.sql")
public class GameJdbcDaoTest {
    private static final String FIRSTNAME = "first_name";
    private static final String LASTNAME = "last_name";
    private static final String EMAIL = "email";
    private static final long USER_1_ID = 1;
    private static final long USER_2_ID = 2;
    private static final String SPORTNAME = "sportname";
    private static final int QUANTITY = 10;
    private static final String ACRONYM = "acronym";
    private static final long ISTEMP = 0;
    private static final String TEAMNAME_1 = "1";
    private static final String TEAMNAME_2 = "2";
    private static final String STARTIME = "2018-12-12 0:00:00";
    private static final String FINISHTIME = "2018-12-12 01:00:00";
    private static final String STARTIME_LOCALTIME = "2018-12-12T00:00";
    private static final String FINISHTIME_LOCALTIME = "2018-12-12T01:00";
    private static final String TYPE = "competitive";
    private static final String RESULT = "3-0";
    private static final String COUNTRY = "country";
    private static final String STATE = "state";
    private static final String CITY = "city";
    private static final String STREET = "street";


    @Autowired
    private DataSource dataSource;

    @Autowired
    private GameJdbcDao gameJdbcDao;

    private JdbcTemplate jdbcTemplate;

    @Before
    public void setUp() {
        jdbcTemplate = new JdbcTemplate(dataSource);
        JdbcTestUtils.deleteFromTables(jdbcTemplate, "games");
    }

    private void insertUser(final String firstName, final String lastName, final String email,
                            final long userId) {
        jdbcTemplate.execute("INSERT INTO users (firstname, lastname, email, userid)" +
                " VALUES ('" + firstName + "' , '" + lastName + "', '" + email + "', " + userId + ");");
    }

    private void insertSport(final String sportName, final long quantity) {
        jdbcTemplate.execute("INSERT INTO sports (sportName, playerQuantity)" +
                " VALUES ('" + sportName + "', " + quantity + ");");
    }

    private void insertTeam(final String teamName, final String acronym, final long leaderId,
                            final long isTemp, final String sportName) {
        jdbcTemplate.execute("INSERT INTO teams (teamName, acronym, leaderId, isTemp, sportName)" +
                " VALUES ('" + teamName + "' , '" + acronym + "', " + leaderId + ", " + isTemp +
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
    public void findGamesTest() {
        insertUser(FIRSTNAME+USER_1_ID,LASTNAME+USER_1_ID, EMAIL+USER_1_ID,
                USER_1_ID);
        insertUser(FIRSTNAME+USER_2_ID,LASTNAME+USER_2_ID, EMAIL+USER_2_ID,
                USER_2_ID);
        insertSport(SPORTNAME,QUANTITY);
        insertTeam(TEAMNAME_1,ACRONYM+TEAMNAME_1,USER_1_ID,ISTEMP,SPORTNAME);
        insertTeam(TEAMNAME_2,ACRONYM+TEAMNAME_2,USER_2_ID,ISTEMP,SPORTNAME);
        insertGame(TEAMNAME_1,TEAMNAME_2,STARTIME,FINISHTIME,TYPE,RESULT,COUNTRY,STATE,CITY,
                STREET);

        List<String> type = new ArrayList<>();
        type.add(TYPE);
        List<String> sportnames = new ArrayList<>();
        sportnames.add(SPORTNAME);
        List<String> countries = new ArrayList<>();
        countries.add(COUNTRY);
        List<String> states = new ArrayList<>();
        states.add(STATE);
        List<String> cities = new ArrayList<>();
        cities.add(CITY);

        final List<Game> games = gameJdbcDao.findGames(STARTIME, FINISHTIME, STARTIME,
                FINISHTIME, type, sportnames, QUANTITY-1, QUANTITY+1,
                countries, states, cities, 0, 19);

        Assert.assertEquals(1,games.size());
        Assert.assertEquals(TEAMNAME_1,games.get(0).getTeam1().getName());
        Assert.assertEquals(TEAMNAME_2,games.get(0).getTeam2().getName());
        Assert.assertEquals(STARTIME_LOCALTIME,games.get(0).getStartTime().toString());
        Assert.assertEquals(FINISHTIME_LOCALTIME,games.get(0).getFinishTime().toString());
    }
}
