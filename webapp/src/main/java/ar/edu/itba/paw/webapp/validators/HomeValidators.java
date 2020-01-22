package ar.edu.itba.paw.webapp.validators;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import org.json.JSONObject;

import java.util.Map;
import java.util.Set;

public class HomeValidators {

    private static final String COUNTRY = "country";
    private static final String STATE = "state";
    private static final String CITY = "city";
    private static final String STREET = "street";
    private static final Set<String> CREATION_KNOWN_FIELDS = ImmutableSet.of(COUNTRY, STATE, CITY, STREET);
    private static final Set<String> CREATION_REQUIRED_FIELDS = ImmutableSet.of();
    private static final Set<String> UPDATE_KNOWN_FIELDS = ImmutableSet.of(COUNTRY, STATE, CITY, STREET);
    private static final Set<String> UPDATE_REQUIRED_FIELDS = ImmutableSet.of();

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
                .put(COUNTRY, ValidatorFactory.fieldIsStringValidatorOf(COUNTRY, log))
                .put(STATE,  ValidatorFactory.fieldIsStringValidatorOf(STATE, log))
                .put(CITY, ValidatorFactory.fieldIsStringValidatorOf(CITY, log))
                .put(STREET, ValidatorFactory.fieldIsStringValidatorOf(STREET, log))
                .build();
    }
}
