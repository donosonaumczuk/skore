package ar.edu.itba.paw.models;

import org.joda.time.DateTime;

public class Notification {
    private DateTime time;
    private String content;
    private boolean seen;

    public Notification(DateTime time, String content, boolean seen) {
        this.time = time;
        this.content = content;
        this.seen = seen;
    }

    public DateTime getTime() {
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
