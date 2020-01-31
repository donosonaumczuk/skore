package ar.edu.itba.paw.exceptions;

public abstract class EntityAlreadyExistsException extends RuntimeException {
    //TODO: I will map this to a CONFLICT (409)
    public EntityAlreadyExistsException(final String entityName, final String entityId) {
        super(entityName + " '" + entityId + "' already exists");
    }

    public EntityAlreadyExistsException(final String resourceType, final String idAttributeName, final String idAttributeValue) {
        super(resourceType + " with " + idAttributeName + " '" + idAttributeValue + "' already exists");
    }
}
