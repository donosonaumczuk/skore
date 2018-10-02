package ar.edu.itba.paw.Exceptions;

public class CannotValidateUserException extends RuntimeException {
    public CannotValidateUserException (String message) {
        super(message);
    }

}
