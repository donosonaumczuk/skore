package ar.edu.itba.paw.exceptions;

public class UserNotFoundException extends EntityNotFoundException {

    private UserNotFoundException(final String username) {
        super("User", username);
    }

    private UserNotFoundException(final String idAttributeName, final String idAttributeValue) {
        super("User", idAttributeName, idAttributeValue);
    }

    public static UserNotFoundException ofUsername(final String username) {
        return new UserNotFoundException(username);
    }

    public static UserNotFoundException ofId(final long id) {
        return new UserNotFoundException("id", String.valueOf(id));
    }
}
