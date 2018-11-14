package ar.edu.itba.paw.Exceptions;

public class AlreadyJoinedToMatchException extends RuntimeException {
    public AlreadyJoinedToMatchException (String message) {
        super(message);
    }
}