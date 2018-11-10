package ar.edu.itba.paw.models;


import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

@Embeddable
public class NotificationPK implements Serializable {

    @Column
    private LocalDateTime startTime;

    @Column(length = 100)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "userName")
    private PremiumUser owner;

    /* package */public NotificationPK() {
        // For Hibernate
    }

    public NotificationPK(LocalDateTime startTime, String content, PremiumUser owner) {
        this.startTime = startTime;
        this.content   = content;
        this.owner  = owner;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public String getContent() {
        return content;
    }

    public PremiumUser getOwner() {
        return owner;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setOwner(PremiumUser owner) {
        this.owner = owner;
    }

    @Override
    public boolean equals(Object object) {
        if(object == null || !object.getClass().equals(getClass())) {
            return false;
        }

        NotificationPK aNotificationPK = ((NotificationPK) object);
        return (startTime.equals(aNotificationPK.startTime) && content.equals(aNotificationPK.content)
                && owner.equals(aNotificationPK.owner));
    }

    @Override
    public int hashCode() {
        return Objects.hash(startTime, content, owner);
    }
}
