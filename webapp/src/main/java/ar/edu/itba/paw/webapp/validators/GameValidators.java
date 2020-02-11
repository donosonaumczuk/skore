package ar.edu.itba.paw.webapp.validators;

import ar.edu.itba.paw.webapp.exceptions.ApiException;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import org.checkerframework.javacutil.Pair;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

public class GameValidators {

    private static final Logger LOGGER = LoggerFactory.getLogger(GameValidators.class);
    private static final String TITLE       = "title";
    private static final String DESCRIPTION = "description";
    private static final String SPORT       = "sport";
    private static final String DURATION    = "minutesOfDuration";
    private static final String INDIVIDUAL  = "individual";
    private static final String COMPETITIVE = "competitive";
    private static final String LOCATION    = "location";
    private static final String DATE        = "date";
    private static final String TIME        = "time";
    private static final String TEAM_NAME_1 = "teamName1";
    private static final String TEAM_NAME_2 = "teamName2";
    private static final Set<String> CREATION_KNOWN_FIELDS = ImmutableSet.of(TITLE, DESCRIPTION, SPORT, DURATION,
            INDIVIDUAL, COMPETITIVE, DATE, TIME, LOCATION, TEAM_NAME_1, TEAM_NAME_2);
    private static final Set<String> CREATION_REQUIRED_FIELDS = ImmutableSet.of(TITLE, SPORT, DURATION, INDIVIDUAL,
            COMPETITIVE, DATE, TIME, LOCATION);
    private static final Set<String> UPDATE_KNOWN_FIELDS = ImmutableSet.of(TITLE, DESCRIPTION, DURATION, DATE, TIME,
            LOCATION, TEAM_NAME_1, TEAM_NAME_2);
    private static final Set<String> UPDATE_REQUIRED_FIELDS = ImmutableSet.of();

    private static final String TITLE_REGEX = "[a-zA-Z0-9¿?¡!_-ÑÁÉÍÓÚáéíñóöúü .]+";
    private static final String TITLE_PATTER_DESCRIPTION = "a string containing english alphabetic characters, " +
            "digits, spaces or any of these characters: ¿?¡!_-.ÑÁÉÍÓÚáéíñóöúü";
    private static final String TEAM_NAME_REGEX = TITLE_REGEX;//TODO check team name pattern, it must not have '.'
    private static final String TEAM_NAME_PATTER_DESCRIPTION = TITLE_PATTER_DESCRIPTION;
    private static final String KEY_REGEX = "\\d{12}" + TEAM_NAME_REGEX + "\\d{12}";
    private static final int MIN_LENGTH_TITLE       = 4;
    private static final int MAX_LENGTH_TITLE       = 140;
    private static final int MIN_LENGTH_DESCRIPTION = 0;
    private static final int MAX_LENGTH_DESCRIPTION = 140;
    private static final int MIN_DURATION           = 1;
    private static final int MAX_DURATION           = 9999;
    private static final int MIN_LENGTH_SPORT       = 4;
    private static final int MAX_LENGTH_SPORT       = 100;
    private static final String SPORT_NAME_REGEX    = "[a-zA-Z0-9_]+";
    private static final String SPORT_NAME_PATTERN_DESCRIPTION = "a string containing english alphabetic characters, " +
            "digits or underscore";

    public static Validator<JSONObject> creationValidatorOf(final String log) {
        return ValidatorFactory.jsonInputValidator(CREATION_KNOWN_FIELDS, CREATION_REQUIRED_FIELDS,
                creationFieldValidatorListOf(log), log);
    }

    public static Validator<JSONObject> updateValidatorOf(final String log) {
        return ValidatorFactory.jsonInputValidator(UPDATE_KNOWN_FIELDS, UPDATE_REQUIRED_FIELDS,
                updateFieldValidatorListOf(log), log);
    }

    public static Validator<String> keyValidator(final String log) {
        return (string) -> {
            if (Pattern.compile(KEY_REGEX).matcher(string).matches()) {
                ApiException apiException = ApiException.of(HttpStatus.NOT_FOUND, "Match '" + string + "' does not exist");
                LOGGER.error(log, apiException);
                throw apiException;
            }
        };
    }

