package ar.edu.itba.paw.webapp.validators;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import org.json.JSONObject;

import java.util.Map;
import java.util.Set;

public class TimeValidators {

    private static final String HOUR   = "hour";
    private static final String MINUTE = "minute";
    private static final Set<String> CREATION_KNOWN_FIELDS    = ImmutableSet.of(HOUR, MINUTE);
    private static final Set<String> CREATION_REQUIRED_FIELDS = ImmutableSet.of(HOUR, MINUTE);
    private static final Set<String> UPDATE_KNOWN_FIELDS      = ImmutableSet.of(HOUR, MINUTE);
    private static final Set<String> UPDATE_REQUIRED_FIELDS   = ImmutableSet.of(HOUR, MINUTE);
    private static final int MAX_HOUR = 23;
    private static final int MAX_MINUTE = 59;

    public static Validator<JSONObject> creationValidatorOf(final String log) {
        return ValidatorFactory.jsonInputValidator(CREATION_KNOWN_FIELDS, CREATION_REQUIRED_FIELDS,
                fieldValidatorMapOf(log), log);
    }

    public static Validator<JSONObject> updateValidatorOf(final String log) {
        return ValidatorFactory.jsonInputValidator(UPDATE_KNOWN_FIELDS, UPDATE_REQUIRED_FIELDS,
                fieldValidatorMapOf(log), log);
    }

    private static Map<String, Validator<JSONObject>> fieldValidatorMapOf(final String log) {
        return new ImmutableMap.Builder<String, Validator<JSONObject>>()
                .put(HOUR, ValidatorFactory.fieldIsIntegerInRangeValidatorOf(HOUR, 0, MAX_HOUR,log))
                .put(MINUTE,  ValidatorFactory.fieldIsIntegerInRangeValidatorOf(MINUTE, 0, MAX_MINUTE, log))
                .build();
    }
}
