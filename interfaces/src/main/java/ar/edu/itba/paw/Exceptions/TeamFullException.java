package ar.edu.itba.paw.Exceptions;

public class TeamFullException extends RuntimeException{
    public TeamFullException (String message) {
        super(message);
    }
}
