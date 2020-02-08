package ar.edu.itba.paw.services;

import ar.edu.itba.paw.exceptions.TemporalTeamNotCreatedException;
import ar.edu.itba.paw.exceptions.notfound.TeamNotFoundException;
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
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
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

    @Transactional
    @Override
    public Optional<Team> findByTeamName(final String teamName) {
        return teamDao.findByTeamName(teamName);
    }

    @Transactional
    @Override
    public Team create(final String leaderName, final long leaderId,
                       final String acronym, final String teamName,
                       final boolean isTemp, final String sportName) {
        Optional<Team> team;
        try {
            team = teamDao.create(leaderName, leaderId, acronym, teamName, isTemp,
                    sportName, null);
        } catch (IOException e) {
            //TODO: heeeeey! we must throw an special exception here (maybe IOException and map then to a 500 in controller?)
            //TODO: why image always null? why this exception if image is always null, why not remove the file param?
            LOGGER.error("Image corrupted\n");
            team = Optional.empty();
        }
        return team.orElseThrow(() -> TeamNotFoundException.ofId(teamName));
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
            throw new TemporalTeamNotCreatedException("Could not generate team");
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

    @Transactional
    @Override
    public boolean remove(final String teamName) {
        return teamDao.remove(teamName);
    }

    @Transactional
    @Override
    public Team addPlayer(final String teamName, final long userId) {
        return teamDao.addPlayer(teamName, userId)
                .orElseThrow(() -> TeamNotFoundException.ofId(teamName));
    }

    @Transactional
    @Override
    public Team removePlayer(final String teamName, final long userId) {
        return teamDao.removePlayer(teamName, userId)
                .orElseThrow(() -> TeamNotFoundException.ofId(teamName));
    }

    @Transactional
    @Override
    public Team updateTeamInfo(final String newTeamName, final String newAcronym,
                               final String newLeaderName, final String newSportName,
                               final String oldTeamName) {
        return teamDao.updateTeamInfo(newTeamName, newAcronym, newLeaderName,
                newSportName, oldTeamName).orElseThrow(() ->
                TeamNotFoundException.ofId(oldTeamName)
        );
    }

    @Transactional
    @Override
    public Map<User, PremiumUser> getAccountsMap(Team team) {
        Map<User, PremiumUser> accountsList = new HashMap<>();
        if (team != null) {
            for (User user : team.getPlayers()) {
                accountsList.put(user, premiumUserService.findById(user.getUserId()).orElse(null));
            }
        }
        return accountsList;
    }
}
