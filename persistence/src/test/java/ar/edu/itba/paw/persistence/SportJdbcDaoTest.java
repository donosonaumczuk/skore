package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.Sport;
import ar.edu.itba.paw.models.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.jdbc.JdbcTestUtils;

import javax.sql.DataSource;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
@Rollback
@Sql("classpath:schema.sql")
public class SportJdbcDaoTest {

    private static final String SPORT_NAME = "futbol5";
    private static final int PLAYER_QUANTITY = 5;
    private static final String DISPLAY_NAME = "Futbol 5";

    @Autowired
    private DataSource dataSource;

    @Autowired
    private SportHibernateDao sportDao;

    private JdbcTemplate jdbcTemplate;


    @Before
    public void setUp() {
        jdbcTemplate = new JdbcTemplate(dataSource);
        JdbcTestUtils.deleteFromTables(jdbcTemplate, "sports");
    }

    @Test
    public void testCreate() throws IOException {
        //exercise class
        final Sport sport = sportDao.create(SPORT_NAME, PLAYER_QUANTITY, DISPLAY_NAME, null).get();

        //postconditions
        Assert.assertNotNull(sport);
        Assert.assertEquals(SPORT_NAME, sport.getName());
        Assert.assertEquals(PLAYER_QUANTITY, sport.getQuantity());
        Assert.assertEquals(DISPLAY_NAME, sport.getDisplayName());
        Assert.assertEquals(1, JdbcTestUtils.countRowsInTable(jdbcTemplate, "sports"));
    }

    private void insertSport(final String sportName) {
        jdbcTemplate.execute("INSERT INTO sports (sportName, playerQuantity, displayName)" +
                " VALUES ('" + sportName + "' , " + PLAYER_QUANTITY + ", '" + DISPLAY_NAME + "');");
    }

    @Test
    public void testFindByNameWithExistantId() {
        //set up
        insertSport(SPORT_NAME);

        //exercise class
        final Optional<Sport> returnedSport = sportDao.findByName(SPORT_NAME);

        //postconditions
        Assert.assertTrue(returnedSport.isPresent());
        final Sport sport = returnedSport.get();
        Assert.assertEquals(SPORT_NAME, sport.getName());
    }

    @Test
    public void testFindIdWithNonExistantId() {
        //exercise class
        final Optional<Sport> returnedSport = sportDao.findByName(SPORT_NAME);

        //postconditions
        Assert.assertTrue(!returnedSport.isPresent());
    }

    @Test
    public void testRemoveNonExistantUser(){
        //exercise class
        boolean returnValue = sportDao.remove(SPORT_NAME);

        //postconditions
        Assert.assertEquals(false, returnValue);
    }

    @Test
    public void testRemoveExistantUser(){
        //set up
        insertSport(SPORT_NAME);

        //exercise class
        boolean returnValue = sportDao.remove(SPORT_NAME);

        //postconditions
        Assert.assertEquals(true, returnValue);
        Assert.assertEquals(0, JdbcTestUtils.countRowsInTable(jdbcTemplate, "sports"));
    }

    @Test
    public void testgetAllSports() {
        //set up
        insertSport(SPORT_NAME);
        insertSport("futbol11");
        insertSport("padel");

        //exercise class
        List<Sport> sports = sportDao.getAllSports();

        //postconditions
        Assert.assertEquals(sports.size(), 3);
    }
}
