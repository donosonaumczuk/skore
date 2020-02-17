package ar.edu.itba.paw.services;

import ar.edu.itba.paw.exceptions.UnauthorizedException;
import ar.edu.itba.paw.exceptions.notfound.GameNotFoundException;
import ar.edu.itba.paw.exceptions.notfound.PlayerNotFoundException;
import ar.edu.itba.paw.interfaces.GameDao;
import ar.edu.itba.paw.interfaces.PremiumUserService;
import ar.edu.itba.paw.interfaces.SessionService;
import ar.edu.itba.paw.interfaces.TeamService;
import ar.edu.itba.paw.interfaces.UserService;
import ar.edu.itba.paw.models.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashSet;
import java.util.Optional;

import static ar.edu.itba.paw.models.GameType.FRIENDLY;
import static ar.edu.itba.paw.models.GameType.INDIVIDUAL;
import static org.mockito.Mockito.when;


@SuppressWarnings("Duplicates")
@RunWith(MockitoJUnitRunner.class)
@Configuration
public class GameServiceImplTest {
    private static final String  TEAMNAME_1          = "teamname1";
    private static final String  TEAMNAME_2          = "teamname2";
    private static final String  STARTTIME_1         = LocalDateTime.now().plusMinutes(60).toString().substring(0,16);
    private static final String  FINISHTIME_1        = LocalDateTime.now().plusMinutes(80).toString().substring(0,16);
    private static final String  STARTTIME_PAST      = LocalDateTime.now().minusMinutes(60).toString().substring(0,16);
    private static final String  FINISHTIME_PAST     = LocalDateTime.now().minusMinutes(40).toString().substring(0,16);

    private static final String  SPORTNAME           = "sPoRtNaMe";
    private static final int     SPORTQUANTITY       = 10;
    private static final String  SPORTDISPLAYNAME    = "sportname";

    private static final String  LEADER_1_FIRSTNAME  = "firstname1";
    private static final String  LEADER_1_LASTNAME   = "lastname1";
    private static final String  LEADER_1_EMAIL      = "email1";
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
    private static final String  LEADER_2_USERNAME   = "username2";

    private static final String  TEAM_2_ACRONYM      = "acronym2";
    private static final boolean TEAM_2_ISTEMP       =  true;

    private static final String  USER_2_FIRSTNAME    = "userfirstname2";
    private static final String  USER_2_LASTNAME     = "userlastname2";
    private static final String  USER_2_EMAIL        = "useremail2";
    private static final long    USER_2_ID           = 4;

    private static final long    USER_3_ID           = 9;
    private static final String NOT_INSERTED_FIRSTNAME = "notInsertedFirstName";
    private static final String NOT_INSERTED_LASTNAME  = "notInsertedLastName";
    private static final String NOT_INSERTED_EMAIL     = "notInsertedEmail";
    private static final String NOT_INSERTED_USERNAME  = "notInsertedUsername";
    private static final long USER_4_ID = 10;
    private static final long USER_5_ID = 11;
    private static final String CODE = "code";

    @Mock
    private UserService userServiceMock;

    @Mock
    private GameDao gameDaoMock;

    @Mock
    private TeamService teamServiceMock;

    @Mock
    private PremiumUserService premiumUserServiceMock;

    @Mock
    private SessionService sessionServiceMock;

    @InjectMocks
    private GameServiceImpl gameService;

    private PremiumUser leaderTeam1;

    private PremiumUser notInsertedUser;

    private Game game1;

    private Game game2;

    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        Sport sport = new Sport(SPORTNAME, SPORTQUANTITY, SPORTDISPLAYNAME, null);
        Sport sport2 = new Sport(SPORTNAME, 2, SPORTDISPLAYNAME, null);


