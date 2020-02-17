package ar.edu.itba.paw.exceptions.invalidstate;

public class UserInvalidStateException extends EntityInvalidStateException {

    private final static String ENTITY_NAME = "User";

    private UserInvalidStateException(final String entityId, final String reason, final String type) {
        super(ENTITY_NAME, entityId, reason, type);
    }

    public static UserInvalidStateException ofUserAlreadyEnable(final String username) {
        return new UserInvalidStateException(username, "is already enabled", "ALREADY_ENABLED");
    }

    public static UserInvalidStateException ofUserWithWrongMail(String username, String email) {
        return new UserInvalidStateException(username, "has not field '" + email + "' as email", "WRONG_MAIL");
    }
}
