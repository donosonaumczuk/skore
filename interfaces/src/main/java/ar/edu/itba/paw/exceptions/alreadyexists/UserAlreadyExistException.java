package ar.edu.itba.paw.exceptions.alreadyexists;

public class UserAlreadyExistException extends EntityAlreadyExistsException {

    private final static String ENTITY_NAME = "User";
    private final String conflictingAttributeName;

    private UserAlreadyExistException(final String username) {
        super(ENTITY_NAME, username);
        conflictingAttributeName = "username";
    }

    private UserAlreadyExistException(final String idAttributeName, final String idAttributeValue) {
        super(ENTITY_NAME, idAttributeName, idAttributeValue);
        conflictingAttributeName = idAttributeName;
    }

    public static UserAlreadyExistException ofUsername(final String username) {
        return new UserAlreadyExistException(username);
    }

    public static UserAlreadyExistException ofEmail(final String email) {
        return new UserAlreadyExistException("email", email);
    }

    public String getConflictingAttributeName() {
        return conflictingAttributeName;
    }
}
