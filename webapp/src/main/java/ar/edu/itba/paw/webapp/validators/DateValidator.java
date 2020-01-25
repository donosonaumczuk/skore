package ar.edu.itba.paw.webapp.validators;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import org.checkerframework.javacutil.Pair;
import org.json.JSONObject;

import java.time.YearMonth;
import java.util.List;
import java.util.Set;

public class DateValidator {

    private static final String YEAR  = "year";
    private static final String MONTH = "monthNumber";
    private static final String DAY   = "dayOfMonth";
    private static final Set<String> CREATION_KNOWN_FIELDS    = ImmutableSet.of(YEAR, MONTH, DAY);
    private static final Set<String> CREATION_REQUIRED_FIELDS = ImmutableSet.of(YEAR, MONTH, DAY);
    private static final Set<String> UPDATE_KNOWN_FIELDS      = ImmutableSet.of(YEAR, MONTH, DAY);
    private static final Set<String> UPDATE_REQUIRED_FIELDS   = ImmutableSet.of();

    public static Validator<JSONObject> creationValidatorOf(final String log) {
        return ValidatorFactory.jsonInputValidator(CREATION_KNOWN_FIELDS, CREATION_REQUIRED_FIELDS,
                fieldValidatorListOf(log), log);
    }

    public static Validator<JSONObject> updateValidatorOf(final String log) {
        return ValidatorFactory.jsonInputValidator(UPDATE_KNOWN_FIELDS, UPDATE_REQUIRED_FIELDS,
                fieldValidatorListOf(log), log);
    }

    private static List<Pair<String, Validator<JSONObject>>> fieldValidatorListOf(final String log) {
        return new ImmutableList.Builder<Pair<String, Validator<JSONObject>>>()
                .add(Pair.of(YEAR, ValidatorFactory.fieldIsIntegerInRangeValidatorOf(YEAR, 0, null, log)))
                .add(Pair.of(MONTH,  ValidatorFactory.fieldIsIntegerInRangeValidatorOf(MONTH, 1, 12, log)))
                .add(Pair.of(DAY, jsonObject -> ValidatorFactory.fieldIsIntegerInRangeValidatorOf(DAY, 1,
                        getMaxDay(jsonObject.getInt(YEAR), jsonObject.getInt(MONTH)), log)
                        .validate(jsonObject)))
                .build();
    }

    private static int getMaxDay(int year, int month) {
        return YearMonth.of(year, month).atEndOfMonth().getDayOfMonth();
    }
}
