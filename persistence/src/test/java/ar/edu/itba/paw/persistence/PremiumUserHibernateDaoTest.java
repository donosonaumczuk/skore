package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.Place;
import ar.edu.itba.paw.models.PremiumUser;
import ar.edu.itba.paw.models.Role;
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
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Optional;
import java.util.Set;

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

    @Test
    public void testFindUserByEmail() {
        //set up
        em.persist(insertedUser);

        //exercise class
        Optional<PremiumUser> returnedValue = premiumUserDao.findByEmail(insertedUser.getEmail());

        //postconditions
        Assert.assertTrue(returnedValue.isPresent());
        Assert.assertTrue(returnedValue.get().equals(insertedUser));
    }

    @Test
    public void testCreatePremiumUserl() throws IOException {

        //exercise class
        Optional<PremiumUser> returnedValue = premiumUserDao.create(insertedUser.getFirstName(),
                insertedUser.getLastName(), "newEmail", "newUserName",
                insertedUser.getCellphone(), "1994-12-26", insertedUser.getHome().getCountry(),
                insertedUser.getHome().getState(), insertedUser.getHome().getCity(),
                insertedUser.getHome().getStreet(), insertedUser.getReputation(), insertedUser.getPassword(),
                null);

        //postconditions
//        PremiumUser user = em.find(PremiumUser.class, 1);
//        Assert.assertNotNull(user);
//        Assert.assertTrue(user.equals(insertedUser));
    }

    @Test
    public void testRemove() {
        //set up
        em.persist(insertedUser);

        //exercise class
        boolean returnedValue = premiumUserDao.remove(insertedUser.getUserName());

        //postconditions
        Assert.assertTrue(returnedValue);
        Assert.assertNull(em.find(PremiumUser.class, insertedUser.getUserId()));

    }

    @Test
    public void testRemoveNonExistend() {
        //exercise class
        boolean returnedValue = premiumUserDao.remove(insertedUser.getUserName());

        //postconditions
        Assert.assertTrue(!returnedValue);

    }

    @Test
    public void testReadImage() {
        //set up
        em.persist(insertedUser);

        //exercise class
        Optional<byte[]> image = premiumUserDao.readImage(insertedUser.getUserName());

        //postconditions
        Assert.assertTrue(!image.isPresent());

    }

    @Test
    public void testupdateUserInfo() {
        //set up
        em.persist(insertedUser);

        //exercise class
        PremiumUser user = premiumUserDao.findByUserName(insertedUser.getUserName()).get();
        user.setUserName("newUserName");
        user.setFirstName("newFirstName");
        premiumUserDao.updateUserInfo("newFirstName", insertedUser.getLastName(),
                insertedUser.getEmail(), "newUserName",insertedUser.getCellphone(),
                insertedUser.getBirthday().toString(), insertedUser.getHome().getCountry(),
                insertedUser.getHome().getState(), insertedUser.getHome().getCity(),
                insertedUser.getHome().getStreet(), insertedUser.getReputation(),
                insertedUser.getPassword(), insertedUser.getUserName());
        PremiumUser returnedUser = em.find(PremiumUser.class, insertedUser.getUserId());

        //postconditions
        Assert.assertNotNull(returnedUser);
        Assert.assertTrue(returnedUser.getUserName().equals("newUserName"));
        Assert.assertTrue(returnedUser.getFirstName().equals("newFirstName"));

    }

    public void testEnableUser() {
        //set up
        em.persist(insertedUser);

        //exercise class
        premiumUserDao.enableUser("userName", "code");

        //postcondition
        PremiumUser premiumUser = em.find(PremiumUser.class, insertedUser.getUserId());
        Assert.assertTrue(premiumUser.getEnabled());

    }

    @Test
    public void testGetRoles() {
        //set up
        Role role = new Role("ROLE_USER", 1);
        em.persist(role);
        em.persist(insertedUser);

        //exercise the class
        boolean returnedValue = premiumUserDao.addRole(insertedUser.getUserName(),
                role.getRoleId());
        PremiumUser user = em.find(PremiumUser.class, insertedUser.getUserId());

        //postconditions
        Assert.assertTrue(returnedValue);
        Assert.assertEquals(1, user.getRoles().size());
        Assert.assertTrue(user.getRoles().contains(role));
    }



}

