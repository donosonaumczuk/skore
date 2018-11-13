package ar.edu.itba.paw;

import ar.edu.itba.paw.Exceptions.CannotValidateUserException;
import ar.edu.itba.paw.interfaces.GameService;
import ar.edu.itba.paw.interfaces.PremiumUserDao;
import ar.edu.itba.paw.models.Game;
import ar.edu.itba.paw.models.PremiumUser;
import ar.edu.itba.paw.models.Team;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.services.PremiumUserServiceImpl;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.context.annotation.Configuration;

import java.util.*;

import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
@Configuration
public class PremiumUserServiceImplTest {
    private static final String USERNAME  = "username";
    private static final String CODE      = "code";
    private static final String URL       = "/confirm/username&code";
    private static final String LASTNAME  = "lastname";
    private static final String FIRSTNAME = "firstname";
    private static final String EMAIL     = "email";
    private static final long   ID        = 0;

    @Mock
    private PremiumUserDao premiumUserDaoMock;

    @Mock
    private GameService gameServiceMock;

    @InjectMocks
    private PremiumUserServiceImpl premiumUserService;

    @Test
    public void confirmationPathFinishTest() {
        List<List<Game>> list = new LinkedList<>();
        list.add(new LinkedList<>());
        list.add(new LinkedList<>());
        when(premiumUserDaoMock.enableUser(USERNAME, CODE)).thenReturn(true);
        when(premiumUserDaoMock.findByUserName(USERNAME))
                .thenReturn(Optional.of(new PremiumUser(FIRSTNAME, LASTNAME, EMAIL, USERNAME)));
        when(gameServiceMock.getGamesThatPlay(ID)).thenReturn(list);

        boolean ans = premiumUserService.confirmationPath(URL);

        Assert.assertEquals(true, ans);
    }

    @Test
    public void confirmationPathExceptionTest() {
        PremiumUser account = new PremiumUser(FIRSTNAME, LASTNAME, EMAIL, USERNAME);
        account.setUser(new User(FIRSTNAME, LASTNAME, EMAIL, ID));
        List<List<Game>> list = new LinkedList<>();
        list.add(new LinkedList<>());
        list.add(new LinkedList<>());
        when(premiumUserDaoMock.enableUser(USERNAME, CODE)).thenReturn(false);
        when(premiumUserDaoMock.findByUserName(USERNAME))
                .thenReturn(Optional.of(account));
        when(gameServiceMock.getGamesThatPlay(ID)).thenReturn(list);

        boolean ans = premiumUserService.confirmationPath(URL);


        Assert.assertEquals(false, ans);
    }

    @Test
    public void findByKeyTestWinAll() {
        PremiumUser account = new PremiumUser(FIRSTNAME, LASTNAME, EMAIL, USERNAME);
        account.setUser(new User(FIRSTNAME, LASTNAME, EMAIL, ID));
        LinkedHashSet<User> playerList = new LinkedHashSet<>();
        playerList.add(account.getUser());
        List<List<Game>> listofList = new LinkedList<>();
        List<Game> listTeam1 = new LinkedList<>();
        List<Game> listTeam2 = new LinkedList<>();
        Team team1 = new Team(null, null, null, false,
                null, null);
        team1.setPlayers(playerList);
        Team team2 = new Team(null, null, null, false,
                null, null);
        team1.setPlayers(new LinkedHashSet<>());
        listTeam1.add(new Game(team1, team2, null, null, null, null,
                "2-1", null, null, null));
        listTeam2.add(new Game(team2, team1, null, null, null, null,
                "1-10", null, null, null));
        listofList.add(listTeam1);
        listofList.add(listTeam2);
        when(premiumUserDaoMock.findByUserName(USERNAME))
                .thenReturn(Optional.of(account));
        when(gameServiceMock.getGamesThatPlay(ID)).thenReturn(listofList);

        Optional<PremiumUser> ans = premiumUserService.findByUserName(USERNAME);

        Assert.assertEquals(true, ans.isPresent());
        Assert.assertEquals(100, ans.get().getWinRate(), 0.00001);
    }

    @Test
    public void findByKeyTestWinAndLose() {
        PremiumUser account = new PremiumUser(FIRSTNAME, LASTNAME, EMAIL, USERNAME);
        account.setUser(new User(FIRSTNAME, LASTNAME, EMAIL, ID));
        LinkedHashSet<User> playerList = new LinkedHashSet<>();
        playerList.add(account.getUser());
        List<List<Game>> listofList = new LinkedList<>();
        List<Game> listTeam1 = new LinkedList<>();
        List<Game> listTeam2 = new LinkedList<>();
        Team team1 = new Team(null, null, null, false,
                null, null);
        team1.setPlayers(playerList);
        Team team2 = new Team(null, null, null, false,
                null, null);
        team1.setPlayers(new LinkedHashSet<>());
        listTeam1.add(new Game(team1, team2, null, null, null, null,
                "2-1", null, null, null));
        listTeam2.add(new Game(team2, team1, null, null, null, null,
                "1-0", null, null, null));
        listofList.add(listTeam1);
        listofList.add(listTeam2);
        when(premiumUserDaoMock.findByUserName(USERNAME))
                .thenReturn(Optional.of(account));
        when(gameServiceMock.getGamesThatPlay(ID)).thenReturn(listofList);

        Optional<PremiumUser> ans = premiumUserService.findByUserName(USERNAME);

        Assert.assertEquals(true, ans.isPresent());
        Assert.assertEquals(50, ans.get().getWinRate(), 0.00001);
    }

    @Test
    public void findByKeyTestLoseAll() {
        PremiumUser account = new PremiumUser(FIRSTNAME, LASTNAME, EMAIL, USERNAME);
        account.setUser(new User(FIRSTNAME, LASTNAME, EMAIL, ID));
        LinkedHashSet<User> playerList = new LinkedHashSet<>();
        playerList.add(account.getUser());
        List<List<Game>> listofList = new LinkedList<>();
        List<Game> listTeam1 = new LinkedList<>();
        List<Game> listTeam2 = new LinkedList<>();
        Team team1 = new Team(null, null, null, false,
                null, null);
        team1.setPlayers(playerList);
        Team team2 = new Team(null, null, null, false,
                null, null);
        team1.setPlayers(new LinkedHashSet<>());
        listTeam1.add(new Game(team1, team2, null, null, null, null,
                "2-5", null, null, null));
        listTeam2.add(new Game(team2, team1, null, null, null, null,
                "1-0", null, null, null));
        listofList.add(listTeam1);
        listofList.add(listTeam2);
        when(premiumUserDaoMock.findByUserName(USERNAME))
                .thenReturn(Optional.of(account));
        when(gameServiceMock.getGamesThatPlay(ID)).thenReturn(listofList);

        Optional<PremiumUser> ans = premiumUserService.findByUserName(USERNAME);

        Assert.assertEquals(true, ans.isPresent());
        Assert.assertEquals(0, ans.get().getWinRate(), 0.00001);
    }
}
