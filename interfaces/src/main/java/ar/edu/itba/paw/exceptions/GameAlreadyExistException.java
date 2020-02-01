package ar.edu.itba.paw.exceptions;

import ar.edu.itba.paw.models.GameKey;

public class GameAlreadyExistException extends EntityAlreadyExistsException {

    private final static String ENTITY_NAME = "Match";

    private GameAlreadyExistException(final String key) {
        super(ENTITY_NAME, key);
    }

    public static GameAlreadyExistException ofKey(final GameKey key) {
        return new GameAlreadyExistException(key.toString());
    }
}
