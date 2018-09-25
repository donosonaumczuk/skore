package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.Team;
import org.junit.After;
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
import java.util.Optional;

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

    private void insertLeader(final String leaderName, final long userId) {
        jdbcTemplate.execute("INSERT INTO users (userId, email)" +
                " VALUES (" + userId + ", '" + EMAIL + "');");
        jdbcTemplate.execute("INSERT INTO accounts (userName, userId)" +
                " VALUES ('" +  leaderName + "', " + userId + ");");
    }

    private void insertSport(final String sportName, final int playerQuantity) {
        jdbcTemplate.execute("INSERT INTO sports (sportName, playerQuantity)" +
                " VALUES ('" + sportName + "', " + playerQuantity + ");");
    }

    private void insertTeam(final String leaderName, final String acronym, final String teamName,
                            final boolean isTemp, final String sportName) {
        jdbcTemplate.execute("INSERT INTO teams (leaderName, acronym, teamName, isTemp, sportName)" +
                " VALUES ('" + leaderName + "', '" + acronym + "', '" + teamName + "', " +
                (isTemp? 1 : 0) + ", '" + sportName + "');");
    }

    private void removeLeader(final String leaderName, final long userId) {
        jdbcTemplate.execute("DELETE FROM accounts CASCADE WHERE userName = '" + leaderName + "';");
        jdbcTemplate.execute("DELETE FROM users CASCADE WHERE userId = " + userId +";");

    }

    private void removeSport(final String sportName) {
        jdbcTemplate.execute("DELETE FROM sports CASCADE WHERE sportName = '" + sportName + "';");
    }


    @Test
    public void testCreate() {
        //set up
        insertLeader(LEADERNAME, USERID);
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

    @After
    public void afterCreateTest() {
        removeSport(SPORTNAME);
        removeLeader(LEADERNAME, USERID);
    }

    @Test
    public void testFindByTeamName() {
        //set up
        insertLeader(LEADERNAME, USERID);
        insertSport(SPORTNAME, PLAYERQUANTITY);
        insertTeam(LEADERNAME, ACRONYM, TEAMNAME, ISTEMP, SPORTNAME);

        //exercise class
        final Optional<Team> returnedTeam = teamDao.findByTeamName(TEAMNAME);

        //postconditions
        Assert.assertTrue(returnedTeam.isPresent());
        Assert.assertEquals(TEAMNAME, returnedTeam.get().getName());
    }

    @After
    public void afterFindByTeamNameTest() {
        removeSport(SPORTNAME);
        removeLeader(LEADERNAME, USERID);
    }

    @Test
    public void testRemoveTeam(){
        //set up
        insertLeader(LEADERNAME, USERID);
        insertSport(SPORTNAME, PLAYERQUANTITY);
        insertTeam(LEADERNAME, ACRONYM, TEAMNAME, ISTEMP, SPORTNAME);

        //exercise class
        boolean returnValue = teamDao.remove(TEAMNAME);

        //postconditions
        Assert.assertEquals(true, returnValue);
        Assert.assertEquals(0, JdbcTestUtils.countRowsInTable(jdbcTemplate, "teams"));
    }

    @After
    public void afterRemoveTeamTest() {
        removeSport(SPORTNAME);
        removeLeader(LEADERNAME, USERID);
    }

    @Test
    public void testUpdateTeamInfoSuccess() {
        //set up
        insertLeader(LEADERNAME, USERID);
        insertSport(SPORTNAME, PLAYERQUANTITY);
        insertTeam(LEADERNAME, ACRONYM, TEAMNAME, ISTEMP, SPORTNAME);
        final String newTeamName = "newTeamName";
        final String newLeaderName = "newLeaderName";
        final long newLeaderId = 1000;
        insertLeader(newLeaderName, newLeaderId);

        //exercise class
        final Optional<Team> returnedTeam = teamDao.updateTeamInfo(newTeamName, ACRONYM,
                newLeaderName, SPORTNAME, TEAMNAME);

        System.out.println("Acronym: " + returnedTeam.get().getAcronym() + "\n LeaderName: " + returnedTeam.get().getLeader().getUserName());
        //postconditions
        Assert.assertTrue(returnedTeam.isPresent());
        Assert.assertEquals(newTeamName, returnedTeam.get().getName());
        //Assert.assertEquals("recibio : " + returnedTeam.get().getLeader().getUserName(), newLeaderName, returnedTeam.get().getLeader().getUserName());
        Assert.assertEquals("recibio: " + returnedTeam.get().getSport().getName(), SPORTNAME, returnedTeam.get().getSport().getName());
    }
    @After
    public void afterUpdateTeamInfoSuccessTestt() {
        final String newLeaderName = "newLeaderName";
        final long newLeaderId = 1000;
        removeSport(SPORTNAME);
        removeLeader(LEADERNAME, USERID);
        removeLeader(newLeaderName, newLeaderId);
    }
}
