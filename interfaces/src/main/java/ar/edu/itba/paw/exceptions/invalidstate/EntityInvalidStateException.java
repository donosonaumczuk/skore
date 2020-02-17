package ar.edu.itba.paw.exceptions.invalidstate;

public abstract class EntityInvalidStateException extends RuntimeException {

    private final String type;

    public EntityInvalidStateException(final String entityName, final String entityId, final String reason,
                                       final String type) {
        super(entityName + " '" + entityId + "' " + reason);
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
