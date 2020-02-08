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
        return new ApiException(statusCode, errorCode, message);
    }

    public static ApiException of(final HttpStatus statusCode, final String message) {
        return new ApiException(statusCode, statusCode.toString(), message);
    }

    public static ApiException of(final HttpStatus statusCode) {
        return new ApiException(statusCode, statusCode.toString(), statusCode.getReasonPhrase());
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
