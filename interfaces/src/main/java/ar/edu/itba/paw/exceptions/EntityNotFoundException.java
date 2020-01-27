package ar.edu.itba.paw.exceptions;

public abstract class EntityNotFoundException extends RuntimeException {

    public EntityNotFoundException(final String message) {
        super(message);
    }
}
