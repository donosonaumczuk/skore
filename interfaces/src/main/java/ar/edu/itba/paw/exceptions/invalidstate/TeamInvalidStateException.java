package ar.edu.itba.paw.exceptions.invalidstate;

public class TeamInvalidStateException extends EntityInvalidStateException {

    private final static String ENTITY_NAME = "Team";

    private TeamInvalidStateException(final String entityId, final String reason) {
        super(ENTITY_NAME, entityId, reason);
    }

    public static TeamInvalidStateException ofTeamAlreadyFull(final String name) {
        return new TeamInvalidStateException(name, "is already full");
    }
}
