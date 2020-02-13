package ar.edu.itba.paw.webapp.validators;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Set;

public class ResultValidators {

    private static final Logger LOGGER = LoggerFactory.getLogger(ResultValidators.class);
    private static final String SCORE_TEAM_1 = "scoreTeam1";
    private static final String SCORE_TEAM_2 = "scoreTeam2";

    private static final Set<String> CREATION_KNOWN_AND_REQUIRED_FIELDS = ImmutableSet.of(SCORE_TEAM_1, SCORE_TEAM_2);
    private static final int MIN_RESULT = 0;
    private static final int MAX_RESULT = 9999;

    public static Validator<JSONObject> creationValidatorOf(final String log) {
        return ValidatorFactory.jsonInputValidator(CREATION_KNOWN_AND_REQUIRED_FIELDS,
                CREATION_KNOWN_AND_REQUIRED_FIELDS, creationFieldValidatorMapOf(log), log);
    }

    private static Map<String, Validator<JSONObject>> creationFieldValidatorMapOf(final String log) {
        return new ImmutableMap.Builder<String, Validator<JSONObject>>()
                .put(SCORE_TEAM_1, ValidatorFactory.fieldIsIntegerInRangeValidatorOf(SCORE_TEAM_1, MIN_RESULT,
                        MAX_RESULT, log))
                .put(SCORE_TEAM_2, ValidatorFactory.fieldIsIntegerInRangeValidatorOf(SCORE_TEAM_2, MIN_RESULT,
                        MAX_RESULT, log))
                .build();
    }
}