        leaderTeam1 = new PremiumUser(LEADER_1_FIRSTNAME, LEADER_1_LASTNAME, LEADER_1_EMAIL,
                                                    LEADER_1_USERNAME, USER_3_ID);
        Team team1 = new Team(leaderTeam1, TEAM_1_ACRONYM, TEAMNAME_1, TEAM_1_ISTEMP, sport, null);
        team1.addPlayer(leaderTeam1.getUser());
        team1.addPlayer(new User(USER_1_FIRSTNAME, USER_1_LASTNAME, USER_1_EMAIL, USER_1_ID));

        PremiumUser leaderTeam2 = new PremiumUser(LEADER_2_FIRSTNAME, LEADER_2_LASTNAME, LEADER_2_EMAIL,
                                                    LEADER_2_USERNAME, USER_4_ID);
        Team team2 = new Team(leaderTeam1, TEAM_2_ACRONYM, TEAMNAME_2, TEAM_2_ISTEMP, sport, null);
        team2.addPlayer(leaderTeam2.getUser());
        team2.addPlayer(new User(USER_2_FIRSTNAME, USER_2_LASTNAME, USER_2_EMAIL, USER_2_ID));

        Team team3 = new Team(leaderTeam1, TEAM_1_ACRONYM, TEAMNAME_1, TEAM_1_ISTEMP, sport2, null);
        team3.addPlayer(leaderTeam1.getUser());
        team3.addPlayer(new User(USER_1_FIRSTNAME, USER_1_LASTNAME, USER_1_EMAIL, USER_1_ID));

        Team team4 = new Team(leaderTeam1, TEAM_2_ACRONYM, TEAMNAME_2, TEAM_2_ISTEMP, sport2, null);
        team4.addPlayer(leaderTeam2.getUser());
        team4.addPlayer(new User(USER_2_FIRSTNAME, USER_2_LASTNAME, USER_2_EMAIL, USER_2_ID));

        notInsertedUser = new PremiumUser(NOT_INSERTED_FIRSTNAME, NOT_INSERTED_LASTNAME, NOT_INSERTED_EMAIL,
                NOT_INSERTED_USERNAME, USER_5_ID);

