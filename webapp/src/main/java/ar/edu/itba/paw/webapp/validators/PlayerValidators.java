package ar.edu.itba.paw.webapp.validators;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import org.checkerframework.javacutil.Pair;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

public class PlayerValidators {//TODO reutilzar something

    private static final Logger LOGGER = LoggerFactory.getLogger(PlayerValidators.class);
    private static final String FIRST_NAME = "firstName";
    private static final String LAST_NAME  = "lastName";
    private static final String EMAIL      = "email";
    private static final String USER_ID    = "userId";

    private static final Set<String> CREATION_KNOWN_AND_REQUIRED_FIELDS = ImmutableSet.of(FIRST_NAME, LAST_NAME, EMAIL);
    private static final Set<String> UPDATE_KNOWN_FIELDS      = ImmutableSet.of(USER_ID);
    private static final Set<String> UPDATE_REQUIRED_FIELDS   = ImmutableSet.of(USER_ID);

    private static final String EMAIL_REGEX = "^[+a-zA-ZñÑ0-9_.-]+@[a-zA-Z0-9]+(\\.[A-Za-z]+)+$";
    private static final String EMAIL_PATTERN_DESCRIPTION = "an email";
    private static final String NAME_REGEX = "[a-zA-ZáéíóúñÁÉÍÓÚÑ ]+";
    private static final String NAME_PATTERN_DESCRIPTION = "a string containing english alphabetic characters, spaces or any of these characters: áéíóúñÁÉÍÓÚÑ";
    private static final int MIN_LENGTH_NAME = 2;
    private static final int MAX_LENGTH_NAME = 100;

    public static Validator<JSONObject> createValidatorOf(final String log) {
        return ValidatorFactory.jsonInputValidator(CREATION_KNOWN_AND_REQUIRED_FIELDS, CREATION_KNOWN_AND_REQUIRED_FIELDS,
                createFieldValidatorListOf(log), log);
    }

    public static Validator<JSONObject> updateValidatorOf(final String log) {
        return ValidatorFactory.jsonInputValidator(UPDATE_KNOWN_FIELDS,UPDATE_REQUIRED_FIELDS,
                updateFieldValidatorListOf(log), log);
    }

    private static List<Pair<String, Validator<JSONObject>>> updateFieldValidatorListOf(final String log) {
        return new ImmutableList.Builder<Pair<String, Validator<JSONObject>>>()
                .add(Pair.of(USER_ID, ValidatorFactory.fieldIsIntegerInRangeValidatorOf(USER_ID, 0, null, log)))
                .build();
    }

    private static Map<String, Validator<JSONObject>> createFieldValidatorListOf(final String log) {
        return new ImmutableMap.Builder<String, Validator<JSONObject>>()
                .put(EMAIL, ValidatorFactory.fieldIsStringAndMatchesRegexOf(EMAIL, Pattern.compile(EMAIL_REGEX),
                        EMAIL_PATTERN_DESCRIPTION, log))
                .put(FIRST_NAME, ValidatorFactory.fieldIsStringAndMatchesRegexOf(FIRST_NAME,
                        Pattern.compile(NAME_REGEX), NAME_PATTERN_DESCRIPTION, log).and(ValidatorFactory
                        .fieldIsStringWithLengthInRangeValidatorOf(FIRST_NAME, MIN_LENGTH_NAME, MAX_LENGTH_NAME, log)))
                .put(LAST_NAME,  ValidatorFactory.fieldIsStringAndMatchesRegexOf(LAST_NAME,
                        Pattern.compile(NAME_REGEX), NAME_PATTERN_DESCRIPTION, log).and(ValidatorFactory
                        .fieldIsStringWithLengthInRangeValidatorOf(LAST_NAME, MIN_LENGTH_NAME, MAX_LENGTH_NAME, log)))
                .build();
    }
}
