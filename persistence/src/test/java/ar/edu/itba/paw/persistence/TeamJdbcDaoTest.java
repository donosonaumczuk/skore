package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.PremiumUser;
import ar.edu.itba.paw.models.Team;
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
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
@Sql("classpath:schema.sql")
public class TeamJdbcDaoTest {
    private static final String LEADERNAME = "leader_name";
    private static final String ACRONYM = "acronym";
    private static final String TEAMNAME = "team_name";
    private static final boolean ISTEMP = true;
    private static final String SPORTNAME = "football";
    private static final long   USERID = 14;
    private static final String EMAIL = "email";
    private static final int   PLAYERQUANTITY = 5;


    private static final String BIRTHDAY = "1994-12-26";
    private static final String COUNTRY = "country";
    private static final String STATE = "state";
    private static final String CITY = "city";
    private static final String STREET = "street";
    private static final int REPUTATION = 10;
    private static final String PASSWORD = "password";

    private static final String EXISTANT_USERNAME = "ExistantUsername";
    private static final String NONEXISTANT_USERNAME = "NonExistantUsername";

    @Autowired
    private DataSource dataSource;

    @Autowired
    private TeamJdbcDao teamDao;

    private JdbcTemplate jdbcTemplate;

    @Before
    public void setUp() {
        jdbcTemplate = new JdbcTemplate(dataSource);
        JdbcTestUtils.deleteFromTables(jdbcTemplate, "teams");
    }

    private void insertLeader(final String leaderName) {
        jdbcTemplate.execute("INSERT INTO users (userId, email)" +
                " VALUES (" + USERID + ", '" + EMAIL + "');");
        jdbcTemplate.execute("INSERT INTO accounts (userName, userId)" +
                " VALUES ('" +  LEADERNAME + "', " + USERID + ");");
    }

    private void insertSport(final String sportName, final int playerQuantity) {
        jdbcTemplate.execute("INSERT INTO sports (sportName, playerQuantity)" +
                " VALUES ('" + SPORTNAME + "', " + PLAYERQUANTITY + ");");
    }

    @Test
    public void testCreate() {
        //set up
        insertLeader(LEADERNAME);
        insertSport(SPORTNAME, PLAYERQUANTITY);

        //exercise class
        final Team team = teamDao.create(LEADERNAME, ACRONYM, TEAMNAME, ISTEMP, SPORTNAME).get();

        //postconditions
        Assert.assertNotNull(team);
        Assert.assertEquals(LEADERNAME, team.getLeader().getUserName());
        Assert.assertEquals(ACRONYM, team.getAcronym());
        Assert.assertEquals(TEAMNAME, team.getName());
        Assert.assertEquals(ISTEMP, team.isTemporal());
        Assert.assertEquals(1, JdbcTestUtils.countRowsInTable(jdbcTemplate, "teams"));
    }


}
