package ar.edu.itba.paw.webapp.exceptions;

import org.springframework.http.HttpStatus;

import javax.xml.ws.http.HTTPException;

public class ApiException extends HTTPException {

    private final int statusCode;
    private final String message;

    public ApiException(final HttpStatus statusCode, final String message) {
        super(statusCode.value());
        this.statusCode = statusCode.value();
        this.message = message;
    }

    public ApiException(final HttpStatus statusCode) {
        this(statusCode, statusCode.getReasonPhrase());
    }

    @Override
    public int getStatusCode() {
        return statusCode;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
