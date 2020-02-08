package ar.edu.itba.paw.exceptions;

public class InvalidUserCodeException extends RuntimeException {

    private InvalidUserCodeException(final String message) {
        super(message);
    }

    public static InvalidUserCodeException of(final String username, final String code) {
        return new InvalidUserCodeException("Invalid code '" + code + "' for user '" + username + "'");
    }
}
