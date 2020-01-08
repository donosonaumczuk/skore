package ar.edu.itba.paw.webapp.exceptions;

import org.springframework.http.HttpStatus;

public class ApiException extends RuntimeException {

    private final int statusCode;
    private final String message;

    public ApiException(final HttpStatus statusCode, final String message) {
        this.statusCode = statusCode.value();
        this.message = message;
    }

    public ApiException(final HttpStatus statusCode) {
        this(statusCode, statusCode.getReasonPhrase());
    }

    public int getStatusCode() {
        return statusCode;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
