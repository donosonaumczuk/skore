package ar.edu.itba.paw.webapp.validators;

import ar.edu.itba.paw.models.PremiumUser;
import ar.edu.itba.paw.webapp.exceptions.ApiException;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;

import java.util.Map;
import java.util.Optional;
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

    public static Validator<Optional<PremiumUser>> existenceValidatorOf(final String username, final String log) {
        return ValidatorFactory.existenceValidatorOf("User", username, log);
    }

    public static Validator<JSONObject> creationValidatorOf(final String log) {
        return ValidatorFactory.jsonInputValidator(CREATION_KNOWN_FIELDS, CREATION_REQUIRED_FIELDS,
                creationFieldValidatorMapOf(log), log);
    }

    public static Validator<JSONObject> updateValidatorOf(final String log) {
        return ValidatorFactory.jsonInputValidator(UPDATE_KNOWN_FIELDS, UPDATE_REQUIRED_FIELDS,
                updateFieldValidatorMapOf(log), log).and(passwordUpdateValidatorOf(log));
    }

    public static Validator<Optional<PremiumUser>> isAuthorizedForUpdateValidatorOf(final String username,
                                                                                     final String log) {
        return loggedUserOptional -> {
            if (!loggedUserOptional.isPresent() || !loggedUserOptional.get().getUserName().equals(username)) {
                LOGGER.error(log);
                throw new ApiException(HttpStatus.FORBIDDEN, "Only '" + username + "' can update his own user");
            }
        };
    }

    private static Validator<JSONObject> passwordUpdateValidatorOf(final String log) {
        return jsonObject -> {
            if (jsonObject.has(PASSWORD) && !jsonObject.has(OLD_PASSWORD)) {
                LOGGER.error(log);
                throw new ApiException(HttpStatus.BAD_REQUEST, "To update '" + PASSWORD + "' field you must " +
                        "provide an '" + OLD_PASSWORD + "' field with the old password");
            }
            else if (!jsonObject.has(PASSWORD)) {
                //TODO: replace the following line with a call to ValidatorFactory::forbiddenValidatorOf over OLD_PASSWORD
                if (jsonObject.has(OLD_PASSWORD)) throw new ApiException(HttpStatus.BAD_REQUEST, "This is for testing!");
            }
        };
    }

    private static ImmutableMap.Builder<String, Validator<JSONObject>> baseFieldValidatorMapOf(final String log) {
        return new ImmutableMap.Builder<String, Validator<JSONObject>>()
                .put(EMAIL, ValidatorFactory.fieldIsStringAndMatchesRegexOf(EMAIL, Pattern.compile(".*@.*"),
                        "an email", log)) //TODO: find an standard email pattern
                .put(FIRST_NAME, ValidatorFactory.fieldIsStringValidatorOf(FIRST_NAME, log))
                .put(LAST_NAME,  ValidatorFactory.fieldIsStringValidatorOf(LAST_NAME, log))
                .put(CELLPHONE, ValidatorFactory.fieldIsStringAndMatchesRegexOf(CELLPHONE,
                        Pattern.compile("([0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9])|( *)"), //TODO: add a better regex
                        "a cellphone number", log)) //TODO: maybe exists an standard pattern for cellphone
                .put(BIRTHDAY, ValidatorFactory.fieldIsStringAndMatchesRegexOf(BIRTHDAY,
                        Pattern.compile("[0-9][0-9][0-9][0-9]-[0-9][0-9]-[0-9][0-9]"), "a date", log)) //TODO: maybe exists an standard pattern for this date format
                .put(PASSWORD, ValidatorFactory.fieldIsStringValidatorOf(PASSWORD, log))
                .put(IMAGE, ValidatorFactory.fieldIsStringValidatorOf(IMAGE, log)); //TODO: call the ImageValidator ?
    }

    private static Map<String, Validator<JSONObject>> updateFieldValidatorMapOf(final String log) {
        return baseFieldValidatorMapOf(log).put(HOME,
                ValidatorFactory.fieldIsValidObjectValidatorOf(HOME, HomeValidators.updateValidatorOf(log), log))
                .put(OLD_PASSWORD, ValidatorFactory.fieldIsStringValidatorOf(OLD_PASSWORD, log))
                .build();
    }

    private static Map<String, Validator<JSONObject>> creationFieldValidatorMapOf(final String log) {
        return baseFieldValidatorMapOf(log)
                .put(USERNAME, ValidatorFactory.fieldIsStringAndMatchesRegexOf(USERNAME, Pattern.compile("[a-zA-Z0-9_]+"),
                        "a string containing english alphabetic characters, digits or underscore", log))
                .put(HOME,
                ValidatorFactory.fieldIsValidObjectValidatorOf(HOME, HomeValidators.creationValidatorOf(log), log))
                .build();
    }
}