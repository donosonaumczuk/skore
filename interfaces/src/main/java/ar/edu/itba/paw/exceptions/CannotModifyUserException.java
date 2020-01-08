package ar.edu.itba.paw.exceptions;

public class CannotModifyUserException extends RuntimeException {

    public CannotModifyUserException(String message) {
        super(message);
    }
}