package ar.edu.itba.paw.models;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Embeddable
public class GamePK implements Serializable {

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "teamName1")
    private Team team1;

    @Column
    private LocalDateTime startTime;

    @Column
    private LocalDateTime finishTime;

    /* package */public GamePK() {
        // For Hibernate
    }

   public GamePK(Team team1, LocalDateTime startTime, LocalDateTime finishTime) {
        this.team1  = team1;
        this.startTime  = startTime;
        this.finishTime = finishTime;
    }

    public Team getTeam1() {
        return team1;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public LocalDateTime getFinishTime() {
        return finishTime;
    }

    public void setTeam1(Team team1) {
        this.team1 = team1;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public void setFinishTime(LocalDateTime finishTime) {
        this.finishTime = finishTime;
    }
}
