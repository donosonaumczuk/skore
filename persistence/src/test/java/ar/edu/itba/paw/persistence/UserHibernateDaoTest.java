package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.User;
import org.junit.After;
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
import java.util.Optional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
@Sql("classpath:schema.sql")
@Transactional
public class UserHibernateDaoTest {

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private UserHibernateDao userDao;

    private User insertedUser;

    public UserHibernateDaoTest() {
        insertedUser = new User("userName", "lastName", "email");
    }

    @After
    public void removeAllData() {
        em.createNativeQuery("delete from user");
        em.flush();
    }

    @Test
    public void testFindUserById() {
        //set up
        em.persist(insertedUser);

        //exercise class
        Optional<User> returnedValue = userDao.findById(insertedUser.getUserId());

        //postconditions
        Assert.assertTrue(returnedValue.isPresent());
        Assert.assertTrue(returnedValue.get().equals(insertedUser));
    }

    @Test
    public void testFindNonExistentUserById() {
        //exercise class
        Optional<User> returnedValue = userDao.findById(insertedUser.getUserId());

        //postconditions
        Assert.assertTrue(!returnedValue.isPresent());
    }

    @Test
    public void testCreateUser() {
        //exercise class
        Optional<User> returnedValue = userDao.create(insertedUser.getFirstName(),
                                        insertedUser.getLastName(), insertedUser.getEmail());

        //postconditions
        Assert.assertTrue(returnedValue.isPresent());
        Assert.assertTrue(returnedValue.get().getFirstName().equals(insertedUser.getFirstName()));
    }

    @Test
    public void testremoveUser() {
        //set up
        em.persist(insertedUser);

        //exercise class
        Boolean returnedValue = userDao.remove(insertedUser.getUserId());

        //postconditions
        Assert.assertTrue(returnedValue);
        Assert.assertNull(em.find(User.class, insertedUser.getUserId()));
    }

    @Test
    public void testUpdateUserInfo() {
        //set up
        em.persist(insertedUser);

        //exercise class
        Optional<User> returnedValue = userDao.updateBasicUserInfo(insertedUser.getUserId(),
                "newUserName", "newLastName", "newEmail");
        User expectedUser = new User("newUserName", "newLastName", "newEmail",
                insertedUser.getUserId());
        User user = em.find(User.class, insertedUser.getUserId());

        //postconditions
        Assert.assertTrue(returnedValue.isPresent());
        Assert.assertTrue(expectedUser.equals(user));
    }

    @Test
    public void testUpdateNonExistentUserInfo() {

        //exercise class
        Optional<User> returnedValue = userDao.updateBasicUserInfo(insertedUser.getUserId(),
                "newUserName", "newLastName", "newEmail");

        //postconditions
        Assert.assertTrue(!returnedValue.isPresent());
    }
}
