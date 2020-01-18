package ar.edu.itba.paw.validators;

import ar.edu.itba.paw.webapp.exceptions.ApiException;
import ar.edu.itba.paw.webapp.validators.ValidatorFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableSet;
import org.json.JSONObject;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.IOException;
import java.util.Set;

public class ValidatorFactoryTest {

    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();

    @Test
    public void whenValidatingKnownFieldsIfAnUnknownFieldsIsPresentThenThrowApiExceptionWithExpectedValues()
            throws IOException {
        exceptionRule.expect(ApiException.class);
        exceptionRule.expectMessage("Field 'unknownField' is unknown");
        final Set<String> knownFields = ImmutableSet.of("knownField1", "knownField2");
        JSONObject jsonObject = new JSONObject(
                "{\n" +
                        "  \"knownField1\" : \"value\",\n" +
                        "  \"knownField2\" : \"value\",\n" +
                        "  \"unknownField\" : \"value\"\n" +
                        "}"
        ); //TODO: I prefer to read this from a file resource, improve this later
        ValidatorFactory.knownFieldsValidatorOf(knownFields, "log").validate(jsonObject);
    }
}
