package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.TeamDao;
import ar.edu.itba.paw.models.PremiumUser;
import ar.edu.itba.paw.models.Sport;
import ar.edu.itba.paw.models.Team;
import ar.edu.itba.paw.models.User;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
@Sql("classpath:schema.sql")
@Transactional
public class TeamHibernateDaoTest {

    @Autowired
    private TeamDao teamDao;

    @PersistenceContext
    private EntityManager em;

    private final List<Team> teams;

    private final Team team;

    private final Team teamNotInserted;

    private final Sport sport;

    private final PremiumUser account;

    private final User user;

    public TeamHibernateDaoTest() {
        sport           = new Sport("baloncesto", 5, "Baloncesto", null);
        user            = new User("Agustin", "Dammiano", "dammiano@gmail.com", 0);
        account         = new PremiumUser("Agustin", "Dammiano", "dammiano98@itba.edu.ar",
                            "dammiano98", "92262123", LocalDate.parse("1998-06-05"),
                            null, 50,"321dammiano_aguistin123", "admin", null);
        team            = new Team(account, "C.A.R.", "Club Atletico River", false, sport,
                            null);
        team.addPlayer(account.getUser());
        teamNotInserted = new Team(account, "C.A.B.", "Club Atletico Boca", false, sport,
                            null);
        teamNotInserted.addPlayer(account.getUser());
        teams           = new ArrayList<>();
        teams.add(team);
        Team aux        = new Team(account, "C.A.I.", "Club Atletico Independiente", false,
                            sport, null);
        aux.addPlayer(account.getUser());
        teams.add(aux);
    }

    @Before
    public void initializeDatabase() {
        for (Team t: teams) {
            em.persist(t.getSport());
            em.persist(t.getLeader().getUser());
            em.persist(t.getLeader());
            for (User p: t.getPlayers()) {
                em.persist(p);
            }
            em.persist(t);
        }
        em.persist(user);
        em.flush();
    }

    @After
    public void removeAllData() {
        em.createNativeQuery("delete from teams");
        em.createNativeQuery("delete from sports");
        em.createNativeQuery("delete from accounts");
        em.createNativeQuery("delete from users");
//        em.flush();
    }

    @Test
    public void testCreate() throws IOException {

        //exercise class
        final Team teamReturn = teamDao.create(teamNotInserted.getLeader().getUserName(),
                teamNotInserted.getLeader().getUser().getUserId(), teamNotInserted.getAcronym(),
                teamNotInserted.getName(), teamNotInserted.isTemporal(),
                teamNotInserted.getSport().getName(), null).get();

        //postconditions
        Assert.assertEquals(teamNotInserted, teamReturn);
    }

    @Test
    public void testFindByTeamName() {

        //exercise class
        final Optional<Team> returnedTeam = teamDao.findByTeamName(team.getName());

        //postconditions
        Assert.assertTrue(returnedTeam.isPresent());
        Assert.assertEquals(team, returnedTeam.get());
    }

    @Test
    public void testRemoveTeam(){

        //exercise class
        boolean returnValue = teamDao.remove(team.getName());

        //postconditions
        Assert.assertEquals(true, returnValue);
        Assert.assertEquals(null, em.find(Team.class, team.getName()));
    }

    @Test
    public void testUpdateTeamInfo() {

        //exercise class
        final Optional<Team> returnedTeam = teamDao.updateTeamInfo(teamNotInserted.getName(),
                team.getAcronym(), team.getLeader().getUserName(), team.getSport().getName(),
                team.getName());

        //postconditions
        Assert.assertTrue(returnedTeam.isPresent());
        Assert.assertEquals(teamNotInserted.getName(), returnedTeam.get().getName());
        Assert.assertEquals(team.getLeader().getUserName(), returnedTeam.get().getLeader().getUserName());
        Assert.assertEquals(team.getSport().getName(), returnedTeam.get().getSport().getName());
    }

    @Test
    public void testAddPlayer() {

        final Optional<Team> returnedTeam = teamDao.addPlayer(team.getName(), user.getUserId());

        Assert.assertTrue(returnedTeam.isPresent());
        Assert.assertEquals(team.getName(), returnedTeam.get().getName());
        Assert.assertEquals(2, returnedTeam.get().getPlayers().size());
        Assert.assertEquals(true, returnedTeam.get().getPlayers().contains(user));
    }

    @Test
    public void testReamovePlayer() {

        final Optional<Team> returnedTeam = teamDao.removePlayer(team.getName(), account.getUser().getUserId());

        Assert.assertTrue(returnedTeam.isPresent());
        Assert.assertEquals(team, returnedTeam.get());
        Assert.assertEquals(0, returnedTeam.get().getPlayers().size());
    }
}
