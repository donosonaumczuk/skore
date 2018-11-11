package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.Exceptions.UserNotFoundException;
import ar.edu.itba.paw.interfaces.NotificationDao;
import ar.edu.itba.paw.interfaces.PremiumUserDao;
import ar.edu.itba.paw.models.Notification;
import ar.edu.itba.paw.models.PremiumUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Repository
public class NotificationHibernateDao implements NotificationDao {

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private PremiumUserDao premiumUserDao;

    @Override
    public Optional<Notification> create(final String startTime, final String content,
                                         final String userName) {
        PremiumUser user = premiumUserDao.findByUserName(userName)
                .orElseThrow(() -> new UserNotFoundException("User does not exist"));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        Notification notification = new Notification(LocalDateTime.parse(startTime, formatter), content, false,
                user);
        em.persist(notification);
        return Optional.of(notification);
    }

    @Override
    public Optional<Notification> findByKey(final String startTime, final String content) {
        final TypedQuery<Notification> query = em.createQuery("FROM Notification WHERE startTime = :startTime AND " +
                "content = :content", Notification.class);
        query.setParameter("startTime", startTime);
        query.setParameter("content", content);
        final List<Notification> list = query.getResultList();
        return list.stream().findFirst();
    }

    @Override
    public List<Notification> getNotificationsByUserName(final String userName) {
        final TypedQuery<Notification> query = em.createQuery("FROM Notification WHERE userName = :userName",
                Notification.class);
        query.setParameter("userName", userName);
        final List<Notification> list = query.getResultList();
        return list;
    }
}