        game1 = new Game(team1, team2, new Place("country", "state", "city", "street"),
                LocalDateTime.parse(STARTTIME_1), LocalDateTime.parse(FINISHTIME_1),
                INDIVIDUAL.toString() + '-' + FRIENDLY.toString(), null,
                null, null, null);
        game2 = new Game(team3, team4, new Place("country", "state", "city", "street"),
                LocalDateTime.parse(STARTTIME_PAST), LocalDateTime.parse(FINISHTIME_PAST),
                INDIVIDUAL.toString() + '-' + FRIENDLY.toString(), null,
                null, null, null);
    }

    @Test
    public void findByUrlTest() {
        when(gameDaoMock.findByKey(game1.getTeam1().getName(), game1.getStartTime(), game1.getFinishTime()))
                .thenReturn(Optional.of(game1));

        Optional<Game> ans = gameService.findByKey(game1.getKey());

        Assert.assertTrue(ans.isPresent());
        Assert.assertEquals(game1, ans.get());
    }

    @Test
    public void findByUrlTestButItDoesNotExist() {
        when(gameDaoMock.findByKey(game1.getTeam1().getName(), game1.getStartTime(), game1.getFinishTime()))
                .thenReturn(Optional.empty());

        Optional<Game> ans = gameService.findByKey(game1.getKey());

        Assert.assertFalse(ans.isPresent());
    }

    @Test
    public void deleteAUserInAGameTeam1Test() {
        when(gameDaoMock.findByKey(game1.getTeam1().getName(), game1.getStartTime(), game1.getFinishTime()))
                .thenReturn(Optional.of(game1));
        when(teamServiceMock.removePlayer(TEAMNAME_1, USER_1_ID)).thenReturn(null);
        when(premiumUserServiceMock.findById(USER_1_ID)).thenReturn(Optional.ofNullable(game1.getTeam1().getLeader()));
        when(sessionServiceMock.getLoggedUser()).thenReturn(Optional.of(game1.getTeam1().getLeader()));

        gameService.deleteUserInGameWithCode(game1.getKey(), USER_1_ID, null);

        //No exception
    }

    @Test
    public void deleteAUserInAGameTeam2Test() {
        when(gameDaoMock.findByKey(game1.getTeam1().getName(), game1.getStartTime(), game1.getFinishTime()))
                .thenReturn(Optional.of(game1));
        when(teamServiceMock.removePlayer(TEAMNAME_2, USER_2_ID)).thenReturn(null);
        when(premiumUserServiceMock.findById(USER_2_ID)).thenReturn(Optional.ofNullable(game1.getTeam2().getLeader()));
        when(sessionServiceMock.getLoggedUser()).thenReturn(Optional.of(leaderTeam1));

        gameService.deleteUserInGameWithCode(game1.getKey(), USER_2_ID, null);

        //No exception
    }

    @Test
    public void deleteAUserThatIsNotInTheGame() {
        exceptionRule.expect(PlayerNotFoundException.class);
        exceptionRule.expectMessage("No Player found with id '" + 80 + "'");

        when(gameDaoMock.findByKey(game1.getTeam1().getName(), game1.getStartTime(), game1.getFinishTime()))
                .thenReturn(Optional.of(game1));
        when(sessionServiceMock.getLoggedUser()).thenReturn(Optional.of(leaderTeam1));

        gameService.deleteUserInGameWithCode(game1.getKey(), 80, null);
    }

    @Test
    public void createGame() {
        game1.getTeam1().setPlayers(new HashSet<>());
        game1.getTeam2().setPlayers(new HashSet<>());
        when(sessionServiceMock.getLoggedUser()).thenReturn(Optional.of(leaderTeam1));
        when(teamServiceMock.createTempTeam1(leaderTeam1.getUserName(), leaderTeam1.getUser().getUserId(),
                game1.getTeam1().getSport().getName())).thenReturn(game1.getTeam1());
        when(teamServiceMock.createTempTeam2(leaderTeam1.getUserName(), leaderTeam1.getUser().getUserId(),
                game1.getTeam1().getSport().getName())).thenReturn(game1.getTeam2());
        when(gameDaoMock.create(game1.getTeam1().getName(), game1.getTeam2().getName(), game1.getStartTime(),
                game1.getFinishTime(), game1.getType(), null,
                game1.getPlace().getCountry(), game1.getPlace().getState(), game1.getPlace().getCity(),
                game1.getPlace().getStreet(), game1.getTornament(), game1.getDescription(), game1.getTitle()))
                .thenReturn(Optional.of(game1));
        when(teamServiceMock.addPlayer(game1.team1Name(), leaderTeam1.getUser().getUserId()))
                .thenReturn(game1.getTeam1());

        Game gameReturn = gameService.create(null, null, game1.getStartTime(),
                ChronoUnit.MINUTES.between(game1.getStartTime(), game1.getFinishTime()), false,
                true, game1.getPlace().getCountry(), game1.getPlace().getState(), game1.getPlace().getCity(),
                game1.getPlace().getStreet(), game1.getTornament(), game1.getDescription(), game1.getTitle(),
                game1.getTeam1().getSport().getName());

        Assert.assertEquals(gameReturn, game1);
    }

    @Test
    public void modifyGame() {
        when(gameDaoMock.findByKey(game1.getTeam1().getName(), game1.getStartTime(), game1.getFinishTime()))
                .thenReturn(Optional.of(game1));
        when(sessionServiceMock.getLoggedUser()).thenReturn(Optional.of(leaderTeam1));
        when(gameDaoMock.modify(null, null, game1.getStartTime(),
                game1.getFinishTime(), game1.getType(), null, game1.getPlace().getCountry(),
                game1.getPlace().getState(), game1.getPlace().getCity(), game1.getPlace().getStreet(),
                game1.getTornament(), game1.getDescription(), game1.getTitle(), game1.getTeam1().getName(),
                game1.getStartTime(), game1.getFinishTime()))
                .thenReturn(Optional.of(game1));

        Game gameReturn = gameService.modify(null, null, game1.getStartTime(),
                ChronoUnit.MINUTES.between(game1.getStartTime(), game1.getFinishTime()), game1.getType(),
                null, game1.getPlace().getCountry(), game1.getPlace().getState(), game1.getPlace().getCity(),
                game1.getPlace().getStreet(), game1.getTornament(), game1.getDescription(), game1.getTitle(),
                game1.getKey());

        Assert.assertEquals(gameReturn, game1);
    }

    @Test
    public void modifyGameButItDoesNotExist() {
        exceptionRule.expect(GameNotFoundException.class);
        exceptionRule.expectMessage("Match '" + game1.getKey() + "' not found");
        when(gameDaoMock.findByKey(game1.getTeam1().getName(), game1.getStartTime(), game1.getFinishTime()))
                .thenReturn(Optional.empty());

        Game gameReturn = gameService.modify(null, null, game1.getStartTime(),
                ChronoUnit.MINUTES.between(game1.getStartTime(), game1.getFinishTime()), game1.getType(),
                null, game1.getPlace().getCountry(), game1.getPlace().getState(), game1.getPlace().getCity(),
                game1.getPlace().getStreet(), game1.getTornament(), game1.getDescription(), game1.getTitle(),
                game1.getKey());

        Assert.assertEquals(gameReturn, game1);
    }

    @Test
    public void modifyGameButUserIsNotLogged() {
        exceptionRule.expect(UnauthorizedException.class);
        exceptionRule.expectMessage("Must be logged to update match");
        when(gameDaoMock.findByKey(game1.getTeam1().getName(), game1.getStartTime(), game1.getFinishTime()))
                .thenReturn(Optional.of(game1));
        when(sessionServiceMock.getLoggedUser()).thenReturn(Optional.empty());

        Game gameReturn = gameService.modify(null, null, game1.getStartTime(),
                ChronoUnit.MINUTES.between(game1.getStartTime(), game1.getFinishTime()), game1.getType(),
                null, game1.getPlace().getCountry(), game1.getPlace().getState(), game1.getPlace().getCity(),
                game1.getPlace().getStreet(), game1.getTornament(), game1.getDescription(), game1.getTitle(),
                game1.getKey());

        Assert.assertEquals(gameReturn, game1);
    }

    @Test
    public void removeGame() {
        when(gameDaoMock.findByKey(game1.getTeam1().getName(), game1.getStartTime(), game1.getFinishTime()))
                .thenReturn(Optional.of(game1));
        when(sessionServiceMock.getLoggedUser()).thenReturn(Optional.of(leaderTeam1));
        when(gameDaoMock.remove(game1.getTeam1().getName(), game1.getStartTime(), game1.getFinishTime()))
                .thenReturn(true);

        gameService.remove(game1.getKey());
    }

    @Test
    public void removeGameButGameDoesNotExist() {
        exceptionRule.expect(GameNotFoundException.class);
        exceptionRule.expectMessage("Match '" + game1.getKey() + "' not found");
        when(gameDaoMock.findByKey(game1.getTeam1().getName(), game1.getStartTime(), game1.getFinishTime()))
                .thenReturn(Optional.empty());

        gameService.remove(game1.getKey());
    }

    @Test
    public void removeGameButUserIsNotLogged() {
        exceptionRule.expect(UnauthorizedException.class);
        exceptionRule.expectMessage("Must be logged to delete match");
        when(gameDaoMock.findByKey(game1.getTeam1().getName(), game1.getStartTime(), game1.getFinishTime()))
                .thenReturn(Optional.of(game1));
        when(sessionServiceMock.getLoggedUser()).thenReturn(Optional.empty());

        gameService.remove(game1.getKey());
    }

    @Test
    public void insertAPlayer() {
        Team team1WithLastInsertion = new Team(leaderTeam1, TEAM_1_ACRONYM, TEAMNAME_1, TEAM_1_ISTEMP,
                game1.getTeam1().getSport(), null);
        team1WithLastInsertion.addPlayer(leaderTeam1.getUser());
        team1WithLastInsertion.addPlayer(new User(USER_1_FIRSTNAME, USER_1_LASTNAME, USER_1_EMAIL, USER_1_ID));
        team1WithLastInsertion.addPlayer(notInsertedUser.getUser());
        when(sessionServiceMock.getLoggedUser()).thenReturn(Optional.of(notInsertedUser));
        when(gameDaoMock.findByKey(game1.getTeam1().getName(), game1.getStartTime(), game1.getFinishTime()))
                .thenReturn(Optional.of(game1));
        when(teamServiceMock.addPlayer(game1.team1Name(), notInsertedUser.getUser().getUserId()))
                .thenReturn(team1WithLastInsertion);

        Game gameReturn = gameService.insertPlayerInGame(game1.getKey(), notInsertedUser.getUser().getUserId(),
                null,  null);

        Assert.assertEquals(game1, gameReturn);
        Assert.assertTrue(game1.getTeam1().getPlayers().contains(notInsertedUser.getUser()));
    }

    @Test
    public void insertAPlayerTemporalUser() {
        Team team1WithLastInsertion = new Team(leaderTeam1, TEAM_1_ACRONYM, TEAMNAME_1, TEAM_1_ISTEMP,
                game1.getTeam1().getSport(), null);
        team1WithLastInsertion.addPlayer(leaderTeam1.getUser());
        team1WithLastInsertion.addPlayer(new User(USER_1_FIRSTNAME, USER_1_LASTNAME, USER_1_EMAIL, USER_1_ID));
        team1WithLastInsertion.addPlayer(notInsertedUser.getUser());
        when(gameDaoMock.findByKey(game1.getTeam1().getName(), game1.getStartTime(), game1.getFinishTime()))
                .thenReturn(Optional.of(game1));
        when(userServiceMock.getUserFromData(CODE, game1.getKey())).thenReturn(notInsertedUser.getUser());
        when(teamServiceMock.addPlayer(game1.team1Name(), notInsertedUser.getUser().getUserId()))
                .thenReturn(team1WithLastInsertion);

        Game gameReturn = gameService.insertPlayerInGame(game1.getKey(), notInsertedUser.getUser().getUserId(),
                CODE,  null);

        Assert.assertEquals(game1, gameReturn);
        Assert.assertTrue(game1.getTeam1().getPlayers().contains(notInsertedUser.getUser()));
    }

    @Test
    public void updateResult() {
        when(gameDaoMock.findByKey(game2.getTeam1().getName(), game2.getStartTime(), game2.getFinishTime()))
                .thenReturn(Optional.of(game2));
        when(sessionServiceMock.getLoggedUser()).thenReturn(Optional.of(leaderTeam1));

        Game gameReturn = gameService.updateResultOfGame(game2.getKey(), 1, 2);

        Assert.assertEquals(game2, gameReturn);
        Assert.assertEquals(game2.getResult(), "1-2");
    }

    @Test
    public void updateResultButItIsnOtLogged() {
        exceptionRule.expect(UnauthorizedException.class);
        exceptionRule.expectMessage("Must be logged to update match result");
        when(gameDaoMock.findByKey(game2.getTeam1().getName(), game2.getStartTime(), game2.getFinishTime()))
                .thenReturn(Optional.of(game2));
        when(sessionServiceMock.getLoggedUser()).thenReturn(Optional.empty());

        gameService.updateResultOfGame(game2.getKey(), 1, 2);
    }

    @Test
    public void updateResultButItGameIsNotFOund() {
        exceptionRule.expect(GameNotFoundException.class);
        exceptionRule.expectMessage("Match '" + game2.getKey() + "' not found");
        when(gameDaoMock.findByKey(game2.getTeam1().getName(), game2.getStartTime(), game2.getFinishTime()))
                .thenReturn(Optional.empty());

        gameService.updateResultOfGame(game2.getKey(), 1, 2);
    }
}
