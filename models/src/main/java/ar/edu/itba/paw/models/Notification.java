package ar.edu.itba.paw.models;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "notification")
public class Notification {

    @EmbeddedId
    private NotificationPK primaryKey;

    @Column
    private boolean seen;

    /* package */public Notification() {
        // For Hibernate
    }

    public Notification(LocalDateTime startTime, String content, boolean seen, PremiumUser owner) {
        this.primaryKey = new NotificationPK(startTime, content, owner);
        this.seen       = seen;
    }

    public LocalDateTime getTime() {
        return primaryKey.getStartTime();
    }

    public String getContent() {
        return primaryKey.getContent();
    }

    public boolean hasBeenSeen() {
        return seen;
    }

    public void setSeen() {
        seen = true;
    }

    public PremiumUser getOwner() {
        return primaryKey.getOwner();
    }

    @Override
    public boolean equals(Object object) {
        if(object == null || !object.getClass().equals(getClass())) {
            return false;
        }

        Notification aNotification = ((Notification) object);
        return this.primaryKey.equals(aNotification.primaryKey);
    }

    @Override
    public int hashCode() {
        return Objects.hash(primaryKey.hashCode());
    }
}
