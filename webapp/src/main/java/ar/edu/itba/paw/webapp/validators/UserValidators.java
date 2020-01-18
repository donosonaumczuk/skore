package ar.edu.itba.paw.webapp.validators;

import ar.edu.itba.paw.models.PremiumUser;
import com.google.common.collect.ImmutableSet;
import org.json.JSONObject;

import java.util.Optional;
import java.util.Set;

public class UserValidators {

    //TODO: here we must add all fields of UserDto, maybe we will have a USER_UPDATE_KNOWN_FIELDS and USER_CREATE_KNOWN FIELDS, and two validators
    private static final Set<String> USER_KNOWN_FIELDS = ImmutableSet.of("username", "email");

    //TODO: update when adding validations to controller
    private static final Set<String> REQUIRED_CREATION_FIELDS = ImmutableSet.of("username", "email");

    public static Validator<JSONObject> knownFieldsValidatorOf(final String log) {
        return ValidatorFactory.knownFieldsValidatorOf(USER_KNOWN_FIELDS, log);
    }

    public static Validator<Optional<PremiumUser>> existenceValidatorOf(final String username, final String log) {
        return ValidatorFactory.existenceValidatorOf("User", username, log);
    }

    public static Validator<JSONObject> requiredFieldsForCreationValidatorOf(final  String log) {
        return ValidatorFactory.requiredFieldsValidatorOf(REQUIRED_CREATION_FIELDS, log);
    }
}
