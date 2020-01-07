package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.JWT;
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
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
@Sql("classpath:schema.sql")
@Transactional
public class JWTHibernateDaoTest {

    @Autowired
    private JWTHibernateDao jwtHibernateDao;

    @PersistenceContext
    private EntityManager em;

    private List<JWT> jwts;
    private JWT jwt;
    private JWT jwtNotInserted;

    public JWTHibernateDaoTest() {
        jwt = new JWT("token1", LocalDateTime.parse("2018-12-12T00:00:00"), 1);
        jwts = new ArrayList<>();
        jwts.add(jwt);
        jwts.add(new JWT("token2", LocalDateTime.parse("2021-12-12T00:00:00"), 2));
        jwts.add(new JWT("token3", LocalDateTime.parse("2020-12-12T00:00:00"), 3));
        jwtNotInserted = new JWT("token4", LocalDateTime.parse("2023-12-12T00:00:00"), 4);
    }

    @Before
    public void initializeDatabase() {
        for (JWT jwt: jwts) {
            em.merge(jwt);
        }
        em.flush();
    }

    @After
    public void removeAllData() {
        em.createNativeQuery("delete from blacklist");
        em.flush();
    }

    @Test
    public void addToBlacklistTest() {

        final JWT jwtcreated = jwtHibernateDao
                .addBlacklist(jwtNotInserted.getToken(), jwtNotInserted.getExpiry());

        Assert.assertEquals(jwtNotInserted, jwtcreated);
        Assert.assertEquals(jwtNotInserted.getToken(), jwtcreated.getToken());
        Assert.assertEquals(jwtNotInserted.getExpiry(), jwtcreated.getExpiry());
    }

    @Test
    public void isInBlackListTest() {

        final boolean inBlacklist = jwtHibernateDao.isInBlacklist(jwt.getToken());

        Assert.assertTrue(inBlacklist);
    }

    @Test
    public void isNotInBlackListTest() {

        final boolean inBlacklist = jwtHibernateDao.isInBlacklist(jwtNotInserted.getToken());

        Assert.assertFalse(inBlacklist);
    }

    @Test
    public void getAllTest() {

        final List<JWT> jwtInDatabase = jwtHibernateDao.getAll();

        JWT auxWant, auxHave;
        for (int i = 0; i < 3; i++) {
            auxHave = jwtInDatabase.get(i);
            auxWant = jwts.get(i);
            Assert.assertEquals(auxWant, auxHave);
            Assert.assertEquals(auxWant.getToken(), auxHave.getToken());
            Assert.assertEquals(auxWant.getExpiry(), auxHave.getExpiry());
        }
    }
}
