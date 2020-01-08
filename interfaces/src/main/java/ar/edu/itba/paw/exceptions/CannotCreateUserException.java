package ar.edu.itba.paw.exceptions;

public class CannotCreateUserException extends RuntimeException {

    public CannotCreateUserException (String message) {
        super(message);
    }
}
