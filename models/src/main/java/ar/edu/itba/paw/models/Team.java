package ar.edu.itba.paw.models;

import javax.persistence.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "teams")
public class Team {

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "isPartOf",
            joinColumns = { @JoinColumn(name = "teamName") },
            inverseJoinColumns = { @JoinColumn(name = "userId") }
    )
    private List<User> players;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "leaderName")
    private PremiumUser leader;

    @Column(length = 100)
    private String acronym;

    @Id
    @Column(length = 100)
    private String teamName;

    @Column(nullable = false)
    private boolean isTemp;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "sportName")
    private Sport sport;

    @Column
    private byte[] image;

    /* package */public Team() {
        // For Hibernate
    }

    public Team(PremiumUser leader, String acronym, String teamName, boolean isTemp, Sport sport, byte[] image) {
        this.leader     = leader;
        this.acronym    = acronym;
        this.teamName   = teamName;
        this.isTemp     = isTemp;
        this.sport      = sport;
        this.image      = image;
        this.players    = new LinkedList<>();
    }

    public Sport getSport() {
        return sport;
    }

    public boolean isTemporal() {
        return isTemp;
    }

    public String getName() {
        return teamName;
    }

    public void setName(final String newName) {
        teamName = newName;
    }

    public String getAcronym() {
        return acronym;
    }

    public void setAcronym(final String newAcronym) {
        acronym = newAcronym;
    }

    public PremiumUser getLeader() {
        return leader;
    }

    public List<User> getPlayers() {
        return players;
    }

    public void addPlayer(final User player) {
        players.add(player);
    }

    public boolean removePlayer(final long userId) {
        int i = 0;
        for (User u: players) {
            if(u.getUserId() == userId) {
                players.remove(i);
                return true;
            }
            i++;
        }
        return false;
    }

    public void setLeader(PremiumUser leader) {
        this.leader = leader;
    }

    public void setSport(Sport sport) {
        this.sport = sport;
    }

    @Override
    public boolean equals(Object object) {
        if(object == null || !object.getClass().equals(getClass())) {
            return false;
        }

        Team aTeam = ((Team) object);
        return getName().equals(aTeam.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(teamName);
    }
}
