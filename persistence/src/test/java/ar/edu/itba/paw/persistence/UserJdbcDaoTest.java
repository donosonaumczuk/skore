package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.Assert;
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
public class UserJdbcDaoTest {
    private static final String FIRSTNAME = "first_name";
    private static final String LASTNAME = "last_name";
    private static final String EMAIL = "email";
    private static final long NONEXISTANT_ID = 10;
    private static final long EXISTANT_ID = 100;

    @Autowired
    private DataSource dataSource;

    @Autowired
    private UserJdbcDao userDao;

    private JdbcTemplate jdbcTemplate;

    @Before
    public void setUp() {
        jdbcTemplate = new JdbcTemplate(dataSource);
        JdbcTestUtils.deleteFromTables(jdbcTemplate, "users");
    }

    @Test
    public void testCreate() {
        //exercise class
        final User user = userDao.create(FIRSTNAME, LASTNAME, EMAIL).get();

        //postconditions
        Assert.assertNotNull(user);
        Assert.assertEquals(FIRSTNAME, user.getFirstName());
        Assert.assertEquals(LASTNAME, user.getLastName());
        Assert.assertEquals(EMAIL, user.getEmail());
        Assert.assertEquals(1, JdbcTestUtils.countRowsInTable(jdbcTemplate, "users"));
    }

    private void insertUser(final long userId) {
        jdbcTemplate.execute("INSERT INTO users (firstname, lastname, email, userid)" +
                " VALUES ('" + FIRSTNAME + "' , '" + LASTNAME + "', '" + EMAIL + "', " + userId + ");");
    }

    @Test
    public void testFindIdWithExistantId() {
        //set up
        insertUser(EXISTANT_ID);

        //exercise class
        final Optional<User> returnedUser = userDao.findById(EXISTANT_ID);

        //postconditions
        Assert.assertTrue(returnedUser.isPresent());
        final User user = returnedUser.get();
        Assert.assertEquals(EXISTANT_ID, user.getUserId());
    }

    @Test
    public void testFindIdWithNonExistantId() {
        //exercise class
        final Optional<User> returnedUser = userDao.findById(NONEXISTANT_ID);

        //postconditions
        Assert.assertTrue(!returnedUser.isPresent());
    }

    @Test
    public void testRemoveNonExistantUser(){
        //exercise class
        boolean returnValue = userDao.remove(NONEXISTANT_ID);

        //postconditions
        Assert.assertEquals(false, returnValue);
    }

    @Test
    public void testRemoveExistantUser(){
        //set up
        insertUser(EXISTANT_ID);

        //exercise class
        boolean returnValue = userDao.remove(EXISTANT_ID);

        //postconditions
        Assert.assertEquals(true, returnValue);
        Assert.assertEquals(0, JdbcTestUtils.countRowsInTable(jdbcTemplate, "users"));
    }

    @Test
    public void testUpdateFirstName() {
        //set up
        insertUser(EXISTANT_ID);
        final String newFirstName = "newFirstName";

        //exercise
        User newUser = userDao.updateFirstName(EXISTANT_ID, newFirstName).get();

        //postconditions
        Assert.assertEquals(newFirstName, newUser.getFirstName());
    }

    @Test
    public void testUpdateFirstNameNonExistatnId() {
        //set up
        final String newFirstName = "newFirstName";

        //exercise class
        final Optional<User> returnedUser = userDao.updateFirstName(NONEXISTANT_ID, newFirstName);

        //postconditions
        Assert.assertTrue(!returnedUser.isPresent());
    }

    @Test
    public void testUpdateLastName() {
        //set up
        insertUser(EXISTANT_ID);
        final String newLastName = "newLastName";

        //exercise
        User newUser = userDao.updateLastName(EXISTANT_ID, newLastName).get();

        //postconditions
        Assert.assertEquals(newLastName, newUser.getLastName());
    }

    @Test
    public void testUpdateLastNameNonExistatnId() {
        //set up
        final String newLastName = "newLastName";

        //exercise class
        final Optional<User> returnedUser = userDao.updateLastName(NONEXISTANT_ID, newLastName);

        //postconditions
        Assert.assertTrue(!returnedUser.isPresent());
    }

    @Test
    public void testUpdateEmail() {
        //set up
        insertUser(EXISTANT_ID);
        final String newEmail = "newEmail";

        //exercise
        User newUser = userDao.updateEmail(EXISTANT_ID, newEmail).get();

        //postconditions
        Assert.assertEquals(newEmail, newUser.getEmail());
    }

    @Test
    public void testUpdateEmailNonExistatnId() {
        //set up
        final String newEmail = "newEmail";

        //exercise class
        final Optional<User> returnedUser = userDao.updateEmail(NONEXISTANT_ID, newEmail);

        //postconditions
        Assert.assertTrue(!returnedUser.isPresent());
    }
}
