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

    public static Validator<JSONObject> creationValidatorOf(final String log) {
        return ValidatorFactory.jsonInputValidator(CREATION_KNOWN_FIELDS, CREATION_REQUIRED_FIELDS,
                creationFieldValidatorMapOf(log), log);
    }

    public static Validator<JSONObject> updateValidatorOf(final String log) {
        return ValidatorFactory.jsonInputValidator(UPDATE_KNOWN_FIELDS, UPDATE_REQUIRED_FIELDS,
                updateFieldValidatorMapOf(log), log);
    }

    private static ImmutableMap.Builder<String, Validator<JSONObject>> baseFieldValidatorMapOf(final String log) {
        return new ImmutableMap.Builder<String, Validator<JSONObject>>()
                .put(QUANTITY, ValidatorFactory.fieldIsIntegerInRangeValidatorOf(QUANTITY, 1, 100, log))
                .put(DISPLAY_NAME, ValidatorFactory.fieldIsStringAndMatchesRegexOf(DISPLAY_NAME, Pattern.compile("[0-9a-zA-ZáéíóúñÁÉÍÓÚÑ ]+"),
                        "a string containing english alphabetic characters, space, digits or any of these characters: áéíóúñÁÉÍÓÚÑ", log)
                        .and(ValidatorFactory.fieldIsStringWithLengthInRangeValidatorOf(DISPLAY_NAME, 4, 100, log)))
                .put(IMAGE, ValidatorFactory.fieldIsStringValidatorOf(IMAGE, log));
    }

    private static Map<String, Validator<JSONObject>> creationFieldValidatorMapOf(final String log) {
        return baseFieldValidatorMapOf(log)
                .put(SPORT_NAME, ValidatorFactory.fieldIsStringAndMatchesRegexOf(SPORT_NAME, Pattern.compile("[a-zA-Z0-9_]+"),
                        "a string containing english alphabetic characters, digits or underscore", log)
                        .and(ValidatorFactory.fieldIsStringWithLengthInRangeValidatorOf(SPORT_NAME, 4, 100, log)))
                .build();
    }

    private static Map<String, Validator<JSONObject>> updateFieldValidatorMapOf(final String log) {
        return baseFieldValidatorMapOf(log)
                .build();
    }
}
