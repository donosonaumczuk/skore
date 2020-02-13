package ar.edu.itba.paw.exceptions.notfound;

public abstract class EntityNotFoundException extends RuntimeException {

    public EntityNotFoundException(final String entityName, final String entityId) {
        super(entityName + " '" + entityId + "' not found");
    }

    public EntityNotFoundException(final String entityName, final String idAttributeName, final String idAttributeValue) {
        super("No " + entityName + " found with " + idAttributeName + " '" + idAttributeValue + "'");
    }
}
