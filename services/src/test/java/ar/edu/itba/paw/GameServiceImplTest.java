package ar.edu.itba.paw;

import ar.edu.itba.paw.interfaces.GameDao;
import ar.edu.itba.paw.interfaces.TeamDao;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.when;


@RunWith(MockitoJUnitRunner.class)
@Configuration
public class GameServiceImplTest {
    private static final String  TEAMNAME_1          = "teamname1";
    private static final String  TEAMNAME_2          = "teamname2";
    private static final String  STARTTIME_1         = "2018-12-12 00:00:00";
    private static final String  FINISHTIME_1        = "2018-12-13 00:00:00";
    private static final int     QUANTITY_1          = 0;
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

    private static Optional<Game> GAME_1;

    @Mock
    private GameDao gameDaoMock;

    @Mock
    private TeamService teamServiceMock;

    @InjectMocks
    private GameServiceImpl gameService;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        DateTimeFormatter dateformat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        Sport sport = new Sport(SPORTNAME, SPORTQUANTITY, SPORTDISPLAYNAME, null);

        PremiumUser leaderTeam1 = new PremiumUser(LEADER_1_FIRSTNAME, LEADER_1_LASTNAME, LEADER_1_EMAIL,
                                                    LEADER_1_USERNAME);
        Team team1 = new Team(leaderTeam1, TEAM_1_ACRONYM, TEAMNAME_1, TEAM_1_ISTEMP, sport, null);
        team1.addPlayer(leaderTeam1.getUser());
        team1.addPlayer(new User(USER_1_FIRSTNAME, USER_1_LASTNAME, USER_1_EMAIL, USER_1_ID));

        PremiumUser leaderTeam2 = new PremiumUser(LEADER_2_FIRSTNAME, LEADER_2_LASTNAME, LEADER_2_EMAIL,
                                                    LEADER_2_USERNAME);
        Team team2 = new Team(leaderTeam1, TEAM_2_ACRONYM, TEAMNAME_2, TEAM_2_ISTEMP, sport, null);
        team2.addPlayer(leaderTeam2.getUser());
        team2.addPlayer(new User(USER_2_FIRSTNAME, USER_2_LASTNAME, USER_2_EMAIL, USER_2_ID));

        GAME_1 = Optional.of(new Game(team1, team2,
                 null, LocalDateTime.parse(STARTTIME_1, dateformat),
                LocalDateTime.parse(FINISHTIME_1, dateformat), null, null,
                null, null, null));
    }

    @Test
    public void findByUrlTest() {
        when(gameDaoMock.findByKey(TEAMNAME_1, STARTTIME_1, FINISHTIME_1)).thenReturn(GAME_1);

        Game ans = gameService.findByKeyFromURL(URL);

        Assert.assertEquals(GAME_1.get(), ans);
    }

    @Test
    public void deleteAUserInAGameTeam1Test() {
        when(gameDaoMock.findByKey(TEAMNAME_1, STARTTIME_1, FINISHTIME_1)).thenReturn(GAME_1);
        when(teamServiceMock.removePlayer(TEAMNAME_1, USER_1_ID)).thenReturn(null);

        Game ans = gameService.deleteUserInGame(TEAMNAME_1, STARTTIME_1, FINISHTIME_1, USER_1_ID);

        Assert.assertEquals(GAME_1.get(), ans);
    }

    @Test
    public void deleteAUserInAGameTeam2Test() {
        when(gameDaoMock.findByKey(TEAMNAME_1, STARTTIME_1, FINISHTIME_1)).thenReturn(GAME_1);
        when(teamServiceMock.removePlayer(TEAMNAME_2, USER_2_ID)).thenReturn(null);

        Game ans = gameService.deleteUserInGame(TEAMNAME_1, STARTTIME_1, FINISHTIME_1, USER_2_ID);

        Assert.assertEquals(GAME_1.get(), ans);
    }
}
