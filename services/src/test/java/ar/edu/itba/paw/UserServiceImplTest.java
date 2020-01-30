package ar.edu.itba.paw;

import ar.edu.itba.paw.exceptions.UserNotFoundException;
import ar.edu.itba.paw.interfaces.UserDao;
import ar.edu.itba.paw.models.SimpleEncrypter;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.services.UserServiceImpl;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.context.annotation.Configuration;

import java.util.Optional;

import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
@Configuration
public class UserServiceImplTest {

    private static long ID           = 96;
    private static String FIRST_NAME = "Agustin";
    private static String GAME_KEY   = "201812120000teamname1201812130000";
    private static String CODE = new SimpleEncrypter().encryptString(ID + FIRST_NAME + "$" + GAME_KEY);
    private static String ANOTHER_GAME_KEY   = "201912120000XteamnameX1201912130000";
    private static String ANOTHER_FIRST_NAME = "Alan";

    @Mock
    private UserDao userDaoMock;

    @InjectMocks
    private UserServiceImpl userService;

    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();

    @Test
    public void getIdFromCode() {
        when(userDaoMock.findById(ID)).thenReturn(Optional.of(new User(FIRST_NAME, null, null)));

        long idReceive = userService.getUserIdFromData(CODE, GAME_KEY);

        Assert.assertEquals(ID, idReceive);
    }

    @Test
    public void getIdFromCodeButIdDoesNotExistInDataBase() {
        exceptionRule.expect(UserNotFoundException.class);
        exceptionRule.expectMessage("User with id: " + ID + " doesn't exist.");
        when(userDaoMock.findById(ID)).thenReturn(Optional.empty());

        userService.getUserIdFromData(CODE, GAME_KEY);
    }

    @Test
    public void getIdFromCodeButGameKeyIsDifferent() {
        exceptionRule.expect(IllegalArgumentException.class);
        exceptionRule.expectMessage("The code '" + CODE + "' is invalid");
        when(userDaoMock.findById(ID)).thenReturn(Optional.of(new User(FIRST_NAME, null, null)));

        userService.getUserIdFromData(CODE, ANOTHER_GAME_KEY);
    }

    @Test
    public void getIdFromCodeButFirstNameIsDifferent() {
        exceptionRule.expect(IllegalArgumentException.class);
        exceptionRule.expectMessage("The code '" + CODE + "' is invalid");
        when(userDaoMock.findById(ID)).thenReturn(Optional.of(new User(ANOTHER_FIRST_NAME, null, null)));

        userService.getUserIdFromData(CODE, GAME_KEY);
    }
}