    private static ImmutableList.Builder<Pair<String, Validator<JSONObject>>> baseFieldValidatorListOf(final String log) {
        return new ImmutableList.Builder<Pair<String, Validator<JSONObject>>>()
                .add(Pair.of(TITLE, ValidatorFactory.fieldIsStringAndMatchesRegexOf(TITLE, Pattern.compile(TITLE_REGEX),
                        TITLE_PATTER_DESCRIPTION, log).and(ValidatorFactory.fieldIsStringWithLengthInRangeValidatorOf(TITLE,
                        MIN_LENGTH_TITLE, MAX_LENGTH_TITLE, log))))
                .add(Pair.of(DESCRIPTION, ValidatorFactory.fieldIsStringWithLengthInRangeValidatorOf(DESCRIPTION,
                        MIN_LENGTH_DESCRIPTION, MAX_LENGTH_DESCRIPTION, log)))
                .add(Pair.of(DURATION, ValidatorFactory.fieldIsIntegerInRangeValidatorOf(DURATION, MIN_DURATION,
                        MAX_DURATION, log)))
                .add(Pair.of(DATE, ValidatorFactory.fieldIsValidObjectValidatorOf(DATE,
                        DateValidators.creationValidatorOf(log), log)))
                .add(Pair.of(TIME, ValidatorFactory.fieldIsValidObjectValidatorOf(TIME,
                        TimeValidators.creationValidatorOf(log), log)));
    }

    private static List<Pair<String, Validator<JSONObject>>> creationFieldValidatorListOf(final String log) {
        return baseFieldValidatorListOf(log)
                .add(Pair.of(LOCATION, ValidatorFactory.fieldIsValidObjectValidatorOf(LOCATION,
                        LocationValidator.creationValidatorOf(log), log)))
                .add(Pair.of(SPORT, ValidatorFactory.fieldIsStringAndMatchesRegexOf(SPORT,
                        Pattern.compile(SPORT_NAME_REGEX), SPORT_NAME_PATTERN_DESCRIPTION, log)
                        .and(ValidatorFactory.fieldIsStringWithLengthInRangeValidatorOf(SPORT, MIN_LENGTH_SPORT,
                                MAX_LENGTH_SPORT, log))))
                .add(Pair.of(INDIVIDUAL, individualValidation(log)))
                .add(Pair.of(COMPETITIVE, ValidatorFactory.fieldIsBooleanValidatorOf(COMPETITIVE, log)))
                .add(Pair.of(TEAM_NAME_1, ValidatorFactory.fieldIsStringAndMatchesRegexOf(TEAM_NAME_1,
                        Pattern.compile(TEAM_NAME_REGEX), TEAM_NAME_PATTER_DESCRIPTION, log)))
                .add(Pair.of(TEAM_NAME_2, ValidatorFactory.fieldIsStringAndMatchesRegexOf(TEAM_NAME_2,
                        Pattern.compile(TEAM_NAME_REGEX), TEAM_NAME_PATTER_DESCRIPTION, log)))
                .build();
    }

    private static List<Pair<String, Validator<JSONObject>>> updateFieldValidatorListOf(final String log) {
        return baseFieldValidatorListOf(log)
                .add(Pair.of(LOCATION, ValidatorFactory.fieldIsValidObjectValidatorOf(LOCATION,
                        LocationValidator.updateValidatorOf(log), log)))
                .add(Pair.of(TEAM_NAME_1, ValidatorFactory.fieldIsStringAndMatchesRegexOf(TEAM_NAME_1,
                        Pattern.compile(TEAM_NAME_REGEX), TEAM_NAME_PATTER_DESCRIPTION, log)))
                .add(Pair.of(TEAM_NAME_2, ValidatorFactory.fieldIsStringAndMatchesRegexOf(TEAM_NAME_2,
                        Pattern.compile(TEAM_NAME_REGEX), TEAM_NAME_PATTER_DESCRIPTION, log)))
                .build();
    }

    private static Validator<JSONObject> individualValidation(final String log) {
        return ValidatorFactory.fieldIsBooleanValidatorOf(INDIVIDUAL, log).and((jsonObject) -> {
            if (jsonObject.getBoolean(INDIVIDUAL)) {
                ValidatorFactory.forbiddenFieldsValidatorOf(ImmutableSet.of(TEAM_NAME_1, TEAM_NAME_2), log)
                        .validate(jsonObject);
            }
            else {
                ValidatorFactory.requiredFieldsValidatorOf(ImmutableSet.of(TEAM_NAME_1), log).validate(jsonObject);
            }
        });
    }
}
