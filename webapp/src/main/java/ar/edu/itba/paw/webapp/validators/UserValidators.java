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
    private static final String FIRST_NAME = "firstName";
    private static final String LAST_NAME = "lastName";
    private static final String EMAIL = "email";
    private static final String CELLPHONE = "cellphone";
    private static final String BIRTHDAY = "birthday";
    private static final String HOME = "home";
    private static final String PASSWORD = "password";
    private static final String IMAGE = "image";
    private static final Set<String> CREATION_KNOWN_FIELDS = ImmutableSet.of(FIRST_NAME, LAST_NAME, EMAIL,
            CELLPHONE, BIRTHDAY, HOME);
    private static final Set<String> CREATION_REQUIRED_FIELDS = ImmutableSet.of(FIRST_NAME, LAST_NAME, EMAIL,
            CELLPHONE, BIRTHDAY, HOME);
    private static final Set<String> UPGRADE_KNOWN_FIELDS = ImmutableSet.of(FIRST_NAME, LAST_NAME, EMAIL,
            CELLPHONE, BIRTHDAY, HOME, PASSWORD, IMAGE);
    private static final Set<String> UPGRADE_REQUIRED_FIELDS = ImmutableSet.of();

    public static Validator<Optional<PremiumUser>> existenceValidatorOf(final String username, final String log) {
        return ValidatorFactory.existenceValidatorOf("User", username, log);
    }

    public static Validator<JSONObject> creationValidatorOf(final String log) {
        return ValidatorFactory.jsonInputValidator(CREATION_KNOWN_FIELDS, CREATION_REQUIRED_FIELDS,
                creationFieldValidatorMapOf(log), log);
    }

    public static Validator<JSONObject> upgradeValidatorOf(final String log) {
        return ValidatorFactory.jsonInputValidator(UPGRADE_KNOWN_FIELDS, UPGRADE_REQUIRED_FIELDS,
                upgradeFieldValidatorMapOf(log), log);
    }

    public static Validator<Optional<PremiumUser>> isAuthorizedForUpgradeValidatorOf(final String username,
                                                                                     final String log) {
        return loggedUserOptional -> {
            if (!loggedUserOptional.isPresent() || !loggedUserOptional.get().getUserName().equals(username)) {
                LOGGER.error(log);
                throw new ApiException(HttpStatus.FORBIDDEN, "Only '" + username + "' can update his own user");
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
                        Pattern.compile("([0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9])|( *)"),
                        "a cellphone number", log)) //TODO: maybe exists an standard pattern for cellphone
                .put(BIRTHDAY, ValidatorFactory.fieldIsStringAndMatchesRegexOf(BIRTHDAY,
                        Pattern.compile("[0-9][0-9]/[0-9][0-9]/[0-9][0-9][0-9][0-9]"), "a date", log)) //TODO: maybe exists an standard pattern for this date format
                .put(PASSWORD, ValidatorFactory.fieldIsStringValidatorOf(PASSWORD, log))
                .put(IMAGE, ValidatorFactory.fieldIsStringValidatorOf(IMAGE, log)); //TODO: call the ImageValidator ?
    }

    private static Map<String, Validator<JSONObject>> upgradeFieldValidatorMapOf(final String log) {
        return baseFieldValidatorMapOf(log).put(HOME,
                ValidatorFactory.fieldIsValidObjectValidatorOf(HOME, HomeValidators.upgradeValidatorOf(log), log))
                .build();
    }

    private static Map<String, Validator<JSONObject>> creationFieldValidatorMapOf(final String log) {
        return baseFieldValidatorMapOf(log).put(HOME,
                ValidatorFactory.fieldIsValidObjectValidatorOf(HOME, HomeValidators.creationValidatorOf(log), log))
                .build();
    }
}