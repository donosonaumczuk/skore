package ar.edu.itba.paw.exceptions;

public class CannotValidateUserException extends RuntimeException {

    public CannotValidateUserException (String message) {
        super(message);
    }
}
