package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.NotificationDao;
import ar.edu.itba.paw.models.Notification;
import ar.edu.itba.paw.models.NotificationPK;
import ar.edu.itba.paw.models.PremiumUser;
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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
@Sql("classpath:schema.sql")
@Transactional
public class NotificationHibernateTest {

    @Autowired
    private NotificationDao notificationDao;

    @PersistenceContext
    private EntityManager em;

    private List<Notification> notifications;

    private Notification notification;

    private Notification notificationNotInserted;

    private PremiumUser account;

    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public NotificationHibernateTest() {
        account = new PremiumUser("Agustin", "Dammiano", "dammiano98@itba.edu.ar",
                "dammiano98", "92262123", LocalDate.parse("1998-06-05"),
                null, 50,"321dammiano_aguistin123", "admin", null);
        notification = new Notification(LocalDateTime.parse("2018-12-12 0:00:00"), "Tiene un patido en un dia",
                false, account);
        notificationNotInserted = new Notification(LocalDateTime.parse("2017-12-12 0:00:00"), "Tiene un patido " +
                "en dos dia", false, account);
        notifications = new LinkedList<>();
        notifications.add(new Notification(LocalDateTime.parse("206-12-12 0:00:00"), "Tiene un patido en un dia",
                false, account));
        notifications.add(notification);
    }

    @Before
    public void initializeDatabase() {
        notifications.forEach(em::persist);
        em.flush();
    }

    @After
    public void removeAllData() {
        em.createNativeQuery("delete from notification");
        em.flush();
    }

    @Test
    public void createTest() {
        final Notification notificationReturn = notificationDao.create(notificationNotInserted.getTime().format(formatter),
                notificationNotInserted.getContent(), notificationNotInserted.getOwner().getUserName()).get();

        Assert.assertNotNull(notification);
        Assert.assertEquals(notificationNotInserted, notificationReturn);
        Assert.assertEquals(notificationNotInserted, em.find(Notification.class ,new NotificationPK(
                notificationNotInserted.getTime(), notificationNotInserted.getContent(),
                notificationNotInserted.getOwner())));
    }

    @Test
    public void findByKeyTest() {
        final Notification notificationReturned = notificationDao.findByKey(notification.getTime().format(formatter),
                notification.getContent()).get();

        Assert.assertEquals(notification, notificationReturned);
    }

    @Test
    public void getNotificationsByUserNameTest(){
        final List<Notification> notificationsReturned = notificationDao.getNotificationsByUserName(
                account.getUserName());

        Assert.assertEquals(2, notificationsReturned.size());
        Assert.assertEquals(notifications.get(0), notificationsReturned.get(0));
        Assert.assertEquals(notifications.get(1), notificationsReturned.get(1));
    }
}
