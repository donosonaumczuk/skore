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

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
@Sql("classpath:schema.sql")
public class UserJdbcDaoTest {
    private static final String FIRSTNAME = "first name";
    private static final String LASTNAME = "last name";
    private static final String EMAIL = "email";

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
        final User user = userDao.create(FIRSTNAME, LASTNAME, EMAIL).get();

        Assert.assertNotNull(user);
        Assert.assertEquals(FIRSTNAME, user.getFirstName());
        Assert.assertEquals(LASTNAME, user.getLastName());
        Assert.assertEquals(EMAIL, user.getEmail());
        Assert.assertEquals(1, JdbcTestUtils.countRowsInTable(jdbcTemplate, "users"));
    }
}
