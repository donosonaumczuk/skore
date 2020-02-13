package ar.edu.itba.paw.exceptions.invalidstate;

public abstract class EntityInvalidStateException extends RuntimeException {

    public EntityInvalidStateException(final String entityName, final String entityId, final String reason) {
        super(entityName + " '" + entityId + "' " + reason);
    }
}
