package ar.edu.itba.paw;

import ar.edu.itba.paw.interfaces.PremiumUserService;
import ar.edu.itba.paw.interfaces.TeamDao;
import ar.edu.itba.paw.models.PremiumUser;
import ar.edu.itba.paw.models.Team;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.services.TeamServiceImpl;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.context.annotation.Configuration;

import java.util.Optional;

import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
@Configuration
public class TeamServiceImplTest {
    private static final String USERNAME  = "username";
    private static final String LASTNAME  = "lastname";
    private static final String FIRSTNAME = "firstname";
    private static final String EMAIL     = "email";
    private static final long   ID_0      = 0;
    private static final long   ID_1      = 1;
    private static final long   ID_2      = 2;

    @Mock
    private PremiumUserService premiumUserServiceMock;

    @Mock
    private TeamDao teamDaoMock;

    @InjectMocks
    private TeamServiceImpl teamService;

    @Test
    public void getAccountsListTest() {
        Team team  = new Team(null, null, null, true, null, null);
        User user0 = new User(FIRSTNAME+ID_0, LASTNAME+ID_0, EMAIL+ID_0, ID_0);
        User user1 = new User(FIRSTNAME+ID_1, LASTNAME+ID_1, EMAIL+ID_1, ID_1);
        User user2 = new User(FIRSTNAME+ID_2, LASTNAME+ID_2, EMAIL+ID_2, ID_2);
        team.addPlayer(user0);
        team.addPlayer(user1);
        team.addPlayer(user2);
        PremiumUser account1 = new PremiumUser(FIRSTNAME+ID_1, LASTNAME+ID_1,
                                EMAIL+ID_1, USERNAME+ID_1);
        when(premiumUserServiceMock.findById(ID_0)).thenReturn(Optional.empty());
        when(premiumUserServiceMock.findById(ID_1)).thenReturn(Optional.of(account1));
        when(premiumUserServiceMock.findById(ID_2)).thenReturn(Optional.empty());

        teamService.getAccountsList(team);

        Assert.assertNotNull(team.getAccountsPlayers());
        Assert.assertEquals(3, team.getAccountsPlayers().size());
        Assert.assertEquals(true , team.getAccountsPlayers().containsKey(user0));
        Assert.assertEquals(null, team.getAccountsPlayers().get(user0));
        Assert.assertEquals(true , team.getAccountsPlayers().containsKey(user1));
        Assert.assertEquals(account1, team.getAccountsPlayers().get(user1));
        Assert.assertEquals(true , team.getAccountsPlayers().containsKey(user2));
        Assert.assertEquals(null, team.getAccountsPlayers().get(user2));
    }

    @Test
    public void getAccountsListWithOutPlayerTest() {
        Team team  = new Team(null, null, null, true, null, null);

        teamService.getAccountsList(team);

        Assert.assertNotNull(team.getAccountsPlayers());
        Assert.assertEquals(0, team.getAccountsPlayers().size());
    }

    @Test
    public void getAccountsListTeamNullTest() {
        Team team  = null;

        teamService.getAccountsList(team);

        Assert.assertNull(team);
    }
}
