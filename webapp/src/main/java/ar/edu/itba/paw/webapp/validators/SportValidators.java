package ar.edu.itba.paw.webapp.validators;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

public class SportValidators {

    private static final Logger LOGGER = LoggerFactory.getLogger(SportValidators.class);
    private static final String SPORT_NAME   = "sportName";
    private static final String QUANTITY     = "playerQuantity";
    private static final String DISPLAY_NAME = "displayName";
    private static final String IMAGE        = "imageSport";
    private static final Set<String> CREATION_KNOWN_FIELDS = ImmutableSet.of(SPORT_NAME, QUANTITY, DISPLAY_NAME, IMAGE);
    private static final Set<String> CREATION_REQUIRED_FIELDS = ImmutableSet.of(SPORT_NAME, QUANTITY, DISPLAY_NAME, IMAGE);
    private static final Set<String> UPDATE_KNOWN_FIELDS = ImmutableSet.of(SPORT_NAME, QUANTITY, DISPLAY_NAME, IMAGE);
    private static final Set<String> UPDATE_REQUIRED_FIELDS = ImmutableSet.of();
    private static final Set<String> LIKED_SPORT_KNOWN_FIELDS = ImmutableSet.of(SPORT_NAME);
    private static final Set<String> LIKED_SPORT_REQUIRED_FIELDS = ImmutableSet.of(SPORT_NAME);

    private static final int MIN_QUANTITY          = 1;
    private static final int MAX_QUANTITY          = 100;
    private static final int MIN_LENGTH_NAME       = 4;
    private static final int MAX_LENGTH_NAME       = 100;
    private static final String DISPLAY_NAME_REGEX = "[0-9a-zA-ZáéíóúñÁÉÍÓÚÑ ]+";
    private static final String DISPLAY_NAME_PATTER_DESCRIPTION = "a string containing english alphabetic characters, " +
            "space, digits or any of these characters: áéíóúñÁÉÍÓÚÑ";
    private static final String SPORT_NAME_REGEX   = "[a-zA-Z0-9_]+";
    private static final String SPORT_NAME_PATTERN_DESCRIPTION = "a string containing english alphabetic characters, " +
            "digits or underscore";

    public static Validator<JSONObject> creationValidatorOf(final String log) {
        return ValidatorFactory.jsonInputValidator(CREATION_KNOWN_FIELDS, CREATION_REQUIRED_FIELDS,
                creationFieldValidatorMapOf(log), log);
    }

    public static Validator<JSONObject> updateValidatorOf(final String log) {
        return ValidatorFactory.jsonInputValidator(UPDATE_KNOWN_FIELDS, UPDATE_REQUIRED_FIELDS,
                updateFieldValidatorMapOf(log), log);
    }

    public static Validator<JSONObject> likedSportCreationValidator(final String log) {
        return ValidatorFactory.jsonInputValidator(LIKED_SPORT_KNOWN_FIELDS, LIKED_SPORT_REQUIRED_FIELDS,
                likedSportCreationValidatorMapOf(log), log);
    }

    private static ImmutableMap.Builder<String, Validator<JSONObject>> baseFieldValidatorMapOf(final String log) {
        return new ImmutableMap.Builder<String, Validator<JSONObject>>()
                .put(QUANTITY, ValidatorFactory.fieldIsIntegerInRangeValidatorOf(QUANTITY, MIN_QUANTITY,
                        MAX_QUANTITY, log))
                .put(DISPLAY_NAME, ValidatorFactory.fieldIsStringAndMatchesRegexOf(DISPLAY_NAME,
                        Pattern.compile(DISPLAY_NAME_REGEX), DISPLAY_NAME_PATTER_DESCRIPTION, log)
                        .and(ValidatorFactory.fieldIsStringWithLengthInRangeValidatorOf(DISPLAY_NAME, MIN_LENGTH_NAME,
                                MAX_LENGTH_NAME, log)))
                .put(IMAGE, ValidatorFactory.fieldIsStringValidatorOf(IMAGE, log));
    }

    private static Map<String, Validator<JSONObject>> creationFieldValidatorMapOf(final String log) {
        return baseFieldValidatorMapOf(log)
                .put(SPORT_NAME, getSportNameValidator(log))
                .build();
    }

    private static Map<String, Validator<JSONObject>> updateFieldValidatorMapOf(final String log) {
        return baseFieldValidatorMapOf(log)
                .build();
    }

    private static Map<String, Validator<JSONObject>> likedSportCreationValidatorMapOf(final String log) {
        return baseFieldValidatorMapOf(log)
                .put(SPORT_NAME, getSportNameValidator(log))
                .build();
    }

    private static Validator<JSONObject> getSportNameValidator(String log) {
        return ValidatorFactory.fieldIsStringAndMatchesRegexOf(SPORT_NAME,
                Pattern.compile(SPORT_NAME_REGEX), SPORT_NAME_PATTERN_DESCRIPTION, log)
                .and(ValidatorFactory.fieldIsStringWithLengthInRangeValidatorOf(SPORT_NAME, MIN_LENGTH_NAME,
                        MAX_LENGTH_NAME, log));
    }
}
