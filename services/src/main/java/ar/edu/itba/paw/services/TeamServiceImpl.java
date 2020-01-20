package ar.edu.itba.paw.services;

import ar.edu.itba.paw.exceptions.TeamNotCreated;
import ar.edu.itba.paw.exceptions.TeamNotFoundException;
import ar.edu.itba.paw.interfaces.PremiumUserService;
import ar.edu.itba.paw.interfaces.TeamDao;
import ar.edu.itba.paw.interfaces.TeamService;
import ar.edu.itba.paw.models.PremiumUser;
import ar.edu.itba.paw.models.Team;
import ar.edu.itba.paw.models.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Optional;

@Service
public class TeamServiceImpl implements TeamService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TeamServiceImpl.class);

    @Autowired
    private TeamDao teamDao;

    @Autowired
    private PremiumUserService premiumUserService;

    public TeamServiceImpl() {

    }

    @Override
    public Team findByTeamName(final String teamName) {
        Optional<Team> team = teamDao.findByTeamName(teamName);

        return team.orElseThrow(() -> new TeamNotFoundException("Team " + teamName + " does not exists"));
    }

    @Override
    public Team create(final String leaderName, final long leaderId,
                       final String acronym, final String teamName,
                       final boolean isTemp, final String sportName) {

        Optional<Team> team = Optional.empty();
        try {
            team = teamDao.create(leaderName, leaderId, acronym, teamName, isTemp,
                    sportName, null);

        }catch (IOException e) {
            LOGGER.error("image corrupted\n");
        }

        return team.orElseThrow(() -> new TeamNotFoundException("Team " + teamName + " does not exists"));

    }

    private Team createTempTeam(final String start, final String leaderName, final long leaderId,
                                final String sportName) {
        boolean aux = true;
        int i = 0;
        Team team = null;
        while(aux && i < 50) {
            aux = false;
            try {
                 team = create(leaderName, leaderId, null,
                        start + leaderId + LocalDateTime.now().hashCode() + i,
                        true, sportName);
            } catch (Exception e) {
                aux = true;
            }
            i++;
        }
        if(aux) {
            throw new TeamNotCreated("Could not generate team");
        }
        return team;
    }

    @Override
    public Team createTempTeam1(final String leaderName, final long leaderId, final String sportName) {
        return createTempTeam("1.", leaderName, leaderId, sportName);
    }

    @Override
    public Team createTempTeam2(final String leaderName, final long leaderId, final String sportName) {
        return createTempTeam("2.", leaderName, leaderId, sportName);
    }

    @Override
    public boolean remove(final String teamName) {
        return teamDao.remove(teamName);
    }

    @Override
    public Team addPlayer(final String teamName, final long userId) {
        Optional<Team> team = teamDao.addPlayer(teamName, userId);

        return team.orElseThrow(() -> new TeamNotFoundException("Team " + teamName + " does not exists"));
    }

    @Override
    public Team removePlayer(final String teamName, final long userId) {
        Optional<Team> team = teamDao.removePlayer(teamName, userId);

        return team.orElseThrow(() -> new TeamNotFoundException("Team " + teamName + " does not exists"));
    }

    @Override
    public Team updateTeamInfo(final String newTeamName, final String newAcronym,
                               final String newLeaderName, final String newSportName,
                               final String oldTeamName) {
        Optional<Team> team = teamDao.updateTeamInfo(newTeamName, newAcronym, newLeaderName,
                newSportName, oldTeamName);

        return team.orElseThrow(() -> new TeamNotFoundException("Team " + newTeamName + " does not exists"));
    }


    @Override
    public void getAccountsList(Team team) {
        if(team != null) {
            HashMap<User, PremiumUser> accountsList = new HashMap<>();
            for (User u:team.getPlayers()) {
                Optional<PremiumUser> account = premiumUserService.findById(u.getUserId());
                accountsList.put(u, (account.isPresent())?account.get():null);
            }
            team.setAccountsPlayers(accountsList);
        }
    }
}
