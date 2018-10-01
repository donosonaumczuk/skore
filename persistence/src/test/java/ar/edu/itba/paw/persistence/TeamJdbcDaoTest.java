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
    private static final String  LEADERNAME     = "leader_name";
    private static final String  USERNAME       = "userName";
    private static final String  ACRONYM        = "acronym";
    private static final String  TEAMNAME       = "team_name";
    private static final boolean ISTEMP         = true;
    private static final String  SPORTNAME      = "football";
    private static final long    USERID_1       = 14;
    private static final long    USERID_2       = 15;
    private static final String  EMAIL_1        = "email1";
    private static final String  EMAIL_2        = "email2";
    private static final int     PLAYERQUANTITY = 5;
    private static final String  PASSWORD       = "password";
    private static final String  BIRTHDAY       = "1994-12-26";


    @Autowired
    private DataSource dataSource;

    @Autowired
    private TeamJdbcDao teamDao;

    private JdbcTemplate jdbcTemplate;

    @Before
    public void setUp() {
        jdbcTemplate = new JdbcTemplate(dataSource);
        JdbcTestUtils.deleteFromTables(jdbcTemplate, "isPartOf","teams",
                "accounts", "users", "sports");
    }

    private void insertUser(final String leaderName, final long userId, final  String email) {
        jdbcTemplate.execute("INSERT INTO users (userId, email)" +
                " VALUES (" + userId + ", '" + email + "');");
        jdbcTemplate.execute("INSERT INTO accounts (userName, userId, email, password, birthday)" +
                " VALUES ('" +  leaderName + "', " + userId + ",'"+email+"','"+PASSWORD+"','"+BIRTHDAY+"');");
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

    private void insertIsAPartOf(final String teamName, final long userId) {
        jdbcTemplate.execute("INSERT INTO isPartOf (teamName, userId) VALUES ('" + teamName +
                "'," + userId + ")");
    }

    private void insertAPlayer(final String email, final long userId, final String teamName) {
        jdbcTemplate.execute("INSERT INTO users (userId, email)" +
                " VALUES (" + userId + ", '" + email + "');");
        insertIsAPartOf(teamName, userId);
    }

    @Test
    public void testCreate() {
        //set up
        insertUser(LEADERNAME, USERID_1, EMAIL_1);
        insertSport(SPORTNAME, PLAYERQUANTITY);

        //exercise class
        final Team team = teamDao.create(LEADERNAME, USERID_1, ACRONYM, TEAMNAME, ISTEMP, SPORTNAME).get();

        //postconditions
        Assert.assertEquals(1, JdbcTestUtils.countRowsInTable(jdbcTemplate, "teams"));
        Assert.assertNotNull(team);
        Assert.assertEquals(LEADERNAME, team.getLeader().getUserName());
        Assert.assertEquals(ACRONYM, team.getAcronym());
        Assert.assertEquals(TEAMNAME, team.getName());
        Assert.assertEquals(ISTEMP, team.isTemporal());
        Assert.assertEquals(SPORTNAME, team.getSport().getName());
    }

    @Test
    public void testFindByTeamName() {
        //set up
        insertUser(LEADERNAME, USERID_1, EMAIL_1);
        insertSport(SPORTNAME, PLAYERQUANTITY);
        insertTeam(LEADERNAME, ACRONYM, TEAMNAME, ISTEMP, SPORTNAME);
        insertIsAPartOf(TEAMNAME, USERID_1);
        insertAPlayer(EMAIL_1, USERID_2, TEAMNAME);

        //exercise class
        final Optional<Team> returnedTeam = teamDao.findByTeamName(TEAMNAME);

        //postconditions
        Assert.assertTrue(returnedTeam.isPresent());
        Assert.assertEquals(TEAMNAME, returnedTeam.get().getName());
        Assert.assertEquals(2, returnedTeam.get().getPlayers().size());
        Assert.assertEquals(USERID_1, returnedTeam.get().getPlayers().get(0).getUserId());
        Assert.assertEquals(USERID_2, returnedTeam.get().getPlayers().get(1).getUserId());
    }

    @Test
    public void testRemoveTeam(){
        //set up
        insertUser(LEADERNAME, USERID_1, EMAIL_1);
        insertSport(SPORTNAME, PLAYERQUANTITY);
        insertTeam(LEADERNAME, ACRONYM, TEAMNAME, ISTEMP, SPORTNAME);

        //exercise class
        boolean returnValue = teamDao.remove(TEAMNAME);

        //postconditions
        Assert.assertEquals(true, returnValue);
        Assert.assertEquals(0, JdbcTestUtils.countRowsInTable(jdbcTemplate, "teams"));
    }

    @Test
    public void testUpdateTeamInfo() {
        //set up
        insertUser(LEADERNAME, USERID_1, EMAIL_1);
        insertSport(SPORTNAME, PLAYERQUANTITY);
        insertTeam(LEADERNAME, ACRONYM, TEAMNAME, ISTEMP, SPORTNAME);
        insertIsAPartOf(TEAMNAME, USERID_1);
        final String newTeamName = "newTeamName";
        final String newLeaderName = "newLeaderName";
        final long newLeaderId = 1000;
        insertUser(newLeaderName, newLeaderId, EMAIL_2);
        insertIsAPartOf(TEAMNAME, newLeaderId);

        //exercise class
        final Optional<Team> returnedTeam = teamDao.updateTeamInfo(newTeamName, ACRONYM,
                newLeaderName, SPORTNAME, TEAMNAME);

        //postconditions
        Assert.assertTrue(returnedTeam.isPresent());
        Assert.assertEquals(newTeamName, returnedTeam.get().getName());
        Assert.assertEquals(newLeaderName, returnedTeam.get().getLeader().getUserName());
        Assert.assertEquals(SPORTNAME, returnedTeam.get().getSport().getName());
    }

    @Test
    public void testAddPlayer() {
        insertUser(LEADERNAME, USERID_1, EMAIL_1);
        insertUser(USERNAME, USERID_2, EMAIL_2);
        insertSport(SPORTNAME, PLAYERQUANTITY);
        insertTeam(LEADERNAME, ACRONYM, TEAMNAME, ISTEMP, SPORTNAME);
        insertIsAPartOf(TEAMNAME, USERID_1);

        final Optional<Team> returnedTeam = teamDao.addPlayer(TEAMNAME, USERID_2);

        Assert.assertTrue(returnedTeam.isPresent());
        Assert.assertEquals(TEAMNAME, returnedTeam.get().getName());
        Assert.assertEquals(2, returnedTeam.get().getPlayers().size());
        Assert.assertEquals(USERID_2, returnedTeam.get().getPlayers().get(1).getUserId());
        Assert.assertEquals(2, JdbcTestUtils.countRowsInTable(jdbcTemplate, "isPartOf"));
    }

    @Test
    public void testReamovePlayer() {
        insertUser(LEADERNAME, USERID_1, EMAIL_1);
        insertUser(USERNAME, USERID_2, EMAIL_2);
        insertSport(SPORTNAME, PLAYERQUANTITY);
        insertTeam(LEADERNAME, ACRONYM, TEAMNAME, ISTEMP, SPORTNAME);
        insertIsAPartOf(TEAMNAME, USERID_1);
        insertIsAPartOf(TEAMNAME, USERID_2);

        final Optional<Team> returnedTeam = teamDao.removePlayer(TEAMNAME, USERID_2);

        Assert.assertTrue(returnedTeam.isPresent());
        Assert.assertEquals(TEAMNAME, returnedTeam.get().getName());
        Assert.assertEquals(1, returnedTeam.get().getPlayers().size());
        Assert.assertEquals(1, JdbcTestUtils.countRowsInTable(jdbcTemplate, "isPartOf"));
    }
}
