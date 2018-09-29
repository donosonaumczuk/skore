package ar.edu.itba.paw.interfaces;

import ar.edu.itba.paw.models.Notification;

import java.util.List;
import java.util.Optional;

public interface NotificationDao {
    public Optional<Notification> create(final String startTime, final String content,
                                         final String userName);

    public Optional<Notification> findByKey(final String startTime, final String content);

    public List<Notification> getNotificationsByUserName(final String userName);
}
