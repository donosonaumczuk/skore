package ar.edu.itba.paw.validators;

import ar.edu.itba.paw.webapp.exceptions.ApiException;
import ar.edu.itba.paw.webapp.utils.JSONUtils;
import ar.edu.itba.paw.webapp.validators.ValidatorFactory;
import com.google.common.collect.ImmutableSet;
import org.json.JSONObject;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.Set;
import java.util.regex.Pattern;

//TODO: I prefer to read JSON strings from a file resources, improve this later
public class ValidatorFactoryTest {

    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();

    @Test
    public void whenValidatingKnownFieldsIfAnUnknownFieldsIsPresentThenSuccessByDoingNothing() {
        final Set<String> knownFields = ImmutableSet.of("knownField1", "knownField2");
        JSONObject jsonObject = JSONUtils.jsonObjectFrom(
                "{\n" +
                        "  \"knownField1\" : \"value\",\n" +
                        "  \"knownField2\" : \"value\",\n" +
                        "}"
        );
        ValidatorFactory.knownFieldsValidatorOf(knownFields, "log").validate(jsonObject);
    }

    @Test
    public void whenValidatingKnownFieldsIfAnUnknownFieldsIsPresentThenThrowApiExceptionWithExpectedValues() {
        exceptionRule.expect(ApiException.class);
        exceptionRule.expectMessage("Field 'unknownField' is unknown");
        final Set<String> knownFields = ImmutableSet.of("knownField1", "knownField2");
        JSONObject jsonObject = JSONUtils.jsonObjectFrom(
                "{\n" +
                        "  \"knownField1\" : \"value\",\n" +
                        "  \"knownField2\" : \"value\",\n" +
                        "  \"unknownField\" : \"value\"\n" +
                        "}"
        );
        ValidatorFactory.knownFieldsValidatorOf(knownFields, "log").validate(jsonObject);
    }

    @Test
    public void whenValidatingFieldIsStringIfFieldIsAStringThenSuccessByDoingNothing() {
        JSONObject jsonObject = JSONUtils.jsonObjectFrom("{ \"field\" : \"a string\" }");
        ValidatorFactory.fieldIsStringValidatorOf("field", "log").validate(jsonObject);
    }

    @Test
    public void whenValidatingFieldIsStringIfFieldIsNotAStringThenThrowApiExceptionWithExpectedValues() {
        exceptionRule.expect(ApiException.class);
        exceptionRule.expectMessage("Field 'field' must be a string");
        JSONObject jsonObject = JSONUtils.jsonObjectFrom("{ \"field\" : 1234 }");
        ValidatorFactory.fieldIsStringValidatorOf("field", "log").validate(jsonObject);
    }

    @Test
    public void whenValidatingFieldIsStringAndMatchesRegexIfFieldIsAStringAndMatchesRegexThenSuccessByDoingNothing() {
        JSONObject jsonObject = JSONUtils.jsonObjectFrom("{ \"field\" : \"lowercase\" }");
        ValidatorFactory.fieldIsStringAndMatchesRegexOf("field", Pattern.compile("[a-z]+"),
                "a lowercase non empty string", "log").validate(jsonObject);
    }

    @Test
    public void whenValidatingFieldIsStringAndMatchesRegexIfFieldIsNotAStringThenThrowApiExceptionWithExpectedValues() {
        exceptionRule.expect(ApiException.class);
        exceptionRule.expectMessage("Field 'field' must be a string");
        JSONObject jsonObject = JSONUtils.jsonObjectFrom("{ \"field\" : 1234 }");
        ValidatorFactory.fieldIsStringAndMatchesRegexOf("field", Pattern.compile("[a-z]+"),
                "a lowercase non empty string", "log").validate(jsonObject);
    }

    @Test
    public void whenValidatingFieldIsStringAndMatchesRegexIfFieldIsAStringButNotMatchesTheRegexThenThrowApiExceptionWithExpectedValues() {
        exceptionRule.expect(ApiException.class);
        exceptionRule.expectMessage("Field 'field' must be a lowercase non empty string");
        JSONObject jsonObject = JSONUtils.jsonObjectFrom("{ \"field\" : \"NOT ONLY lowercase\" }");
        ValidatorFactory.fieldIsStringAndMatchesRegexOf("field", Pattern.compile("[a-z]+"),
                "a lowercase non empty string", "log").validate(jsonObject);
    }
}
