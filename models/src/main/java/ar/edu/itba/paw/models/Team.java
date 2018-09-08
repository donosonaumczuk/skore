package ar.edu.itba.paw.models;

import java.util.LinkedList;
import java.util.List;

public class Team {
    private List<User> players;
    private PremiumUser leader;
    private String acronym;
    private String name;
    private boolean isTemp;
    private Sport sport;

    public Team(PremiumUser leader, String acronym, String name, boolean isTemp, Sport sport) {
        this.leader = leader;
        this.acronym = acronym;
        this.name = name;
        this.isTemp = isTemp;
        this.sport = sport;
        this.players = new LinkedList<>();
        this.players.add(leader);
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

    public void setName(String newName) {
        name = newName;
    }

    public String getAcronym() {
        return acronym;
    }

    public void setAcronym(String newAcronym) {
        acronym = newAcronym;
    }

    public PremiumUser getLeader() {
        return leader;
    }

    public void setLeader(PremiumUser newLeader) {
       leader = newLeader;
    }

    public List<User> getPlayers() {
        return players;
    }

    public void setPlayers(List<User> players) {
        this.players = players;
    }
}
