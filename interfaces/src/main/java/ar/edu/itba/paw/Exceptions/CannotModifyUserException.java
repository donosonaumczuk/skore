package ar.edu.itba.paw.Exceptions;

public class CannotModifyUserException extends RuntimeException {

    public CannotModifyUserException(String message) {
        super(message);
    }
}