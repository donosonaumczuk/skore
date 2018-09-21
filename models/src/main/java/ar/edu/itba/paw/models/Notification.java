package ar.edu.itba.paw.models;

import java.time.LocalDateTime;

public class Notification {
    private LocalDateTime time;
    private String content;
    private boolean seen;

    public Notification(LocalDateTime time, String content, boolean seen) {
        this.time = time;
        this.content = content;
        this.seen = seen;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public String getContent() {
        return content;
    }

    public boolean hasBeenSeen() {
        return seen;
    }

    public void setSeen() {
        seen = true;
    }
}
