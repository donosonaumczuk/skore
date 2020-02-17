package ar.edu.itba.paw.exceptions.invalidstate;

public class GameInvalidStateException extends EntityInvalidStateException {

    private final static String ENTITY_NAME = "Match";

    private GameInvalidStateException(final String entityId, final String reason, final String type) {
        super(ENTITY_NAME, entityId, reason, type);
    }

    public static GameInvalidStateException ofGameAlreadyStarted(final String key) {
        return new GameInvalidStateException(key, "has already started", "ALREADY_STARTED");
    }

    public static GameInvalidStateException ofGameNotPlayedYet(final String key) {
        return new GameInvalidStateException(key, "has not been played yet", "NOT_PLAYED");
    }

    public static GameInvalidStateException ofGameFull(final String key) {
        return new GameInvalidStateException(key, "is full", "FULL");
    }

    public static GameInvalidStateException ofGameAlreadyJoined(final String key, final long userId) {
        return new GameInvalidStateException(key, "already has the user '" + userId + "' joined as player",
                "ALREADY_JOINED");
    }

    public static GameInvalidStateException ofGameWithOutLeader(String key) {
        return new GameInvalidStateException(key, "must always have the leader as a player", "WITHOUT_LEADER");
    }

    public static GameInvalidStateException ofGameNotFull(String key) {
        return new GameInvalidStateException(key, "is not full", "NOT_FULL");
    }

    public static GameInvalidStateException ofGameWithResult(String key) {
        return new GameInvalidStateException(key, "already has a result", "RESULT_EXIST");
    }
}
