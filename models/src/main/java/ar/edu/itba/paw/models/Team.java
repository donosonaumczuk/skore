package ar.edu.itba.paw.models;

import javax.persistence.*;
import java.util.*;

@Entity
@Table(name = "teams")
public class Team {

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "isPartOf",
            joinColumns = { @JoinColumn(name = "teamName", referencedColumnName = "teamName") },
            inverseJoinColumns = { @JoinColumn(name = "userId", referencedColumnName = "userId") }
    )
    private Set<User> players;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "leaderName")
    private PremiumUser leader;

    @Column(length = 100)
    private String acronym;

    @Id
    @Column(length = 100)
    private String teamName;

    @Column(nullable = false)
    private int isTemp;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "sportName")
    private Sport sport;

    @Column
    private byte[] image;

    /* package */public Team() {
        // For Hibernate
    }

    public Team(PremiumUser leader, String acronym, String teamName, boolean isTemp, Sport sport, byte[] image) {
        this.leader          = leader;
        this.acronym         = acronym;
        this.teamName        = teamName;
        this.isTemp          = (isTemp)?1:0;
        this.sport           = sport;
        this.image           = image;
        this.players         = new LinkedHashSet<>();
    }

    public Sport getSport() {
        return sport;
    }

    public boolean isTemporal() {
        return isTemp == 1;
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

    public Set<User> getPlayers() {
        return players;
    }

    public void addPlayer(final User player) {
        players.add(player);
    }

    public boolean removePlayer(final long userId) {
        for (User u: players) {
            if(u.getUserId() == userId) {
                players.remove(u);
                return true;
            }
        }
        return false;
    }

    public void setLeader(PremiumUser leader) {
        this.leader = leader;
    }

    public void setSport(Sport sport) {
        this.sport = sport;
    }

    public void setPlayers(Set<User> players) {
        this.players = players;
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
