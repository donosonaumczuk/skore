package ar.edu.itba.paw.services;

import ar.edu.itba.paw.Exceptions.TeamNotFoundException;
import ar.edu.itba.paw.interfaces.TeamDao;
import ar.edu.itba.paw.interfaces.TeamService;
import ar.edu.itba.paw.models.Team;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

public class TeamServiceImpl implements TeamService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TeamServiceImpl.class);


    @Autowired
    private TeamDao teamDao;

    public TeamServiceImpl() {

    }

    @Override
    public Team findByTeamName(final String teamName) {
        Optional<Team> team = teamDao.findByTeamName(teamName);
        if(!team.isPresent()) {
            LOGGER.error("Could not find team: " + teamName);
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
            LOGGER.error("Could not create team: " + teamName);
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
            LOGGER.error("Could not add player: " + userId + " to team: " + teamName);
            throw new TeamNotFoundException("Team " + teamName + " does not exists");
        }
        return team.get();
    }

    @Override
    public Team removePlayer(final String teamName, final long userId) {
        Optional<Team> team = teamDao.removePlayer(teamName, userId);
        if(!team.isPresent()) {
            LOGGER.error("Could not remove player: " + userId + " from team: " + teamName);
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
            LOGGER.error("Could not update team: " + oldTeamName);
            throw new TeamNotFoundException("Team " + newTeamName + " does not exists");
        }
        return team.get();
    }
}
