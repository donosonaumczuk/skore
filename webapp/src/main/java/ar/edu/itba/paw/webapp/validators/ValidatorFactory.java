package ar.edu.itba.paw.webapp.validators;

import ar.edu.itba.paw.webapp.exceptions.ApiException;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;

import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Pattern;

public class ValidatorFactory {

    private static final Logger LOGGER = LoggerFactory.getLogger(ValidatorFactory.class);

    /**
     * Validates that a JSON Object contains only known fields
     *
     * @param knownFields the names of the known fields
     * @param log a String to log if something is invalid
     * @return the validator which performs the described validation
     */
    public static Validator<JSONObject> knownFieldsValidatorOf(final Set<String> knownFields, final String log) {
        return jsonObject -> validateAllFieldsAreKnown(jsonObject, knownFields, log);
    }

    /**
     * Validates that a JSON Object field is an String
     *
     * @param field the name of the field to validate
     * @param log a String to log if something is invalid
     * @return the validator which performs the described validation
     */
    public static Validator<JSONObject> fieldIsStringValidatorOf(final String field, final String log) {
        return jsonObject -> {
            try {
                jsonObject.getString(field);
            } catch (JSONException e) {
                logAndThrowApiException(log, new ApiException(HttpStatus.BAD_REQUEST, "Field '"
                        + field + "' must be a string"));
            }
        };
    }

    /**
     * Validates that a JSON Object field is an String and matches a regular expression pattern
     *
     * @param field the name of the field to validate
     * @param regex the regular expression pattern
     * @param regexDescription a human-friendly description of the regular expression
     * @param log a String to log if something is invalid
     * @return the validator which performs the described validation
     */
    public static Validator<JSONObject> fieldIsStringAndMatchesRegexOf(final String field, final Pattern regex,
                                                                       final String regexDescription, final String log) {
        return fieldIsStringValidatorOf(field, log).and(jsonObject -> {
                    if (!regex.matcher(jsonObject.getString(field)).matches()) {
                        logAndThrowApiException(log, new ApiException(HttpStatus.BAD_REQUEST, "Field '" + field
                                + "' must be " + regexDescription));
                    }
                }
        );
    }

    /**
     * Validates that a JSON Object field is a JSON Object and is valid through a given validator
     *
     * @param field the name of the field to validate
     * @param fieldObjectValidator a validator that validates the object
     * @param log a String to log if something is invalid
     * @return the validator which performs the described validation
     */
    public static Validator<JSONObject> fieldIsValidObjectValidatorOf(final String field,
                                                                      final Validator<JSONObject> fieldObjectValidator,
                                                                      final String log) {
        return jsonObject -> {
            JSONObject fieldObject = null;
            try {
                fieldObject = jsonObject.getJSONObject(field);
            } catch (JSONException e) {
                logAndThrowApiException(log, new ApiException(HttpStatus.BAD_REQUEST, "Field '"
                        + field + "' must be a JSON object"));
            }
            fieldObjectValidator.validate(fieldObject);
        };
    }

    /**
     * Validate that a JSON Object contains all required fields
     *
     * @param requiredFields the required fields
     * @param log a String to log if something is invalid
     * @return the validator which performs the described validation
     */
    public static Validator<JSONObject> requiredFieldsValidatorOf(final Set<String> requiredFields, final String log) {
        return jsonObject -> validateAllRequiredFieldsArePresent(jsonObject, requiredFields, log);
    }

    /**
     * Validate that an Optional of a resource must be present
     *
     * @param resourceType a description of the resource type
     * @param resourceId the id of the resource that must be present
     * @param log a String to log if something is invalid
     * @param <T> the type of the resource
     * @return the validator which performs the described validation
     */
    public static <T> Validator<Optional<T>> existenceValidatorOf(final String resourceType, final String resourceId,
                                                                  final String log) {
        return optional -> {
            if (!optional.isPresent()) {
                logAndThrowApiException(log, new ApiException(HttpStatus.NOT_FOUND, resourceType
                        + (resourceId != null ? " '" + resourceId + "'" : "") + " not found"));
            }
        };
    }

    /**
     * Validate that all JSON Object known fields have valid values
     *
     * @param fieldValidators a Map with the Validator for every known JSON field
     * @return the validator which performs the described validation
     */
    public static Validator<JSONObject> jsonFieldValuesValidatorOf(
            final Map<String, Validator<JSONObject>> fieldValidators
    ) {
        return jsonObject -> {
            Validator<JSONObject> validator = json -> { /* Clean validator, do nothing */ };
            for (String field : jsonObject.keySet()) {
                validator = validator.and(fieldValidators.get(field));
            }
            validator.validate(jsonObject);
        };
    }

    /**
     * Validate that a JSON Object have only known fields, have all the required field and that all the fields have
     * valid values
     *
     * @param knownFields a Set with the JSON known fields
     * @param requiredFields a Set with the JSON required fields
     * @param fieldValidators a Map with the Validator for every known JSON field
     * @param log a String to log if something is invalid
     * @return the validator which performs the described validation
     */
    public static Validator<JSONObject> jsonInputValidator(final Set<String> knownFields,
                                                            final Set<String> requiredFields,
                                                            final Map<String, Validator<JSONObject>> fieldValidators,
                                                            final String log) {
        return ValidatorFactory.knownFieldsValidatorOf(knownFields, log)
                .and(ValidatorFactory.requiredFieldsValidatorOf(requiredFields, log))
                .and(ValidatorFactory.jsonFieldValuesValidatorOf(fieldValidators));
    }

    private static void validateAllFieldsAreKnown(final JSONObject jsonObject, final Set<String> knownFields,
                                                  final String log) {
        final Set<String> jsonFields = jsonObject.keySet();
        Optional<String> unknownField = jsonFields.stream().filter(field -> !knownFields.contains(field)).findFirst();
        unknownField.ifPresent(field -> logAndThrowApiException(log,
                new ApiException(HttpStatus.BAD_REQUEST, "Field '" + field + "' is unknown or unaccepted")));
    }

    private static void validateAllRequiredFieldsArePresent(final JSONObject jsonObject,
                                                            final Set<String> requiredFields, final String log) {
        final Set<String> jsonFields = jsonObject.keySet();
        Optional<String> missingField = requiredFields.stream()
                .filter(field -> !jsonFields.contains(field))
                .findFirst();
        missingField.ifPresent(field -> logAndThrowApiException(log,
                new ApiException(HttpStatus.BAD_REQUEST, "Missing required '" + field + "' field")));
    }

    private static void logAndThrowApiException(final String log, final ApiException apiException) {
        LOGGER.error(log, apiException);
        throw apiException;
    }
}
