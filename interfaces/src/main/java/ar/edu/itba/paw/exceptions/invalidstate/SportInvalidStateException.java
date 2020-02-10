package ar.edu.itba.paw.exceptions.invalidstate;

public class SportInvalidStateException extends EntityInvalidStateException {

    private final static String ENTITY_NAME = "Sport";

    private SportInvalidStateException(final String entityId, final String reason) {
        super(ENTITY_NAME, entityId, reason);
    }

    public static SportInvalidStateException ofSportUsed(final String sportName) {
        return new SportInvalidStateException(sportName, "is already used in a match");
    }
}
