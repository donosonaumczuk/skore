package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.Sport;
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
    public void testRemoveNonExistantUser(){
        //exercise class
        boolean returnValue = sportDao.remove(sportNotInserted.getName());

        //postconditions
        Assert.assertEquals(false, returnValue);
    }

    @Test
    public void testRemoveExistantUser(){
        //exercise class
        boolean returnValue = sportDao.remove(sport.getName());

        //postconditions
        Assert.assertEquals(true, returnValue);
        Assert.assertEquals(null, em.find(Sport.class, sport.getName()));
    }

    @Test
    public void testgetAllSports() {
        //exercise class
        List<Sport> sports = sportDao.getAllSports();

        //postconditions
        Assert.assertEquals(sports.size(), 2);
    }
}
