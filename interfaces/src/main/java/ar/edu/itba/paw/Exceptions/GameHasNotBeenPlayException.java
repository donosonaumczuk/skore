package ar.edu.itba.paw.Exceptions;

public class GameHasNotBeenPlayException extends RuntimeException {
    public GameHasNotBeenPlayException(String message) {
        super(message);
    }
}
