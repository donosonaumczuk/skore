package ar.edu.itba.paw.Exceptions;

public class GameNotFoundException extends RuntimeException {

    public GameNotFoundException (String message) {
        super(message);
    }
}
