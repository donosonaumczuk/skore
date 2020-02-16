package ar.edu.itba.paw.validators;

import ar.edu.itba.paw.models.PremiumUser;
import ar.edu.itba.paw.webapp.exceptions.ApiException;
import ar.edu.itba.paw.webapp.utils.JSONUtils;
import ar.edu.itba.paw.webapp.validators.UserValidators;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.Optional;

public class UserValidatorsTest {

    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();

    @Test
    public void whenValidatingUpdateIfHasAllKnownAndRequiredFieldsWithValidFormatThenSuccessByDoingNothing() {
        UserValidators.updateValidatorOf("log").validate(JSONUtils.jsonObjectFrom(
                "{\n" +
                    "\t\"password\" : \"myNewPassword\",\n" +
                    "\t\"oldPassword\" : \"myOldPassword\",\n" +
                    "\t\"email\" : \"an@email.com\",\n" +
                    "\t\"home\" : {\n" +
                    "\t\t\"state\" : \"a state\",\n" +
                    "\t\t\"city\" : \"a city\",\n" +
                    "\t\t\"country\" : \"a country\",\n" +
                    "\t\t\"street\" : \"a street\"\n" +
                    "\t}\n" +
                "}"
                )
        );
    }

    @Test
    public void whenValidatingUpdateIfHasAnUnknownUpdateFieldThenThrowApiExceptionWithExpectedValues() {
        exceptionRule.expect(ApiException.class);
        exceptionRule.expectMessage("Field 'unknown' is unknown or unaccepted");
        UserValidators.updateValidatorOf("log").validate(JSONUtils.jsonObjectFrom(
                "{\n" +
                    "\t\"email\" : \"an@email.com\",\n" +
                    "\t\"home\" : {\n" +
                    "\t\t\"state\" : \"a state\",\n" +
                    "\t\t\"city\" : \"a city\",\n" +
                    "\t\t\"country\" : \"a country\",\n" +
                    "\t\t\"street\" : \"a street\"\n" +
                    "\t},\n" +
                    "\t\"unknown\" : \"whatever value\"\n" +
                "}"
                )
        );
    }

    @Test
    public void whenValidatingUpdateIfHasNoFieldsThenSuccessByDoingNothingBecauseOfNoRequiredFieldsSetUp() {
        UserValidators.updateValidatorOf("log").validate(JSONUtils.jsonObjectFrom("{ }"));
    }

    @Test
    public void whenValidatingCreationIfHasAllKnownAndRequiredFieldsWithValidFormatThenSuccessByDoingNothing() {
        UserValidators.creationValidatorOf("log").validate(JSONUtils.jsonObjectFrom(
                "{\n" +
                    "\t\"username\" : \"a_Username_69\",\n" +
                    "\t\"firstName\" : \"a first name\",\n" +
                    "\t\"lastName\" : \"a last name\",\n" +
                    "\t\"cellphone\" : \"1512345678\",\n" +
                    "\t\"birthday\" : {\n" +
                    "\t\t\"year\": 1969,\n" +
                    "\t\t\"monthNumber\": 06,\n" +
                    "\t\t\"dayOfMonth\": 25\n" +
                    "\t},\n" +
                    "\t\"email\" : \"an@email.com\",\n" +
                    "\t\"home\" : {\n" +
                    "\t\t\"state\" : \"a state\",\n" +
                    "\t\t\"city\" : \"a city\",\n" +
                    "\t\t\"country\" : \"a country\",\n" +
                    "\t\t\"street\" : \"a street\"\n" +
                    "\t},\n" +
                    "\t\"password\" : \"aPassword\",\n" +
                    "}"
                )
        );
    }

    @Test
    public void whenValidatingCreationIfHasAnUnknownCreationFieldThenThrowApiExceptionWithExpectedValues() {
        exceptionRule.expect(ApiException.class);
        exceptionRule.expectMessage("Field 'unknown' is unknown or unaccepted");
        UserValidators.creationValidatorOf("log").validate(JSONUtils.jsonObjectFrom(
                "{\n" +
                    "\t\"email\" : \"an@email.com\",\n" +
                    "\t\"home\" : {\n" +
                    "\t\t\"state\" : \"a state\",\n" +
                    "\t\t\"city\" : \"a city\",\n" +
                    "\t\t\"country\" : \"a country\",\n" +
                    "\t\t\"street\" : \"a street\"\n" +
                    "\t},\n" +
                    "\t\"unknown\" : \"whatever value\"\n" +
                "}"
                )
        );
    }

    @Test
    public void whenValidatingCreationIfARequiredCreationFieldIsMissingThenThrowApiExceptionWithExpectedValues() {
        exceptionRule.expect(ApiException.class);
        exceptionRule.expectMessage("Missing required 'username' field");
        UserValidators.creationValidatorOf("log").validate(JSONUtils.jsonObjectFrom(
                "{\n" +
                    "\t\"firstName\" : \"a first name\",\n" +
                    "\t\"lastName\" : \"a last name\",\n" +
                    "\t\"birthday\" : {\n" +
                    "\t\t\"year\": 1969,\n" +
                    "\t\t\"monthNumber\": 06,\n" +
                    "\t\t\"dayOfMonth\": 25\n" +
                    "\t},\n" +
                    "\t\"email\" : \"an@email.com\"\n" +
                "}"
                )
        );
    }

    @Test
    public void whenValidatingCreationIfAFieldHasInvalidValueThenThrowApiExceptionWithExpectedValues() {
        exceptionRule.expect(ApiException.class);
        exceptionRule.expectMessage("Field 'birthday' must be a JSON object");
        UserValidators.creationValidatorOf("log").validate(JSONUtils.jsonObjectFrom(
                "{\n" +
                    "\t\"username\" : \"a_Username_69\",\n" +
                    "\t\"firstName\" : \"a first name\",\n" +
                    "\t\"lastName\" : \"a last name\",\n" +
                    "\t\"cellphone\" : \"1512345678\",\n" +
                    "\t\"birthday\" : \"this is not a date!\",\n" +
                    "\t\"email\" : \"an@email.com\",\n" +
                    "\t\"home\" : {\n" +
                    "\t\t\"state\" : \"a state\",\n" +
                    "\t\t\"city\" : \"a city\",\n" +
                    "\t\t\"country\" : \"a country\",\n" +
                    "\t\t\"street\" : \"a street\"\n" +
                    "\t},\n" +
                    "\t\"password\" : \"aPassword\",\n" +
                    "}"
                )
        );
    }
}
