package ar.edu.itba.paw.exceptions.invalidstate;

public class TeamInvalidStateException extends EntityInvalidStateException {

    private final static String ENTITY_NAME = "Team";

    private TeamInvalidStateException(final String entityId, final String reason, final String type) {
        super(ENTITY_NAME, entityId, reason, type);
    }

    public static TeamInvalidStateException ofTeamAlreadyFull(final String name) {
        return new TeamInvalidStateException(name, "is already full", "FULL");
    }
}
