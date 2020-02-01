package ar.edu.itba.paw.exceptions;

public class GameNotFoundException extends EntityNotFoundException {

    private final static String ENTITY_NAME = "Match";

    private GameNotFoundException(final String key) {
        super(ENTITY_NAME, key);
    }

    public static GameNotFoundException ofKey(final String key) {
        return new GameNotFoundException(key);
    }
}
