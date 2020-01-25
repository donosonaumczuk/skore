package ar.edu.itba.paw.webapp.validators;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import org.json.JSONObject;

import java.util.Map;
import java.util.Set;

public class TimeValidator {

    private static final String HOUR   = "hour";
    private static final String MINUTE = "minute";
    private static final String SECOND = "second";
    private static final Set<String> CREATION_KNOWN_FIELDS    = ImmutableSet.of(HOUR, MINUTE, SECOND);
    private static final Set<String> CREATION_REQUIRED_FIELDS = ImmutableSet.of(HOUR, MINUTE);
    private static final Set<String> UPDATE_KNOWN_FIELDS      = ImmutableSet.of(HOUR, MINUTE, SECOND);
    private static final Set<String> UPDATE_REQUIRED_FIELDS   = ImmutableSet.of();

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
                .put(HOUR, ValidatorFactory.fieldIsIntegerInRangeValidatorOf(HOUR, 0, 23,log))
                .put(MINUTE,  ValidatorFactory.fieldIsIntegerInRangeValidatorOf(MINUTE, 0, 59, log))
                .put(SECOND,  ValidatorFactory.fieldIsIntegerInRangeValidatorOf(SECOND, 0, 59, log))
                .build();
    }
}
