package ar.edu.itba.paw.exceptions;

public class WrongOldUserPasswordException extends RuntimeException {

    private WrongOldUserPasswordException(final String username) {
        super("Invalid or missing old password for user '" + username + "'");
    }

    public static WrongOldUserPasswordException ofUsername(final String username) {
        return new WrongOldUserPasswordException(username);
    }
}
