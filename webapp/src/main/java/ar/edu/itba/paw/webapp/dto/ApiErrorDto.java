package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.webapp.constants.MessageConstants;
import ar.edu.itba.paw.webapp.exceptions.ApiException;
import org.springframework.http.HttpStatus;

public class ApiErrorDto {

    private final int statusCode;
    private final String errorCode;
    private final String message;

    private ApiErrorDto(final int statusCode, final String errorCode, final String message) {
        this.statusCode = statusCode;
        this.errorCode = errorCode;
        this.message = message;
    }

    public static ApiErrorDto from(ApiException apiException) {
        return new ApiErrorDto(apiException.getStatusCode(), apiException.getErrorCode(), apiException.getMessage());
    }

    public static ApiErrorDto of(final HttpStatus statusCode, final String message) {
        return new ApiErrorDto(statusCode.value(), statusCode.name(), message);
    }

    public static ApiErrorDto of(final HttpStatus statusCode) {
        final String message = HttpStatus.INTERNAL_SERVER_ERROR.equals(statusCode) ?
                MessageConstants.SERVER_ERROR_GENERIC_MESSAGE : statusCode.getReasonPhrase();
        return new ApiErrorDto(statusCode.value(), statusCode.name(), message);
    }

    public static ApiErrorDto of(final HttpStatus statusCode, final String errorCode, final String message) {
        return new ApiErrorDto(statusCode.value(), errorCode, message);
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getMessage() {
        return message;
    }

    public String getErrorCode() {
        return errorCode;
    }
}
