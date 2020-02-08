package ar.edu.itba.paw.webapp.exceptions;

import org.springframework.http.HttpStatus;

public class ApiException extends RuntimeException {

    private final int statusCode;
    private final String errorCode;
    private final String message;

    private ApiException(final HttpStatus statusCode, final String errorCode, final String message) {
        this.statusCode = statusCode.value();
        this.errorCode = errorCode;
        this.message = message;
    }

    public static ApiException of(final HttpStatus statusCode, final String errorCode, final String message) {
        return ApiException.of(statusCode, errorCode, message);
    }

    public static ApiException of(final HttpStatus statusCode, final String message) {
        return ApiException.of(statusCode, statusCode.name(), message);
    }

    public static ApiException of(final HttpStatus statusCode) {
        return ApiException.of(statusCode, statusCode.name(), statusCode.getReasonPhrase());
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getErrorCode() {
        return errorCode;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
