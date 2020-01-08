package ar.edu.itba.paw.exceptions;

public class GameHasNotBeenPlayException extends RuntimeException {

    public GameHasNotBeenPlayException(String message) {
        super(message);
    }
}
