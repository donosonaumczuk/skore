package ar.edu.itba.paw.exceptions.notfound;

public class SportNotFoundException extends EntityNotFoundException {

    private final static String ENTITY_NAME = "Sport";

    private SportNotFoundException(final String sportId) {
        super(ENTITY_NAME, sportId);
    }

    public static SportNotFoundException ofSportId(final String sportId) {
        return new SportNotFoundException(sportId);
    }
}
