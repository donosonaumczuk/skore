package ar.edu.itba.paw.exceptions.invalidstate;

public class GameInvalidStateException extends EntityInvalidStateException {

    private final static String ENTITY_NAME = "Match";

    private GameInvalidStateException(final String entityId, final String reason) {
        super(ENTITY_NAME, entityId, reason);
    }

    public static GameInvalidStateException ofGameAlreadyStarted(final String key) {
        return new GameInvalidStateException(key, "has already started");
    }

    public static GameInvalidStateException ofGameNotPlayedYet(final String key) {
        return new GameInvalidStateException(key, "has not been played yet");
    }

    public static GameInvalidStateException ofGameFull(final String key) {
        return new GameInvalidStateException(key, "is full");
    }

    public static GameInvalidStateException ofGameAlreadyJoined(final String key, final long userId) {
        return new GameInvalidStateException(key, "already has the user '" + userId + "' joined as player");
    }

    public static GameInvalidStateException ofGameWithOutLeader(String key) {
        return new GameInvalidStateException(key, "must always have the leader as a player");
    }

    public static GameInvalidStateException ofGameNotFull(String key) {
        return new GameInvalidStateException(key, "is not full");
    }

    public static GameInvalidStateException ofGameWithResult(String key) {
        return new GameInvalidStateException(key, "already has a result");
    }
}
