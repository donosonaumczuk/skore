package ar.edu.itba.paw.services;

import ar.edu.itba.paw.Exceptions.TeamNotFoundException;
import ar.edu.itba.paw.interfaces.TeamDao;
import ar.edu.itba.paw.interfaces.TeamService;
import ar.edu.itba.paw.models.Team;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

public class TeamServiceImpl implements TeamService {

    @Autowired
    private TeamDao teamDao;

    public TeamServiceImpl() {

    }

    @Override
    public Team findByTeamName(final String teamName) {
        Optional<Team> team = teamDao.findByTeamName(teamName);
        if(!team.isPresent()) {
            throw new TeamNotFoundException("Team " + teamName + " does not exists");
        }
        return team.get();
    }

    @Override
    public Team create(final String leaderName, final long leaderId,
                       final String acronym, final String teamName,
                       final boolean isTemp, final String sportName) {
        Optional<Team> team = teamDao.create(leaderName, leaderId, acronym, teamName, isTemp,
            sportName);
        if(!team.isPresent()) {
            throw new TeamNotFoundException("Team " + teamName + " does not exists");
        }
        return team.get();
    }

    @Override
    public boolean remove(final String teamName) {
        return teamDao.remove(teamName);
    }

    @Override
    public Team addPlayer(final String teamName, final long userId) {
        Optional<Team> team = teamDao.addPlayer(teamName, userId);
        if(!team.isPresent()) {
            throw new TeamNotFoundException("Team " + teamName + " does not exists");
        }
        return team.get();
    }

    @Override
    public Team removePlayer(final String teamName, final long userId) {
        Optional<Team> team = teamDao.removePlayer(teamName, userId);
        if(!team.isPresent()) {
            throw new TeamNotFoundException("Team " + teamName + " does not exists");
        }
        return team.get();
    }

    @Override
    public Team updateTeamInfo(final String newTeamName, final String newAcronym,
                               final String newLeaderName, final String newSportName,
                               final String oldTeamName) {
        Optional<Team> team = teamDao.updateTeamInfo(newTeamName, newAcronym, newLeaderName,
                newSportName, oldTeamName);
        if(!team.isPresent()) {
            throw new TeamNotFoundException("Team " + newTeamName + " does not exists");
        }
        return team.get();
    }
}
