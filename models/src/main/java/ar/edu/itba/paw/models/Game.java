package ar.edu.itba.paw.models;

import org.hibernate.annotations.ColumnTransformer;
import org.hibernate.annotations.Formula;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

@Entity
@Table(name = "games")
public class Game {

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "teamName2", referencedColumnName = "teamName")
    private Team team2;

    @EmbeddedId
    private GamePK primaryKey;

    @Embedded
    private Place place;

    @Column(length = 100)
    private String result;

    @Column(length = 100, nullable = false)
    private String type;

    @Column(length = 140)
    private String description;

    @Column(length = 100)
    private String title;

    @Column(length = 100)
    private String tornamentName;

    @Transient
    private int quantityOccupiedPlaces;

    /* package */public Game() {
        // For Hibernate
    }

    public Game(Team team1, Team team2, Place place, LocalDateTime startTime,
                LocalDateTime finishTime, String type, String result, String description,
                String title, String tornamentName) {
        this.team2                  = team2;
        this.place                  = place;
        this.primaryKey             = new GamePK(team1, startTime, finishTime);
        this.type                   = type;
        this.result                 = result;
        this.description            = description;
        this.title                  = title;
        this.tornamentName          = tornamentName;
    }

    public Team getTeam1() {
        return primaryKey.getTeam1();
    }

    public Team getTeam2() {
        return team2;
    }

    public void setTeam1(Team team1) {
        primaryKey.setTeam1(team1);
    }

    public void setTeam2(Team team2) {
        this.team2 = team2;
    }

    public String team1Name() {
        return primaryKey.getTeam1().getName();
    }

    public String team2Name() {
        return team2.getName();
    }

    public Place getPlace() {
        return place;
    }

    public void setPlace(final Place place) {
        this.place = place;
    }

    public LocalDateTime getStartTime() {
        return primaryKey.getStartTime();
    }

    public void setStartTime(final LocalDateTime startTime) {
        primaryKey.setStartTime(startTime);
    }

    public LocalDateTime getFinishTime() {
        return primaryKey.getFinishTime();
    }

    public void setFinishTime(final LocalDateTime finishTime) {
        primaryKey.setStartTime(finishTime);
    }

    public void setResult(final String result) {
        this.result = result;
    }

    public String getResult() {
        return result;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setTornament(String tornament) {
        this.tornamentName = tornament;
    }

    public String getTornament() {
        return tornamentName;
    }

    @Override
    public boolean equals(Object object) {
        if(object == null || !object.getClass().equals(getClass())) {
            return false;
        }

        Game aGame = ((Game) object);
        return getTeam1().equals(aGame.getTeam1())
                && getStartTime().equals(aGame.getStartTime())
                && getFinishTime().equals(aGame.getFinishTime());
    }

    @Override
    public int hashCode() {
        return Objects.hash(primaryKey);
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setQuantityOccupiedPlaces() {
        quantityOccupiedPlaces = primaryKey.getTeam1().getPlayers().size()
                + ((team2 == null)?0:team2.getPlayers().size());
    }

    public GamePK getPrimaryKey() {
        return primaryKey;
    }

    public int getQuantityOccupiedPlaces() {
        return quantityOccupiedPlaces;
    }

    public String getStartTimeString() {
        DateTimeFormatter formater= DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm");
        return getStartTime().format(formater);
    }

    public String getFinishTimeString() {
        DateTimeFormatter formater= DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm");
        return getFinishTime().format(formater);
    }

    public String getCompetitiveness() {
        return type.split("-")[1];
    }

    public String getGroupType() {
        return type.split("-")[0];
    }

    public int getFirstScoreFromResult() {
        String result = getResult();
        int firstScore = 0;

        for(int i = 0; i < result.length() && result.charAt(i) != '-'; i++) {
            firstScore = firstScore * 10 + (result.charAt(i) - '0');
        }

        return firstScore;
    }

    public int getSecondScoreFromResult() {
        String result = getResult();
        int position = result.indexOf('-');
        int secondScore = 0;

        for(int i = position + 1; i < result.length(); i++) {
            secondScore = secondScore * 10 + (result.charAt(i) - '0');
        }

        return secondScore;
    }

    public String getKey() {
        GameKey gameKey = new GameKey(getStartTime(), getTeam1().getName(), getFinishTime());
        return gameKey.toString();
    }
}
