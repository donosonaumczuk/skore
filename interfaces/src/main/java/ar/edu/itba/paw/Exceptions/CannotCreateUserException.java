package ar.edu.itba.paw.Exceptions;

public class CannotCreateUserException extends RuntimeException {

    public CannotCreateUserException (String message) {
        super(message);
    }
}
