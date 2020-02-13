package ar.edu.itba.paw.validators;

import ar.edu.itba.paw.webapp.exceptions.ApiException;
import ar.edu.itba.paw.webapp.utils.JSONUtils;
import ar.edu.itba.paw.webapp.validators.SportValidators;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class SportValidatorsTest {

    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();

    @Test
    public void whenValidatingUpdateIfHasAllKnownAndRequiredFieldsWithValidFormatThenSuccessByDoingNothing() {
        SportValidators.updateValidatorOf("log").validate(JSONUtils.jsonObjectFrom(
                "{\n" +
                            "\t\"sportName\" : \"name_1\",\n" +
                            "\t\"displayName\" : \"NAME\",\n" +
                            "\t\"playerQuantity\" : 11,\n" +
                            "\t\"imageSport\" : \"a image\"\n" +
                        "}"
                )
        );
    }

    @Test
    public void whenValidatingUpdateIfHasAnUnknownUpdateFieldThenThrowApiExceptionWithExpectedValues() {
        exceptionRule.expect(ApiException.class);
        exceptionRule.expectMessage("Field 'unknown' is unknown or unaccepted");
        SportValidators.updateValidatorOf("log").validate(JSONUtils.jsonObjectFrom(
                "{\n" +
                            "\t\"sportName\" : \"name_1\",\n" +
                            "\t\"displayName\" : \"NAME\",\n" +
                            "\t\"playerQuantity\" : 11,\n" +
                            "\t\"unknown\" : \"whatever value\",\n" +
                            "\t\"imageSport\" : \"a image\"\n" +
                        "}"
                )
        );
    }

    @Test
    public void whenValidatingUpdateIfHasNoFieldsThenSuccessByDoingNothingBecauseOfNoRequiredFieldsSetUp() {
        SportValidators.updateValidatorOf("log").validate(JSONUtils.jsonObjectFrom("{ }"));
    }

    @Test
    public void whenValidatingCreationIfHasAllKnownAndRequiredFieldsWithValidFormatThenSuccessByDoingNothing() {
        SportValidators.creationValidatorOf("log").validate(JSONUtils.jsonObjectFrom(
                "{\n" +
                            "\t\"sportName\" : \"name_1\",\n" +
                            "\t\"displayName\" : \"NAME\",\n" +
                            "\t\"playerQuantity\" : 11,\n" +
                            "\t\"imageSport\" : \"a image\"\n" +
                        "}"
                )
        );
    }

    @Test
    public void whenValidatingCreationIfHasAnUnknownUpdateFieldThenThrowApiExceptionWithExpectedValues() {
        exceptionRule.expect(ApiException.class);
        exceptionRule.expectMessage("Field 'unknown' is unknown or unaccepted");
        SportValidators.updateValidatorOf("log").validate(JSONUtils.jsonObjectFrom(
                "{\n" +
                            "\t\"sportName\" : \"name_1\",\n" +
                            "\t\"displayName\" : \"NAME\",\n" +
                            "\t\"playerQuantity\" : 11,\n" +
                            "\t\"unknown\" : \"whatever value\",\n" +
                            "\t\"imageSport\" : \"a image\"\n" +
                        "}"
                )
        );
    }

    @Test
    public void whenValidatingCreationIfARequiredCreationFieldIsMissingThenThrowApiExceptionWithExpectedValues() {
        exceptionRule.expect(ApiException.class);
        exceptionRule.expectMessage("Missing required 'displayName' field");
        SportValidators.creationValidatorOf("log").validate(JSONUtils.jsonObjectFrom(
                "{\n" +
                            "\t\"sportName\" : \"name_1\",\n" +
                            "\t\"playerQuantity\" : 11,\n" +
                            "\t\"imageSport\" : \"a image\"\n" +
                        "}"
                )
        );
    }

    @Test
    public void whenValidatingCreationIfSportNameHasInvalidValueThenThrowApiExceptionWithExpectedValues() {
        exceptionRule.expect(ApiException.class);
        exceptionRule.expectMessage("Field 'sportName' must be a string containing english alphabetic " +
                "characters, digits or underscore");
        SportValidators.creationValidatorOf("log").validate(JSONUtils.jsonObjectFrom(
                "{\n" +
                        "\t\"sportName\" : \"name_}1\",\n" +
                        "\t\"displayName\" : \"NAME\",\n" +
                        "\t\"playerQuantity\" : 11,\n" +
                        "\t\"imageSport\" : \"a image\"\n" +
                        "}"
                )
        );
    }

    @Test
    public void whenValidatingCreationIfDisplayNameHasInvalidValueThenThrowApiExceptionWithExpectedValues() {
        exceptionRule.expect(ApiException.class);
        exceptionRule.expectMessage("Field 'displayName' must be a string containing english alphabetic " +
                "characters, space, digits or any of these characters: áéíóúñÁÉÍÓÚÑ");
        SportValidators.creationValidatorOf("log").validate(JSONUtils.jsonObjectFrom(
                "{\n" +
                        "\t\"sportName\" : \"name_1\",\n" +
                        "\t\"displayName\" : \"NAME{}\",\n" +
                        "\t\"playerQuantity\" : 11,\n" +
                        "\t\"imageSport\" : \"a image\"\n" +
                        "}"
                )
        );
    }

    @Test
    public void whenValidatingCreationIfQuantityHasInvalidValueThenThrowApiExceptionWithExpectedValues() {
        exceptionRule.expect(ApiException.class);
        exceptionRule.expectMessage("Field 'playerQuantity' must belong to [1,100]");
        SportValidators.creationValidatorOf("log").validate(JSONUtils.jsonObjectFrom(
                "{\n" +
                        "\t\"sportName\" : \"name_1\",\n" +
                        "\t\"displayName\" : \"NAME{}\",\n" +
                        "\t\"playerQuantity\" : 0,\n" +
                        "\t\"imageSport\" : \"a image\"\n" +
                        "}"
                )
        );
    }
}
