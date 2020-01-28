package ar.edu.itba.paw.webapp.validators;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import org.checkerframework.javacutil.Pair;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Set;

public class PlayerValidators {

    private static final Logger LOGGER = LoggerFactory.getLogger(PlayerValidators.class);
    private static final String USER_ID = "userId";

    private static final Set<String> CREATION_KNOWN_AND_REQUIRED_FIELDS = ImmutableSet.of(USER_ID);

    public static Validator<JSONObject> creationValidatorOf(final String log) {
        return ValidatorFactory.jsonInputValidator(CREATION_KNOWN_AND_REQUIRED_FIELDS, CREATION_KNOWN_AND_REQUIRED_FIELDS,
                creationFieldValidatorListOf(log), log);
    }

    private static List<Pair<String, Validator<JSONObject>>> creationFieldValidatorListOf(final String log) {
        return new ImmutableList.Builder<Pair<String, Validator<JSONObject>>>()
                .add(Pair.of(USER_ID, ValidatorFactory.fieldIsIntegerInRangeValidatorOf(USER_ID, 1, null, log)))
                .build();
    }
}
