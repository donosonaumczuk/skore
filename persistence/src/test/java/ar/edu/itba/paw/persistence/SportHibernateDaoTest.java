package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.Sport;
import ar.edu.itba.paw.models.SportSort;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
@Sql("classpath:schema.sql")
@Transactional
public class SportHibernateDaoTest {

    @Autowired
    private SportHibernateDao sportDao;

    @PersistenceContext
    private EntityManager em;

    private final List<Sport> sports;

    private final Sport sport;

    private final Sport sportNotInserted;

    public SportHibernateDaoTest() {
        sport = new Sport("baloncesto", 5, "Baloncesto", null);
        sports = new LinkedList<>();
        sports.add(sport);
        sports.add(new Sport("football", 11, "Football", null));
        sportNotInserted = new Sport("golf", 2, "Golf", null);
    }

    @Before
    public void initializeDatabase() {
        sports.forEach(em::persist);
        em.flush();
    }

    @After
    public void removeAllData() {
        em.createNativeQuery("delete from sports");
        em.flush();
    }

    @Test
    public void testCreate() throws IOException {
        //exercise class
        final Sport sport = sportDao.create(sportNotInserted.getName(), sportNotInserted.getQuantity(),
                sportNotInserted.getDisplayName(), null).get();

        //postconditions
        Assert.assertNotNull(sport);
        Assert.assertEquals(sportNotInserted.getName(), sport.getName());
        Assert.assertEquals(sportNotInserted.getQuantity(), sport.getQuantity());
        Assert.assertEquals(sportNotInserted.getDisplayName(), sport.getDisplayName());
        Assert.assertEquals(sportNotInserted, em.find(Sport.class, sportNotInserted.getName()));
    }


    @Test
    public void testFindByNameWithExistantId() {
        //exercise class
        final Optional<Sport> returnedSportOpt = sportDao.findByName(sport.getName());

        //postconditions
        Assert.assertTrue(returnedSportOpt.isPresent());
        final Sport returnedSport = returnedSportOpt.get();
        Assert.assertEquals(sport, returnedSport);
    }

    @Test
    public void testFindIdWithNonExistantId() {
        //exercise class
        final Optional<Sport> returnedSport = sportDao.findByName(sportNotInserted.getName());

        //postconditions
        Assert.assertTrue(!returnedSport.isPresent());
    }

    @Test
    public void testModifySport() throws IOException {
        //exercise
        Optional<Sport> returnedSport = sportDao.modifySport(sport.getName(),
                "BASKETBALL", null);

        //postconditions
        Assert.assertTrue(returnedSport.isPresent());
        Assert.assertEquals("BASKETBALL", returnedSport.get().getDisplayName());
    }

    @Test
    public void testRemoveNonExistantSport(){
        //exercise class
        boolean returnValue = sportDao.remove(sportNotInserted.getName());

        //postconditions
        Assert.assertEquals(false, returnValue);
    }

    @Test
    public void testRemoveExistantSport(){
        //exercise class
        boolean returnValue = sportDao.remove(sport.getName());

        //postconditions
        Assert.assertEquals(true, returnValue);
        Assert.assertEquals(null, em.find(Sport.class, sport.getName()));
    }

    @Test
    public void testGetAllSportsSorted() {
        //exercise class
        List<Sport> sportsResult = sportDao.findSports(null, null, null,
                new SportSort("quantity desc"));

        //postconditions
        Assert.assertEquals(2, sportsResult.size());
        Assert.assertEquals(sports.get(1), sportsResult.get(0));
        Assert.assertEquals(sports.get(0), sportsResult.get(1));
    }

    @Test
    public void testGetAllSportsBySportName() {
        //Set up
        List<String> sportnames = new ArrayList<>();
        sportnames.add(sport.getName());

        //exercise class
        List<Sport> sportsResult = sportDao.findSports(sportnames, null, null, null);

        //postconditions
        Assert.assertEquals(1, sportsResult.size());
        Assert.assertEquals(sport, sportsResult.get(0));
    }

    @Test
    public void testGetAllSportsByQuantity() {

        //exercise class
        List<Sport> sportsResult = sportDao.findSports(null, 1, 7, null);

        //postconditions
        Assert.assertEquals(1, sportsResult.size());
        Assert.assertEquals(sport, sportsResult.get(0));
    }
}
