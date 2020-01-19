package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.webapp.exceptions.ApiException;
import org.springframework.http.HttpStatus;

public class ApiErrorDto {

    private final int statusCode;
    private final String message;

    private ApiErrorDto(final int statusCode, final String message) {
        this.statusCode = statusCode;
        this.message = message;
    }

    public static ApiErrorDto from(ApiException apiException) {
        return new ApiErrorDto(apiException.getStatusCode(), apiException.getMessage());
    }

    public static ApiErrorDto of(final HttpStatus statusCode, final String message) {
        return new ApiErrorDto(statusCode.value(), message);
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getMessage() {
        return message;
    }
}
