package ar.edu.itba.paw.models;


import javax.persistence.Column;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

public class NotificationPK implements Serializable{

    @Column
    private LocalDateTime startTime;

    @Column(length = 100)
    private String content;

    @Column(length = 100)
    private String userName;

    /* package */public NotificationPK() {
        // For Hibernate
    }

    public NotificationPK(LocalDateTime startTime, String content, String userName) {
        this.startTime = startTime;
        this.content   = content;
        this.userName  = userName;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public String getContent() {
        return content;
    }

    public String getUserName() {
        return userName;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Override
    public boolean equals(Object object) {
        if(object == null || !object.getClass().equals(getClass())) {
            return false;
        }

        NotificationPK aNotificationPK = ((NotificationPK) object);
        return (startTime.equals(aNotificationPK.startTime) && content.equals(aNotificationPK.content)
                && userName.equals(aNotificationPK.userName));
    }

    @Override
    public int hashCode() {
        return Objects.hash(startTime, content, userName);
    }
}
