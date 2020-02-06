package ar.edu.itba.paw.exceptions.alreadyexists;

public class SportAlreadyExistException extends EntityAlreadyExistsException {

    private final static String ENTITY_NAME = "Sport";

    private SportAlreadyExistException(final String sportId) {
        super(ENTITY_NAME, sportId);
    }

    public static SportAlreadyExistException ofId(final String sportId) {
        return new SportAlreadyExistException(sportId);
    }
}
