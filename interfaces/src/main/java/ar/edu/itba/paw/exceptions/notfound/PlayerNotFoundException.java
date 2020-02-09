package ar.edu.itba.paw.exceptions.notfound;

public class PlayerNotFoundException extends EntityNotFoundException {

    private final static String ENTITY_NAME = "Player";

    private PlayerNotFoundException(final String username) {
        super(ENTITY_NAME, username);
    }

    private PlayerNotFoundException(final String idAttributeName, final String idAttributeValue) {
        super(ENTITY_NAME, idAttributeName, idAttributeValue);
    }

    public static PlayerNotFoundException ofId(long id) {
        return new PlayerNotFoundException("id", String.valueOf(id));
    }
}
