package ar.edu.itba.paw.exceptions.notfound;

import ar.edu.itba.paw.exceptions.notfound.EntityNotFoundException;

public class SportNotFoundException extends EntityNotFoundException {

    private final static String ENTITY_NAME = "Sport";

    public SportNotFoundException(final String sportId) {
        super(ENTITY_NAME, sportId);
    }
}
