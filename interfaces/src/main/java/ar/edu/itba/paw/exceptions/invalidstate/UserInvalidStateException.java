package ar.edu.itba.paw.exceptions.invalidstate;

public class UserInvalidStateException extends EntityInvalidStateException {

    private final static String ENTITY_NAME = "User";

    private UserInvalidStateException(final String entityId, final String reason) {
        super(ENTITY_NAME, entityId, reason);
    }

    public static UserInvalidStateException ofUserAlreadyEnable(final String username) {
        return new UserInvalidStateException(username, "is already enable");
    }
}
