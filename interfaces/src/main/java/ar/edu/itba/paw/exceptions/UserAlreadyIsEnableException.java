package ar.edu.itba.paw.exceptions;

public class UserAlreadyIsEnableException extends RuntimeException {

    private UserAlreadyIsEnableException(final String username) {
        super("User '" + username + "' is already enable");
    }

    public static UserAlreadyIsEnableException ofUsername(final String username) {
        return new UserAlreadyIsEnableException(username);
    }
}
