package ar.edu.itba.paw.exceptions;

public class SportNotFoundException extends EntityNotFoundException {

    public SportNotFoundException(final String sportId) {
        super("Sport", sportId);
    }
}
