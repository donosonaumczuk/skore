package ar.edu.itba.paw.webapp.validators;

import ar.edu.itba.paw.webapp.exceptions.ApiException;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;

import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

public class UserValidators {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserValidators.class);
    private static final String USERNAME = "username";
    private static final String FIRST_NAME = "firstName";
    private static final String LAST_NAME = "lastName";
    private static final String EMAIL = "email";
    private static final String CELLPHONE = "cellphone";
    private static final String BIRTHDAY = "birthday";
    private static final String HOME = "home";
    private static final String PASSWORD = "password";
    private static final String OLD_PASSWORD = "oldPassword";
    private static final String IMAGE = "image";
    private static final Set<String> CREATION_KNOWN_FIELDS = ImmutableSet.of(USERNAME, FIRST_NAME, LAST_NAME, EMAIL,
            CELLPHONE, BIRTHDAY, HOME, PASSWORD, IMAGE);
    private static final Set<String> CREATION_REQUIRED_FIELDS = ImmutableSet.of(USERNAME, FIRST_NAME, LAST_NAME, EMAIL,
            BIRTHDAY, PASSWORD);
    private static final Set<String> UPDATE_KNOWN_FIELDS = ImmutableSet.of(FIRST_NAME, LAST_NAME, EMAIL,
            CELLPHONE, BIRTHDAY, HOME, PASSWORD, OLD_PASSWORD, IMAGE);
    private static final Set<String> UPDATE_REQUIRED_FIELDS = ImmutableSet.of();

    private static final String EMAIL_REGEX = "^[+a-zA-ZñÑ0-9_.-]+@[a-zA-Z0-9]+(\\.[A-Za-z]+)+$";
    private static final String EMAIL_PATTERN_DESCRIPTION = "an email";
    private static final String NAME_REGEX = "[a-zA-ZáéíóúñÁÉÍÓÚÑ ]+";
    private static final String NAME_PATTERN_DESCRIPTION = "a string containing english alphabetic characters, spaces or any of these characters: áéíóúñÁÉÍÓÚÑ";
    private static final int MIN_LENGTH_NAME = 2;
    private static final int MAX_LENGTH_NAME = 100;
    private static final String CELLPHONE_REGEX = "([0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9])|( *)"; //TODO: add a better regex
    private static final String CELLPHONE_PATTERN_DESCRIPTION = "a cellphone number";
    private static final int MIN_LENGTH_PASSWORD = 6;
    private static final int MAX_LENGTH_PASSWORD = 100;
    private static final String USERNAME_REGEX = "[a-zA-Z0-9_.]+";
    private static final String USERNAME_PATTERN_DESCRIPTION = "a string containing english alphabetic characters, digits or underscore";
    private static final int MIN_LENGTH_USERNAME = 4;
    private static final int MAX_LENGTH_USERNAME = 100;

    public static Validator<JSONObject> creationValidatorOf(final String log) {
        return ValidatorFactory.jsonInputValidator(CREATION_KNOWN_FIELDS, CREATION_REQUIRED_FIELDS,
                creationFieldValidatorMapOf(log), log);
    }

    public static Validator<JSONObject> updateValidatorOf(final String log) {
        return ValidatorFactory.jsonInputValidator(UPDATE_KNOWN_FIELDS, UPDATE_REQUIRED_FIELDS,
                updateFieldValidatorMapOf(log), log).and(passwordUpdateValidatorOf(log));
    }

    private static Validator<JSONObject> passwordUpdateValidatorOf(final String log) {
        return jsonObject -> {
            if (jsonObject.has(PASSWORD) && !jsonObject.has(OLD_PASSWORD)) {
                LOGGER.error(log);
                throw ApiException.of(HttpStatus.BAD_REQUEST, "To update '" + PASSWORD + "' field you must " +
                        "provide an '" + OLD_PASSWORD + "' field with the old password");
            }
            else if (!jsonObject.has(PASSWORD)) {
                ValidatorFactory.forbiddenFieldsValidatorOf(ImmutableSet.of(OLD_PASSWORD), log).validate(jsonObject);
            }
        };
    }

    private static ImmutableMap.Builder<String, Validator<JSONObject>> baseFieldValidatorMapOf(final String log) {
        return new ImmutableMap.Builder<String, Validator<JSONObject>>()
                .put(EMAIL, ValidatorFactory.fieldIsStringAndMatchesRegexOf(EMAIL, Pattern.compile(EMAIL_REGEX),
                        EMAIL_PATTERN_DESCRIPTION, log))
                .put(FIRST_NAME, ValidatorFactory.fieldIsStringAndMatchesRegexOf(FIRST_NAME,
                        Pattern.compile(NAME_REGEX), NAME_PATTERN_DESCRIPTION, log).and(ValidatorFactory
                        .fieldIsStringWithLengthInRangeValidatorOf(FIRST_NAME, MIN_LENGTH_NAME, MAX_LENGTH_NAME, log)))
                .put(LAST_NAME,  ValidatorFactory.fieldIsStringAndMatchesRegexOf(LAST_NAME,
                        Pattern.compile(NAME_REGEX), NAME_PATTERN_DESCRIPTION, log).and(ValidatorFactory
                        .fieldIsStringWithLengthInRangeValidatorOf(LAST_NAME, MIN_LENGTH_NAME, MAX_LENGTH_NAME, log)))
                .put(CELLPHONE, ValidatorFactory.fieldIsStringAndMatchesRegexOf(CELLPHONE,
                        Pattern.compile(CELLPHONE_REGEX), CELLPHONE_PATTERN_DESCRIPTION, log))
                .put(BIRTHDAY, ValidatorFactory.fieldIsValidObjectValidatorOf(BIRTHDAY,
                        DateValidators.creationValidatorOf(log), log))
                .put(PASSWORD, ValidatorFactory.fieldIsStringWithLengthInRangeValidatorOf(PASSWORD, MIN_LENGTH_PASSWORD,
                        MAX_LENGTH_PASSWORD, log))
                .put(IMAGE, ValidatorFactory.fieldIsStringValidatorOf(IMAGE, log));
    }

    private static Map<String, Validator<JSONObject>> updateFieldValidatorMapOf(final String log) {
        return baseFieldValidatorMapOf(log).put(HOME,
                ValidatorFactory.fieldIsValidObjectValidatorOf(HOME, HomeValidators.updateValidatorOf(log), log))
                .put(OLD_PASSWORD, ValidatorFactory.fieldIsStringWithLengthInRangeValidatorOf(OLD_PASSWORD, 6, 100, log))
                .build();
    }

    private static Map<String, Validator<JSONObject>> creationFieldValidatorMapOf(final String log) {
        return baseFieldValidatorMapOf(log)
                .put(USERNAME, ValidatorFactory.fieldIsStringAndMatchesRegexOf(USERNAME,
                        Pattern.compile(USERNAME_REGEX), USERNAME_PATTERN_DESCRIPTION, log)
                        .and(ValidatorFactory.fieldIsStringWithLengthInRangeValidatorOf(USERNAME, MIN_LENGTH_USERNAME,
                                MAX_LENGTH_USERNAME, log)))
                .put(HOME, ValidatorFactory
                        .fieldIsValidObjectValidatorOf(HOME, HomeValidators.creationValidatorOf(log), log))
                .build();
    }
}