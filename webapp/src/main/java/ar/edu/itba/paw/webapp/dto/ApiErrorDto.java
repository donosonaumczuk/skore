package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.webapp.exceptions.ApiException;

public class ApiErrorDto {

    private final int statusCode;
    private final String message;

    private ApiErrorDto(ApiException apiException) {
        this.statusCode = apiException.getStatusCode();
        this.message = apiException.getMessage();
    }

    public static ApiErrorDto from(ApiException apiException) {
        return new ApiErrorDto(apiException);
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getMessage() {
        return message;
    }
}
