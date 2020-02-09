package ar.edu.itba.paw.services;

import ar.edu.itba.paw.exceptions.InvalidUserCodeException;
import ar.edu.itba.paw.interfaces.GameService;
import ar.edu.itba.paw.interfaces.PremiumUserDao;
import ar.edu.itba.paw.models.Game;
import ar.edu.itba.paw.models.PremiumUser;
import ar.edu.itba.paw.models.Team;
import ar.edu.itba.paw.models.User;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.context.annotation.Configuration;

import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

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

    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();

    @Test
    public void findByKeyTestWinAll() {
        PremiumUser account = new PremiumUser(FIRSTNAME, LASTNAME, EMAIL, USERNAME);
        account.setUser(new User(FIRSTNAME, LASTNAME, EMAIL, ID));
        LinkedHashSet<User> playerList = new LinkedHashSet<>();
        playerList.add(account.getUser());
        List<List<Game>> listOfList = new LinkedList<>();
        List<Game> listTeam1 = new LinkedList<>();
        List<Game> listTeam2 = new LinkedList<>();
        Team team1 = new Team(null, null, null, false,
                null, null);
        team1.setPlayers(playerList);
        Team team2 = new Team(null, null, null, false,
                null, null);
        team1.setPlayers(new LinkedHashSet<>());
        listTeam1.add(new Game(team1, team2, null, null, null, "Individual-Competitive",
                "2-1", null, null, null));
        listTeam2.add(new Game(team2, team1, null, null, null, "Individual-Competitive",
                "1-10", null, null, null));
        listOfList.add(listTeam1);
        listOfList.add(listTeam2);
        when(premiumUserDaoMock.findByUserName(USERNAME))
                .thenReturn(Optional.of(account));
        when(gameServiceMock.getGamesThatPlay(ID)).thenReturn(listOfList);

        Optional<PremiumUser> ans = premiumUserService.findByUserName(USERNAME);

        Assert.assertTrue(ans.isPresent());
        Assert.assertEquals(100, ans.get().getWinRate(), 0.00001);
    }

    @Test
    public void findByKeyTestWinAndLose() {
        PremiumUser account = new PremiumUser(FIRSTNAME, LASTNAME, EMAIL, USERNAME);
        account.setUser(new User(FIRSTNAME, LASTNAME, EMAIL, ID));
        LinkedHashSet<User> playerList = new LinkedHashSet<>();
        playerList.add(account.getUser());
        List<List<Game>> listOfList = new LinkedList<>();
        List<Game> listTeam1 = new LinkedList<>();
        List<Game> listTeam2 = new LinkedList<>();
        Team team1 = new Team(null, null, null, false,
                null, null);
        team1.setPlayers(playerList);
        Team team2 = new Team(null, null, null, false,
                null, null);
        team1.setPlayers(new LinkedHashSet<>());
        listTeam1.add(new Game(team1, team2, null, null, null, "Individual-Competitive",
                "2-1", null, null, null));
        listTeam2.add(new Game(team2, team1, null, null, null, "Individual-Competitive",
                "1-0", null, null, null));
        listOfList.add(listTeam1);
        listOfList.add(listTeam2);
        when(premiumUserDaoMock.findByUserName(USERNAME))
                .thenReturn(Optional.of(account));
        when(gameServiceMock.getGamesThatPlay(ID)).thenReturn(listOfList);

        Optional<PremiumUser> ans = premiumUserService.findByUserName(USERNAME);

        Assert.assertTrue(ans.isPresent());
        Assert.assertEquals(50, ans.get().getWinRate(), 0.00001);
    }

    @Test
    public void findByKeyTestLoseAll() {
        PremiumUser account = new PremiumUser(FIRSTNAME, LASTNAME, EMAIL, USERNAME);
        account.setUser(new User(FIRSTNAME, LASTNAME, EMAIL, ID));
        LinkedHashSet<User> playerList = new LinkedHashSet<>();
        playerList.add(account.getUser());
        List<List<Game>> listOfList = new LinkedList<>();
        List<Game> listTeam1 = new LinkedList<>();
        List<Game> listTeam2 = new LinkedList<>();
        Team team1 = new Team(null, null, null, false,
                null, null);
        team1.setPlayers(playerList);
        Team team2 = new Team(null, null, null, false,
                null, null);
        team1.setPlayers(new LinkedHashSet<>());
        listTeam1.add(new Game(team1, team2, null, null, null, "Individual-Competitive",
                "2-5", null, null, null));
        listTeam2.add(new Game(team2, team1, null, null, null, "Individual-Competitive",
                "1-0", null, null, null));
        listOfList.add(listTeam1);
        listOfList.add(listTeam2);
        when(premiumUserDaoMock.findByUserName(USERNAME))
                .thenReturn(Optional.of(account));
        when(gameServiceMock.getGamesThatPlay(ID)).thenReturn(listOfList);

        Optional<PremiumUser> ans = premiumUserDaoMock.findByUserName(USERNAME);

        Assert.assertTrue(ans.isPresent());
        Assert.assertEquals(0, ans.get().getWinRate(), 0.00001);
    }

    @Test
    public void findByKeyTestLoseAndTie() {
        PremiumUser account = new PremiumUser(FIRSTNAME, LASTNAME, EMAIL, USERNAME);
        account.setUser(new User(FIRSTNAME, LASTNAME, EMAIL, ID));
        LinkedHashSet<User> playerList = new LinkedHashSet<>();
        playerList.add(account.getUser());
        List<List<Game>> listOfList = new LinkedList<>();
        List<Game> listTeam1 = new LinkedList<>();
        List<Game> listTeam2 = new LinkedList<>();
        Team team1 = new Team(null, null, null, false,
                null, null);
        team1.setPlayers(playerList);
        Team team2 = new Team(null, null, null, false,
                null, null);
        team1.setPlayers(new LinkedHashSet<>());
        listTeam1.add(new Game(team1, team2, null, null, null, "Individual-Competitive",
                "0-2", null, null, null));
        listTeam2.add(new Game(team2, team1, null, null, null, "Individual-Competitive",
                "1-1", null, null, null));
        listOfList.add(listTeam1);
        listOfList.add(listTeam2);
        when(premiumUserDaoMock.findByUserName(USERNAME))
                .thenReturn(Optional.of(account));
        when(gameServiceMock.getGamesThatPlay(ID)).thenReturn(listOfList);

        Optional<PremiumUser> ans = premiumUserService.findByUserName(USERNAME);

        Assert.assertTrue(ans.isPresent());
        Assert.assertEquals(25, ans.get().getWinRate(), 0.00001);
    }
}
