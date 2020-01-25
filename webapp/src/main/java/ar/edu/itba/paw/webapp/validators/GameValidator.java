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

public class GameValidator {

    private static final Logger LOGGER = LoggerFactory.getLogger(GameValidator.class);
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

    private static final String TITLE_REGEX = "[a-zA-Z0-9¿?¡!ÁÉÍÓÚáéíñóöúü ]+";
    private static final String PATTER_DESCRIPTION = "a string containing english alphabetic characters, digits, " +
                                                    "spaces or any of these characters: ¿?¡!ÁÉÍÓÚáéíñóöúü";

    public static Validator<JSONObject> creationValidatorOf(final String log) {
        return ValidatorFactory.jsonInputValidator(CREATION_KNOWN_FIELDS, CREATION_REQUIRED_FIELDS,
                creationFieldValidatorListOf(log), log);
    }

    private static List<Pair<String, Validator<JSONObject>>> creationFieldValidatorListOf(final String log) {
        return new ImmutableList.Builder<Pair<String, Validator<JSONObject>>>()
                .add(Pair.of(TITLE, ValidatorFactory.fieldIsStringAndMatchesRegexOf(TITLE, Pattern.compile(TITLE_REGEX),
                        PATTER_DESCRIPTION, log).and(ValidatorFactory.fieldIsStringWithLengthInRangeValidatorOf(TITLE,
                        4, 140, log))))
                .add(Pair.of(DESCRIPTION, ValidatorFactory.fieldIsStringWithLengthInRangeValidatorOf(DESCRIPTION,
                        0, 140, log)))
                .add(Pair.of(DESCRIPTION, ValidatorFactory.fieldIsStringWithLengthInRangeValidatorOf(DESCRIPTION,
                        0, 140, log)))
                .add(Pair.of(SPORT, ValidatorFactory.fieldIsStringValidatorOf(SPORT, log)))
                .add(Pair.of(DURATION, ValidatorFactory.fieldIsIntegerInRangeValidatorOf(DURATION, 1, null, log)))
                .add(Pair.of(INDIVIDUAL, individualValidation(log)))
                .add(Pair.of(COMPETITIVE, ValidatorFactory.fieldIsBooleanValidatorOf(COMPETITIVE, log)))
                .add(Pair.of(LOCATION, ValidatorFactory.fieldIsValidObjectValidatorOf(LOCATION,
                        HomeValidators.creationValidatorOf(log), log)))
                .add(Pair.of(DATE, ValidatorFactory.fieldIsValidObjectValidatorOf(DATE,
                        DateValidator.creationValidatorOf(log), log)))
                .add(Pair.of(TIME, ValidatorFactory.fieldIsValidObjectValidatorOf(TIME,
                        TimeValidator.creationValidatorOf(log), log)))
                .add(Pair.of(TEAM_NAME_1, ValidatorFactory.fieldIsStringAndMatchesRegexOf(TEAM_NAME_1,
                        Pattern.compile(TITLE_REGEX), PATTER_DESCRIPTION, log)))//TODO check team name pattern, it must not have '.'
                .add(Pair.of(TEAM_NAME_2, ValidatorFactory.fieldIsStringAndMatchesRegexOf(TEAM_NAME_2,
                        Pattern.compile(TITLE_REGEX), PATTER_DESCRIPTION, log)))
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