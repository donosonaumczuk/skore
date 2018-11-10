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
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Optional;
import java.util.Set;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
@Sql("classpath:schema.sql")
@Transactional
public class PremiumUserHibernateDaoTest implements Serializable{

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private PremiumUserHibernateDao premiumUserDao;

    private PremiumUser insertedUser;

    public PremiumUserHibernateDaoTest() {
        insertedUser = new PremiumUser("firstName", "lastName", "email",  "userName",
                "cellphone", LocalDate.parse("1994-12-26"), new Place("country", "state",
                "city", "street"), 10, "password", "code", null);
    }

    @After
    public void removeAllData() {
        em.createNativeQuery("delete from users");
        em.createNativeQuery("delete from accounts");
        //em.flush();
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
        Optional<PremiumUser> returnedValue = premiumUserDao.findByEmail(insertedUser.getUser().getEmail());

        //postconditions
        Assert.assertTrue(returnedValue.isPresent());
        Assert.assertTrue(returnedValue.get().equals(insertedUser));
    }

    @Test
    public void testCreatePremiumUser() throws IOException {

        //set up
        //em.remove(insertedUser);

        //exercise class
        Optional<PremiumUser> returnedValue = premiumUserDao.create(insertedUser.getUser().getFirstName(),
                insertedUser.getUser().getLastName(), "newEmail", "newUserName",
                insertedUser.getCellphone(), "1994-12-26", insertedUser.getHome().getCountry(),
                insertedUser.getHome().getState(), insertedUser.getHome().getCity(),
                insertedUser.getHome().getStreet(), insertedUser.getReputation(), insertedUser.getPassword(),
                null);


        //postconditions
        PremiumUser user = em.find(PremiumUser.class, "newUserName");
        Assert.assertNotNull(user);
        Assert.assertTrue(!user.equals(insertedUser));
    }

    @Test
    public void testRemove() {
        //set up
        em.persist(insertedUser);

        //exercise class
        boolean returnedValue = premiumUserDao.remove(insertedUser.getUserName());

        //postconditions
        Assert.assertTrue(returnedValue);
        Assert.assertNull(em.find(PremiumUser.class, insertedUser.getUserName()));

    }

    @Test
    public void testRemoveNonExistenUser() {
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
        premiumUserDao.updateUserInfo("newFirstName", insertedUser.getUser().getLastName(),
                "newEmail", insertedUser.getUserName(), insertedUser.getCellphone(),
                insertedUser.getBirthday().toString(), insertedUser.getHome().getCountry(),
                insertedUser.getHome().getState(), insertedUser.getHome().getCity(),
                insertedUser.getHome().getStreet(), insertedUser.getReputation(),
                "newPassword", insertedUser.getUserName());
        PremiumUser returnedUser = em.find(PremiumUser.class, insertedUser.getUserName());

        //postconditions
        Assert.assertNotNull(returnedUser);
        Assert.assertTrue(returnedUser.getUser().getFirstName().equals("newFirstName"));
        Assert.assertTrue(returnedUser.getPassword().equals("newPassword"));

    }

    @Test
    public void testEnableUser() {
        //set up
        em.persist(insertedUser);

        //exercise class
        premiumUserDao.enableUser("userName", "code");

        //postcondition
        PremiumUser premiumUser = em.find(PremiumUser.class, insertedUser.getUserName());
        Assert.assertTrue(premiumUser.getEnabled());

    }

    @Test
    public void testAddRoles() {
        //set up
        Role role = new Role("ROLE_USER", 1);
        em.persist(role);
        em.persist(insertedUser);

        //exercise the class
        boolean returnedValue = premiumUserDao.addRole(insertedUser.getUserName(),
                role.getRoleId());
        PremiumUser user = em.find(PremiumUser.class, insertedUser.getUserName());

        //postconditions
        Assert.assertTrue(returnedValue);
        Assert.assertEquals(1, user.getRoles().size());
        Assert.assertTrue(user.getRoles().contains(role));
    }


    @Test
    public void testRemoveRoles() {
        //set up
        Role role = new Role("ROLE_USER", 1);
        insertedUser.getRoles().add(role);
        em.persist(role);
        em.persist(insertedUser);

        //exercise the class
        boolean returnedValue = premiumUserDao.removeRole(insertedUser.getUserName(),
                role.getRoleId());
        PremiumUser user = em.find(PremiumUser.class, insertedUser.getUserName());

        //postconditions
        Assert.assertTrue(returnedValue);
        Assert.assertEquals(0, user.getRoles().size());
        Assert.assertTrue(!user.getRoles().contains(role));
    }

    @Test
    public void testGetRoles() {
        //set up
        Role role = new Role("ROLE_USER", 1);
        em.persist(role);
        insertedUser.getRoles().add(role);
        em.persist(insertedUser);

        //exercise the class
        Set<Role> roles = premiumUserDao.getRoles(insertedUser.getUserName());

        //postconditions
        Assert.assertEquals(1, roles.size());
        Assert.assertTrue(roles.contains(role));
    }
//
//    @Test
//    public void testGetLikes() {
//        //set up
//        Role role = new Role("ROLE_USER", 1);
//        em.persist(role);
//        em.persist(insertedUser);
//
//        //exercise the class
//        boolean returnedValue = premiumUserDao.addRole(insertedUser.getUserName(),
//                role.getRoleId());
//        PremiumUser user = em.find(PremiumUser.class, insertedUser.getUserId());
//
//        //postconditions
//        Assert.assertTrue(returnedValue);
//        Assert.assertEquals(1, user.getRoles().size());
//        Assert.assertTrue(user.getRoles().contains(role));
//    }
//
//    @Test
//    public void testAddLikes() {
//        //set up
//        Role role = new Role("ROLE_USER", 1);
//        em.persist(role);
//        em.persist(insertedUser);
//
//        //exercise the class
//        boolean returnedValue = premiumUserDao.addRole(insertedUser.getUserName(),
//                role.getRoleId());
//        PremiumUser user = em.find(PremiumUser.class, insertedUser.getUserId());
//
//        //postconditions
//        Assert.assertTrue(returnedValue);
//        Assert.assertEquals(1, user.getRoles().size());
//        Assert.assertTrue(user.getRoles().contains(role));
//    }
//
//    @Test
//    public void testRemoveLikes() {
//        //set up
//        Role role = new Role("ROLE_USER", 1);
//        em.persist(role);
//        em.persist(insertedUser);
//
//        //exercise the class
//        boolean returnedValue = premiumUserDao.addRole(insertedUser.getUserName(),
//                role.getRoleId());
//        PremiumUser user = em.find(PremiumUser.class, insertedUser.getUserId());
//
//        //postconditions
//        Assert.assertTrue(returnedValue);
//        Assert.assertEquals(1, user.getRoles().size());
//        Assert.assertTrue(user.getRoles().contains(role));
//    }


}

