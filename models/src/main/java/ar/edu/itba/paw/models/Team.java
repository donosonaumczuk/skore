package ar.edu.itba.paw.models;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class Team {
    private List<User> players;
    private PremiumUser leader;
    private String acronym;
    private String name;
    private boolean isTemp;
    private Sport sport;

    public Team(PremiumUser leader, String acronym, String name, boolean isTemp, Sport sport) {
        this.leader     = leader;
        this.acronym    = acronym;
        this.name       = name;
        this.isTemp     = isTemp;
        this.sport      = sport;
        this.players    = new LinkedList<>();
    }

    public Team(String name, Sport sport) {
        this.name = name;
        this.sport = sport;
    }

    public Sport getSport() {
        return sport;
    }

    public boolean isTemporal() {
        return isTemp;
    }

    public String getName() {
        return name;
    }

    public void setName(final String newName) {
        name = newName;
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

    public void setLeader(final PremiumUser newLeader) {
       leader = newLeader;
    }

    public List<User> getPlayers() {
        return players;
    }

    public void setPlayers(final List<User> players) {
        this.players = players;
    }

    public void addPlayer(final User player) {
        players.add(player);
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
        return Objects.hash(name);
    }
}
