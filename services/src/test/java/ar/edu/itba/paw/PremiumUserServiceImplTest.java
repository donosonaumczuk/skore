package ar.edu.itba.paw;

import ar.edu.itba.paw.Exceptions.CannotValidateUserException;
import ar.edu.itba.paw.interfaces.PremiumUserDao;
import ar.edu.itba.paw.models.PremiumUser;
import ar.edu.itba.paw.services.PremiumUserServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.context.annotation.Configuration;

import java.util.Optional;

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

    @InjectMocks
    private PremiumUserServiceImpl premiumUserService;

    @Test
    public void confirmationPathFinishTest() {
        when(premiumUserDaoMock.enableUser(USERNAME, CODE)).thenReturn(true);
        when(premiumUserDaoMock.findByUserName(USERNAME))
                .thenReturn(Optional.of(new PremiumUser(FIRSTNAME, LASTNAME, EMAIL, USERNAME)));

        premiumUserService.confirmationPath(URL);

        inOrder(premiumUserDaoMock).verify(premiumUserDaoMock).enableUser(USERNAME, CODE);
    }

    @Test
    public void confirmationPathExceptionTest() {
        when(premiumUserDaoMock.enableUser(USERNAME, CODE)).thenReturn(false);
        when(premiumUserDaoMock.findByUserName(USERNAME))
                .thenReturn(Optional.of(new PremiumUser(FIRSTNAME, LASTNAME, EMAIL, USERNAME)));

        premiumUserService.confirmationPath(URL);

        inOrder(premiumUserDaoMock).verify(premiumUserDaoMock).enableUser(USERNAME, CODE);
    }
}
