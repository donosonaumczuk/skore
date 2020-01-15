package ar.edu.itba.paw.webapp.validators;

import ar.edu.itba.paw.models.PremiumUser;
import com.google.common.collect.ImmutableSet;
import org.json.JSONObject;

import java.util.Optional;
import java.util.Set;

public class UserValidators {

    //TODO: here we must add all fields of UserDto, maybe we will have a USER_UPDATE_KNOWN_FIELDS and USER_CREATE_KNOWN FIELDS, and two validators
    private static final Set<String> USER_KNOWN_FIELDS = ImmutableSet.of("username", "email");

    public static Validator<JSONObject> knownFieldsValidatorOf(String log) {
        return ValidatorFactory.knownFieldsValidatorOf(USER_KNOWN_FIELDS, log);
    }

    public static Validator<Optional<PremiumUser>> existenceValidatorOf(final String username, final String log) {
        return ValidatorFactory.existenceValidatorOf("User", username, log);
    }
}
