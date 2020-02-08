package ar.edu.itba.paw.exceptions;

public class GameNotPlayedYetException extends RuntimeException {

    public GameNotPlayedYetException(String message) {
        super(message);
    }
}
