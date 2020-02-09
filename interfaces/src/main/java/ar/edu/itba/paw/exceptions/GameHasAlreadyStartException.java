package ar.edu.itba.paw.exceptions;

public class GameHasAlreadyStartException extends RuntimeException {

    public GameHasAlreadyStartException(String message) {
        super(message);
    }
}