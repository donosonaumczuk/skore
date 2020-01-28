package ar.edu.itba.paw.webapp.validators;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import org.checkerframework.javacutil.Pair;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

public class PlayerValidators {

    private static final Logger LOGGER = LoggerFactory.getLogger(PlayerValidators.class);
    private static final String FIRST_NAME = "firstName";
    private static final String LAST_NAME = "lastName";
    private static final String EMAIL = "email";
    private static final String USERNAME = "username";

    private static final Set<String> CREATION_KNOWN_FIELDS = ImmutableSet.of(USERNAME, FIRST_NAME, LAST_NAME, EMAIL);
    private static final Set<String> CREATION_REQUIRED_FIELDS = ImmutableSet.of();

    private static final String USERNAME_PATTERN = "[a-zA-Z0-9_]+";
    private static final String USERNAME_PATTERN_DESCRIPTION = "a string containing english alphabetic " +
            "characters, digits or underscore";
    private static final String EMAIL_PATTERN = ".*@.*"; //TODO: find an standard email pattern
    private static final String EMAIL_PATTERN_DESCRIPTION = "an email";

    public static Validator<JSONObject> creationValidatorOf(final String log) {
        return ValidatorFactory.jsonInputValidator(CREATION_KNOWN_FIELDS, CREATION_REQUIRED_FIELDS,
                creationFieldValidatorListOf(log), log);
    }

    private static List<Pair<String, Validator<JSONObject>>> creationFieldValidatorListOf(final String log) {
        return new ImmutableList.Builder<Pair<String, Validator<JSONObject>>>()
                .add(Pair.of(EMAIL, ValidatorFactory.fieldIsStringAndMatchesRegexOf(EMAIL, Pattern.compile(EMAIL_PATTERN),
                        EMAIL_PATTERN_DESCRIPTION, log)
                    .and(ValidatorFactory.requiredFieldsValidatorOf(ImmutableSet.of(LAST_NAME), log))))
                .add(Pair.of(FIRST_NAME, ValidatorFactory.fieldIsStringValidatorOf(FIRST_NAME, log)
                        .and(ValidatorFactory.requiredFieldsValidatorOf(ImmutableSet.of(EMAIL), log))))
                .add(Pair.of(LAST_NAME,  ValidatorFactory.fieldIsStringValidatorOf(LAST_NAME, log)
                        .and(ValidatorFactory.requiredFieldsValidatorOf(ImmutableSet.of(FIRST_NAME), log))))
                .add(Pair.of(USERNAME, ValidatorFactory.fieldIsStringAndMatchesRegexOf(USERNAME,
                        Pattern.compile(USERNAME_PATTERN), USERNAME_PATTERN_DESCRIPTION, log)
                        .and(ValidatorFactory.forbiddenFieldsValidatorOf(ImmutableSet.of(FIRST_NAME, LAST_NAME, EMAIL), log))))
                .build();
    }
}
