package ar.edu.itba.paw.exceptions;

public class GameNotFoundException extends EntityNotFoundException {

    private GameNotFoundException(final String key) {
        super("Game", key);
    }

    public static GameNotFoundException ofKey(final String key) {
        return new GameNotFoundException(key);
    }
}
