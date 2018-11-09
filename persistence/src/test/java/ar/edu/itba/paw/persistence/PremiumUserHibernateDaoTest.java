package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.Place;
import ar.edu.itba.paw.models.PremiumUser;
import org.junit.Assert;
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
import java.util.Optional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
@Sql("classpath:schema.sql")
@Transactional
public class PremiumUserHibernateDaoTest {

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private PremiumUserHibernateDao premiumUserDao;

    private PremiumUser insertedUser;

    public PremiumUserHibernateDaoTest() {
        insertedUser = new PremiumUser("firstName", "lastName", "email", 1, "userName", "cellphone",
                LocalDate.parse("1994-12-26"), new Place("country", "state", "city", "street"),
                10, "password", "code", null);
    }

    @Test
    public void testFindUserByUserName() {
        //set up
        em.persist(insertedUser);

        //exercise class
        Optional<PremiumUser> returnedValue = premiumUserDao.findByUserName(insertedUser.getUserName());

        //postconditions
        Assert.assertTrue(returnedValue.isPresent());
        Assert.assertTrue(returnedValue.get().equals(insertedUser));
    }

}

