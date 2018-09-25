package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.Notification;
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
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
@Sql("classpath:schema.sql")
public class NotificationJdbcDaoTest {
    private static final String CONTENT                 = "content";
    private static final String STARTIME_1              = "2018-12-12 0:00:00";
    private static final String STARTIME_2              = "2018-12-13 0:00:00";
    private static final String STARTIME_3              = "2018-12-14 0:00:00";
    private static final String STARTIME_LOCALTIME_1    = "2018-12-12T00:00";
    private static final String STARTIME_LOCALTIME_2    = "2018-12-13T00:00";
    private static final String STARTIME_LOCALTIME_3    = "2018-12-14T00:00";
    private static final String USERNAME                = "username";
    private static final String FIRSTNAME               = "first_name";
    private static final String LASTNAME                = "last_name";
    private static final String EMAIL                   = "email";
    private static final String CELLPHONE               = "cellphone";
    private static final String BIRTHDAY                = "1994-12-26";
    private static final String COUNTRY                 = "country";
    private static final String STATE                   = "state";
    private static final String CITY                    = "city";
    private static final String STREET                  = "street";
    private static final int    REPUTATION              = 10;
    private static final String PASSWORD                = "password";
    private static final long   USERID                  = 14;

    @Autowired
    private DataSource dataSource;

    @Autowired
    private NotificationJdbcDao notificationJdbcDao;

    private JdbcTemplate jdbcTemplate;

    @Before
    public void setUp() {
        jdbcTemplate = new JdbcTemplate(dataSource);
        JdbcTestUtils.deleteFromTables(jdbcTemplate, "notification", "accounts", "users");
    }

    private void insertAccount(final String firstName, final String lastName, final String email,
                            final long userId, final String userName, final String password,
                            final String country, final String state, final String city,
                            final String street, final int reputation, final String cellphone,
                            final String birthday) {
        jdbcTemplate.execute("INSERT INTO users (firstname, lastname, email, userid)" +
                " VALUES ('" + firstName + "' , '" + lastName + "', '" + email + "', " + userId + ");");
        jdbcTemplate.execute("INSERT INTO accounts (username, cellphone, birthday," +
                " country, state, city, street, reputation, password, userId)" +
                " VALUES ('" + userName + "' , '" + cellphone + "', '" + birthday + "', '" +
                country + "', '" + state + "', '" + city + "', '" + street + "', " + reputation +
                ", '" + password +"', " + userId +");");
    }

    private void insertNotification(final String startTime, final String content,
                                    final String userName) {
        jdbcTemplate.execute("INSERT INTO notification (startTime, content, userName) " +
                "VALUES ('" + startTime + "','" + content + "','" + userName + "');");
    }

    @Test
    public void createTest() {
        insertAccount(FIRSTNAME, LASTNAME, EMAIL, USERID, USERNAME, PASSWORD, COUNTRY, STATE, CITY,
                STREET, REPUTATION, CELLPHONE, BIRTHDAY);

        final Notification notification = notificationJdbcDao.create(STARTIME_1,CONTENT,USERNAME).get();

        Assert.assertNotNull(notification);
        Assert.assertEquals(STARTIME_LOCALTIME_1, notification.getTime().toString());
        Assert.assertEquals(CONTENT, notification.getContent());
        Assert.assertEquals(false, notification.hasBeenSeen());

        Assert.assertEquals(1, JdbcTestUtils.countRowsInTable(jdbcTemplate,
                "notification"));
    }

    @Test
    public void findByKeyTest() {
        insertAccount(FIRSTNAME, LASTNAME, EMAIL, USERID, USERNAME, PASSWORD, COUNTRY, STATE, CITY,
                STREET, REPUTATION, CELLPHONE, BIRTHDAY);
        insertNotification(STARTIME_1, CONTENT, USERNAME);

        final Notification notification = notificationJdbcDao.findByKey(STARTIME_1, CONTENT).get();

        Assert.assertNotNull(notification);
        Assert.assertEquals(STARTIME_LOCALTIME_1, notification.getTime().toString());
        Assert.assertEquals(CONTENT, notification.getContent());
        Assert.assertEquals(false, notification.hasBeenSeen());
    }

    @Test
    public void getNotificationsByUserNameTest(){
        insertAccount(FIRSTNAME, LASTNAME, EMAIL, USERID, USERNAME, PASSWORD, COUNTRY, STATE, CITY,
                STREET, REPUTATION, CELLPHONE, BIRTHDAY);
        insertNotification(STARTIME_1, CONTENT, USERNAME);
        insertNotification(STARTIME_2, CONTENT, USERNAME);
        insertNotification(STARTIME_3, CONTENT, USERNAME);

        final List<Notification> notifications = notificationJdbcDao.getNotificationsByUserName(
                USERNAME);

        Assert.assertEquals(3, notifications.size());
        Assert.assertEquals(STARTIME_LOCALTIME_1, notifications.get(0).getTime().toString());
        Assert.assertEquals(CONTENT, notifications.get(0).getContent());
        Assert.assertEquals(STARTIME_LOCALTIME_2, notifications.get(1).getTime().toString());
        Assert.assertEquals(CONTENT, notifications.get(1).getContent());
        Assert.assertEquals(STARTIME_LOCALTIME_3, notifications.get(2).getTime().toString());
        Assert.assertEquals(CONTENT, notifications.get(2).getContent());
    }
}
