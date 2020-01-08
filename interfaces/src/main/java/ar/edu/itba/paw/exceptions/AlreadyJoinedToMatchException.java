package ar.edu.itba.paw.exceptions;

public class AlreadyJoinedToMatchException extends RuntimeException {

    public AlreadyJoinedToMatchException (String message) {
        super(message);
    }
}