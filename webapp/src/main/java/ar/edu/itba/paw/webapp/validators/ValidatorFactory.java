package ar.edu.itba.paw.webapp.validators;

import ar.edu.itba.paw.webapp.exceptions.ApiException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;

import java.util.Optional;
import java.util.Set;

public class ValidatorFactory {

    private static final Logger LOGGER = LoggerFactory.getLogger(ValidatorFactory.class);

    private static void validateAllFieldsAreKnown(final JSONObject jsonObject, final Set<String> knownFields, final String log) {
        final Set<String> jsonFields = jsonObject.keySet();
        Optional<String> unknownField = jsonFields.stream().filter(field -> !knownFields.contains(field)).findFirst();
        unknownField.ifPresent(field -> logAndThrowApiException(log, new ApiException(HttpStatus.BAD_REQUEST, "The field " + field + " is unknown")));
    }

    public static Validator<JSONObject> knownFieldsValidatorOf(final Set<String> knownFields, final String log) {
        return jsonObject -> validateAllFieldsAreKnown(jsonObject, knownFields, log);
    }

    public static <T> Validator<Optional<T>> existenceValidatorOf(final String resourceType, final String log) {
        return existenceValidatorOf(resourceType, null, log);
    }

    public static <T> Validator<Optional<T>> existenceValidatorOf(final String resourceType, final String resourceId, final String log) {
        return optional -> {
            if (!optional.isPresent()) {
                logAndThrowApiException(log, new ApiException(HttpStatus.NOT_FOUND, resourceType + (resourceId != null ? " '" + resourceId + "'" : "")
                        + " not found"));
            }
        };
    }

    private static void logAndThrowApiException(final String log, final ApiException apiException) {
        LOGGER.error(log, apiException);
        throw apiException;
    }
}
