package ar.edu.itba.paw.validators;

import ar.edu.itba.paw.models.PremiumUser;
import ar.edu.itba.paw.webapp.exceptions.ApiException;
import ar.edu.itba.paw.webapp.validators.UserValidators;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.Optional;

public class UserValidatorsTest {

    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();

    @Test
    public void whenValidatingExistenceIfOptionalIsEmptyThenThrowApiExceptionWithExpectedValues() {
        exceptionRule.expect(ApiException.class);
        exceptionRule.expectMessage("User 'username' not found");
        UserValidators.existenceValidatorOf("username", "log").validate(Optional.empty());
    }

    @Test
    public void whenValidatingExistenceIfOptionalIsPresentThenSuccessByDoingNothing() {
        UserValidators.existenceValidatorOf("username", "log").validate(Optional.of(new PremiumUser()));
    }
}
