package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.GameDao;
import ar.edu.itba.paw.interfaces.PremiumUserService;
import ar.edu.itba.paw.interfaces.SessionService;
import ar.edu.itba.paw.interfaces.TeamService;
import ar.edu.itba.paw.models.*;
import ar.edu.itba.paw.services.GameServiceImpl;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import static org.mockito.Mockito.when;


@SuppressWarnings("Duplicates")
@RunWith(MockitoJUnitRunner.class)
@Configuration
public class GameServiceImplTest {
    private static final String  TEAMNAME_1          = "teamname1";
    private static final String  TEAMNAME_2          = "teamname2";
    private static final String  STARTTIME_1         = "2018-12-12T00:00";
    private static final String  FINISHTIME_1        = "2018-12-13T00:00";
    private static final String  URL                 = "201812120000teamname1201812130000";

    private static final String  SPORTNAME           = "sPoRtNaMe";
    private static final int     SPORTQUANTITY       = 10;
    private static final String  SPORTDISPLAYNAME    = "sportname";

    private static final String  LEADER_1_FIRSTNAME  = "firstname1";
    private static final String  LEADER_1_LASTNAME   = "lastname1";
    private static final String  LEADER_1_EMAIL      = "email1";
    private static final long    LEADER_1_USERID     = 1;
    private static final String  LEADER_1_USERNAME   = "username1";

    private static final String  TEAM_1_ACRONYM      = "acronym1";
    private static final boolean TEAM_1_ISTEMP       =  true;

    private static final String  USER_1_FIRSTNAME    = "userfirstname1";
    private static final String  USER_1_LASTNAME     = "userlastname1";
    private static final String  USER_1_EMAIL        = "useremail1";
    private static final long    USER_1_ID           = 3;

    private static final String  LEADER_2_FIRSTNAME  = "firstname2";
    private static final String  LEADER_2_LASTNAME   = "lastname2";
    private static final String  LEADER_2_EMAIL      = "email2";
    private static final long    LEADER_2_USERID     = 2;
    private static final String  LEADER_2_USERNAME   = "username2";

    private static final String  TEAM_2_ACRONYM      = "acronym2";
    private static final boolean TEAM_2_ISTEMP       =  true;

    private static final String  USER_2_FIRSTNAME    = "userfirstname2";
    private static final String  USER_2_LASTNAME     = "userlastname2";
    private static final String  USER_2_EMAIL        = "useremail2";
    private static final long    USER_2_ID           = 4;

    private static final long    USER_3_ID           = 9;

    private static Game GAME_1;

    @Mock
    private GameDao gameDaoMock;

    @Mock
    private TeamService teamServiceMock;

    @Mock
    private PremiumUserService premiumUserService;

    @Mock
    private SessionService sessionService;

    @InjectMocks
    private GameServiceImpl gameService;

    private PremiumUser leaderTeam1;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        Sport sport = new Sport(SPORTNAME, SPORTQUANTITY, SPORTDISPLAYNAME, null);

        leaderTeam1 = new PremiumUser(LEADER_1_FIRSTNAME, LEADER_1_LASTNAME, LEADER_1_EMAIL,
                                                    LEADER_1_USERNAME);
        Team team1 = new Team(leaderTeam1, TEAM_1_ACRONYM, TEAMNAME_1, TEAM_1_ISTEMP, sport, null);
        team1.addPlayer(leaderTeam1.getUser());
        team1.addPlayer(new User(USER_1_FIRSTNAME, USER_1_LASTNAME, USER_1_EMAIL, USER_1_ID));

        PremiumUser leaderTeam2 = new PremiumUser(LEADER_2_FIRSTNAME, LEADER_2_LASTNAME, LEADER_2_EMAIL,
                                                    LEADER_2_USERNAME);
        Team team2 = new Team(leaderTeam1, TEAM_2_ACRONYM, TEAMNAME_2, TEAM_2_ISTEMP, sport, null);
        team2.addPlayer(leaderTeam2.getUser());
        team2.addPlayer(new User(USER_2_FIRSTNAME, USER_2_LASTNAME, USER_2_EMAIL, USER_2_ID));

        GAME_1 = new Game(team1, team2, null, LocalDateTime.parse(STARTTIME_1), LocalDateTime.parse(FINISHTIME_1),
                null, null, null, null, null);
    }

    @Test
    public void findByUrlTest() {
        when(gameDaoMock.findByKey(GAME_1.getTeam1().getName(), GAME_1.getStartTime(), GAME_1.getFinishTime()))
                .thenReturn(Optional.of(GAME_1));

        Game ans = gameService.findByKey(URL);

        Assert.assertEquals(GAME_1, ans);
    }

    @Test
    public void deleteAUserInAGameTeam1Test() {
        when(gameDaoMock.findByKey(GAME_1.getTeam1().getName(), GAME_1.getStartTime(), GAME_1.getFinishTime()))
                .thenReturn(Optional.of(GAME_1));
        when(teamServiceMock.removePlayer(TEAMNAME_1, USER_1_ID)).thenReturn(null);
        when(premiumUserService.findById(USER_1_ID)).thenReturn(Optional.ofNullable(GAME_1.getTeam1().getLeader()));
        when(sessionService.getLoggedUser()).thenReturn(Optional.of(leaderTeam1));

        boolean ans = gameService.deleteUserInGameById(URL, USER_1_ID);

        Assert.assertTrue(ans);
    }

    @Test
    public void deleteAUserInAGameTeam2Test() {
        when(gameDaoMock.findByKey(GAME_1.getTeam1().getName(), GAME_1.getStartTime(), GAME_1.getFinishTime()))
                .thenReturn(Optional.of(GAME_1));
        when(teamServiceMock.removePlayer(TEAMNAME_2, USER_2_ID)).thenReturn(null);
        when(premiumUserService.findById(USER_2_ID)).thenReturn(Optional.ofNullable(GAME_1.getTeam2().getLeader()));
        when(sessionService.getLoggedUser()).thenReturn(Optional.of(leaderTeam1));

        boolean ans = gameService.deleteUserInGameById(URL, USER_2_ID);

        Assert.assertTrue(ans);
    }

    @Test
    public void deleteAUserThatIsNotInTheGame() {
        when(gameDaoMock.findByKey(GAME_1.getTeam1().getName(), GAME_1.getStartTime(), GAME_1.getFinishTime()))
                .thenReturn(Optional.of(GAME_1));
        when(sessionService.getLoggedUser()).thenReturn(Optional.of(leaderTeam1));

        boolean ans = gameService.deleteUserInGameById(URL, USER_3_ID);

        Assert.assertFalse(ans);
    }
}
