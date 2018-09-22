package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.Place;
import ar.edu.itba.paw.models.User;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.jdbc.JdbcTestUtils;

import javax.sql.DataSource;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
@Sql("classpath:schema.sql")
public class PremiumUserJdbcDaoTest {

        private static final String FIRSTNAME = "first_name";
        private static final String LASTNAME = "last_name";
        private static final String EMAIL = "email";
        private static final String USERNAME = "username";
        private static final String CELLPHONE = "cellphone";
        private static final String BIRTHDAY = "1994-12-26";
        private static final String COUNTRY = "country";
        private static final String STATE = "state";
        private static final String CITY = "city";
        private static final String STREET = "street";
        private static final int REPUTATION = 10;
        private static final String PASSWORD = "password";
        private static final long NONEXISTANT_ID = 10;
        private static final long EXISTANT_ID = 100;

        @Autowired
        private DataSource dataSource;

        @Autowired
        private PremiumUserJdbcDao premiumUserDao;


        private JdbcTemplate jdbcTemplate;

        @Before
        public void setUp() {
            jdbcTemplate = new JdbcTemplate(dataSource);
            JdbcTestUtils.deleteFromTables(jdbcTemplate, "accounts");
        }

        @Test
        public void testCreate() {
            //exercise class
            final User user = premiumUserDao.create(FIRSTNAME, LASTNAME, EMAIL, USERNAME,
            CELLPHONE, BIRTHDAY, COUNTRY, STATE, CITY, STREET, REPUTATION, PASSWORD).get();

            //postconditions
//            Assert.assertNotNull(user);
//            Assert.assertEquals(FIRSTNAME, user.getFirstName());
//            Assert.assertEquals(LASTNAME, user.getLastName());
//            Assert.assertEquals(EMAIL, user.getEmail());
//            Assert.assertEquals(1, JdbcTestUtils.countRowsInTable(jdbcTemplate, "users"));
        }
    }
