package ar.edu.itba.paw.exceptions;

public class UserNotFoundException extends EntityNotFoundException {

    private final static String ENTITY_NAME = "User";

    private UserNotFoundException(final String username) {
        super(ENTITY_NAME, username);
    }

    private UserNotFoundException(final String idAttributeName, final String idAttributeValue) {
        super(ENTITY_NAME, idAttributeName, idAttributeValue);
    }

    public static UserNotFoundException ofUsername(final String username) {
        return new UserNotFoundException(username);
    }

    public static UserNotFoundException ofId(final long id) {
        return new UserNotFoundException("id", String.valueOf(id));
    }
}
